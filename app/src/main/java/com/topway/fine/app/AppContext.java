package com.topway.fine.app;

import java.util.Properties;
import java.util.UUID;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.fresco.helper.Phoenix;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.squareup.leakcanary.LeakCanary;
import com.topway.fine.api.ApiHttpClient;
import com.topway.fine.base.BaseApplication;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.DaoSession;
import com.topway.fine.model.UserInfo;
import com.topway.fine.utils.StringUtils;
import static com.topway.fine.app.AppConfig.KEY_FRITST_START;
import static com.topway.fine.app.AppConfig.KEY_LOGIN_AUTO;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * @version 1.0
 * @created 2016-3-1
 */

public class AppContext extends BaseApplication {

    private static AppContext app;

    private int loginUid;
    private boolean login;
    private DaoSession daoSession;

    public AppContext() {
        app = this;
    }

    public static synchronized AppContext getInstance() {
        if (app == null) {
            app = new AppContext();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        LeakCanary.install(this);
        registerUncaughtExceptionHandler();

        // 初始化图片缓存
        initImageCache();

        // 初始化数据库
        initDatabase();

        // 初始化网络请求
        initHttpClient();
    }

    /**
     * 初始化图片缓存
     */
    private void initImageCache() {
        //Fresco.initialize(this);
        Phoenix.init(this);
    }

    /**
     * 初始化本地sqlite数据库
     */
    private void initDatabase() {
        // 初始化Sqlite数据库
        //DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "gf.db", null);
        //SQLiteDatabase db = helper.getWritableDatabase();
        //FileUtils.copyFile(new File(db.getPath()),new File("/sdcard/guangfu/gf.db"));
        DatabaseHelper.instance().init();
    }

    /**
     * 初始化网络客户端请求
     */
    private void initHttpClient() {
        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        client.setMaxRetriesAndTimeout(2,3000);
        client.setCookieStore(cookieStore);
        ApiHttpClient.setHttpClient(client);
        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));
    }

    /**
     * 注册App异常崩溃处理器
     */
    private void registerUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }

    /**
     * APP的Property操作函数
     */
    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res == null ? "" : res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 保存登录信息
     *
     * @param user 用户信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final UserInfo user) {
        this.login = true;
        this.loginUid = user.getId();
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getId()));
                setProperty("user.name", user.getName());
                setProperty("user.pwd",user.getPasswd());
            }
        });
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final UserInfo user) {
        setProperties(new Properties() {
            {
                setProperty("user.id", String.valueOf(user.getId()));
                setProperty("user.name", user.getName());
                setProperty("user.pwd", user.getPasswd());
            }
        });
    }

    /**
     * 获得登录用户的信息
     *
     * @return
     */
    public UserInfo getLoginUser() {
        UserInfo user = new UserInfo();
        user.setId(StringUtils.toInt(getProperty("user.uid"), 0));
        user.setName(getProperty("user.name"));
        user.setPasswd(getProperty("user.pwd"));
        return user;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.login = false;
        this.loginUid = 0;
        removeProperty("user.uid", "user.name", "user.pwd");
    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    /**
     * 设置是否第一次登录
     */
    public static boolean isLogout() {
        return get(KEY_FRITST_START, true);
    }

    public static void setFristStart(boolean frist) {
        set(KEY_FRITST_START, frist);
    }

    public static boolean isFristStart() {
        return get(KEY_FRITST_START, true);
    }

    /**
     * 第一次登录成功,以后自动登录
     */
    public static void setAuotLgoin(boolean auto) {
        set(KEY_LOGIN_AUTO, auto);
    }

    public static boolean isAuotLgoin() {
        return get(KEY_LOGIN_AUTO, false);
    }

    /**
     * 退出当前登录,需要重新登录
     */
    public void logout() {
        cleanLoginInfo();
        set(KEY_LOGIN_AUTO, false);
    }

}