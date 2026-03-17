package com.tss.model;

public class CuisineType {
    private String name;
    private long id;

    public CuisineType(String name){
        this.name=name;
    }

    public CuisineType(long id , String name){
        this.id=id;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CuisineType)) return false;
        CuisineType cuisine = (CuisineType) o;
        return id == cuisine.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
