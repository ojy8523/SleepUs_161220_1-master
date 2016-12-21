package com.example.seungkyu.dreamtodream;

/**
 * Created by SeungKyu on 2016-12-02.
 */

public class CompanyVO {
    private int c_id;
    private String c_name;
    private String c_giveTo;
    private int c_totalTime;
    private String c_imagePath;

    public CompanyVO() {

    }

    public CompanyVO(String c_name, String c_giveTo) {
        this.c_name = c_name;
        this.c_giveTo = c_giveTo;
    }

    public CompanyVO(int id, String c_name, String c_giveTo, String c_imagePath) {
        this.c_id = id;
        this.c_name = c_name;
        this.c_giveTo = c_giveTo;
        this.c_imagePath = c_imagePath;
    }

    public CompanyVO(String c_name, String c_giveTo, int c_totalTime, String c_imagePath) {
        this.c_name = c_name;
        this.c_giveTo = c_giveTo;
        this.c_totalTime = c_totalTime;
        this.c_imagePath = c_imagePath;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_giveTo() {
        return c_giveTo;
    }

    public void setC_giveTo(String c_giveTo) {
        this.c_giveTo = c_giveTo;
    }

    public int getC_totalTime() {
        return c_totalTime;
    }

    public void setC_totalTime(int c_totalTime) {
        this.c_totalTime = c_totalTime;
    }

    public String getC_imagePath() {
        return c_imagePath;
    }

    public void setC_imagePath(String c_imagePath) {
        this.c_imagePath = c_imagePath;
    }

    @Override
    public String toString() {
        return "CompanyVO{" +
                "c_name='" + c_name + '\'' +
                ", c_giveTo='" + c_giveTo + '\'' +
                ", c_totalTime=" + c_totalTime +
                ", c_imagePath='" + c_imagePath + '\'' +
                '}';
    }
}
