package com.topway.fine.model;

import com.topway.fine.model.DaoSession;
import de.greenrobot.dao.DaoException;
import com.topway.fine.db.DatabaseHelper;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "Products".
 */
public class Product implements Parcelable {

    private Long id;
    private String name;
    private String description;
    private Long userId;
    private Long partId;
    private Long categoryId;
    private Long brandId;
    private Long serieId;
    private Long engineId;
    private Long supplierId;
    private String path;
    private Float price;
    private Long quantity;
    private Long status;
    private String image;
    private String code;
    private String number;
    private Long hot;
    private Long uptime;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ProductDao myDao;

    private Part part;
    private Long part__resolvedKey;

    private Category category;
    private Long category__resolvedKey;

    private Brand brand;
    private Long brand__resolvedKey;

    private Serie serie;
    private Long serie__resolvedKey;

    private Engine engine;
    private Long engine__resolvedKey;

    private Company company;
    private Long company__resolvedKey;


    public Product() {
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(Long id, String name, String description, Long userId, Long partId, Long categoryId, Long brandId, Long serieId, Long engineId, Long supplierId, String path, Float price, Long quantity, Long status, String image, String code, String number, Long hot, Long uptime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.partId = partId;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.serieId = serieId;
        this.engineId = engineId;
        this.supplierId = supplierId;
        this.path = path;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.image = image;
        this.code = code;
        this.number = number;
        this.hot = hot;
        this.uptime = uptime;
    }

    public void setDefaultValue() {
        if (this.id == null )
           this.id = new Long(0);
        if (this.name == null )
           this.name = "";
        if (this.description == null )
           this.description = "";
        if (this.userId == null )
           this.userId = new Long(0);
        if (this.partId == null )
           this.partId = new Long(0);
        if (this.categoryId == null )
           this.categoryId = new Long(0);
        if (this.brandId == null )
           this.brandId = new Long(0);
        if (this.serieId == null )
           this.serieId = new Long(0);
        if (this.engineId == null )
           this.engineId = new Long(0);
        if (this.supplierId == null )
           this.supplierId = new Long(0);
        if (this.path == null )
           this.path = "";
        if (this.price == null )
           this.price = new Float(0);
        if (this.quantity == null )
           this.quantity = new Long(0);
        if (this.status == null )
           this.status = new Long(0);
        if (this.image == null )
           this.image = "";
        if (this.code == null )
           this.code = "";
        if (this.number == null )
           this.number = "";
        if (this.hot == null )
           this.hot = new Long(0);
        if (this.uptime == null )
           this.uptime = new Long(0);
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProductDao() : null;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getSerieId() {
        return serieId;
    }

    public void setSerieId(Long serieId) {
        this.serieId = serieId;
    }

    public Long getEngineId() {
        return engineId;
    }

    public void setEngineId(Long engineId) {
        this.engineId = engineId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getHot() {
        return hot;
    }

    public void setHot(Long hot) {
        this.hot = hot;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    /** To-one relationship, resolved on first access. */
    public Part getPart() {
        Long __key = this.partId;
        if (part__resolvedKey == null || !part__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                daoSession = DatabaseHelper.instance().getSession();
                //throw new DaoException("Entity is detached from DAO context");
            }
            PartDao targetDao = daoSession.getPartDao();
            Part partNew = targetDao.load(__key);
            synchronized (this) {
                part = partNew;
            	part__resolvedKey = __key;
            }
        }
        return part;
    }

    public void setPart(Part part) {
        synchronized (this) {
            this.part = part;
            partId = part == null ? null : part.getId();
            part__resolvedKey = partId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Category getCategory() {
        Long __key = this.categoryId;
        if (category__resolvedKey == null || !category__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                daoSession = DatabaseHelper.instance().getSession();
                //throw new DaoException("Entity is detached from DAO context");
            }
            CategoryDao targetDao = daoSession.getCategoryDao();
            Category categoryNew = targetDao.load(__key);
            synchronized (this) {
                category = categoryNew;
            	category__resolvedKey = __key;
            }
        }
        return category;
    }

    public void setCategory(Category category) {
        synchronized (this) {
            this.category = category;
            categoryId = category == null ? null : category.getId();
            category__resolvedKey = categoryId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Brand getBrand() {
        Long __key = this.brandId;
        if (brand__resolvedKey == null || !brand__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                daoSession = DatabaseHelper.instance().getSession();
                //throw new DaoException("Entity is detached from DAO context");
            }
            BrandDao targetDao = daoSession.getBrandDao();
            Brand brandNew = targetDao.load(__key);
            synchronized (this) {
                brand = brandNew;
            	brand__resolvedKey = __key;
            }
        }
        return brand;
    }

    public void setBrand(Brand brand) {
        synchronized (this) {
            this.brand = brand;
            brandId = brand == null ? null : brand.getId();
            brand__resolvedKey = brandId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Serie getSerie() {
        Long __key = this.serieId;
        if (serie__resolvedKey == null || !serie__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                daoSession = DatabaseHelper.instance().getSession();
                //throw new DaoException("Entity is detached from DAO context");
            }
            SerieDao targetDao = daoSession.getSerieDao();
            Serie serieNew = targetDao.load(__key);
            synchronized (this) {
                serie = serieNew;
            	serie__resolvedKey = __key;
            }
        }
        return serie;
    }

    public void setSerie(Serie serie) {
        synchronized (this) {
            this.serie = serie;
            serieId = serie == null ? null : serie.getId();
            serie__resolvedKey = serieId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Engine getEngine() {
        Long __key = this.engineId;
        if (engine__resolvedKey == null || !engine__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                daoSession = DatabaseHelper.instance().getSession();
                //throw new DaoException("Entity is detached from DAO context");
            }
            EngineDao targetDao = daoSession.getEngineDao();
            Engine engineNew = targetDao.load(__key);
            synchronized (this) {
                engine = engineNew;
            	engine__resolvedKey = __key;
            }
        }
        return engine;
    }

    public void setEngine(Engine engine) {
        synchronized (this) {
            this.engine = engine;
            engineId = engine == null ? null : engine.getId();
            engine__resolvedKey = engineId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Company getCompany() {
        Long __key = this.supplierId;
        if (company__resolvedKey == null || !company__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                daoSession = DatabaseHelper.instance().getSession();
                //throw new DaoException("Entity is detached from DAO context");
            }
            CompanyDao targetDao = daoSession.getCompanyDao();
            Company companyNew = targetDao.load(__key);
            synchronized (this) {
                company = companyNew;
            	company__resolvedKey = __key;
            }
        }
        return company;
    }

    public void setCompany(Company company) {
        synchronized (this) {
            this.company = company;
            supplierId = company == null ? null : company.getId();
            company__resolvedKey = supplierId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }


    //Parcelable
    protected Product (Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        userId = in.readLong();
        partId = in.readLong();
        categoryId = in.readLong();
        brandId = in.readLong();
        serieId = in.readLong();
        engineId = in.readLong();
        supplierId = in.readLong();
        path = in.readString();
        price = in.readFloat();
        quantity = in.readLong();
        status = in.readLong();
        image = in.readString();
        code = in.readString();
        number = in.readString();
        hot = in.readLong();
        uptime = in.readLong();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
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
        dest.writeLong(userId);
        dest.writeLong(partId);
        dest.writeLong(categoryId);
        dest.writeLong(brandId);
        dest.writeLong(serieId);
        dest.writeLong(engineId);
        dest.writeLong(supplierId);
        dest.writeString(path);
        dest.writeFloat(price);
        dest.writeLong(quantity);
        dest.writeLong(status);
        dest.writeString(image);
        dest.writeString(code);
        dest.writeString(number);
        dest.writeLong(hot);
        dest.writeLong(uptime);
    }
    //Parcelable END

    //JsonSerializable
    public static Product fromJson(JSONObject jsonObject) throws JSONException {
        Product domain = new Product();

        domain.id = jsonObject.getLong("id");
        domain.name = jsonObject.getString("NAME");
        domain.description = jsonObject.getString("DESCRIPTION");
        domain.userId = jsonObject.getLong("USER_ID");
        domain.partId = jsonObject.getLong("PART_ID");
        domain.categoryId = jsonObject.getLong("CATEGORY_ID");
        domain.brandId = jsonObject.getLong("BRAND_ID");
        domain.serieId = jsonObject.getLong("SERIE_ID");
        domain.engineId = jsonObject.getLong("ENGINE_ID");
        domain.supplierId = jsonObject.getLong("SUPPLIER_ID");
        domain.path = jsonObject.getString("PATH");
        domain.price = jsonObject.getFloat("PRICE");
        domain.quantity = jsonObject.getLong("QUANTITY");
        domain.status = jsonObject.getLong("STATUS");
        domain.image = jsonObject.getString("IMAGE");
        domain.code = jsonObject.getString("CODE");
        domain.number = jsonObject.getString("NUMBER");
        domain.hot = jsonObject.getLong("HOT");
        domain.uptime = jsonObject.getLong("UPTIME");

        return domain;
    }

    public static String toJson(Product obj) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",obj.id);
        jsonObject.put("NAME",obj.name);
        jsonObject.put("DESCRIPTION",obj.description);
        jsonObject.put("USER_ID",obj.userId);
        jsonObject.put("PART_ID",obj.partId);
        jsonObject.put("CATEGORY_ID",obj.categoryId);
        jsonObject.put("BRAND_ID",obj.brandId);
        jsonObject.put("SERIE_ID",obj.serieId);
        jsonObject.put("ENGINE_ID",obj.engineId);
        jsonObject.put("SUPPLIER_ID",obj.supplierId);
        jsonObject.put("PATH",obj.path);
        jsonObject.put("PRICE",obj.price);
        jsonObject.put("QUANTITY",obj.quantity);
        jsonObject.put("STATUS",obj.status);
        jsonObject.put("IMAGE",obj.image);
        jsonObject.put("CODE",obj.code);
        jsonObject.put("NUMBER",obj.number);
        jsonObject.put("HOT",obj.hot);
        jsonObject.put("UPTIME",obj.uptime);
        return jsonObject.toString();
    }
    //JsonSerializable END

}
