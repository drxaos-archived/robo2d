package robo2d.game.impl.proxy;

public class Radio implements com.robotech.military.api.Radio {
    com.robotech.military.api.Radio radio;

    public Radio(com.robotech.military.api.Radio radio) {
        this.radio = radio;
    }

    public void setChannel(Double frequency) {
        radio.setChannel(frequency);
    }

    public Double listen() {
        return radio.listen();
    }

    public void broadcast(Double data) {
        radio.broadcast(data);
    }
}
