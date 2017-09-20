package com.prudent.fms.utils.Calender;

/**
 * Created by HCL on 02-10-2016.
 */
public class EventModel {

    private String strDate;
    private String strStartTime;
    private String strEndTime;
    private String strName;

    public String getStrColor() {
        return strColor;
    }

    public EventModel setStrColor(String strColor) {
        this.strColor = strColor;
        return this;
    }

    private String strColor;
    private int image = -1;


    public EventModel(String strDate, String strStartTime, String strEndTime, String strName,String strColor) {
        this.strDate = strDate;
        this.strStartTime = strStartTime;
        this.strEndTime = strEndTime;
        this.strName = strName;
        this.strColor = strColor;
    }

    public EventModel(String strDate, String strStartTime, String strEndTime, String strName,String strColor, int image) {
        this.strDate = strDate;
        this.strStartTime = strStartTime;
        this.strEndTime = strEndTime;
        this.strName = strName;
        this.image = image;
        this.strColor = strColor;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrStartTime() {
        return strStartTime;
    }

    public void setStrStartTime(String strStartTime) {
        this.strStartTime = strStartTime;
    }

    public String getStrEndTime() {
        return strEndTime;
    }

    public void setStrEndTime(String strEndTime) {
        this.strEndTime = strEndTime;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
