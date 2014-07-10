package com.robotech.military.api;

public interface Computer extends Equipment {

    void saveFile(String fileName, String content);

    String loadFile(String fileName);

}
