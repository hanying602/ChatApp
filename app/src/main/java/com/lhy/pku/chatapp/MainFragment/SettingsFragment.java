package com.lhy.pku.chatapp.MainFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.lhy.pku.chatapp.Config.UserInfo;
import com.lhy.pku.chatapp.LoginActivity;
import com.lhy.pku.chatapp.R;


public class SettingsFragment extends Fragment {

    private View view;
    private TextView logoutTxv, usernameTxv, userIDTxv;
    private static final String TAG = SettingsFragment.class.getSimpleName();
    private static final int MODE_PRIVATE = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView();
        setClickHandler();
        return view;
    }
    private void initView(){
        logoutTxv = view.findViewById(R.id.logout_txv);
        usernameTxv = view .findViewById(R.id.settings_username_txv);
        userIDTxv = view.findViewById(R.id.settings_userid_txv);
        DocumentSnapshot user = UserInfo.getInstance().getUserSnapshot();
        usernameTxv.setText(user.getString("username"));
        userIDTxv.setText(user.getId());
        
    }
    private void setClickHandler(){
        logoutTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }
    private void logout(){
        UserInfo.getInstance().clearAllData();
        if(getActivity()!=null) {
            SharedPreferences pref = getActivity().getSharedPreferences("config", MODE_PRIVATE);
            pref.edit().clear().apply();
            startActivity(new Intent().setClass(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }
}
