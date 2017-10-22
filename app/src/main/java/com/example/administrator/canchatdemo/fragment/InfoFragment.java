package com.example.administrator.canchatdemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.canchatdemo.R;
import com.example.administrator.canchatdemo.activity.MainActivity;
import com.example.administrator.canchatdemo.activity.SetImageActivity;
import com.example.administrator.canchatdemo.activity.SetNameActivity;
import com.example.administrator.canchatdemo.util.CanChatUdpReceiver;
import com.example.administrator.canchatdemo.util.Udp;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment implements View.OnClickListener {

    private Button setUserNameBtn;
    private Button setImageBtn;

    private TextView userName;
    private TextView hostIp;
    private ImageView userImage;

    private String localIp = null;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        userName = (TextView)view.findViewById(R.id.userName);
        hostIp = (TextView)view.findViewById(R.id.hostIp);
        userImage = (ImageView)view.findViewById(R.id.userImage);

        setUserNameBtn = (Button)view.findViewById(R.id.setUserNameBtn);
        setImageBtn = (Button)view.findViewById(R.id.setImageBtn);
        setUserNameBtn.setOnClickListener(this);
        setImageBtn.setOnClickListener(this);

        hostIp.setText(MainActivity.localHostIp);

        return view;
    }

    //设置昵称
    public void setUserName(){
        SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String name = sp.getString("userName","");
        userName.setText(name);
    }

    //设置头像
    public void setUserImage(){
        SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int imageId = sp.getInt("userImageId",0);
        userImage.setImageResource(imageId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setUserNameBtn:
                getActivity().startActivityForResult(new Intent(getContext(), SetNameActivity.class), 0);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.setImageBtn:
                getActivity().startActivityForResult(new Intent(getContext(), SetImageActivity.class), 0);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            default:
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //设置昵称
        setUserName();
        setUserImage();
    }
}
