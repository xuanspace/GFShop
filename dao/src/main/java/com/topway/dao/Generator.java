package com.topway.dao;

import java.util.List;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Generator {

    Schema schema = new Schema(1, "com.topway.fine.model");
    Entity table = addTables();
    Entity sequnce = addSequnce();
    Entity company = addCompany();
    Entity zone = addZone();
    Entity category= addCategories();
    Entity group = addGroup();
    Entity brand = addBrands();
    Entity generic = addGenerics();
    Entity engine = addEngines();
    Entity serieengine = addSerieEngine(); 
    Entity addManufacturer = addManufacturers();
    Entity serie = addSeries();
    Entity part = addParts();
    Entity product = addProducts();
    Entity supplier = addSupplier();
    Entity contact = addContacts();
    Entity contactway = addContactWay();
    Entity photo = addPhotos();
    Entity dictionary = addDictionary();

    public void make() throws Exception {
        // get the project root path
        String path = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Work dir:" + path);
        new DaoGenerator().generateAll(schema, path + "/app/src/main/java-gen");
    }

    private Property getProperty(Entity table, String name) {
        List<Property> list=table.getProperties();
        for(int i=0; i<list.size(); i++)    {
            String str = list.get(i).getColumnName();
            String str2 = list.get(i).getPropertyName();
            if (list.get(i).getColumnName().equals(name))
                return list.get(i);
        }
        return null;
    }

    private Entity addTables() {
        Entity table = schema.addEntity("Table");
        table.setTableName("Tables");
        table.addIdProperty().primaryKey();
        table.addStringProperty("name");
        table.addStringProperty("description");
        table.setImplParcelable(true);
        return table;
    }

    private Entity addSequnce() {
        Entity table = schema.addEntity("Sequnce");
        table.setTableName("Sequnce");
        table.addIdProperty().primaryKey().autoincrement();
        //table.addLongProperty("next");
        table.setImplParcelable(true);
        return table;
    }

    private Entity addZone() {
        Entity table = schema.addEntity("Zone");
        table.setTableName("Zones");
        table.addIdProperty().primaryKey();
        table.addStringProperty("name");
        return table;
    }

    private Entity addCategories() {
        Entity table = schema.addEntity("Category");
        table.setTableName("Categories");
        table.addIdProperty().primaryKey();
        Property property = table.addLongProperty("pid").getProperty();
        table.addLongProperty("child");
        table.addStringProperty("name");
        table.addStringProperty("path");
        table.addStringProperty("clazz");
        table.addLongProperty("rid").getProperty();
        table.addLongProperty("gid").getProperty();
        table.addStringProperty("image");
        table.addStringProperty("ename");
        table.addStringProperty("pinyin");
        table.addStringProperty("abbr");
        table.addStringProperty("alpha");
        table.addLongProperty("hot");

        table.addToOne(table,property).setName("parent");;
        table.addToMany(table,property).setName("children");
        table.setImplParcelable(true);
        return table;
    }

    private Entity addGroup() {
        Entity table = schema.addEntity("CategoryGroup");
        table.setTableName("CategoryGroups");
        table.addIdProperty().primaryKey();
        table.addStringProperty("name");
        Property property1 = table.addLongProperty("categoryId").getProperty();
        table.addLongProperty("hot");

        table.addToOne(category,property1);
        table.setImplParcelable(true);
        return table;
    }

    private Entity addBrands() {
        Entity table = schema.addEntity("Brand");
        table.setTableName("Brands");
        table.addIdProperty().primaryKey();
        Property property1 = table.addLongProperty("categoryId").getProperty();
        Property property2 = table.addLongProperty("zoneId").getProperty();;
        table.addStringProperty("name");
        table.addStringProperty("path");
        table.addStringProperty("ename");
        table.addStringProperty("pinyin");
        table.addStringProperty("abbr");
        table.addStringProperty("alpha");
        table.addStringProperty("image");
        table.addLongProperty("common");
        table.addLongProperty("hot");

        table.addToOne(category,property1);
        table.addToOne(zone,property2);
        table.setImplParcelable(true);
        return table;
    }

    private Entity addManufacturers() {
        Entity table = schema.addEntity("Manufacturer");
        table.setTableName("Manufacturers");
        table.addIdProperty().primaryKey();
        Property property1 = table.addLongProperty("categoryId").getProperty();
        Property property2 = table.addLongProperty("brandId").getProperty();
        table.addStringProperty("name");
        table.addStringProperty("path");
        table.addLongProperty("common");
        table.addLongProperty("hot");

        table.addToOne(category,property1);
        table.addToOne(brand,property2);
        table.setImplParcelable(true);
        return table;
    }


    private Entity addEngines() {
        Entity table = schema.addEntity("Engine");
        table.setTableName("Engines");
        table.addIdProperty().primaryKey();
        table.addStringProperty("name");
        table.addStringProperty("apply");
        Property property1 = table.addLongProperty("genericId").getProperty();
        Property property2 = table.addLongProperty("brandId").getProperty();
        table.addStringProperty("brandName");
        table.addStringProperty("path");
        table.addStringProperty("image");
        table.addLongProperty("common");
        table.addLongProperty("hot");

        table.addToOne(generic,property1);
        table.addToOne(brand,property2);
        table.setImplParcelable(true);
        return table;
    }


    private Entity addSerieEngine() {
        Entity table = schema.addEntity("SerieEngines");
        table.setTableName("SerieEngine");
        table.addIdProperty().primaryKey();
        table.addLongProperty("serieId");
        table.addStringProperty("serieName");
        table.addLongProperty("engineId");
        table.addStringProperty("engineName");
        table.addLongProperty("brandId");
        table.addStringProperty("brandName");
        table.addStringProperty("description");
        table.addLongProperty("hot");

        table.setImplParcelable(true);
        return table;
    }

    private Entity addSeries() {
        Entity table = schema.addEntity("Serie");
        table.setTableName("Series");
        table.addIdProperty().primaryKey();
        table.addStringProperty("name");
        Property property0 = table.addLongProperty("genericId").getProperty();
        Property property1 = table.addLongProperty("brandId").getProperty();
        table.addStringProperty("brandName");
        Property property2 = table.addLongProperty("engineId").getProperty();
        table.addStringProperty("engineName");
        table.addStringProperty("path");
        table.addStringProperty("image");
        table.addLongProperty("common");
        table.addLongProperty("hot");

        table.addToOne(generic,property0);
        table.addToOne(brand,property1);
        table.addToOne(engine,property2);
        table.setImplParcelable(true);
        return table;
    }

    private Entity addParts() {
        Entity table = schema.addEntity("Part");
        table.setTableName("Parts");
        table.addIdProperty().primaryKey();
        Property property1 = table.addLongProperty("categoryId").getProperty();;
        Property property2 = table.addLongProperty("brandId").getProperty();
        Property property3 = table.addLongProperty("serieId").getProperty();
        Property property4 = table.addLongProperty("engineId").getProperty();
        table.addStringProperty("name");
        table.addStringProperty("number");
        table.addStringProperty("unit");
        table.addStringProperty("image");
        table.addLongProperty("hot");

        table.addToOne(category,property1);
        table.addToOne(brand,property2);
        table.addToOne(serie,property3);
        table.addToOne(engine,property4);

        table.setImplParcelable(true);
        return table;
    }

    private Entity addProducts() {
        Entity table = schema.addEntity("Product");
        table.setTableName("Products");
        table.addIdProperty().primaryKey();
        table.addStringProperty("name");
        table.addStringProperty("description");
        Property property0 = table.addLongProperty("userId").getProperty();
        Property property1 = table.addLongProperty("partId").getProperty();
        Property property2 = table.addLongProperty("categoryId").getProperty();
        Property property3 = table.addLongProperty("brandId").getProperty();
        Property property4 = table.addLongProperty("serieId").getProperty();
        Property property5 = table.addLongProperty("engineId").getProperty();
        Property property6 = table.addLongProperty("supplierId").getProperty();
        table.addStringProperty("path");
        table.addFloatProperty("price");
        table.addLongProperty("quantity");
        table.addLongProperty("status");
        table.addStringProperty("image");
        table.addStringProperty("code");
        table.addStringProperty("number");
        table.addLongProperty("hot");
        table.addLongProperty("uptime");

        table.addToOne(part,property1);
        table.addToOne(category,property2);
        table.addToOne(brand,property3);
        table.addToOne(serie,property4);
        table.addToOne(engine,property5);
        table.addToOne(company,property6);
        table.setImplParcelable(true);
        return table;
    }


    private Entity addSupplier() {
        Entity table = schema.addEntity("Supplier");
        table.setTableName("Suppliers");
        table.addIdProperty().primaryKey();
        table.addLongProperty("companyId");
        table.addLongProperty("goodId");
        table.addFloatProperty("price");
        table.addLongProperty("quantity");
        table.addLongProperty("hot");

        table.setImplParcelable(true);
        return table;
    }


    private Entity addCompany() {
        Entity table = schema.addEntity("Company");
        table.setTableName("Companys");
        table.addIdProperty().primaryKey();
        table.addStringProperty("name");
        table.addStringProperty("shortname");
        table.addStringProperty("alpha");
        table.addStringProperty("pinyin");
        table.addStringProperty("abbr");
        table.addStringProperty("address");
        table.addStringProperty("telephone");
        table.addStringProperty("mobile");
        table.addStringProperty("fax");
        table.addStringProperty("email");
        table.addStringProperty("qq");
        table.addStringProperty("man");
        table.addStringProperty("memo");
        table.addStringProperty("image");
        table.addLongProperty("hot");

        table.setImplParcelable(true);
        return table;
    }


    private Entity addContacts() {
        Entity table = schema.addEntity("Contact");
        table.setTableName("Contacts");
        table.addIdProperty().primaryKey();
        table.addLongProperty("name");
        table.addLongProperty("sex");
        table.addLongProperty("companyId");
        table.addStringProperty("memo");
        table.addStringProperty("image");
        table.addLongProperty("hot");

        table.setImplParcelable(true);
        return table;
    }

    private Entity addContactWay() {
        Entity table = schema.addEntity("Link");
        table.setTableName("Links");
        table.addIdProperty().primaryKey();
        table.addLongProperty("tableId");
        table.addLongProperty("primaryId");
        table.addLongProperty("way");
        table.addStringProperty("number");
        //table.addStringProperty("address");
        //table.addStringProperty("phone");
        //table.addStringProperty("mobile");
        //table.addStringProperty("qq");
        //table.addStringProperty("wexin");
        //table.addStringProperty("weibo");
        //table.addStringProperty("wweixinpub");
        //table.addStringProperty("fax");

        table.setImplParcelable(true);
        return table;
    }

    private Entity addGenerics() {
        Entity table = schema.addEntity("Generic");
        table.setTableName("Generics");
        table.addIdProperty().primaryKey();
        Property property1 = table.addLongProperty("categoryId").getProperty();
        table.addLongProperty("Id1");
        table.addLongProperty("Id2");
        table.addLongProperty("unit");
        table.addStringProperty("memo");

        table.addToOne(category,property1);
        table.setImplParcelable(true);
        return table;
    }

    private Entity addPhotos() {
        Entity table = schema.addEntity("Photo");
        table.setTableName("Photos");
        table.addIdProperty().primaryKey().autoincrement();
        table.addLongProperty("userId").getProperty();
        table.addLongProperty("tableId");
        table.addLongProperty("primaryId");
        table.addStringProperty("url");

        table.setImplParcelable(true);
        return table;
    }

    private Entity addHistory() {
        Entity table = schema.addEntity("History");
        table.setTableName("Histories");
        table.addIdProperty().primaryKey();
        table.addLongProperty("userId").getProperty();
        table.addStringProperty("name");

        table.setImplParcelable(true);
        return table;
    }

    private Entity addDictionary() {
        Entity table = schema.addEntity("Dictionary");
        table.setTableName("Dictionary");
        table.addIdProperty().primaryKey();
        table.addStringProperty("field");
        table.addLongProperty("value");
        table.addLongProperty("description");

        table.setImplParcelable(true);
        return table;
    }

    private Entity addSync() {
        Entity table = schema.addEntity("Sync");
        table.setTableName("Syncs");
        table.addIdProperty().primaryKey();
        table.addLongProperty("tableId");
        table.addStringProperty("primaryId");
        table.addLongProperty("flag");

        table.setImplParcelable(true);
        return table;
    }


    public static void main(String[] args) throws Exception {
        Generator builder = new Generator();
        builder.make();
    }

}