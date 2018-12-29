package com.lhy.pku.chatapp;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

public class ChatRoomActivity extends AppCompatActivity {

    private static final String TAG = ChatRoomActivity.class.getSimpleName();
    private EditText inputMessageEdt;
    private ImageView sendMessageImg;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        if (getIntent().getExtras() != null) {
            String roomID = getIntent().getExtras().getString("room_id", "");
            Log.i(TAG, "onCreate: " + roomID);
        }

        initView();
    }

    private void initView() {
        sendMessageImg = findViewById(R.id.chatroom_send_img);
        int sendIconColor = ContextCompat.getColor(ChatRoomActivity.this, R.color.colorPrimary);
        sendMessageImg.setColorFilter(sendIconColor, PorterDuff.Mode.SRC_IN);
        inputMessageEdt = findViewById(R.id.chatroom_send_edt);
        recyclerView = findViewById(R.id.chatroom_recyclerview);
    }
}
