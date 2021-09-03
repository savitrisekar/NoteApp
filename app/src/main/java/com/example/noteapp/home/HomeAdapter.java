package com.example.noteapp.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.crud.INote;
import com.example.noteapp.databinding.ActivityAddNoteBinding;
import com.example.noteapp.databinding.ItemNoteBinding;
import com.example.noteapp.model.ItemNote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

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
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new HomeHolder(ItemNoteBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        ItemNote note = arrayList.get(position);
        String title = note.getTitle();
        String date = note.getDate();
        String description = note.getDescription();

        holder.binding.tvTitleNote.setText(title);
        holder.binding.tvDateNote.setText(date);
        holder.binding.tvDscNote.setText(description);

        holder.binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemNote itemNote = arrayList.get(position);
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ActivityAddNoteBinding addNoteBinding = ActivityAddNoteBinding.inflate(layoutInflater);
                editNote(itemNote, addNoteBinding);
            }
        });

        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNote();
            }
        });
    }

    private void editNote(ItemNote itemNote, ActivityAddNoteBinding addBinding) {
        Dialog dialog = new Dialog(context);
        View view = addBinding.getRoot();
        dialog.setContentView(view);
        dialog.show();

        addBinding.tvTitleAdd.setText(itemNote.getTitle());
        addBinding.tvDscAdd.setText(itemNote.getDescription());
        keyId = itemNote.getUserId();

        addBinding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("LLL dd, yyyy");
                Calendar calendar = Calendar.getInstance();
                date = format.format(calendar.getTime());

                String title = addBinding.tvTitleAdd.getText().toString();
                String dsc = addBinding.tvDscAdd.getText().toString();

                ItemNote note = new ItemNote(title, dsc, date, keyId);
                iNote.updateNote(note);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("EDIT", "edited");
                        dialog.dismiss();
                    }
                }, 200);
            }
        });
    }

    private void deleteNote() {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class HomeHolder extends RecyclerView.ViewHolder {

        ItemNoteBinding binding;

        public HomeHolder(@NonNull ItemNoteBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
