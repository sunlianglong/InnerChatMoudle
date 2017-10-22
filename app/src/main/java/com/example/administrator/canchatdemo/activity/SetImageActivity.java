package com.example.administrator.canchatdemo.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.example.administrator.canchatdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetImageActivity extends AppCompatActivity {

    private int[] imageIds = new int[]{
            R.drawable.dml,
            R.drawable.dmk,
            R.drawable.dmn,
            R.drawable.dmm,
            R.drawable.head1,
            R.drawable.head2,
            R.drawable.head3,
            R.drawable.head4,
            R.drawable.head5,
            R.drawable.head6,
            R.drawable.head7,
            R.drawable.head8
    };

    private GridView myImageGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_headimage);

        myImageGridView = (GridView)findViewById(R.id.myImageGridView);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i=0;i<imageIds.length;i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("headImage",imageIds[i]);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.item_gridview_image,new String[]{"headImage"},new int[]{R.id.headImage});
        myImageGridView.setAdapter(adapter);

        myImageGridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("userImageId",imageIds[position]);
                editor.commit();
                finish();
                overridePendingTransition(R.anim.stay_in,R.anim.slide_out_down);
            }
        });
    }
}
