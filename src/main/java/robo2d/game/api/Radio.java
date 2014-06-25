package robo2d.game.api;

public interface Radio extends Equipment {

    void setChannel(Integer frequency);

    void broadcast(Integer data); // xor if multiple broadcasters

    Integer listen(); // stop broadcast and listen for data
}
