package com.example.noteapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.crud.AddNoteActivity;
import com.example.noteapp.crud.INote;
import com.example.noteapp.databinding.ActivityHomeBinding;
import com.example.noteapp.model.ItemNote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements INote {

    ActivityHomeBinding binding;
    ArrayList<ItemNote> itemNotes;
    DatabaseReference reference;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayoutManager layoutManager;
    HomeAdapter adapter;
    ItemNote note;
    ProgressDialog dialog;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        preferences = getSharedPreferences("account", MODE_PRIVATE);
        id = preferences.getString("id", ""); //cal id from signup

        reference = FirebaseDatabase.getInstance().getReference("DataNote").child(id);

        itemNotes = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        binding.rvItemNote.setLayoutManager(layoutManager);

        dialog = new ProgressDialog(this);

        binding.fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AddNoteActivity.class));
            }
        });

        showDiary();

    }

    private void showDiary() {
        dialog.setMessage("Please wait, is fetching data. . .");
        dialog.show();
        itemNotes.clear();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    note = ds.getValue(ItemNote.class);
                    itemNotes.add(note);
                }

                adapter = new HomeAdapter(HomeActivity.this, itemNotes);
                binding.rvItemNote.setAdapter(adapter);
                binding.tvEmpty.setVisibility(View.GONE);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void updateNote(ItemNote itemNote) {
        reference.child(note.getUserId()).setValue(itemNote).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(HomeActivity.this, "Edited", Toast.LENGTH_SHORT).show();
                    showDiary();
                } else {
                    Toast.makeText(HomeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void deleteNote(ItemNote note) {

    }
}