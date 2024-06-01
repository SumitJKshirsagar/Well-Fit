package com.example.well_fit;

public class Suggest{
    private String sname;
    private String sid;
    private String simageUrl;

    public Suggest(String name, String imageUrl, String id) {
        this.sname = name;
        this.simageUrl = imageUrl;
        this.sid = id;
    }

    public String getId ( ) {
        return sid;
    }

    public String getName() {
        return sname;
    }

    public String getImageUrl() {
        return simageUrl;
    }
}
