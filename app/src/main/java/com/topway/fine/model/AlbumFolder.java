package com.topway.fine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册文件夹
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-1
 */

public class AlbumFolder {

    // 文件夹名
    private String name;

    // 文件夹路径
    private String path;

    // 图片数量
    private int count;

    // 该文件夹下图片列表
    private List<AlbumPhoto> photos;

    // 标识是否选中该文件夹
    private boolean isSelected;

    public AlbumFolder() {
        photos = new ArrayList<AlbumPhoto>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<AlbumPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<AlbumPhoto> photos) {
        this.photos = photos;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void add(AlbumPhoto photo) {
        photos.add(photo);
        count++;
    }

    public AlbumPhoto getCover() {
        if (photos.size() > 0)
            return photos.get(0);
        return null;
    }

}
