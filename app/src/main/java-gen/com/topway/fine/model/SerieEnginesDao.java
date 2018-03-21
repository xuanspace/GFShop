package com.topway.fine.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.topway.fine.model.SerieEngines;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SerieEngine".
*/
public class SerieEnginesDao extends AbstractDao<SerieEngines, Long> {

    public static final String TABLENAME = "SerieEngine";

    /**
     * Properties of entity SerieEngines.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SerieId = new Property(1, Long.class, "serieId", false, "SERIE_ID");
        public final static Property SerieName = new Property(2, String.class, "serieName", false, "SERIE_NAME");
        public final static Property EngineId = new Property(3, Long.class, "engineId", false, "ENGINE_ID");
        public final static Property EngineName = new Property(4, String.class, "engineName", false, "ENGINE_NAME");
        public final static Property BrandId = new Property(5, Long.class, "brandId", false, "BRAND_ID");
        public final static Property BrandName = new Property(6, String.class, "brandName", false, "BRAND_NAME");
        public final static Property Description = new Property(7, String.class, "description", false, "DESCRIPTION");
        public final static Property Hot = new Property(8, Long.class, "hot", false, "HOT");
    };


    public SerieEnginesDao(DaoConfig config) {
        super(config);
    }
    
    public SerieEnginesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SerieEngine\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"SERIE_ID\" INTEGER DEFAULT(0) ," + // 1: serieId
                "\"SERIE_NAME\" TEXT DEFAULT('') ," + // 2: serieName
                "\"ENGINE_ID\" INTEGER DEFAULT(0) ," + // 3: engineId
                "\"ENGINE_NAME\" TEXT DEFAULT('') ," + // 4: engineName
                "\"BRAND_ID\" INTEGER DEFAULT(0) ," + // 5: brandId
                "\"BRAND_NAME\" TEXT DEFAULT('') ," + // 6: brandName
                "\"DESCRIPTION\" TEXT DEFAULT('') ," + // 7: description
                "\"HOT\" INTEGER DEFAULT(0) );"); // 8: hot
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SerieEngine\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SerieEngines entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long serieId = entity.getSerieId();
        if (serieId != null) {
            stmt.bindLong(2, serieId);
        }
 
        String serieName = entity.getSerieName();
        if (serieName != null) {
            stmt.bindString(3, serieName);
        }
 
        Long engineId = entity.getEngineId();
        if (engineId != null) {
            stmt.bindLong(4, engineId);
        }
 
        String engineName = entity.getEngineName();
        if (engineName != null) {
            stmt.bindString(5, engineName);
        }
 
        Long brandId = entity.getBrandId();
        if (brandId != null) {
            stmt.bindLong(6, brandId);
        }
 
        String brandName = entity.getBrandName();
        if (brandName != null) {
            stmt.bindString(7, brandName);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(8, description);
        }
 
        Long hot = entity.getHot();
        if (hot != null) {
            stmt.bindLong(9, hot);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SerieEngines readEntity(Cursor cursor, int offset) {
        SerieEngines entity = new SerieEngines( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // serieId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // serieName
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // engineId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // engineName
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // brandId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // brandName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // description
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8) // hot
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SerieEngines entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSerieId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setSerieName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setEngineId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setEngineName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBrandId(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setBrandName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDescription(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setHot(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SerieEngines entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SerieEngines entity) {
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
    
}
