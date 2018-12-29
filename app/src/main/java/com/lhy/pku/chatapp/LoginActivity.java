package com.lhy.pku.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lhy.pku.chatapp.Config.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText userIdEdt, passwordEdt;
    private TextView registerTxv, loginTxv;
    private ProgressBar progressBar;
    private View dimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        setClickHandler();
    }

    private void initView() {
        userIdEdt = findViewById(R.id.userid_edt);
        passwordEdt = findViewById(R.id.password_edt);
        registerTxv = findViewById(R.id.register_txv);
        loginTxv = findViewById(R.id.login_txv);
        progressBar = findViewById(R.id.login_progressbar);
        dimView = findViewById(R.id.login_dim_view);
    }

    private void setClickHandler() {
        registerTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(userIdEdt.getText().toString()) && !"".equals(passwordEdt.getText().toString())) {
                    register(userIdEdt.getText().toString(), passwordEdt.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "帳號或密碼為空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(userIdEdt.getText().toString()) && !"".equals(passwordEdt.getText().toString())) {
                    login(userIdEdt.getText().toString(), passwordEdt.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "帳號或密碼為空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void toMainActivity(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void login(String userId, final String password) {
        showProgressbar();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference userRef = db.collection("User").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if (password.equals(document.get("password"))) {
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Success!");
                            UserInfo.getInstance().setUserReference(userRef);
                            hideProgressbar();
                            toMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Wrong password");
                            hideProgressbar();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "No such document");
                        hideProgressbar();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "get failed with ", task.getException());
                    hideProgressbar();
                }
            }

        });

    }

    private void register(final String userId, final String password) {
        showProgressbar();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("User").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        hideProgressbar();
                        Toast.makeText(LoginActivity.this, "ID已被使用", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                    } else {
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("password", password);
                        docData.put("username", userId);
                        db.collection("User")
                                .document(userId)
                                .set(docData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        hideProgressbar();
                                        Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        hideProgressbar();
                                        Toast.makeText(LoginActivity.this, "Failed" , Toast.LENGTH_SHORT).show();
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "get failed with ", task.getException());
                    hideProgressbar();
                }
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
