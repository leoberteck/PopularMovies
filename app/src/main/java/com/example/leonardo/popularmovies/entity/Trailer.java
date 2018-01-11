package com.example.leonardo.popularmovies.entity;


public class Trailer {

    private String trailerId;
    private String name;
    private String key;
    private String site;
    private String type;

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trailer trailer = (Trailer) o;
        return getTrailerId() != null ? getTrailerId().equals(trailer.getTrailerId()) : trailer.getTrailerId() == null;
    }

    @Override
    public int hashCode() {
        return getTrailerId() != null ? getTrailerId().hashCode() : 0;
    }
}
