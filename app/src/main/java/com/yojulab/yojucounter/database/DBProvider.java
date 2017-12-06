package com.yojulab.yojucounter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DBProvider implements ConstantsImpl {

	private final Context context;
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
		return getItemsAsDate(getDateFormat());
	}

	public List getItemsAsDate(String create_date){
		String query = new StringBuilder()
				.append("select dc.unique_id,ci.counter_name,ci.delete_yn,dc.count_date ")
				.append("  from (select * from daily_count ")
				.append(" 		where count_date like '"+create_date+"%') dc ")
				.append("  		LEFT JOIN ")
				.append("  		counter_information ci")
				.append("  	on ci.unique_id = dc.fk_unique_id ")
				.append("where ci.delete_yn = 'N'")
				.toString();
		Cursor cursor = db.rawQuery(query, null);

		ArrayList<HashMap<String,Object>> arrayList = new ArrayList<>();

		HashMap<String,Object> hashMap = null;

		while (cursor.moveToNext()) {
			hashMap = new HashMap<String,Object>();
			hashMap.put("unique_id", cursor.getString(0));
			hashMap.put("title", cursor.getString(1));
			arrayList.add(hashMap);
		}

		return arrayList;
	}

	public String addItem(String title){
		String uniqueID = getUUID();
		ContentValues values = new ContentValues();
		values.put(UNIQUE_ID, uniqueID);
		values.put(COUNTER_NAME, title);
		values.put(DELETE_YN, "N");

		long id = 0;
		id = db.insert(T_COUNTER_INFOR, null, values);

		return uniqueID;
	}

	public String addSubItem(String fkUniqueId){
		String uniqueID = getUUID();
		ContentValues values = new ContentValues();
		values.put(UNIQUE_ID, uniqueID);
		values.put(COUNT_NUMBER, 0);
		values.put(COUNT_DATE, getDateFormat());
		values.put(FK_UNIQUE_ID, fkUniqueId);

		long id = 0;
		id = db.insert(T_DAILY_COUNT, null, values);

		return uniqueID;
	}

	private String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	public int deleteItem(Map map){
		ContentValues values = new ContentValues();
//		values.put(C_ENDDATE, endDate);
		
		String whereClause = UNIQUE_ID + " = ? " ;
		int cnt = db.update(T_COUNTER_INFOR, values, whereClause, new String[]{(String) map.get("title")});
//		db.delete(T_COUNTER_INFOR, "_id = ?", new String[]{id});
		
		return cnt;
	}
	
	public int modifyItem(Map map){
		ContentValues values = new ContentValues();
		values.put(COUNTER_NAME, (String) map.get("title"));
		
		String whereClause = UNIQUE_ID + " = ? " ;
		int cnt = db.update(T_COUNTER_INFOR, values, whereClause, new String[]{(String) map.get("title")});
		
		return cnt;
	}

	public int increaseCount(String fkUniqueId, int countNumber){

		ContentValues values = new ContentValues();
		values.put(COUNT_NUMBER, countNumber);

		String whereClause = UNIQUE_ID + " = ? " ;
		int cnt = db.update(T_DAILY_COUNT, values, whereClause, new String[]{fkUniqueId});

		return cnt;
	}

	public String getDateFormat(int year, int monthOfYear, int dayOfMonth) {
		String sDate = "";
        Calendar cal = Calendar.getInstance();
		DateFormat formatter ;
		formatter = new SimpleDateFormat(dateFormat);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date date = cal.getTime();

        sDate = formatter.format(date); 
		return sDate ;
	}

	public String getDateFormat(int gainDay) {
		String sDate = "";
		DateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, gainDay);
        Date date = cal.getTime();

        sDate = formatter.format(date); 
        
        return sDate ;
	}

	public String getDateFormat(int gainDay, String currentDate) {
		String sDate = "";
		DateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        Date cDate = null;
		try {
			cDate = formatter.parse(currentDate);
	        cal.setTime(cDate);
	        cal.add(Calendar.DAY_OF_MONTH, gainDay);
	        Date date = cal.getTime();

	        sDate = formatter.format(date); 
		} catch (ParseException e) {
			e.printStackTrace();
		}

        return sDate ;
	}

	public String getDateFormat() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmsss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}
