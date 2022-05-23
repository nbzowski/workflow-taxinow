package com.taxi.app;

public class User {
    public long id;
    public String name;

    public String surname;

    //This is MUST(non-arg constuctor) because of the POST method
    public User() {}

    public User(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;

    }


    public long getId() {
        return id;
    }

    public void setId(long Id)
    {
        this.id = id;
    }

}


