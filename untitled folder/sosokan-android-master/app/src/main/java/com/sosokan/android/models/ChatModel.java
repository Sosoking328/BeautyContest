package com.sosokan.android.models;

/**
 * Created by AnhZin on 9/4/2016.
 */
public class ChatModel {

    private String id;


    public ChatModel(String advertiseId, UserModel userModel, String message, long timeStamp) {

        this.advertiseId = advertiseId;
        this.userModel = userModel;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(String advertiseId) {
        this.advertiseId = advertiseId;
    }

    private String advertiseId;
    private UserModel userModel;
    private String message;
    private long timeStamp;
    private FileModel file;
    private MapModel mapModel;

    public ChatModel() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public FileModel getFile() {
        return file;
    }

    public void setFile(FileModel file) {
        this.file = file;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                "mapModel=" + mapModel +
                ", file=" + file +
                ", timeStamp='" + timeStamp + '\'' +
                ", message='" + message + '\'' +
                ", userModel=" + userModel +
                '}';
    }
}
