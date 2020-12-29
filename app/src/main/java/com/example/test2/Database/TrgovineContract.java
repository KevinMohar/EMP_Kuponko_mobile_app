package com.example.test2.Database;

import android.provider.BaseColumns;

public class TrgovineContract {

    private TrgovineContract(){}

    public static final class TrgovinaEntry implements BaseColumns {
        public static final String TABLE_NAME = "trgovine";
        public static final String COLUM_1_NAME = "ime";
        public static final String COLUM_2_NASLOV = "naslov";
    }
}
