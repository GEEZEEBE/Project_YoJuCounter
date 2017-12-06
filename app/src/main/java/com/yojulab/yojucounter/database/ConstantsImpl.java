package com.yojulab.yojucounter.database;

/**
 * Created by ohsanghun on 10/29/16.
 */

interface ConstantsImpl {
    final String dateFormat = "yyyy-MM-dd";

    final String DB = "yojucounter.db";
    final int VERSION = 2;

    final String T_COUNTER_INFOR = "counter_information";
    final String UNIQUE_ID = "unique_id";
    final String COUNTER_NAME = "counter_name";
    final String DELETE_YN = "delete_yn";

    final String CREATE_T_COUNTER_INFOR = "create table " + T_COUNTER_INFOR +
            " (" + UNIQUE_ID + " text not null " +
            ", " + COUNTER_NAME + " text not null " +
            ", " + DELETE_YN + " text not null " +
            ", PRIMARY KEY ("+UNIQUE_ID+")"+
            " ); ";

    final String T_DAILY_COUNT = "daily_count";
    final String COUNT_NUMBER = "count_number";
    final String COUNT_DATE = "count_date";
    final String FK_UNIQUE_ID = "fk_unique_id";
    final String CREATE_T_DAILY_COUNT = "create table " + T_DAILY_COUNT +
            " ( " + UNIQUE_ID + " text not null " +
            ", " + COUNT_NUMBER + " integer not null " +
            ", " + COUNT_DATE + " text not null " +
            ", " + FK_UNIQUE_ID + " text not null " +
            ", PRIMARY KEY ("+UNIQUE_ID+")"+
            ", FOREIGN KEY ("+FK_UNIQUE_ID+")"+
            " REFERENCES "+T_COUNTER_INFOR+" ("+UNIQUE_ID+")"+
            " ); ";

    final String DROP_T = "drop table ";

}
