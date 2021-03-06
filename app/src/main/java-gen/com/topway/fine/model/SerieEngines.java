package com.topway.fine.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "SerieEngine".
 */
public class SerieEngines implements Parcelable {

    private Long id;
    private Long serieId;
    private String serieName;
    private Long engineId;
    private String engineName;
    private Long brandId;
    private String brandName;
    private String description;
    private Long hot;

    public SerieEngines() {
    }

    public SerieEngines(Long id) {
        this.id = id;
    }

    public SerieEngines(Long id, Long serieId, String serieName, Long engineId, String engineName, Long brandId, String brandName, String description, Long hot) {
        this.id = id;
        this.serieId = serieId;
        this.serieName = serieName;
        this.engineId = engineId;
        this.engineName = engineName;
        this.brandId = brandId;
        this.brandName = brandName;
        this.description = description;
        this.hot = hot;
    }

    public void setDefaultValue() {
        if (this.id == null )
           this.id = new Long(0);
        if (this.serieId == null )
           this.serieId = new Long(0);
        if (this.serieName == null )
           this.serieName = "";
        if (this.engineId == null )
           this.engineId = new Long(0);
        if (this.engineName == null )
           this.engineName = "";
        if (this.brandId == null )
           this.brandId = new Long(0);
        if (this.brandName == null )
           this.brandName = "";
        if (this.description == null )
           this.description = "";
        if (this.hot == null )
           this.hot = new Long(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSerieId() {
        return serieId;
    }

    public void setSerieId(Long serieId) {
        this.serieId = serieId;
    }

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    public Long getEngineId() {
        return engineId;
    }

    public void setEngineId(Long engineId) {
        this.engineId = engineId;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getHot() {
        return hot;
    }

    public void setHot(Long hot) {
        this.hot = hot;
    }


    //Parcelable
    protected SerieEngines (Parcel in) {
        id = in.readLong();
        serieId = in.readLong();
        serieName = in.readString();
        engineId = in.readLong();
        engineName = in.readString();
        brandId = in.readLong();
        brandName = in.readString();
        description = in.readString();
        hot = in.readLong();
    }

    public static final Creator<SerieEngines> CREATOR = new Creator<SerieEngines>() {
        @Override
        public SerieEngines createFromParcel(Parcel in) {
            return new SerieEngines(in);
        }

        @Override
        public SerieEngines[] newArray(int size) {
            return new SerieEngines[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(serieId);
        dest.writeString(serieName);
        dest.writeLong(engineId);
        dest.writeString(engineName);
        dest.writeLong(brandId);
        dest.writeString(brandName);
        dest.writeString(description);
        dest.writeLong(hot);
    }
    //Parcelable END

    //JsonSerializable
    public static SerieEngines fromJson(JSONObject jsonObject) throws JSONException {
        SerieEngines domain = new SerieEngines();

        domain.id = jsonObject.getLong("id");
        domain.serieId = jsonObject.getLong("SERIE_ID");
        domain.serieName = jsonObject.getString("SERIE_NAME");
        domain.engineId = jsonObject.getLong("ENGINE_ID");
        domain.engineName = jsonObject.getString("ENGINE_NAME");
        domain.brandId = jsonObject.getLong("BRAND_ID");
        domain.brandName = jsonObject.getString("BRAND_NAME");
        domain.description = jsonObject.getString("DESCRIPTION");
        domain.hot = jsonObject.getLong("HOT");

        return domain;
    }

    public static String toJson(SerieEngines obj) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",obj.id);
        jsonObject.put("SERIE_ID",obj.serieId);
        jsonObject.put("SERIE_NAME",obj.serieName);
        jsonObject.put("ENGINE_ID",obj.engineId);
        jsonObject.put("ENGINE_NAME",obj.engineName);
        jsonObject.put("BRAND_ID",obj.brandId);
        jsonObject.put("BRAND_NAME",obj.brandName);
        jsonObject.put("DESCRIPTION",obj.description);
        jsonObject.put("HOT",obj.hot);
        return jsonObject.toString();
    }
    //JsonSerializable END

}
