package com.brush.opengldemo.shiming;

import java.util.ArrayList;
import java.util.List;


public class  CommandUtils {
    private static final String TAG = "CommandUtils";
    private static CommandUtils mCommandUtils = null;
    private List<Command> mUndoCommandList = null;
    private List<Command> mRedoCommandList = null;

    private CommandUtils() {
        mUndoCommandList = new ArrayList<>();
        mRedoCommandList = new ArrayList<>();
    }

    public static CommandUtils getInstance() {
        if (mCommandUtils == null) {
            mCommandUtils = new CommandUtils();
        }
        return mCommandUtils;
    }

    public List<Command> getUndoCommandList() {
        return mUndoCommandList;
    }

    public List<Command> getRedoCommandList() {
        return mRedoCommandList;
    }

    /**
     * 撤销
     *
     * @return 返回需要撤销的指令
     */
    public Command undo() {
        Command command = null;
        int size = mUndoCommandList.size();
        if (mUndoCommandList != null && size > 0) {
            command = mUndoCommandList.get(size - 1);
            mRedoCommandList.add(command);
            mUndoCommandList.remove(size - 1);
        }
        return command;
    }

    /**
     * 恢复操作
     *
     * @return 返回恢复的指令
     */
    public Command redo() {
        Command command = null;
        int size = mRedoCommandList.size();
        if (mRedoCommandList != null && size > 0) {
            command = mRedoCommandList.get(size - 1);
            mUndoCommandList.add(command);
            mRedoCommandList.remove(size - 1);
        }
        return command;
    }
}
