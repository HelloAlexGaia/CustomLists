package com.example.android.customlist;

import android.provider.BaseColumns;

/**
 * Created by 张俊秋 on 2017/4/16.
 */

public final class CustomContract {
    public final class CustomEntry implements BaseColumns{
        public static final String TABLENAME="custom_table";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_DESCRIPTION="description";
        public static final String COLUMN_LEVEL="level";
        public static final String COLUMN_TIME="time";
        public static final String COLUMN_UUID="uuid";
        public static final String COLUMN_DEVELOP="develop";

        public static final int MONTH_1=0;
        public static final int MONTH_2=1;
        public static final int MONTH_3=2;
        public static final int MONTH_4=3;
        public static final int MONTH_5=4;

        public static final int DEVELOPED=1;
        public static final int DEVELOPING=0;
    }
}
