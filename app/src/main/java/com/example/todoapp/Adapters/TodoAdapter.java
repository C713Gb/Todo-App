package com.example.todoapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Activities.TodoDetailsActivity;
import com.example.todoapp.Models.Todo;
import com.example.todoapp.R;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{

    private final Context mContext;
    private final List<Todo> todoList;

    public TodoAdapter(Context mContext, List<Todo> todoList) {
        this.mContext = mContext;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        String id = todoList.get(position).getTodoId();
        String title = todoList.get(position).getTitle();
        String description = todoList.get(position).getDescription();
        int status = todoList.get(position).getStatus();
        String created = todoList.get(position).getCreatedAt();

        holder.todo.setText(title);
        if (status == 1){
            holder.status.setImageResource(R.drawable.status_done);
        } else {
            holder.status.setImageResource(R.drawable.status_not_done);
        }

        if (description.length() == 0) description = "No description available";

        Intent intent = new Intent(mContext, TodoDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("status", Integer.toString(status));
        intent.putExtra("date", created);

        holder.details.setOnClickListener(view -> mContext.startActivity(intent));
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView status, details;
        public TextView todo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todo = itemView.findViewById(R.id.todo_text);
            status = itemView.findViewById(R.id.todo_status);
            details = itemView.findViewById(R.id.todo_details);
        }
    }
}
