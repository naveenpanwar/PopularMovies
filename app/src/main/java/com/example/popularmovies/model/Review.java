package com.example.popularmovies.model;

public class Review {
    final private String id;
    final private String author;
    final private String content;
    final private String url;

    public Review(String iD,String aU, String cO, String uRL) {
        id = iD;
        author = aU;
        content = cO;
        url = uRL;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getURL() {
        return url;
    }
}
