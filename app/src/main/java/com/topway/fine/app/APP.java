package com.topway.fine.app;

/**
 * 各个全局常量定义
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class APP {

    // 请求定义
    public static final int REQUEST_PHOTO_GALLERY = 100;// 从相册中选择
    public static final int REQUEST_PHOTO_CUT = 101; // 结果
    public static final int REQUEST_BRAND_SELECT = 102;
    public static final int REQUEST_CATEGORY_SELECT = 103;
    public static final int REQUEST_SERIE_SELECT = 104;
    public static final int REQUEST_ENGINE_SELECT = 105;
    public static final int REQUEST_CONTEXT_MENU = 106;
    public static final int REQUEST_ITEM_EDIT= 107;
    public static final int REQUEST_BRAND_EDIT= 108;
    public static final int REQUEST_CATEGORY_MULTISELECT = 109;
    public static final int REQUEST_MANUFACURER_SELECT = 110;
    public static final int REQUEST_REGISTER = 111;
    public static final int REQUEST_LOGIN = 112;
    public static final int REQUEST_LOOUT = 113;
    public static final int REQUEST_NICKNAME_MODIFY = 114;
    public static final int REQUEST_PHOTO_SELECT = 115;
    public static final int REQUEST_ALBUM_SELECT = 116;
    public static final int REQUEST_PHOTO_PREVIEW = 117;
    public static final int REQUEST_CAMERA = 118;

    public static final int RESULT_ITEM_EDIT = 200;
    public static final int RESULT_ITEM_DELETE = 201;


    // 分类定义
    public static final int CATEGORY_UNKNOW = 0;
    public static final int ROOT_CATEGORY = 1;
    public static final int CATEGORY_MACHINE = 2;
    public static final int CATEGORY_PART = 3;
    public static final int CATEGORY_ENGINE = 4;
    public static final int CATEGORY_EXCAVATOR = 100;
    public static final int CATEGORY_ENGINE_PART = 200;
    public static final int UNKNOW_ENGINE = 0;

    // 上下文菜单
    public static final int CONTEXT_CATEGORY_MENU = 1;
    public static final int CONTEXT_BRAND_MENU = 2;
    public static final int CONTEXT_SERIE_MENU = 3;
    public static final int CONTEXT_ENGINE_MENU = 4;
    public static final int CONTEXT_PART_MENU = 5;
    public static final int CONTEXT_GOOD_MENU = 6;

    // 品牌定义
    public static final int BRAND_ALL = 0;
    public static final int BRAND_COMMON = 1;
    public static final int BRAND_FOREIGN = 2;
    public static final int BRAND_CHINA = 3;
    public static final int BRAND_KOMATSU = 1;

    // 系列机型
    public static final int SERIE_ALL = 0;
    public static final int SERIE_COMMON = 1;

    // 发动机机型
    public static final int ENGINE_ALL = 0;
    public static final int ENGINE_COMMON = 1;

    // 公司定义
    public static final int COMPANY_COMMON = 1;

    // 表定义
    public static final long BRAND_TABLE = 1;
    public static final long CATEGORY_TABLE = 2;
    public static final long SERIE_TABLE = 3;
    public static final long ENGINE_TABLE = 4;
    public static final long PRODUCT_TABLE = 5;

    // 搜索定义
    public static final String SEARCH_ALL = "0";
    public static final String SEARCH_BRAND = "1";
    public static final String SEARCH_CATEGORY = "2";
    public static final String SEARCH_SERIE = "3";
    public static final String SEARCH_PRODUCT = "4";
    public static final String SEARCH_CATEGORY_PATH = "5";

    // Acitity之间传递参数和操作
    public static final int ENTITY_NOOP = 0;
    public static final int ENTITY_ADD = 1;
    public static final int ENTITY_EDIT = 2;
    public static final int ENTITY_PID = 3;
    public static final int ENTITY_PART = 4;
    public static final int ENTITY_SELECT = 5;
    public static final int ENTITY_KEY = 5;
    public static final int ENTITY_NOPOS = -1;
}
