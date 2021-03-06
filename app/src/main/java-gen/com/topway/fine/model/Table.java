package com.topway.fine.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "Tables".
 */
public class Table implements Parcelable {

    private Long id;
    private String name;
    private String description;

    public Table() {
    }

    public Table(Long id) {
        this.id = id;
    }

    public Table(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setDefaultValue() {
        if (this.id == null )
           this.id = new Long(0);
        if (this.name == null )
           this.name = "";
        if (this.description == null )
           this.description = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    //Parcelable
    protected Table (Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
    }

    public static final Creator<Table> CREATOR = new Creator<Table>() {
        @Override
        public Table createFromParcel(Parcel in) {
            return new Table(in);
        }

        @Override
        public Table[] newArray(int size) {
            return new Table[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
    }
    //Parcelable END

    //JsonSerializable
    public static Table fromJson(JSONObject jsonObject) throws JSONException {
        Table domain = new Table();

        domain.id = jsonObject.getLong("id");
        domain.name = jsonObject.getString("NAME");
        domain.description = jsonObject.getString("DESCRIPTION");

        return domain;
    }

    public static String toJson(Table obj) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",obj.id);
        jsonObject.put("NAME",obj.name);
        jsonObject.put("DESCRIPTION",obj.description);
        return jsonObject.toString();
    }
    //JsonSerializable END

}
