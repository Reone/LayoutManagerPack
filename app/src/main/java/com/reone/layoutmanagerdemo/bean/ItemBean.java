package com.reone.layoutmanagerdemo.bean;

/**
 * Created by wangxingsheng on 2020/6/22.
 * desc:
 */
public class ItemBean {
    private String name;
    private String breed;
    private String registry;
    private int headerIcon;
    private int movie;
    private int noruto;
    
    public int getNoruto() {
        return noruto;
    }

    public void setNoruto(int noruto) {
        this.noruto = noruto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public int getHeaderIcon() {
        return headerIcon;
    }

    public void setHeaderIcon(int headerIcon) {
        this.headerIcon = headerIcon;
    }

    public int getMovie() {
        return movie;
    }

    public void setMovie(int movie) {
        this.movie = movie;
    }
}