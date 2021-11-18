package com.amplify.amplify;

public class Data {

    private String image;      // 영화이름
    private String time;      // 누적관객수
    private String serial;       // 영화개봉일

    public String getimage() {return image;}
    public void setimage(String image) {this.image = image;}

    public String gettime() {return time;}
    public void settime(String time) {this.time = time;}


    public String getserial() {return serial;}
    public void setserial(String serial) {this.serial = serial;}


    public Data(String image, String time, String serial) {
        this.image = image;
        this.time = time;
        this.serial = serial;
    }

    public Data () {
        this.image = image;
        this.time = time;
        this.serial = serial;
    }
}