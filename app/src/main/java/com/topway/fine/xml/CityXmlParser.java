package com.topway.fine.xml;

import com.topway.fine.model.CityModel;
import com.topway.fine.model.DistrictModel;
import com.topway.fine.model.ProvinceModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 全国省市县XML文件解析器
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-1
 */
public class CityXmlParser extends DefaultHandler {

    /**
     * 存储所有的解析对象
     */
    private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();

    public CityXmlParser() {

    }

    public List<ProvinceModel> getDataList() {
        return provinceList;
    }

    @Override
    public void startDocument() throws SAXException {
    }

    ProvinceModel province = new ProvinceModel();
    CityModel city = new CityModel();
    DistrictModel district = new DistrictModel();

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (qName.equals("province")) {
            province = new ProvinceModel();
            province.setName(attributes.getValue(0));
            province.setCityList(new ArrayList<CityModel>());
        } else if (qName.equals("city")) {
            city = new CityModel();
            city.setName(attributes.getValue(0));
            city.setDistrictList(new ArrayList<DistrictModel>());
        } else if (qName.equals("district")) {
            district = new DistrictModel();
            district.setName(attributes.getValue(0));
            district.setZipcode(attributes.getValue(1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // 遇到结束标记的时候，会调用这个方法
        if (qName.equals("district")) {
            city.getDistrictList().add(district);
        } else if (qName.equals("city")) {
            province.getCityList().add(city);
        } else if (qName.equals("province")) {
            provinceList.add(province);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    }

}
