package com.example.test2.Database;

import android.provider.BaseColumns;

public class RacuniContract {

    private RacuniContract(){

    }

    public static final class RacunEntry implements BaseColumns{
        public static final String TABLE_NAME = "racuni";
        public static final String COLUM_1_ID = "id";
        public static final String COLUM_2_NAME = "naziv";
        public static final String COLUM_3_DATUM = "datum";
        public static final String COLUM_4_ZNESEK = "znesek";
    }

}
