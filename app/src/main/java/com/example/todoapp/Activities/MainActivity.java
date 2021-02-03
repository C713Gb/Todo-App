package com.example.todoapp.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.todoapp.Adapters.TodoAdapter;
import com.example.todoapp.Models.Todo;
import com.example.todoapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    RecyclerView recyclerView;
    List<Todo> todoListAdapter;
    ImageView add;
    Dialog addTodoDialog;
    ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();

        fetchData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        todoListAdapter = new ArrayList<>();
        add = findViewById(R.id.add_todo);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        addTodoDialog = new Dialog(this);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View dialogView = layoutInflater.inflate(R.layout.add_todo_layout, null);
        addTodoDialog.setContentView(dialogView);
        addTodoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addTodoDialog.setCancelable(false);
        addTodoDialog.setCanceledOnTouchOutside(false);

        TextView addBtn = dialogView.findViewById(R.id.add_btn);
        TextView cancel = dialogView.findViewById(R.id.cancel_btn);
        EditText title = dialogView.findViewById(R.id.title_txt);
        EditText description = dialogView.findViewById(R.id.description_txt);

        add.setOnClickListener(view -> addTodoDialog.show());

        addBtn.setOnClickListener(view -> {
            if (title.getText().toString() != null && title.getText().toString().trim().length()>0){
                String titletxt = title.getText().toString().trim();
                String descriptiontxt = description.getText().toString().trim();
                if (descriptiontxt == null) descriptiontxt = "";
                progressDialog.setMessage("Creating Todo");
                progressDialog.show();
                addTodo(titletxt, descriptiontxt);
                addTodoDialog.dismiss();
                title.setText("");
                description.setText("");
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(view -> addTodoDialog.dismiss());

    }

    private void addTodo(String title, String description) {

        long tsLong = System.currentTimeMillis()/1000;
        String timestamp = Long.toString(tsLong);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        String url = "https://g8xdxfyxp7.execute-api.ap-south-1.amazonaws.com/test/createtodo";

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("todoId", timestamp);
            jsonObject.put("title", title);
            jsonObject.put("description", description);
            jsonObject.put("status", 0);
            jsonObject.put("createdAt", formattedDate);

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    jsonObject, response -> {

                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Success");

                        onResume();

                    }, error -> {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Failed"+error.getMessage());

                    }
            );


            requestQueue.add(objectRequest);

        } catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Failed to create Todo", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "addTodo: "+e.getMessage());
        }
    }

    private void fetchData(){
        todoListAdapter.clear();

        String url = "https://wqnlmo97md.execute-api.ap-south-1.amazonaws.com/test/todolists";

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {

                        Log.d(TAG, "onResponse: Success");
                        try {
                            int size = response.getJSONArray("data").length();
                            Gson gson = new GsonBuilder().create();
                            for (int i = 0; i < size; i++){
                                JSONObject jsonObject = response.getJSONArray("data").getJSONObject(i);
                                Todo todo = gson.fromJson(jsonObject.toString(), Todo.class);
                                if (todo.getStatus() == 0) todoListAdapter.add(todo);
                            }

                            for (int i = 0; i < size; i++){
                                JSONObject jsonObject = response.getJSONArray("data").getJSONObject(i);
                                Todo todo = gson.fromJson(jsonObject.toString(), Todo.class);
                                if (todo.getStatus() == 1) todoListAdapter.add(todo);
                            }

                            TodoAdapter todoAdapter = new TodoAdapter(MainActivity.this, todoListAdapter);
                            recyclerView.setAdapter(todoAdapter);

                        } catch (Exception e){
                            e.printStackTrace();
                        }


                    }, error -> Log.d(TAG, "onResponse: Failed"+error.getMessage())
            );


            requestQueue.add(objectRequest);
        } catch (Exception e){
            Log.d(TAG, "fetchData: "+e.getMessage());
        }

    }

}