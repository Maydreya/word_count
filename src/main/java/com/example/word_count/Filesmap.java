package com.example.word_count;

import java.util.TreeMap;

public class Filesmap extends TreeMap<String, Integer>{
    public Filesmap createOrUpdateWordCounter(String path) {

        this.merge(path, 1, Integer::sum);

        return this;
    }
}
