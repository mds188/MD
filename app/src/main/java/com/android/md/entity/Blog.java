package com.android.md.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by mingdasen on 2015/11/23.
 */
public class Blog extends BmobObject {

    private static final long serialVersionUID = 1L;
    private String brief;
    private User user;

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
