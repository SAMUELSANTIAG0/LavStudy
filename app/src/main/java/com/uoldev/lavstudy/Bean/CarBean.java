package com.uoldev.lavstudy.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Carlos Matheus.
 */
public class CarBean implements Parcelable {
    private int id;
    private String modeloVc;
    private String placaVc;
    private String corVc;

    public CarBean(Parcel in) {
        readFromParcelable(in);
    }

    private void readFromParcelable(Parcel in) {
        id = in.readInt();
        modeloVc = in.readString();
        placaVc = in.readString();
        corVc = in.readString();
    }

    public CarBean(){
    }

    public CarBean(String modeloVc, String placaVc, String corVc){
        this.modeloVc = modeloVc;
        this.placaVc = corVc;
        this.corVc = corVc;
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public CarBean createFromParcel(Parcel in) {
            return new CarBean(in);
        }

        public CarBean[] newArray(int size) {
            return new CarBean[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(modeloVc);
        dest.writeString(placaVc);
        dest.writeString(corVc);
    }

    public String toString() {
        return modeloVc  + " " + corVc +" (" +placaVc+ ")";
    }

    public static Parcelable.Creator getCREATOR(){
        return CREATOR;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModeloVc() {
        return modeloVc;
    }

    public void setModeloVc(String modeloVc) {
        this.modeloVc = modeloVc;
    }

    public String getPlacaVc() {
        return placaVc;
    }

    public void setPlacaVc(String placaVc) {
        this.placaVc = placaVc;
    }

    public String getCorVc() {
        return corVc;
    }

    public void setCorVc(String corVc) {
        this.corVc = corVc;
    }

}
