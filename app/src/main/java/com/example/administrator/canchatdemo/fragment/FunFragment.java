package com.example.administrator.canchatdemo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.canchatdemo.R;
import com.example.administrator.canchatdemo.activity.NetImageActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FunFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FunFragment extends Fragment implements View.OnClickListener {

    private Button netImageSeeBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fun, container, false);

        netImageSeeBtn = (Button)view.findViewById(R.id.netImageSeeBtn);
        netImageSeeBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.netImageSeeBtn:
                getActivity().startActivity(new Intent(getActivity(), NetImageActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            default:
        }
    }
}
