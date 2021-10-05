package com.example.noteapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityRegisterBinding;
import com.example.noteapp.model.UserNote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding regisBinding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    DatabaseReference reference;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        regisBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = regisBinding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference("UserNote");

        preferences = getSharedPreferences("account", MODE_PRIVATE);
        editor = preferences.edit();

        regisBinding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = regisBinding.edtName.getText().toString();
                String email = regisBinding.edtEmail.getText().toString();
                String psw = regisBinding.edtPsw.getText().toString();

                storeDataFirebase(name, email, psw);
            }
        });

        regisBinding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void storeDataFirebase(String name, String email, String psw) {
        dialog.setMessage("Please wait. . .");
        dialog.show();
        auth.createUserWithEmailAndPassword(email, psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    String id = user.getUid();
                    UserNote userNote = new UserNote(name, email, "");

                    reference.child(id).setValue(userNote).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                editor.putString("id", id);
                                editor.commit();

                                Toast.makeText(RegisterActivity.this, "Register Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                                Log.d("success", "data terkirim");
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Register Gagal", Toast.LENGTH_SHORT).show();
                    Log.d("failed", "data gagal terkirim");
                }
            }
        });
    }
}