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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lhy.pku.chatapp.Adapter.ContactListAdapter;
import com.lhy.pku.chatapp.Config.UserInfo;
import com.lhy.pku.chatapp.R;
import com.lhy.pku.chatapp.model.Contact;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;


public class ContactFragment extends Fragment {

    private View view;
    private static final String TAG = ContactFragment.class.getSimpleName();private RecyclerView recyclerView;
    private ContactListAdapter adapter;
    private List<Contact> contactList;
    private ProgressBar progressBar;
    private View dimView;
    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contact, container, false);

        initView();
        initAdapter();
        getContactList();

        return view;
    }

    private void initView() {

        recyclerView = view.findViewById(R.id.contactlist_recyclerview);
        progressBar = view.findViewById(R.id.contactlist_progressbar);
        dimView = view.findViewById(R.id.contactlist_dim_view);
    }

    private void initAdapter() {
        contactList = new ArrayList<>();
        adapter = new ContactListAdapter(contactList);
        Consumer<Contact> mClickConsumer = new Consumer<Contact>() {
            @Override
            public void accept(@NonNull Contact contact) throws Exception {
            }
        };
        adapter.getPositionClicks().subscribe(mClickConsumer);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getContactList() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRef = UserInfo.getInstance().getUserSnapshot().getReference();

        showProgressbar();
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                contactList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    if(!document.getId().equals(userRef.getId())){
                                        Contact contact = new Contact();
                                        contact.setUserID(document.getId());
                                        contactList.add(contact);
                                    }
                                }
                                adapter.notifyDataSetChanged();
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
