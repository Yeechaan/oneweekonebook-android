package com.lee.oneweekonebook;

public class ItemReview {
    int id;
    String sub_title;
    String page;
    String contents;
    String review;

    public ItemReview(int id, String sub_title, String page, String contents, String review) {
        this.id = id;
        this.sub_title = sub_title;
        this.page = page;
        this.contents = contents;
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
