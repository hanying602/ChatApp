package com.lhy.pku.chatapp.MainFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lhy.pku.chatapp.Adapter.ChatListAdapter;
import com.lhy.pku.chatapp.Config.UserInfo;
import com.lhy.pku.chatapp.model.LatestMessage;
import com.lhy.pku.chatapp.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;


public class ChatListFragment extends Fragment {

    private View view;
    private static final String TAG = ChatListFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private List<LatestMessage> chatRoomList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        initView();
        initAdapter();
        getChatList();

        return view;
    }

    private void initView() {

        recyclerView = view.findViewById(R.id.chatlist_recyclerview);
    }

    private void initAdapter() {
        RecyclerView recyclerView = view.findViewById(R.id.chatlist_recyclerview);
        chatRoomList = new ArrayList<>();
        adapter = new ChatListAdapter(chatRoomList);
        Consumer<LatestMessage> mClickConsumer = new Consumer<LatestMessage>() {
            @Override
            public void accept(@NonNull LatestMessage message) throws Exception {
            }
        };
        adapter.getPositionClicks().subscribe(mClickConsumer);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getChatList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = UserInfo.getInstance().getUserReference();

        db.collection("Chatroom")
                .whereArrayContains("members", userRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                chatRoomList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    getLatestMessage(document.getDocumentReference("latest_message").getPath());
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void getLatestMessage(String path) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(path).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DocumentSnapshot document = task.getResult();
                        Log.d("getLatestMessage", document.getId() + " => " + document.getData());
                        LatestMessage latestMessage = new LatestMessage();
                        latestMessage.setContent(document.getString("content"));
                        latestMessage.setTime(document.getDate("time"));
                        latestMessage.setUserID(document.getDocumentReference("from").getId());
                        chatRoomList.add(latestMessage);
                    } else {
                        Log.d("getLatestMessage", "No such document");
                    }

                } else {
                    Log.d("getLatestMessage", "Error getting documents: ", task.getException());
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

}
