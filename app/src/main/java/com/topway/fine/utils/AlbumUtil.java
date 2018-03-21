package com.topway.fine.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;

import com.topway.fine.model.AlbumFolder;
import com.topway.fine.model.AlbumPhoto;

/**
 * 相册访问类
 * @version 1.0
 * @created 2016-3-1
 */
public class AlbumUtil {

    /**
     * 获取所有相册目录和图片
     *
     * @return List<AlbumFolder>
     */
    static public List<AlbumFolder> getAlbums(Context context) {
        Map<String, AlbumFolder> map = new HashMap<String, AlbumFolder>();
        List<AlbumFolder> albums = new ArrayList<AlbumFolder>();
        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(
                Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.DATE_ADDED,
                        MediaStore.Images.ImageColumns.SIZE},
                null, null, ImageColumns.DATE_ADDED + " DESC");

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME));
            String path = cursor.getString(cursor.getColumnIndex(ImageColumns.DATA));

            // 添加文件夹
            AlbumFolder folder;
            if (map.keySet().contains(name)) {
                folder = map.get(name);
            } else {
                // 获取文件目录
                File file = new File(path);
                File dir = file.getParentFile();

                folder = new AlbumFolder();
                folder.setName(name);
                folder.setPath(dir.getAbsolutePath());
                map.put(name, folder);
                albums.add(folder);
            }

            // 添加相片到文件夹
            AlbumPhoto photo = new AlbumPhoto();
            photo.setPath(path);
            folder.add(photo);
        }

        return albums;
    }


    /**
     * 获取对应相册下的照片
     *
     * @return 相片列表
     */
    static public List<AlbumPhoto> getAlbum(Context context, String name) {
        List<AlbumPhoto> list = new ArrayList<AlbumPhoto>();
        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(
                Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Media.DATE_ADDED,
                        ImageColumns.BUCKET_DISPLAY_NAME,
                        ImageColumns.DATA,
                        ImageColumns.DATE_ADDED,
                        ImageColumns.SIZE},
                "bucket_display_name = ?",
                new String[]{name},
                ImageColumns.DATE_ADDED + " DESC");

        while (cursor.moveToNext()) {
            AlbumPhoto photo = new AlbumPhoto();
            String path = cursor.getString(cursor.getColumnIndex(ImageColumns.DATA));
            photo.setPath(path);
            list.add(photo);
        }
        return list;
    }

    /**
     * 获取最近照片列表
     *
     * @return List<AlbumPhoto>
     */
    static public List<AlbumPhoto> getCurrent(Context context) {
        List<AlbumPhoto> list = new ArrayList<AlbumPhoto>();
        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.DATE_ADDED,
                        ImageColumns.SIZE},
                null, null, ImageColumns.DATE_ADDED + " DESC");

        while (cursor.moveToNext()) {
            AlbumPhoto photo = new AlbumPhoto();
            String path = cursor.getString(cursor.getColumnIndex(ImageColumns.DATA));
            photo.setPath(path);
            list.add(photo);
        }
        return list;
    }

    /**
     *  获取所有相册相片
     *
     * @return List<AlbumPhoto>
     */
    static public List<AlbumPhoto> getAllPhotos(List<AlbumFolder> albums) {
        List<AlbumPhoto> photos = new ArrayList<AlbumPhoto>();
        for (AlbumFolder folder : albums) {
            photos.addAll(folder.getPhotos());
        }
        return photos;
    }

    /**
     *  获取所有相册相片
     *
     * @return List<AlbumPhoto>
     */
    static public List<AlbumPhoto> getAlbumPhotos(List<AlbumFolder> albums, String name) {
        for (AlbumFolder folder : albums) {
            if (folder.getName().equals(name)) {
                return folder.getPhotos();
            }
        }
        return null;
    }

    /**
     *  获取所有相册相片
     *
     * @return List<AlbumPhoto>
     */
    static public String getCameraPath(List<AlbumFolder> albums) {
        for (AlbumFolder folder : albums) {
            if (folder.getName().equals("Camera")) {
                return folder.getPath();
            }
        }
        return null;
    }


}