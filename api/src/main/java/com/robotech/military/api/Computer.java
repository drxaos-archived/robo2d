package com.robotech.military.api;

public interface Computer {

    void saveFile(String fileName, String content);

    String loadFile(String fileName);

}
