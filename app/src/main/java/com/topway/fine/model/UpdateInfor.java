package com.topway.fine.model;

/**
 * 更新信息实体类
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-1
 */
public class UpdateInfor
{
    private String version;
    private String description;
    private String url;

    public String getVersion()
    {
        return version;
    }
    public void setVersion(String version)
    {
        this.version = version;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public String getUrl()
    {
        return url;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

}