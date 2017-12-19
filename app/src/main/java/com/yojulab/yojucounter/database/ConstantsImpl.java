package com.yojulab.yojucounter.database;

/**
 * Created by ohsanghun on 10/29/16.
 */

public interface ConstantsImpl {
    final String dateFormat = "yyyyMMdd";

    final String DB = "yojucounter.db";
    final int VERSION = 7;

    final String T_COUNTER_INFORMATION = "counter_information";
    final String INFORMATION_UNIQUE_ID = "information_unique_id";
    final String COUNTER_NAME = "counter_name";
    final String DELETE_YN = "delete_yn";

    final String CREATE_T_COUNTER_INFOR = "create table " + T_COUNTER_INFORMATION +
            " (" + INFORMATION_UNIQUE_ID + " text not null " +
            ", " + COUNTER_NAME + " text not null " +
            ", " + DELETE_YN + " text not null " +
            ", PRIMARY KEY ("+ INFORMATION_UNIQUE_ID +")"+
            " ); ";

    final String T_DAILY_COUNT = "daily_count";
    final String DAILY_UNIQUE_ID = "daily_unique_id";
    final String COUNT_NUMBER = "count_number";
    final String COUNT_DATE = "count_date";
    final String CREATE_T_DAILY_COUNT = "create table " + T_DAILY_COUNT +
            " ( " + DAILY_UNIQUE_ID + " text not null " +
            ", " + COUNT_NUMBER + " integer not null " +
            ", " + COUNT_DATE + " text not null " +
            ", " + INFORMATION_UNIQUE_ID + " text not null " +
            ", PRIMARY KEY ("+DAILY_UNIQUE_ID+")"+
            ", FOREIGN KEY ("+INFORMATION_UNIQUE_ID+")"+
            " REFERENCES "+ T_COUNTER_INFORMATION +" ("+ INFORMATION_UNIQUE_ID +")"+
            " ); ";

    final String T_COUNT_LOG = "count_log";
    final String CREATE_T_COUNT_LOG = "create table " + T_COUNT_LOG +
            " ( " + COUNT_DATE + " text not null " +
            ", " + INFORMATION_UNIQUE_ID + " text not null " +
            ", FOREIGN KEY ("+INFORMATION_UNIQUE_ID+")"+
            " REFERENCES "+ T_COUNTER_INFORMATION +" ("+ INFORMATION_UNIQUE_ID +")"+
            " ); ";

    final String DROP_T = "drop table ";

}
