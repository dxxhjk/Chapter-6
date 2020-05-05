package com.byted.camp.todolist.db;

import android.provider.BaseColumns;


/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public final class TodoContract {

    // TODO 定义表结构和 SQL 语句常量

    public static final String SQL_CREATE_TODOLIST =
            "CREATE TABLE " + TodoList.TABLE_NAME + "( " +
                    TodoList._ID + " INTEGER PRIMARY KEY," +
                    TodoList.COLUMN_EVENT + " TEXT," +
                    TodoList.COLUMN_DATE + " DATETIME," +
                    TodoList.COLUMN_STATE + " INTEGER)";

    public static final String SQL_ADD_PRIORITY =
            "ALTER TABLE " + TodoList.TABLE_NAME +
                    " ADD " + TodoList.COLUMN_PRIORITY +
                    " INTEGER";

    private TodoContract() {
    }
    public static class TodoList implements BaseColumns {

        public static final String TABLE_NAME = "TodoList";
        public static final String COLUMN_EVENT = "text";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_PRIORITY = "priority";
    }
}
