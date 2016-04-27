package com.uoldev.lavstudy.Bean;

import android.content.Context;

import com.uoldev.lavstudy.Dao.ParkingDao;
import com.uoldev.lavstudy.Dao.UserDao;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Samuel Santiago on 15/04/2016.
 */
public class RequestBean {

    private Long resquetNumber;
    private String service;
    private ParkingBean parkingBean;
    private String dateRequest;
    private String status;

    public RequestBean(Context context, String service){
        this.service = service;
        UserDao userDao = new UserDao(context);
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        resquetNumber = date.getTime();
        ParkingDao parkingDao = new ParkingDao(context);
        parkingBean = new ParkingBean(parkingDao.consult().latitude, parkingDao.consult().longitude, parkingDao.consultDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateRequest = dateFormat.format(date);
        status = "Pendente";

        parkingDao.close();
        userDao.close();
    }


    public Long getResquetNumber(){
        return this.resquetNumber;
    }

    public String getService() {
        return service;
    }

    public String getDateRequest() {
        return dateRequest;
    }

    public ParkingBean getParkingBean() {
        return parkingBean;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
