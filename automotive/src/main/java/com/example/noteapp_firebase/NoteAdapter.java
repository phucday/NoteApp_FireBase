package com.example.noteapp_firebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titletv.setText(note.title);
        holder.contenttv.setText(note.content);
        holder.timestamptv.setText(Utility.timestampToString(note.timeStamp));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, NoteDetail.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            String docId = getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_note,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titletv,contenttv,timestamptv;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titletv = itemView.findViewById(R.id.title_tv_recycle);
            contenttv = itemView.findViewById(R.id.content_tv_recycle);
            timestamptv = itemView.findViewById(R.id.timestamp_tv_recycle);
        }
    }
}
