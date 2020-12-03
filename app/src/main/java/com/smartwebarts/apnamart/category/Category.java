package com.smartwebarts.apnamart.category;

import java.util.ArrayList;

public class Category {

    private String name;
    private ArrayList<SubCategory> list;

    public Category(String name, ArrayList<SubCategory> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SubCategory> getList() {
        return list;
    }

    public void setList(ArrayList<SubCategory> list) {
        this.list = list;
    }
}
