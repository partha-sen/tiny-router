package com.tiny.router.serialization;

import java.io.Serializable;

public class Address implements Serializable {
    String name;
    String state;
    String street;

    public Address(String name, String state, String street) {
        this.name = name;
        this.state = state;
        this.street = street;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
