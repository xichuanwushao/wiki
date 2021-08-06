package com.xichuan.wiki.req;

import com.xichuan.wiki.resp.PageReq;

public class DocQueryReq extends PageReq {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryQueryReq{} " + super.toString();
    }
}