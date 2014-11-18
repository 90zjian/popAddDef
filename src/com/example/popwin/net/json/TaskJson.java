package com.example.popwin.net.json;

import org.json.JSONArray;

/**
 * 浠诲姟锛屽鎺ユ帴锟�?
 * 
 * @author leoliu
 * 
 */
public class TaskJson {

//    public static final String TASKTYPE = "taskType";
    public static final String URLS = "urls";
    public static final String DATA = "data";
//    public static final String NEXTTIME = "nextTime";
    
//    private int taskType;

    private String urls;

    private String data;


    // minutes
//    private int nextTime = 30;

//    public int getTaskType() {
//        return taskType;
//    }
//
//    public void setTaskType(int taskType) {
//        this.taskType = taskType;
//    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

//    public int getNextTime() {
//        return nextTime;
//    }
//
//    public void setNextTime(int nextTime) {
//        this.nextTime = nextTime;
//    }

}
