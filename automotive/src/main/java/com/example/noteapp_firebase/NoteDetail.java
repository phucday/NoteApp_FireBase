package com.example.noteapp_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp_firebase.databinding.ActivityNoteDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetail extends AppCompatActivity {
    ActivityNoteDetailBinding binding;
    boolean isEditNote = false;
    String title,content,docId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");
        if(docId != null ){
            isEditNote = true;
            binding.titleNoteSave.setText(title);
            binding.contentNoteSave.setText(content);
        }
        if(isEditNote){
            binding.titlePageAddOrEdit.setText("Edit your note");
            binding.btnDeleteNote.setVisibility(View.VISIBLE);
        }
        binding.btnDeleteNote.setOnClickListener(view -> DeleteNoteFromFireBase());
        binding.btnSaveNote.setOnClickListener( v-> SaveNote());
    }

    private void DeleteNoteFromFireBase() {
        DocumentReference documentReference = Utility.getCollectionReferenceForNote().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NoteDetail.this, "Delete note Success", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(NoteDetail.this, "Failure delete note", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SaveNote() {
        String titleSave = binding.titleNoteSave.getText().toString();
        String contentSave = binding.contentNoteSave.getText().toString();
        if(titleSave == null || titleSave.isEmpty()){
            Toast.makeText(NoteDetail.this, "Title is required", Toast.LENGTH_SHORT).show();
        }if(contentSave == null || contentSave.isEmpty()){
            Toast.makeText(NoteDetail.this, "Title is required", Toast.LENGTH_SHORT).show();
        }
        
        Note note = new Note();
        note.setTitle(titleSave);
        note.setContent(contentSave);
        note.setTimeStamp(Timestamp.now());
        
        SaveToFireBase(note);
        
    }

    private void SaveToFireBase(Note note) {
        DocumentReference documentReference;
        if(isEditNote){
            documentReference = Utility.getCollectionReferenceForNote().document(docId);
        }else{
            documentReference = Utility.getCollectionReferenceForNote().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful()){
                  Toast.makeText(NoteDetail.this, "Add or update Success", Toast.LENGTH_SHORT).show();
                  finish();
              }else{
                  Toast.makeText(NoteDetail.this, "Failure add", Toast.LENGTH_SHORT).show();
              }
            }
        });
    }
}