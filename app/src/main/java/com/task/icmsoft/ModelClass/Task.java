package com.task.icmsoft.ModelClass;

public class Task {
    private int id;
    private String task;
    private boolean isDone;
    private String time;
    private String category;

    public Task() {
    }

    public Task(String task, boolean isDone, String time, String category) {
        this.task = task;
        this.isDone = isDone;
        this.time = time;
        this.category = category;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}

