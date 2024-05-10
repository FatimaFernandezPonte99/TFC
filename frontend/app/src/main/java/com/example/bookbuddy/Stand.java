package com.example.bookbuddy;

import androidx.annotation.NonNull;

public class Stand {
    private int id;
    private String name;

    public Stand(int id, String name) {
        this.id=id;
        this.name=name;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return name; //Esto es lo que se mostrará en el Spinner
    }
}
