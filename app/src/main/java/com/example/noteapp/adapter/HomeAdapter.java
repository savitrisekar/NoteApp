package com.example.noteapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.interfaces.INote;
import com.example.noteapp.databinding.ActivityAddNoteBinding;
import com.example.noteapp.databinding.ItemNoteBinding;
import com.example.noteapp.model.ItemNote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    Context context;
    ArrayList<ItemNote> arrayList;
    ItemNoteBinding binding;
    String keyId = "";
    String date;
    INote iNote;

    public HomeAdapter(Context context, ArrayList<ItemNote> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        iNote = (INote) context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        return new HomeViewHolder(ItemNoteBinding.inflate(inflater));
        binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HomeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        ItemNote note = arrayList.get(position);
        String title = note.getTitle();
        String date = note.getDate();
        String description = note.getDescription();

        holder.binding.tvTitleNote.setText(title);
        holder.binding.tvDateNote.setText(date);
        holder.binding.tvDscNote.setText(description);

        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNote();
            }
        });

        holder.binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemNote itemNote = arrayList.get(position);
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ActivityAddNoteBinding addNoteBinding = ActivityAddNoteBinding.inflate(layoutInflater);
                editNote(itemNote, addNoteBinding);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        ItemNoteBinding binding;

        public HomeViewHolder(@NonNull ItemNoteBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    private void editNote(ItemNote itemNote, ActivityAddNoteBinding addNoteBinding) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = addNoteBinding.getRoot();
        dialog.setContentView(view);
        dialog.show();

        addNoteBinding.tvTitleAdd.setText(itemNote.getTitle());
        addNoteBinding.tvDscAdd.setText(itemNote.getDescription());
        keyId = itemNote.getUserId();

        addNoteBinding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                Calendar calendar = Calendar.getInstance();

                date = format.format(calendar.getTime());

                String title = addNoteBinding.tvTitleAdd.getText().toString();
                String description = addNoteBinding.tvDscAdd.getText().toString();

                ItemNote note = new ItemNote(title, description, date, keyId);
                iNote.updateNote(note);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("editBerhasil", "EditBerhasil");
                        dialog.dismiss();
                    }
                }, 200);
            }
        });
    }

    private void deleteNote() {

    }
}
