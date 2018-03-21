package com.topway.fine.db;

import android.database.sqlite.SQLiteDatabase;

import com.topway.fine.app.APP;
import com.topway.fine.model.Brand;
import com.topway.fine.model.BrandDao;
import com.topway.fine.model.Category;
import com.topway.fine.model.CategoryDao;
import com.topway.fine.model.CategoryGroup;
import com.topway.fine.model.CategoryGroupDao;
import com.topway.fine.model.Company;
import com.topway.fine.model.CompanyDao;
import com.topway.fine.model.DaoMaster;
import com.topway.fine.model.DaoSession;
import com.topway.fine.model.Engine;
import com.topway.fine.model.EngineDao;
import com.topway.fine.model.Manufacturer;
import com.topway.fine.model.ManufacturerDao;
import com.topway.fine.model.Part;
import com.topway.fine.model.PartDao;
import com.topway.fine.model.Photo;
import com.topway.fine.model.PhotoDao;
import com.topway.fine.model.Product;
import com.topway.fine.model.ProductDao;
import com.topway.fine.model.Sequnce;
import com.topway.fine.model.Serie;
import com.topway.fine.model.SerieDao;
import com.topway.fine.utils.FileUtils;

import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * @desc 数据访问封装类
 * @author linweixuan@gmail.com
 * @create 2016-3-1
 * @version 1.0
 */
public class DatabaseHelper {

    private static final String DATABASE_PATH = "/sdcard/guangfu/";
    private static final String DATABASE_NAME = "gf.db";
    private static final int DATABASE_VERSION = 1;
    private static final long SEQUENCE_START = 10000;
    public static final int PAGE_SIZE = 20;
    public static final String TABLE_BRANDS = "Brands";

    private static DatabaseHelper instance;
    SQLiteDatabase db;
    DaoMaster master;
    private DaoSession session;

    private DatabaseHelper() {

    }

