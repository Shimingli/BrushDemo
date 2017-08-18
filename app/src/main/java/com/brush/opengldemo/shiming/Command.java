package com.brush.opengldemo.shiming;

import java.util.ArrayList;
import java.util.List;


public class Command {
    private int mCommand = 0;
    private List<PathDrawData> mCommandDrawList;
    private float mOffSetX;
    private float mOffSetY;

    public Command() {
        mCommandDrawList = new ArrayList<>();
    }

    public List<PathDrawData> getCommandDrawList() {
        return mCommandDrawList;
    }

    public int getCommand() {
        return mCommand;
    }

    public void setCommand(int command) {
        mCommand = command;
    }

    public float getOffSetY() {
        return mOffSetY;
    }

    public void setOffSetY(float offSetY) {
        mOffSetY = offSetY;
    }

    public float getOffSetX() {
        return mOffSetX;
    }

    public void setOffSetX(float offSetX) {
        mOffSetX = offSetX;
    }
}
