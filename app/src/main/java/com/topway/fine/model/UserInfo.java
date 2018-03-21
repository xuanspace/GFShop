package com.topway.fine.model;

/**
 * 登录用户实体类
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-1
 */
public class UserInfo {
    private int id;
    private String name;
    private String account;
    private String pwd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return pwd;
    }

    public void setPasswd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User [uid=" + id + ", name=" + name + ", account=" + account
                + ", pwd=" + pwd + ", isRememberMe=" + "]";
    }
}
