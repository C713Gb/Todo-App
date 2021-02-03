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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.todoapp.R;

import org.json.JSONObject;

public class TodoDetailsActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    TextView description, title, date;
    Button update;
    CheckBox checkBox;
    ImageView back, delete, edit;
    String id, str_title, str_description, str_check, str_date;
    ProgressDialog progressDialog;
    Dialog editTodoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_details);

        editTodoDialog = new Dialog(this);
        description = findViewById(R.id.description_txt);
        title = findViewById(R.id.title_txt);
        update = findViewById(R.id.update_btn);
        checkBox = findViewById(R.id.status_check);
        back = findViewById(R.id.back_btn);
        delete = findViewById(R.id.delete_btn);
        date = findViewById(R.id.date_txt);
        edit = findViewById(R.id.edit_icon);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View dialogView = layoutInflater.inflate(R.layout.edit_todo_layout, null);
        editTodoDialog.setContentView(dialogView);
        editTodoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editTodoDialog.setCancelable(false);
        editTodoDialog.setCanceledOnTouchOutside(false);

        TextView ok = dialogView.findViewById(R.id.add_btn);
        TextView cancel = dialogView.findViewById(R.id.cancel_btn);
        EditText editTitle = dialogView.findViewById(R.id.title_txt);
        EditText editDescription = dialogView.findViewById(R.id.description_txt);

        ok.setOnClickListener(view -> {
            if (editTitle.getText().toString().trim().length() > 0) {
                description.setText(editDescription.getText().toString().trim());
                title.setText(editTitle.getText().toString().trim());
                editTodoDialog.dismiss();
                editDescription.setText("");
                editTitle.setText("");
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(view -> editTodoDialog.dismiss());

        if (getIntent() != null){
            id = getIntent().getStringExtra("id");
            str_title = getIntent().getStringExtra("title");
            str_description = getIntent().getStringExtra("description");
            str_check = getIntent().getStringExtra("status");
            str_date = getIntent().getStringExtra("date");

            title.setText(str_title);
            description.setText(str_description);
            date.setText(str_date);
            if (str_check.equals("1")) checkBox.setChecked(true);
            editTitle.setText(str_title);
            editDescription.setText(str_description);
        }

        edit.setOnClickListener(view -> editTodoDialog.show());

        back.setOnClickListener(view -> onBackPressed());

        delete.setOnClickListener(view -> {
            progressDialog.setMessage("Deleting Todo");
            deleteApi();
        });

        update.setOnClickListener(view -> {
            progressDialog.setMessage("Updating Todo");
            updateApi();
        });

    }

    private void deleteApi() {

        String url = "https://312013ls1j.execute-api.ap-south-1.amazonaws.com/test/deleteatodo";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("todoId",id);

            RequestQueue requestQueue = Volley.newRequestQueue(TodoDetailsActivity.this);

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    jsonObject, response -> {

                        progressDialog.dismiss();
                        Toast.makeText(TodoDetailsActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Success");
                        onBackPressed();

                    }, error -> {
                progressDialog.dismiss();
                Toast.makeText(TodoDetailsActivity.this, "Failed to delete!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onResponse: Failed"+error.getMessage());

            }
            );

            requestQueue.add(objectRequest);

        } catch (Exception e){
            Log.d(TAG, "deleteApi: "+e.getMessage());
        }

    }

    private void updateApi() {

        int status = 0;
        if (checkBox.isChecked()) status = 1;

        String url = "https://gw9efgc4sb.execute-api.ap-south-1.amazonaws.com/test/updatetodo";

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("todoId", id);
            jsonObject.put("title", title.getText().toString().trim());
            jsonObject.put("description", description.getText().toString().trim());
            jsonObject.put("status", status);

            RequestQueue requestQueue = Volley.newRequestQueue(TodoDetailsActivity.this);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url,
                    jsonObject, response -> {

                        progressDialog.dismiss();
                        Toast.makeText(TodoDetailsActivity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Success");
                        onBackPressed();

                    }, error -> {
                progressDialog.dismiss();
                Toast.makeText(TodoDetailsActivity.this, "Failed to update!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onResponse: Failed"+error.getMessage());

            }
            );


            requestQueue.add(objectRequest);

        } catch (Exception e){
            Log.d(TAG, "updateApi: "+e.getMessage());
        }

    }

}