package com.topway.fine.model;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import com.topway.fine.model.Brand;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "Brands".
*/
public class BrandDao extends AbstractDao<Brand, Long> {

    public static final String TABLENAME = "Brands";

    /**
     * Properties of entity Brand.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CategoryId = new Property(1, Long.class, "categoryId", false, "CATEGORY_ID");
        public final static Property ZoneId = new Property(2, Long.class, "zoneId", false, "ZONE_ID");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Path = new Property(4, String.class, "path", false, "PATH");
        public final static Property Ename = new Property(5, String.class, "ename", false, "ENAME");
        public final static Property Pinyin = new Property(6, String.class, "pinyin", false, "PINYIN");
        public final static Property Abbr = new Property(7, String.class, "abbr", false, "ABBR");
        public final static Property Alpha = new Property(8, String.class, "alpha", false, "ALPHA");
        public final static Property Image = new Property(9, String.class, "image", false, "IMAGE");
        public final static Property Common = new Property(10, Long.class, "common", false, "COMMON");
        public final static Property Hot = new Property(11, Long.class, "hot", false, "HOT");
    };

    private DaoSession daoSession;


    public BrandDao(DaoConfig config) {
        super(config);
    }
    
    public BrandDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"Brands\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"CATEGORY_ID\" INTEGER DEFAULT(0) ," + // 1: categoryId
                "\"ZONE_ID\" INTEGER DEFAULT(0) ," + // 2: zoneId
                "\"NAME\" TEXT DEFAULT('') ," + // 3: name
                "\"PATH\" TEXT DEFAULT('') ," + // 4: path
                "\"ENAME\" TEXT DEFAULT('') ," + // 5: ename
                "\"PINYIN\" TEXT DEFAULT('') ," + // 6: pinyin
                "\"ABBR\" TEXT DEFAULT('') ," + // 7: abbr
                "\"ALPHA\" TEXT DEFAULT('') ," + // 8: alpha
                "\"IMAGE\" TEXT DEFAULT('') ," + // 9: image
                "\"COMMON\" INTEGER DEFAULT(0) ," + // 10: common
                "\"HOT\" INTEGER DEFAULT(0) );"); // 11: hot
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"Brands\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Brand entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long categoryId = entity.getCategoryId();
        if (categoryId != null) {
            stmt.bindLong(2, categoryId);
        }
 
        Long zoneId = entity.getZoneId();
        if (zoneId != null) {
            stmt.bindLong(3, zoneId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(5, path);
        }
 
        String ename = entity.getEname();
        if (ename != null) {
            stmt.bindString(6, ename);
        }
 
        String pinyin = entity.getPinyin();
        if (pinyin != null) {
            stmt.bindString(7, pinyin);
        }
 
        String abbr = entity.getAbbr();
        if (abbr != null) {
            stmt.bindString(8, abbr);
        }
 
        String alpha = entity.getAlpha();
        if (alpha != null) {
            stmt.bindString(9, alpha);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(10, image);
        }
 
        Long common = entity.getCommon();
        if (common != null) {
            stmt.bindLong(11, common);
        }
 
        Long hot = entity.getHot();
        if (hot != null) {
            stmt.bindLong(12, hot);
        }
    }

    @Override
    protected void attachEntity(Brand entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Brand readEntity(Cursor cursor, int offset) {
        Brand entity = new Brand( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // categoryId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // zoneId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // path
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ename
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // pinyin
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // abbr
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // alpha
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // image
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10), // common
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11) // hot
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Brand entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCategoryId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setZoneId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPath(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEname(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPinyin(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAbbr(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAlpha(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setImage(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCommon(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
        entity.setHot(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Brand entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Brand entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getCategoryDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getZoneDao().getAllColumns());
            builder.append(" FROM Brands T");
            builder.append(" LEFT JOIN Categories T0 ON T.\"CATEGORY_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN Zones T1 ON T.\"ZONE_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Brand loadCurrentDeep(Cursor cursor, boolean lock) {
        Brand entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Category category = loadCurrentOther(daoSession.getCategoryDao(), cursor, offset);
        entity.setCategory(category);
        offset += daoSession.getCategoryDao().getAllColumns().length;

        Zone zone = loadCurrentOther(daoSession.getZoneDao(), cursor, offset);
        entity.setZone(zone);

        return entity;    
    }

    public Brand loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Brand> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Brand> list = new ArrayList<Brand>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Brand> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Brand> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}