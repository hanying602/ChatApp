package com.lhy.pku.chatapp;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lhy.pku.chatapp.Adapter.ChatMessageAdapter;
import com.lhy.pku.chatapp.Config.UserInfo;
import com.lhy.pku.chatapp.model.Contact;
import com.lhy.pku.chatapp.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {

    private static final String TAG = ChatRoomActivity.class.getSimpleName();
    private EditText inputMessageEdt;
    private ImageView sendMessageImg;
    private RecyclerView recyclerView;
    private ChatMessageAdapter adapter;
    private List<Message> messageList;
    private DocumentReference userRef, chatRoomReference;
    private ProgressBar progressBar;
    private View dimView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        if (getIntent().getExtras() != null) {
            String roomID = getIntent().getExtras().getString("room_id", "");
            Log.i(TAG, "onCreate: " + roomID);
            userRef = UserInfo.getInstance().getUserSnapshot().getReference();
            chatRoomReference = FirebaseFirestore.getInstance().collection("Chatroom").document(roomID);
        }

        initView();
        initAdapter();
        setClickHandler();
        getMessageList();
    }

    private void initView() {
        sendMessageImg = findViewById(R.id.chatroom_send_img);
        int sendIconColor = ContextCompat.getColor(ChatRoomActivity.this, R.color.colorPrimary);
        sendMessageImg.setColorFilter(sendIconColor, PorterDuff.Mode.SRC_IN);
        inputMessageEdt = findViewById(R.id.chatroom_send_edt);
        recyclerView = findViewById(R.id.chatroom_recyclerview);
        dimView = findViewById(R.id.chatroom_dim_view);
        progressBar = findViewById(R.id.chatroom_progressbar);
    }

    private void initAdapter() {

        messageList = new ArrayList<>();
        adapter = new ChatMessageAdapter(messageList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setClickHandler() {

        sendMessageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToFirebase(inputMessageEdt.getText().toString());
                inputMessageEdt.setText("");
            }
        });
    }

    private void getMessageList() {

        showProgressbar();
        chatRoomReference.collection("Message")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                messageList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Message message = new Message();
                                    message.setContent(document.getString("content"));
                                    message.setFrom(document.getDocumentReference("from"));
                                    message.setTime(document.getDate("time"));
                                    messageList.add(message);
                                }
                                setOnDataChangeListener();
                                Collections.sort(messageList);
                                adapter.notifyDataSetChanged();
                                scrollMyListViewToBottom();
                                hideProgressbar();
                            } else {
                                hideProgressbar();
                                Log.d(TAG, "No such document");
                            }

                        } else {
                            hideProgressbar();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void sendMessageToFirebase(String messageStr) {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("content", messageStr);
        messageData.put("time", new Date());
        messageData.put("from", userRef);
        chatRoomReference.collection("Message").document().set(messageData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }


    private void setOnDataChangeListener() {
        chatRoomReference.collection("Message").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    messageList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Message message = new Message();
                        message.setContent(document.getString("content"));
                        message.setFrom(document.getDocumentReference("from"));
                        message.setTime(document.getDate("time"));
                        messageList.add(message);
                    }
                    Collections.sort(messageList);
                    adapter.notifyDataSetChanged();
                    scrollMyListViewToBottom();
                }
            }

        });
    }


    private void scrollMyListViewToBottom() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
        dimView.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
        dimView.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
