package com.robotech.game.physicals;

import com.robotech.game.Physical;
import com.robotech.game.box.Box;
import com.robotech.game.box.RobotBox;

public class RobotImpl extends Physical {

    public RobotImpl(String uid) {
        super(uid);
    }

    @Override
    public Box getBox() {
        return new RobotBox(uid, x, y, angle);
    }

    @Override
    public void init() {

    }

    @Override
    public void beforeStep(long time) {

    }

    @Override
    public void afterStep(long time) {

    }


}
