package com.example.test2.Database;

import android.provider.BaseColumns;

public class MesecContract implements BaseColumns {

    private MesecContract(){}

    public static final class MesecEntry implements BaseColumns {
        public static final String TABLE_NAME = "mesci";
        public static final String COLUM_1_DATUM = "datum";
        public static final String COLUM_2_STROSKI = "stroski";
        public static final String COLUM_2_RACUNI = "racuni";
    }
}
