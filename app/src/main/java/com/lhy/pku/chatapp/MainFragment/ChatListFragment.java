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
import android.view.WindowManager;
import android.widget.ProgressBar;

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
import java.util.Collections;
import java.util.List;

import io.reactivex.functions.Consumer;


public class ChatListFragment extends Fragment {

    private View view;
    private static final String TAG = ChatListFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private List<LatestMessage> chatRoomList;
    private ProgressBar progressBar;
    private View dimView;
    private static int chatRoomCount, tempCount;
    private DocumentReference userRef;

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
        progressBar = view.findViewById(R.id.chatlist_progressbar);
        dimView = view.findViewById(R.id.chatlist_dim_view);
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
        chatRoomCount = 0;
        tempCount = 0;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRef = UserInfo.getInstance().getUserSnapshot().getReference();

        showProgressbar();
        db.collection("Chatroom")
                .whereArrayContains("members", userRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                chatRoomList.clear();
                                chatRoomCount = task.getResult().size();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    List<DocumentReference> membersList = (List<DocumentReference>)document.get("members");
                                    String otherUserID = membersList.get(0).getId().equals(userRef.getId()) ? membersList.get(1).getId() : membersList.get(0).getId();
                                    getLatestMessage(document.getDocumentReference("latest_message").getPath(),otherUserID);
                                }
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

    private void getLatestMessage(String path, final String otherUserID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(path).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {

                        tempCount++;

                        DocumentSnapshot document = task.getResult();
                        Log.d("getLatestMessage", document.getId() + " => " + document.getData());
                        LatestMessage latestMessage = new LatestMessage();
                        latestMessage.setContent(document.getString("content"));
                        latestMessage.setTime(document.getDate("time"));
                        latestMessage.setUserID(otherUserID);
                        chatRoomList.add(latestMessage);

                        if(tempCount==chatRoomCount) {
                            Collections.sort(chatRoomList);
                            adapter.notifyDataSetChanged();
                            hideProgressbar();
                        }
                    } else {
                        hideProgressbar();
                        Log.d("getLatestMessage", "No such document");
                    }

                } else {
                    hideProgressbar();
                    Log.d("getLatestMessage", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
        dimView.setVisibility(View.VISIBLE);
        if (getActivity() != null)
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
        dimView.setVisibility(View.GONE);
        if (getActivity() != null)
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
