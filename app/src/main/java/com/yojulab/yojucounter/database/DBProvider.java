package com.yojulab.yojucounter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DBProvider implements ConstantsImpl {

	private Context context;
	private SQLiteDatabase db ;
	private OpenHelper dbHelper;
	
	public DBProvider(Context ctx){
		context = ctx;
	}

	public DBProvider open() throws SQLException {
		dbHelper = new OpenHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		dbHelper.close();
	}

	public List getItemsAsDate(){
		return getItemsAsDate(getDateFormat("yyyyMMdd"));
	}

	public List getItemsAsDate(String create_date){
		String query = new StringBuilder()
				.append("select ci.information_unique_id,ci.counter_name,ci.delete_yn ")
				.append("	,dc.daily_unique_id,ifnull(dc.count_number,0),dc.count_date ")
				.append("from (select * from counter_information ")
				.append(" 		where delete_yn = 'N') ci ")
				.append("  		LEFT JOIN ")
				.append("  		(select * from daily_count ")
				.append("  		where count_date like '"+create_date+"%') dc ")
				.append("on ci.information_unique_id = dc.information_unique_id ")
				.toString();
		Cursor cursor = db.rawQuery(query, null);

		ArrayList<HashMap<String,Object>> arrayList = new ArrayList<>();

		HashMap<String,Object> hashMap = null;

		while (cursor.moveToNext()) {
			hashMap = new HashMap<String,Object>();
			hashMap.put(INFORMATION_UNIQUE_ID, cursor.getString(0));
			hashMap.put(COUNTER_NAME, cursor.getString(1));
			hashMap.put(DAILY_UNIQUE_ID, cursor.getString(3));
			hashMap.put(COUNT_NUMBER, cursor.getInt(4));
			arrayList.add(hashMap);
		}

		return arrayList;
	}


	public List getDailyStatisticAsDate(String create_date){
		String query = new StringBuilder()
				.append("select ci.counter_name, ifnull(dc.count_number,0) ")
				.append("from (select * from counter_information) ci ")
				.append("	INNER JOIN ")
				.append("  	(select * from daily_count ")
				.append("  	where count_date like '"+create_date+"%') dc ")
				.append("on ci.information_unique_id = dc.information_unique_id ")
				.toString();

		Cursor cursor = db.rawQuery(query, null);

		ArrayList<HashMap<String,Object>> arrayList = new ArrayList<>();

		HashMap<String,Object> hashMap = null;

		while (cursor.moveToNext()) {
			hashMap = new HashMap<String,Object>();
			hashMap.put(COUNTER_NAME, cursor.getString(0));
			hashMap.put(COUNT_NUMBER, cursor.getInt(1));
			arrayList.add(hashMap);
		}

		return arrayList;
	}
	public String addItem(String title){
		String uniqueID = getUUID();
		ContentValues values = new ContentValues();
		values.put(INFORMATION_UNIQUE_ID, uniqueID);
		values.put(COUNTER_NAME, title);
		values.put(DELETE_YN, "N");

		long id = 0;
		id = db.insert(T_COUNTER_INFORMATION, null, values);

		return uniqueID;
	}

	public String addSubItem(String informationUniqueId){
		String dailyUniqueId = getUUID();
		ContentValues values = new ContentValues();
		values.put(DAILY_UNIQUE_ID, dailyUniqueId);
		values.put(COUNT_NUMBER, 1);
		values.put(COUNT_DATE, getDateFormat());
		values.put(INFORMATION_UNIQUE_ID, informationUniqueId);

		long id = 0;
		id = db.insert(T_DAILY_COUNT, null, values);

		return dailyUniqueId;
	}

	private String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	public int unUseItem(Map map){
		ContentValues values = new ContentValues();
		values.put(DELETE_YN, "Y");

		String whereClause = INFORMATION_UNIQUE_ID + " = ? " ;
		int cnt = db.update(T_COUNTER_INFORMATION, values, whereClause, new String[]{(String) map.get(INFORMATION_UNIQUE_ID)});

		return cnt;
	}
	
	public int increaseCount(String dailyUniqueId, int countNumber){

		ContentValues values = new ContentValues();
		values.put(COUNT_NUMBER, countNumber);

		String whereClause = DAILY_UNIQUE_ID + " = ? " ;
		int cnt = db.update(T_DAILY_COUNT, values, whereClause, new String[]{dailyUniqueId});

		return cnt;
	}

	public String getDateFormat(String dateForm) {
		DateFormat dateFormat = new SimpleDateFormat(dateForm);
		Date date = new Date();
		return dateFormat.format(date);
	}

	public String getDateFormat() {
		return getDateFormat("yyyyMMddHHmmsss");
	}

	public String getDateFormat(int gainDay) {
		String sDate = "";
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, gainDay);
		Date date = cal.getTime();

		sDate = formatter.format(date);

		return sDate ;
	}}
