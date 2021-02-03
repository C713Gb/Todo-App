package com.example.todoapp.Models;

public class Todo {

    private final String todoId;
    private final String title;
    private final String description;
    private final int status;
    private final String createdAt;

    public Todo(String todoId, String title, String description, int status, String createdAt) {
        this.todoId = todoId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getTodoId() {
        return todoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
