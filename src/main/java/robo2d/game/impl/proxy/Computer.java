package robo2d.game.impl.proxy;

import java.io.FileOutputStream;

public class Computer implements com.robotech.military.api.Computer {
    com.robotech.military.api.Computer computer;

    public Computer(com.robotech.military.api.Computer computer) {
        this.computer = computer;
    }

    @Override
    public void saveFile(String fileName, String content) {
        computer.saveFile(fileName, content);
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(content.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String loadFile(String fileName) {
        return computer.loadFile(fileName);
    }
}
