package com.example.noteapp.crud;

import com.example.noteapp.model.ItemNote;

public interface INote {

    public void updateNote(ItemNote note);

    public void deleteNote(ItemNote note);
}
