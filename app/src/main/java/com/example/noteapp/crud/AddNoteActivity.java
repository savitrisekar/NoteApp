package com.example.noteapp.crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.R;
import com.example.noteapp.databinding.ActivityAddNoteBinding;
import com.example.noteapp.model.ItemNote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {

    ActivityAddNoteBinding binding;
    SharedPreferences preferences;
    String id;
    String keyId;
    DatabaseReference reference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_note);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dialog = new ProgressDialog(this);

        preferences = getSharedPreferences("account", MODE_PRIVATE);
        id = preferences.getString("id", "");

        reference = FirebaseDatabase.getInstance().getReference("DataNote").child(id);

        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.tvTitleAdd.getText().toString();
                String dsc = binding.tvDscAdd.getText().toString();

                SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
                Calendar calendar = Calendar.getInstance();
                String date = dateFormat.format(calendar.getTime());

                dialog.setMessage("Please wait. . .");
                dialog.show();

                keyId = reference.push().getKey(); //mendapatkan key dari db firebase

                ItemNote itemNote = new ItemNote(title, dsc, date, keyId);

                reference.child(keyId).setValue(itemNote).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        } else {
                            Toast.makeText(AddNoteActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}