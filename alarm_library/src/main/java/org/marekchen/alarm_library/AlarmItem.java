package org.marekchen.alarm_library;

/**
 * Created by chenpei on 2017/12/15.
 */

public class AlarmItem {
    private String id;
    private String time;
    private String message;
    private int status;
    private int backTime = 0;
    private int type;

    public AlarmItem() {

    }

    public AlarmItem(String time, String message, int status, int type) {
        this.time = time;
        this.message = message;
        this.status = status;
        this.type = type;
    }

    public AlarmItem(String time, String message, int status, int backTime, int type) {
        this.time = time;
        this.message = message;
        this.status = status;
        this.backTime = backTime;
        this.type = type;
    }

    @Override
    public String toString() {
        return id + "," + time + "," + message + "," + status + "," + backTime + "," + type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBackTime() {
        return backTime;
    }

    public void setBackTime(int backTime) {
        this.backTime = backTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
