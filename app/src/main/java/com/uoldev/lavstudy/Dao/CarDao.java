package com.uoldev.lavstudy.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uoldev.lavstudy.Bean.CarBean;

/**
 * Created by Carlos Matheus on 21/04/2016.
 */
public class CarDao extends SQLiteOpenHelper {

    public static final int VERSAO = 1;
    public static final String TABELA = "veiculo";
    public static final String DATABASE = "LAVSTUDY.DB";
    private CarBean CarBean;

    public CarDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public CarDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version,
                    DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    public CarDao(Context context){
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CRIANDO TABELA " + TABELA
                + "(id INTEGER PRIMARY KEY, "
                + "modeloVc TEXT, "
                + "placaVc TEXT, "
                + "corVc TEXT) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABELA;
        db.execSQL(sql);
        onCreate(db);
    }

    public void salve(CarBean CarBean){
        reset();
        ContentValues valores = new ContentValues();

        valores.put("id", CarBean.getId());
        valores.put("modeloVc", CarBean.getModeloVc());
        valores.put("placaVc", CarBean.getPlacaVc());
        valores.put("corVc", CarBean.getCorVc());

        getWritableDatabase().insert(TABELA, null, valores);
    }

    public boolean isEmpy(){
        String sql = "Select * from veiculo";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        if(cursor.getCount() == 0){
            return true;
        }else {
            return false;
        }
    }

    public CarBean consult(){
        CarBean CarBean = new CarBean();
        String sql = "Select * from veiculo";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        try {
            while (cursor.moveToNext()) {

                CarBean.setId(cursor.getInt(0));
                CarBean.setModeloVc(cursor.getString(1));
                CarBean.setPlacaVc(cursor.getString(2));
                CarBean.setCorVc(cursor.getString(3));

            }
        } catch (android.database.SQLException sqle) {
        } finally {
            cursor.close();
        }

        return CarBean;
    }

    public void reset() {
        String sql = "DROP TABLE IF EXISTS " + TABELA;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        onCreate(db);
        db.close();
    }
}
