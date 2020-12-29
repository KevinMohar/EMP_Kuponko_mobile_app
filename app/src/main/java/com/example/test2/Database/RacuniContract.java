package com.example.test2.Database;

import android.provider.BaseColumns;

public class RacuniContract {

    private RacuniContract(){

    }

    public static final class RacunEntry implements BaseColumns{
        public static final String TABLE_NAME = "racuni";
        public static final String COLUM_1_NAME = "trgovina";
        public static final String COLUM_2_DATUM = "datum";
        public static final String COLUM_3_ZNESEK = "znesek";
    }

}
