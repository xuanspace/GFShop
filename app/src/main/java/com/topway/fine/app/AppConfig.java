package com.topway.fine.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.topway.fine.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 */
public class AppConfig {

    public static final String APP_NAME = "guangfu";
    public static final String APP_CONFIG = "config";
    public static final String HTTP_DOMAIN = "http://192.168.1.100:8000";
    public static final String IMAGE_DOMAIN = "http://192.168.1.100:8000/images";

    public static final String IMAGE_CATEGORY = "/category/";
    public static final String CONF_COOKIE = "cookie";
    public final static String CONF_ACCESSTOKEN = "accessToken";
    public final static String CONF_ACCESSSECRET = "accessSecret";
    public final static String CONF_EXPIRESIN = "expiresIn";
    public static final String CONF_APP_UNIQUEID = "APP_UNIQUEID";
    public static final String KEY_FRITST_START = "KEY_FRIST_START";
    public static final String KEY_LOGIN_TOKEN = "login_token";
    public static final String KEY_LOGIN_TYPE = "login_type";
    public static final String KEY_LOGIN_AUTO = "login_auto";
    public static final String SEARCH_HISTORY = "searchHistory";

    // 默认存放数据库的路径
    public final static String DEFAULT_DATABASE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "guangfu"
            + File.separator;

    // 默认存放图片的路径
    public final static String DEFAULT_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "guangfu"
            + File.separator + "image" + File.separator;

    // 默认存放文件下载的路径
    public final static String DEFAULT_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "guangfu"
            + File.separator + "download" + File.separator;

    private Context mContext;
    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }

        appConfig.initialize();
        return appConfig;
    }

    /**
     * 初始化各个目录路径
     */
    public void initialize() {
        // 在SD卡的创建目录
        FileUtils.createDirectory(APP_NAME);
    }

    public static String getImagePath() {
        return IMAGE_DOMAIN + File.separator;
    }

    public static String getImagePath(String url) {
        if (!url.startsWith("http"))
            url = IMAGE_DOMAIN + url;
        return url;
    }

    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getCookie() {
        return get(CONF_COOKIE);
    }

    public void setAccessToken(String accessToken) {
        set(CONF_ACCESSTOKEN, accessToken);
    }

    public String getAccessToken() {
        return get(CONF_ACCESSTOKEN);
    }

    public void setAccessSecret(String accessSecret) {
        set(CONF_ACCESSSECRET, accessSecret);
    }

    public String getAccessSecret() {
        return get(CONF_ACCESSSECRET);
    }

    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
             // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
             // 把config建在app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);
            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public void set(Properties ps) {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }
}
