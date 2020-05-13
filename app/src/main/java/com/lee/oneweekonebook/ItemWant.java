package com.lee.oneweekonebook;

public class ItemWant {
    int id;
    String title;
    String writer;
    String pub;
    String pic_cover;

    public ItemWant(int id, String title, String writer, String pub, String pic_cover) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.pub = pub;
        this.pic_cover = pic_cover;
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
}
