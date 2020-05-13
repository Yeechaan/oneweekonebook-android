package com.lee.oneweekonebook;

public class ItemDone {
    int id;
    String title;
    String writer;
    String pub;
    String pic_cover;
    String st_date;
    String fi_date;
    int index;

    public ItemDone(int id, String title, String writer, String pub, String pic_cover, String st_date, String fi_date, int index) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.pub = pub;
        this.pic_cover = pic_cover;
        this.st_date = st_date;
        this.fi_date = fi_date;
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public String getPic_cover() {
        return pic_cover;
    }

    public void setPic_cover(String pic_cover) {
        this.pic_cover = pic_cover;
    }

    public String getSt_date() {
        return st_date;
    }

    public void setSt_date(String st_date) {
        this.st_date = st_date;
    }

    public String getFi_date() {
        return fi_date;
    }

    public void setFi_date(String fi_date) {
        this.fi_date = fi_date;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
