package com.example.popularmovies.model;

public class Trailer {
    final private String id;
    final private String key;
    final private String name;
    final private String site;

    public Trailer(String iD,String kY, String nM, String sT) {
        id = iD;
        key = kY;
        name = nM;
        site = sT;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }
}
