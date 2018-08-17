package com.example.demo;

public class myobject {
    int id;

    public String getHaha() {
        return haha;
    }

    public void setHaha(String haha) {
        this.haha = haha;
    }

    String haha;

    @Override
    public String toString() {
        return "myobject{" +
                "id=" + id +
                ", haha='" + haha + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