    public static synchronized DatabaseHelper instance() {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper();
            }
        }

        return instance;
    }

    /**
     * 创建Sqlite数据库和会话
     */
    public void init() {
        FileUtils.createDirectory("guangfu");
        //FileUtils.importDatabase(context, DATABASE_NAME, R.raw.database);
        db = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH + DATABASE_NAME, null);
        master = new DaoMaster(db);
        session = master.newSession();
    }

    public DaoSession getSession() {
        return session;
    }

    public void upgrade() {
    }

    public void close() {
    }

    public long getNextSequnce() {
        Sequnce next = new Sequnce();
        long id = session.getSequnceDao().insert(next);
        return next.getId() + SEQUENCE_START;
    }

    /**
     * 获取单表分页数据
     */
    public static List get(String table, int page) {
        List result = null;
        DatabaseHelper helper = DatabaseHelper.instance();
        if (table.equals(BrandDao.TABLENAME)) {
            result = helper.getBrands(page, PAGE_SIZE);
        }
        else if (table.equals(SerieDao.TABLENAME)) {
            result = helper.getSeries(page, PAGE_SIZE);
        }
        else if (table.equals(CategoryDao.TABLENAME)) {
            result = helper.getCategories(page, PAGE_SIZE);
        }
        else if (table.equals(EngineDao.TABLENAME)) {
            result = helper.getEngines(page);
        }
        else if (table.equals(PartDao.TABLENAME)) {
            result = helper.getParts(page, PAGE_SIZE);
        }

        return result;
    }

    /**
     * 品牌数据实体CURD函数
     */
    public List getAllBrands() {
        return session.getBrandDao().queryBuilder()
                .orderDesc(BrandDao.Properties.Hot)
                .list();
    }

    public List getAllCommonBrands() {
        return session.getBrandDao().queryBuilder()
                .where(BrandDao.Properties.Common.gt(0))
                .orderDesc(BrandDao.Properties.Hot)
                .list();
    }

    public List getBrands(int page, int size) {
        // 如果离线则从sqlite中读取数据
        //List<Brand> brands = new ArrayList<Brand>();
        return session.getBrandDao().queryBuilder()
                .orderDesc(BrandDao.Properties.Hot)
                .offset(page * size).limit(size).list();
    }

    public List getBrandsBy(long condition, int page) {

        if (condition == APP.BRAND_ALL) {
            return session.getBrandDao().queryBuilder()
                    .orderDesc(BrandDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }
        else if (condition == APP.BRAND_COMMON) {
            return session.getBrandDao().queryBuilder()
                    .where(BrandDao.Properties.Common.gt(0))
                    .orderDesc(BrandDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }
        else if (condition == APP.BRAND_FOREIGN) {
            return session.getBrandDao().queryBuilder()
                    .where(BrandDao.Properties.ZoneId.eq(1))
                    .orderDesc(BrandDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }
        else if (condition == APP.BRAND_CHINA) {
            return session.getBrandDao().queryBuilder()
                    .where(BrandDao.Properties.ZoneId.eq(2))
                    .orderDesc(BrandDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }
        return null;
    }

    public List getBrandsBy(long condition, String keyword, int page) {

        if (condition == APP.BRAND_ALL) {
            return session.getBrandDao().queryBuilder()
                    .where(BrandDao.Properties.Name.like("%" + keyword + "%"))
                    .orderDesc(BrandDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        } else if (condition == APP.BRAND_COMMON) {
            return session.getBrandDao().queryBuilder()
                    .where(BrandDao.Properties.Common.gt(0),
                            BrandDao.Properties.Name.like("%" + keyword + "%"))
                    .orderDesc(BrandDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        } else if (condition == APP.BRAND_FOREIGN) {
            return session.getBrandDao().queryBuilder()
                    .where(BrandDao.Properties.ZoneId.eq(1),
                            BrandDao.Properties.Name.like("%" + keyword + "%"))
                    .orderDesc(BrandDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        } else if (condition == APP.BRAND_CHINA) {
            return session.getBrandDao().queryBuilder()
                    .where(BrandDao.Properties.ZoneId.eq(2),
                            BrandDao.Properties.Name.like("%" + keyword + "%"))
                    .orderDesc(BrandDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }
        return null;
    }

    public Brand getBrand(long id) {
        Brand item = null;
        List result = session.getBrandDao().queryBuilder()
                .where(BrandDao.Properties.Id.eq(id)).list();
        if (result != null && result.size() > 0) {
            item = (Brand)result.get(0);
        }
        return item;
    }

    public void setBrandTop(Brand item) {
        Brand max = null;
        List result = session.getBrandDao().queryBuilder()
                .orderDesc(BrandDao.Properties.Hot).list();
        if (result != null && result.size() > 0) {
            max = (Brand)result.get(0);
            item.setHot(max.getHot() + 1);
            session.getBrandDao().update(item);
        }
    }

    public void setBrandCommon(Brand item) {
        item.setCommon(new Long(1));
        session.getBrandDao().update(item);
    }

    public long saveBrand(Brand entity) {
        return session.getBrandDao().insertOrReplace(entity);
    }

    public void deleteBrand(Brand entity) {
        session.getBrandDao().delete(entity);
    }


    /**
     * 制造商CURD函数
     */
    public List getManufacturers(int page, int size) {
        return session.getManufacturerDao().queryBuilder()
                .orderDesc(ManufacturerDao.Properties.Hot)
                .offset(page * size).limit(size).list();
    }

    public List getManufacturersBy(long categoryId, long condition, int page) {
        if (condition == APP.BRAND_ALL) {
            return session.getManufacturerDao().queryBuilder()
                    .where(ManufacturerDao.Properties.CategoryId.eq(categoryId))
                    .orderDesc(ManufacturerDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }
        else if (condition == APP.BRAND_COMMON) {
            return session.getManufacturerDao().queryBuilder()
                    .where(ManufacturerDao.Properties.CategoryId.eq(categoryId),
                           ManufacturerDao.Properties.Common.gt(0))
                    .orderDesc(ManufacturerDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }
        else if (condition == APP.BRAND_FOREIGN) {
            /*
            QueryBuilder<Manufacturer> builder = session.getManufacturerDao().queryBuilder();
            builder.join(ManufacturerDao.Properties.BrandId, Brand.class)
                    .where(BrandDao.Properties.ZoneId.eq(1),
                           ManufacturerDao.Properties.CategoryId.eq(categoryId));
            builder.orderDesc(ManufacturerDao.Properties.Hot);
            builder.offset(page * PAGE_SIZE).limit(PAGE_SIZE);
            return builder.list();
            */

            Query query = session.getManufacturerDao().queryRawCreate(
                    " LEFT JOIN Brands T0 ON T.BRAND_ID = T0._id" +
                    " WHERE T0.ZONE_ID = 1" + " AND T.CATEGORY_ID = " + categoryId +
                    " LIMIT " + PAGE_SIZE + " OFFSET " + PAGE_SIZE*page );
            return query.list();
        }
        else if (condition == APP.BRAND_CHINA) {
            /*
            QueryBuilder<Manufacturer> builder = session.getManufacturerDao().queryBuilder();
            builder.join(ManufacturerDao.Properties.BrandId, Brand.class, BrandDao.Properties.Id)
                    .where(BrandDao.Properties.ZoneId.eq(2),
                            ManufacturerDao.Properties.CategoryId.eq(categoryId));
            builder.orderDesc(ManufacturerDao.Properties.Hot);
            builder.offset(page * PAGE_SIZE).limit(PAGE_SIZE);
            return builder.list();
            */
            Query query = session.getManufacturerDao().queryRawCreate(
                    " LEFT JOIN Brands T0 ON T.BRAND_ID = T0._id " +
                    " WHERE T0.ZONE_ID = 2" + " AND T.CATEGORY_ID = " + categoryId +
                    " LIMIT " + PAGE_SIZE + " OFFSET " + PAGE_SIZE*page );
            return query.list();
        }
        return null;
    }

    public Manufacturer getManufacturer(long id) {
        Manufacturer item = null;
        List result = session.getManufacturerDao().queryBuilder()
                .where(ManufacturerDao.Properties.Id.eq(id)).list();
        if (result != null && result.size() > 0) {
            item = (Manufacturer)result.get(0);
        }
        return item;
    }

    public void setManufacturerTop(Manufacturer item) {
        Manufacturer max = null;
        List result = session.getManufacturerDao().queryBuilder()
                .orderDesc(ManufacturerDao.Properties.Hot).list();
        if (result != null && result.size() > 0) {
            max = (Manufacturer)result.get(0);
            item.setHot(max.getHot() + 1);
            session.getManufacturerDao().update(item);
        }
    }

    public void setManufacturerCommon(Manufacturer item) {
        item.setCommon(new Long(1));
        session.getManufacturerDao().update(item);
    }

    public long saveManufacturer(Manufacturer entity) {
        return session.getManufacturerDao().insertOrReplace(entity);
    }

    public void deleteManufacturer(Manufacturer entity) {
        session.getManufacturerDao().delete(entity);
    }



    /**
     * 挖机品牌型号系列CURD函数
     */
    public List getSeries(int page, int size) {
        return session.getSerieDao().queryBuilder()
                .orderDesc(SerieDao.Properties.Hot)
                .offset(page * size).limit(size).list();
    }

    public Serie getSerie(int id) {
        Serie item = null;
        List result = session.getSerieDao().queryBuilder()
                .where(SerieDao.Properties.Id.eq(id)).list();
        if (result != null && result.size() > 0) {
            item = (Serie)result.get(0);
        }
        return item;
    }

    public List getBrandSeries(long brandId, long condition, int page) {
        List result = session.getSerieDao().queryBuilder()
                .where(SerieDao.Properties.BrandId.eq(brandId))
                .orderDesc(SerieDao.Properties.Hot)
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        return result;
    }

    public void setSerieTop(Serie item) {
        Serie max = null;
        List result = session.getSerieDao().queryBuilder()
                .orderDesc(SerieDao.Properties.Hot).list();
        if (result != null && result.size() > 0) {
            max = (Serie)result.get(0);
            item.setHot(max.getHot() + 1);
            session.getSerieDao().update(item);
        }
    }

    public List getSeriesBy(long brandId, long condition, int page) {
        if (condition == APP.SERIE_ALL) {
            return session.getSerieDao().queryBuilder()
                    .where(SerieDao.Properties.BrandId.eq(brandId))
                    .orderDesc(SerieDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }
        else if (condition == APP.SERIE_COMMON) {
            return session.getSerieDao().queryBuilder()
                    .where(SerieDao.Properties.BrandId.eq(brandId))
                    .where(SerieDao.Properties.Common.eq(1))
                    .orderDesc(SerieDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }
        return null;
    }

    public void setSerieCommon(Serie item) {
        item.setCommon(new Long(1));
        session.getSerieDao().update(item);
    }

    public long saveSerie(Serie entity) {
        return session.getSerieDao().insertOrReplace(entity);
    }

    public void deleteSerie(Serie entity) {
        session.getSerieDao().delete(entity);
    }

    /**
     * 分类目录实体CURD函数
     */
    public List getCategories(int page, int size) {
        // 1是根目录， 2是工程机械
        return session.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.Id.gt(2))
                .orderDesc(CategoryDao.Properties.Hot)
                .offset(page * size).limit(size).list();
    }

    public Category getCategory(long id) {
        Category item = null;
        List result = session.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.Id.eq(id)).list();
        if (result != null && result.size() > 0) {
            item = (Category)result.get(0);
        }
        return item;
    }

    public List getSubCategory(long id, int page) {
        List result = session.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.Pid.eq(id))
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        return result;
    }

    public long saveCategory(Category entity) {
        return session.getCategoryDao().insertOrReplace(entity);
    }

    public void deleteCategory(Category entity) {
        session.getCategoryDao().delete(entity);
    }



    /**
     * 分组实体CURD函数
     */
    public List getCategoryByGroup(long groupId, int page) {
        List result = session.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.Gid.eq(groupId))
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        return result;
    }

    public List getCategoryByFather(long parentId) {
        List result = session.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.Pid.eq(parentId))
                .list();
        return result;
    }

    public List getCategoryByFather(long parentId, int page) {
        List result = session.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.Pid.eq(parentId))
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        return result;
    }

    public List getCategoryByFather(long parentId, String keyword) {
        List result = null;
        if (keyword != null && !keyword.isEmpty()) {
            result = session.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.Pid.eq(parentId),
                            CategoryDao.Properties.Name.like("%" + keyword + "%"))
                    .list();
        }else{
            result = getCategoryByFather(parentId);
        }
        return result;
    }

    public List getCategoryByKeyword(String keyword) {
        List result = null;
        if (keyword != null && !keyword.isEmpty()) {
            result = session.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.Name.like("%" + keyword + "%"))
                    .list();
        }
        return result;
    }

    public List getCategoryByPath(String path, int page) {
        List result = session.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.Path.like(path))
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        return result;
    }

    public long setCategoryGroup(Category entity, CategoryGroup group) {
        entity.setGid(group.getId());
        return session.getCategoryDao().insertOrReplace(entity);
    }

    public long saveCategoryGroup(CategoryGroup entity) {
        return session.getCategoryGroupDao().insertOrReplace(entity);
    }

    public void deleteCategoryGroup(CategoryGroup entity) {
        session.getCategoryGroupDao().delete(entity);
    }

    /**
     * 分组实体CURD函数
     */
    public List getGroups(int page)
    {
        return session.getCategoryGroupDao().queryBuilder()
                .orderDesc(CategoryGroupDao.Properties.Hot)
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
    }

    public CategoryGroup getGroup(long id) {
        CategoryGroup item = null;
        List result = session.getCategoryGroupDao().queryBuilder()
                .where(CategoryGroupDao.Properties.Id.eq(id)).list();
        if (result != null && result.size() > 0) {
            item = (CategoryGroup)result.get(0);
        }
        return item;
    }

    public List getGroupByCategory(long categoryId, int page)
    {
        return session.getCategoryGroupDao().queryBuilder()
                .where(CategoryGroupDao.Properties.CategoryId.eq(categoryId))
                .orderDesc(CategoryGroupDao.Properties.Hot)
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
    }

    public long saveGroup(CategoryGroup entity) {
        return session.getCategoryGroupDao().insertOrReplace(entity);
    }


    /**
     * 发动机实体CURD函数
     */
    public List getEngines(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return session.getEngineDao().queryBuilder()
                    .where(EngineDao.Properties.Name.like("%" + keyword + "%"))
                    .orderDesc(EngineDao.Properties.Hot).list();
        }
        else {
            return session.getEngineDao().queryBuilder()
                    .orderDesc(EngineDao.Properties.Hot).list();
        }
    }

    public List getEngines(int page)
    {
        // 1是根目录， 2是工程机械
        return session.getEngineDao().queryBuilder()
                .orderDesc(EngineDao.Properties.Hot)
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
    }

    public Engine getEngine(long id) {
        Engine item = null;
        List result = session.getEngineDao().queryBuilder()
                .where(EngineDao.Properties.Id.eq(id)).list();
        if (result != null && result.size() > 0) {
            item = (Engine)result.get(0);
        }
        return item;
    }

    public List getEngineByBrand(long barndId)
    {
        return session.getEngineDao().queryBuilder()
                .where(EngineDao.Properties.BrandId.eq(barndId))
                .orderDesc(EngineDao.Properties.Hot).list();
    }

    public List getEngineByBrand(long barndId, long condition, int page)
    {
        return session.getEngineDao().queryBuilder()
                .where(EngineDao.Properties.BrandId.eq(barndId))
                .orderDesc(EngineDao.Properties.Hot)
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
    }

    public long saveEngine(Engine entity) {
        return session.getEngineDao().insertOrReplace(entity);
    }

    /**
     * 部件实体CURD函数
     */
    public List getParts(int page, int size) {
        // 1是根目录， 2是工程机械
        return session.getPartDao().queryBuilder()
                .orderDesc(PartDao.Properties.Hot)
                .offset(page * size).limit(size).list();
    }

    public Part getPart(long id) {
        Part item = null;
        List result = session.getPartDao().queryBuilder()
                .where(PartDao.Properties.Id.eq(id)).list();
        if (result != null && result.size() > 0) {
            item = (Part)result.get(0);
        }
        return item;
    }

    /**
     * 产品实体CURD函数
     */
    public List getProducts(String path, int page) {
        if (path == null) return null;
        List result = session.getProductDao().queryBuilder()
                .where(ProductDao.Properties.Path.eq(path))
                .orderDesc(ProductDao.Properties.Hot)
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        return result;
    }

    public List getProductsByBrand(long brandId, int page) {
        List result = session.getProductDao().queryBuilder()
                .where(ProductDao.Properties.BrandId.eq(brandId))
                .orderDesc(ProductDao.Properties.Hot)
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        return result;
    }

    public List getProductsByBrand(long brandId, String keyword, int page) {
        List result = null;
        if (keyword != null && !keyword.isEmpty()) {
            result = session.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.BrandId.eq(brandId),
                            ProductDao.Properties.Name.like("%" + keyword + "%"))
                    .orderDesc(ProductDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }else{
            result = session.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.BrandId.eq(brandId))
                    .orderDesc(ProductDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();

        }
        return result;
    }

    public List getProductsByEngine(long engineId, String keyword, int page) {
        List result = null;
        if (keyword != null && !keyword.isEmpty()) {
            result = session.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.EngineId.eq(engineId),
                            ProductDao.Properties.Name.like("%" + keyword + "%"))
                    .orderDesc(ProductDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }else{
            result = session.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.EngineId.eq(engineId))
                    .orderDesc(ProductDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();

        }
        return result;
    }

    public List getProductsBySerie(long serieId, String keyword, int page) {
        List result = null;
        if (keyword != null && !keyword.isEmpty()) {
            result = session.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.SerieId.eq(serieId),
                            ProductDao.Properties.Name.like("%" + keyword + "%"))
                    .orderDesc(ProductDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }else{
            result = session.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.SerieId.eq(serieId))
                    .orderDesc(ProductDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();

        }
        return result;
    }

    public List getProductsByCategory(long categoryId, String keyword, int page) {
        List result = null;
        if (keyword != null && !keyword.isEmpty()) {
            result = session.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.CategoryId.eq(categoryId),
                            ProductDao.Properties.Name.like("%" + keyword + "%"))
                    .orderDesc(ProductDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }else{
            result = session.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.CategoryId.eq(categoryId))
                    .orderDesc(ProductDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();

        }
        return result;
    }

    public List getProductsByCategoryPath(String path, int page) {
        List result = null;
        result = session.getProductDao().queryBuilder()
                .where(ProductDao.Properties.Path.like(path + "%"))
                .orderDesc(ProductDao.Properties.Hot)
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();

        return result;
    }

    public List getProductsByKeyword(String type, String keyword, int page) {
        List result = null;
        if (keyword != null && !keyword.isEmpty()) {
            result = session.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.Name.like("%" + keyword + "%"))
                    .orderDesc(ProductDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }
        return result;
    }

    public List getProductsByKeyword(Long brandId, Long categoryId, Long serieId, Long engineId, String keyword, int page) {
        List result = null;
        if (keyword != null && !keyword.isEmpty()) {
            result = session.getProductDao().queryBuilder()
                    .where(ProductDao.Properties.Name.like("%" + keyword + "%"),
                            ProductDao.Properties.CategoryId.eq(brandId),
                            ProductDao.Properties.CategoryId.eq(categoryId),
                            ProductDao.Properties.CategoryId.eq(serieId),
                            ProductDao.Properties.CategoryId.eq(engineId))
                    .orderDesc(ProductDao.Properties.Hot)
                    .offset(page * PAGE_SIZE).limit(PAGE_SIZE).list();
        }
        return result;
    }

    public Product findProduct(String name) {
        Product item = null;
        List result = session.getProductDao().queryBuilder()
                .where(ProductDao.Properties.Name.eq(name)).list();
        if (result.size() > 0) {
            item = (Product)result.get(0);
        }
        return item;
    }

    public Product findProduct(String name, long brandId, long categoryId, long engineId, long serieId) {
        Product item = null;
        List result = session.getProductDao().queryBuilder()
                .where(ProductDao.Properties.Name.eq(name),
                        ProductDao.Properties.BrandId.eq(brandId),
                        ProductDao.Properties.CategoryId.eq(categoryId),
                        ProductDao.Properties.EngineId.eq(engineId),
                        ProductDao.Properties.SerieId.eq(serieId))
                .list();
        if (result !=null && result.size() > 0) {
            item = (Product)result.get(0);
        }
        return item;
    }

    public List findProduct(Product condition, int page) {
        String sql = "WHERE";
        if (condition.getCategoryId() != null) {
            sql += " T.CATEGORY_ID = " + condition.getCategoryId() + " AND";
        }
        if (condition.getSerieId() != null) {
            sql += " T.SERIE_ID = " + condition.getSerieId() + " AND";
        }
        if (condition.getEngineId() != null) {
            sql += " T.ENGINE_ID = " + condition.getEngineId() + " AND";
        }
        if (condition.getBrandId() != null) {
            sql += " T.BRAND_ID = " + condition.getBrandId() + " AND";
        }
        if (condition.getName() != null && !condition.getName().isEmpty()) {
            sql += " T.NAME like '%" + condition.getName() + "%'";
        }
        if (sql.endsWith("AND"))  {
            sql = sql.substring(0, sql.length()-3);
        }
        if (sql.endsWith("WHERE"))  {
            sql += " 1";
        }

        sql += " LIMIT " + PAGE_SIZE;
        sql += " OFFSET " + PAGE_SIZE*page;

        Query query = session.getProductDao().queryRawCreate(sql);
        return query.list();
    }

    public List getNewProduct(int quantity) {
        List result = session.getProductDao().queryBuilder()
                .orderDesc(ProductDao.Properties.Id)
                .limit(quantity).list();;
        return result;
    }


    public long saveProduct(Product entity) {
        return session.getProductDao().insertOrReplace(entity);
    }

    public void deleteProduct(Product entity) {
        session.getProductDao().delete(entity);
    }

    /**
     * 部件实体CURD函数
     */
    public Part findPart(long categoryId, long serieId, long engineId) {
        Part item = null;
        String condition = "WHERE ";
        if (categoryId != 0) {
            condition = "T0.CATEGORY_ID = " + categoryId + " AND ";
        }
        if (serieId != 0) {
            condition = "T0.SERIE_ID = " + serieId + " AND ";
        }
        if (engineId != 0) {
            condition = "T0.ENGINE_ID = " + engineId;
        }
        Query query = session.getPartDao().queryRawCreate(condition);
        List data = query.list();
        if (data != null && data.size() > 0) {
            item = (Part)data.get(0);
        }
        return item;
    }

    public long savePart(Part entity) {
        return session.getPartDao().insertOrReplace(entity);
    }

    /**
     * 公司实体CURD函数
     */
    public List getCompany(String keyword) {
        List result = null;
        if (keyword != null && !keyword.isEmpty()) {
            result = session.getCompanyDao().queryBuilder()
                    .where(CompanyDao.Properties.Name.like("%" + keyword + "%"))
                    .orderAsc(CompanyDao.Properties.Alpha)
                    .list();
        }else{
            result = session.getCompanyDao().queryBuilder()
                    .orderAsc(CompanyDao.Properties.Alpha)
                    .list();
        }
        return result;
    }

    public List getCompany(String keyword, int page) {
        List result = null;
        if (keyword != null && !keyword.isEmpty()) {
            result = session.getCompanyDao().queryBuilder()
                    .where(CompanyDao.Properties.Name.like("%" + keyword + "%"))
                    .orderAsc(CompanyDao.Properties.Alpha)
                    .offset(page * PAGE_SIZE)
                    .limit(PAGE_SIZE).list();
        }else{
            result = session.getCompanyDao().queryBuilder()
                    .orderAsc(CompanyDao.Properties.Alpha)
                    .offset(page * PAGE_SIZE)
                    .limit(PAGE_SIZE).list();
        }
        return result;
    }

    public long saveCompany(Company entity) {
        return session.getCompanyDao().insertOrReplace(entity);
    }

    public List getProductPhotos(long productId) {
        List result = session.getPhotoDao().queryBuilder()
                .where(PhotoDao.Properties.PrimaryId.eq(productId))
                .list();
        return result;
    }

    public long savePhoto(Photo entity) {
        // 保存前删除产品图片
        List result = session.getPhotoDao().queryBuilder()
                .where(PhotoDao.Properties.TableId.eq(entity.getTableId()),
                        PhotoDao.Properties.PrimaryId.eq(entity.getPrimaryId()))
                .list();
        if (result !=null && result.size() > 0) {
            Photo item = (Photo)result.get(0);
            session.getPhotoDao().delete(item);
        }

        return session.getPhotoDao().insertOrReplace(entity);
    }

}

