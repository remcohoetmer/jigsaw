package com.baeldung.entitymodule;

public record User(String name) {

    @Override
    public String toString() {
        return "User{" + "name=" + name + '}';
    }
}