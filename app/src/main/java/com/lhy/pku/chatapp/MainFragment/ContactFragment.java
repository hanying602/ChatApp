package com.lhy.pku.chatapp.MainFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lhy.pku.chatapp.R;



public class ContactFragment extends Fragment {

    private View view;
    private static final String TAG = ContactFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contact, container, false);

        return view;
    }
}
