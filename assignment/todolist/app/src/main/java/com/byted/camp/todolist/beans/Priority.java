package com.byted.camp.todolist.beans;

import android.graphics.Color;

public enum Priority {
    RANK1(0), RANK2(1), RANK3(2);

    public final int intValue;

    Priority(int intValue) {
        this.intValue = intValue;
    }

    public static Priority from(int intValue) {
        for (Priority priority : Priority.values()) {
            if (priority.intValue == intValue) {
                return priority;
            }
        }
        return RANK3; // default
    }

    public static int getBackGroundColor(Priority priority) {
        switch (priority) {
            case RANK1:
                return Color.WHITE;
            case RANK2:
                return Color.GREEN;
            case RANK3:
                return Color.RED;
            default:
                return Color.GRAY;
        }
    }
}
