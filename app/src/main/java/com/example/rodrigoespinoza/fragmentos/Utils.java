package com.example.rodrigoespinoza.fragmentos;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    static final List<String> locations = new ArrayList<String>(){{
        add("Santiago");
        add("Independencia");
        add("Conchali");
        add("Huechuraba");
        add("Recoleta");
        add("Providencia");
        add("Vitacura");
        add("Lo Barnechea");
        add("Las condes");
    }};

    public Utils() {

    }



    public static List getLocations() {
        return locations;
    }
}