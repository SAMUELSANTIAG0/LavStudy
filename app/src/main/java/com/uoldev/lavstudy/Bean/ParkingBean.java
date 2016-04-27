package com.uoldev.lavstudy.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Samuel Santiago on 15/04/2016.
 */
public class ParkingBean {

    private Double latitude;
    private Double longitude;
    private Date date;

    public ParkingBean(){

    }

    public ParkingBean(Double latitude, Double longitude, Date date){
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(this.date);
    }
}
