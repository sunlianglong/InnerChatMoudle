package com.example.administrator.canchatdemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.canchatdemo.R;
import com.example.administrator.canchatdemo.activity.MainChatActivity;
import com.example.administrator.canchatdemo.util.CanChatUdpReceiver;
import com.example.administrator.canchatdemo.util.ListContactInfo;
import com.example.administrator.canchatdemo.view.NoScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    private final int UPDATE_CONTACTLIST = 0;

    private NoScrollListView myListView;

    private ImageView unNamed;

    private int[] imageIds = new int[]{
            R.drawable.dml,
            R.drawable.dmk,
            R.drawable.dmn,
            R.drawable.dmm
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if(msg.what == UPDATE_CONTACTLIST){
                updateMyListView();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //开启线程实时更新列表
        new Thread(){
            public void run(){
                try {
                    while (true) {
                        Thread.sleep(30000);//每30秒更新一次
                        Message msg = new Message();
                        msg.what = UPDATE_CONTACTLIST;
                        handler.sendMessage(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_list, container, false);

        myListView = (NoScrollListView)view.findViewById(R.id.listPeople);

        unNamed = (ImageView)view.findViewById(R.id.unNamed);

        //初始化联系人列表
        updateMyListView();

        //联系人列表点击设置
        myListView.setOnItemClickListener(new NoScrollListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), MainChatActivity.class);

                TextView listUserName = (TextView) view.findViewById(R.id.listUserName);
                TextView listUserIp = (TextView) view.findViewById(R.id.listUserIp);
                String listName = listUserName.getText().toString();
                String listIp = listUserIp.getText().toString();
                intent.putExtra("listName", listName);//传递列表名
                intent.putExtra("listIp", listIp);//传递ip

                startActivity(intent);

            }
        });

        unNamed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMyListView();
            }
        });

        return view;
    }

    //更新联系人列表 ，列表数据处理
    private List<Map<String,Object>> getData(List<ListContactInfo> listContactInfos){
        List<Map<String,Object>> list =new ArrayList<Map<String, Object>>();
        long time = System.currentTimeMillis();
        for(int i =0;i<listContactInfos.size();i++ ){
            Map<String, Object> map = new HashMap<String, Object>();

            ListContactInfo info = listContactInfos.get(i);

            int listImage = info.getListImage();
            String listIp = info.getListIp();
            String listName = info.getListName();
            String listTime = info.getListTime();
            String listUnRead = info.getListUnRead();

            map.put("listUserIp", listIp);
            map.put("listUserName",listName);
            if(i == 0){
                map.put("headImage", R.drawable.lfp);
            }else {
                map.put("headImage", listImage);
            }
            map.put("listTime",listTime);
            map.put("unReadcounts",listUnRead);

            list.add(map);
        }
        return  list;
    }

    //更新联系人列表
    public void updateMyListView(){
        try {
            if(CanChatUdpReceiver.listContactInfos != null) {
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), getData(CanChatUdpReceiver.listContactInfos), R.layout.item_list_people,
                        new String[]{"listUserName", "listUserIp", "headImage", "listTime", "unReadcounts"},
                        new int[]{R.id.listUserName, R.id.listUserIp, R.id.headImage, R.id.listTime, R.id.unReadcounts});
                myListView.setAdapter(adapter);
            }else{
                Log.e("错误提示：","列表无数据");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //更新联系人列表
        updateMyListView();
    }
}
