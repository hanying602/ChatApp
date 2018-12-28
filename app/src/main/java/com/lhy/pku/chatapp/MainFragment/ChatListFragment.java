package com.lhy.pku.chatapp.MainFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.value.ReferenceValue;
import com.lhy.pku.chatapp.Config.UserInfo;
import com.lhy.pku.chatapp.LoginActivity;
import com.lhy.pku.chatapp.R;


public class ChatListFragment extends Fragment {

    private View view;
    private static final String TAG = ChatListFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        initView();
        getChatList();

        return view;
    }

    private void initView() {

    }

    private void getChatList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = UserInfo.getInstance().getUserReference();
        Log.i(TAG, "getChatList: " + userRef.getPath());
        Log.i(TAG, "getChatList: " + userRef.getId());
        Log.i(TAG, "getChatList: " + userRef);

        db.collection("Chatroom")
                .whereArrayContains("members", userRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    getLatestMessage(document.getReference());
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

    private void getLatestMessage(DocumentReference documentReference) {
        documentReference.collection("Message")
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("message", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("message", "No such document");
                    }

                } else {
                    Log.d("message", "Error getting documents: ", task.getException());
                }
            }
        });
    }

}
