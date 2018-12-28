package com.lhy.pku.chatapp;

import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lhy.pku.chatapp.Adapter.MyFragmentAdapter;
import com.lhy.pku.chatapp.MainFragment.ChatListFragment;
import com.lhy.pku.chatapp.MainFragment.ContactFragment;
import com.lhy.pku.chatapp.MainFragment.SettingsFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyFragmentAdapter fragmentAdapter;
    private int[] iconResID = {R.mipmap.tabicon_userfav, R.mipmap.tabicon_chatlist, R.mipmap.tabicon_setting};
    private String[] iconText = {"聯絡人","聊天室","設定"};
    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>() {{
        add(new ContactFragment());
        add(new ChatListFragment());
        add(new SettingsFragment());
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setTabLayout();
        tabLayout.getTabAt(1).select();


    }

    private void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    private void setTabLayout() {

        fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager, true);
        for (int i = 0; i < iconResID.length; i++) {
            tabLayout.getTabAt(i).setIcon(iconResID[i]);
            tabLayout.getTabAt(i).setText(iconText[i]);
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
                if (tab.getIcon() != null)
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray);
                if (tab.getIcon() != null)
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
