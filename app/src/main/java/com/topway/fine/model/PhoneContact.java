package com.topway.fine.model;


import java.util.ArrayList;
import java.util.List;

/**
 * andriod系统联系人数据
 * @version 1.0
 * @created 2016-3-1
 */
public class PhoneContact {
    public int id;
    public String contact_id;
    public String name;
    public String alpha;
    public String pinyin;
    public ArrayList<PhoneData> data;

    public PhoneContact() {
        data = new ArrayList<PhoneData>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = (ArrayList<PhoneData>)data;
    }

    public String getNickName() {
        String nickname = "";
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).type.contains("/nickname")) {
                nickname = data.get(i).data1;
                break;
            }
        }
        return nickname;
    }

}