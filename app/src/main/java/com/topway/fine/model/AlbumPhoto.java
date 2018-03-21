package com.topway.fine.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 相册图片
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-1
 */
public class AlbumPhoto implements Parcelable {

    // 相册图片id
    private int id;

    // 相册图片路径
    private String path;

    public AlbumPhoto() {
    }

    public AlbumPhoto(int id, String path) {
        this.id = id;
        this.path = path;
    }

    protected AlbumPhoto(Parcel in) {
        id = in.readInt();
        path = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AlbumPhoto))
            return false;

        AlbumPhoto photo = (AlbumPhoto)obj;
        return !(path != null ? !path.equalsIgnoreCase(photo.path) : photo.path != null);
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
    }

    public static final Creator<AlbumPhoto> CREATOR = new Creator<AlbumPhoto>() {
        @Override
        public AlbumPhoto createFromParcel(Parcel in) {
            return new AlbumPhoto(in);
        }

        @Override
        public AlbumPhoto[] newArray(int size) {
            return new AlbumPhoto[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
