package com.topway.fine.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.topway.fine.model.Supplier;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "Suppliers".
*/
public class SupplierDao extends AbstractDao<Supplier, Long> {

    public static final String TABLENAME = "Suppliers";

    /**
     * Properties of entity Supplier.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CompanyId = new Property(1, Long.class, "companyId", false, "COMPANY_ID");
        public final static Property GoodId = new Property(2, Long.class, "goodId", false, "GOOD_ID");
        public final static Property Price = new Property(3, Float.class, "price", false, "PRICE");
        public final static Property Quantity = new Property(4, Long.class, "quantity", false, "QUANTITY");
        public final static Property Hot = new Property(5, Long.class, "hot", false, "HOT");
    };


    public SupplierDao(DaoConfig config) {
        super(config);
    }
    
    public SupplierDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"Suppliers\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"COMPANY_ID\" INTEGER DEFAULT(0) ," + // 1: companyId
                "\"GOOD_ID\" INTEGER DEFAULT(0) ," + // 2: goodId
                "\"PRICE\" REAL," + // 3: price
                "\"QUANTITY\" INTEGER DEFAULT(0) ," + // 4: quantity
                "\"HOT\" INTEGER DEFAULT(0) );"); // 5: hot
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"Suppliers\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Supplier entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long companyId = entity.getCompanyId();
        if (companyId != null) {
            stmt.bindLong(2, companyId);
        }
 
        Long goodId = entity.getGoodId();
        if (goodId != null) {
            stmt.bindLong(3, goodId);
        }
 
        Float price = entity.getPrice();
        if (price != null) {
            stmt.bindDouble(4, price);
        }
 
        Long quantity = entity.getQuantity();
        if (quantity != null) {
            stmt.bindLong(5, quantity);
        }
 
        Long hot = entity.getHot();
        if (hot != null) {
            stmt.bindLong(6, hot);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Supplier readEntity(Cursor cursor, int offset) {
        Supplier entity = new Supplier( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // companyId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // goodId
            cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3), // price
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // quantity
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // hot
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Supplier entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCompanyId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setGoodId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setPrice(cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3));
        entity.setQuantity(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setHot(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Supplier entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Supplier entity) {
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
