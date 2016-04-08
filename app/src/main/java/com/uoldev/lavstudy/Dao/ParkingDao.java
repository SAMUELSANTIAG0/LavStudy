package com.uoldev.lavstudy.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Samuel Santiago on 06/04/2016.
 */
public class ParkingDao extends SQLiteOpenHelper {

    public static final int VERSAO = 1;
    public static final String TABELA = "parking";
    public static final String DATABASE = "LAVSTUDY.DB";

    public ParkingDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public ParkingDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    public ParkingDao(Context context){
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABELA
                + "(id INTEGER PRIMARY KEY, "
                + "latitude NUMERIC, "
                + "longitude NUMERIC, "
                + "date DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(sql);
    }

    public void parked(LatLng latLng){
        reset();
        ContentValues valores = new ContentValues();
        valores.put("latitude", latLng.latitude);
        valores.put("longitude", latLng.longitude);
        valores.put("date", System.currentTimeMillis());
        getWritableDatabase().insert(TABELA, null, valores);
    }

    public LatLng consult(){
        LatLng latLng = null;
        String sql = "Select * from parking";

        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                latLng = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
            }
        } catch (android.database.SQLException sqle) {
        } finally {
            cursor.close();
        }

        return latLng;
    }

    public Date consultDate(){
        LatLng latLng = null;
        Date date = new Date();
        String sql = "Select * from parking";

        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                latLng = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
                date.setTime(cursor.getLong(3));
            }
        } catch (android.database.SQLException sqle) {
        } finally {
            cursor.close();
        }

        return date;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABELA;
        db.execSQL(sql);
        onCreate(db);
    }

    public void reset() {
        String sql = "DROP TABLE IF EXISTS " + TABELA;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        onCreate(db);
        db.close();
    }

    public boolean isEmpy(){
        String sql = "Select * from parking";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        if(cursor.getCount() == 0){
            return true;
        }else {
            return false;
        }
    }


}
