package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {
	private static final String LOG_TAG = DbAdapter.class.getSimpleName();

	private Context context;
	private SQLiteDatabase database;
	private DbHelper dbHelper;
	// Database fields
	//public static final String TABLE_EVENTS = "Events";
	//public static final String COLUMN_ID = "id";
	//public static final String COLUMN_IMAGE = "image";
	
	//public static final String COLUMN_TITLE = "title";
	//public static final String COLUMN_DESCRIPTION = "description";
	//public static final String COLUMN_START_TIME = "startTime";
	//public static final String COLUMN_END_TIME = "endTime";
	//public static final String COLUMN_LOCATION = "location";

	public DbAdapter(Context context) {
	    this.context = context;
	  }

	public DbAdapter open() throws SQLException {
		dbHelper = new DbHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	private ContentValues createContentValues(String id,String image,String title, String description,
			String start_time, String end_time, String location ) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.COLUMN_ID, id);
		values.put(DbHelper.COLUMN_IMAGE, image);
		values.put(DbHelper.COLUMN_TITLE, title);
		values.put(DbHelper.COLUMN_DESCRIPTION, description);
		values.put(DbHelper.COLUMN_START_TIME,start_time);
		values.put(DbHelper.COLUMN_END_TIME, end_time);
		values.put(DbHelper.COLUMN_LOCATION, location);
		
		return values;
	}

	// create a event
	public long createEvents(String id,String image,String title, String description, String start_time,
		String end_time,String location) {
		ContentValues initialValues = createContentValues(id,image, title, description, start_time, end_time, location);
		return database.insert(DbHelper.TABLE_EVENTS, null, initialValues);
		
		
	}

	// update a event
	public boolean updateEvents(String id,String image,String title, String description, String start_time,
			String end_time,String location) {
	    ContentValues updateValues = createContentValues(id,image, title, description, start_time, end_time, location);
		return database.update(DbHelper.TABLE_EVENTS, updateValues, DbHelper.COLUMN_ID + "=" + id, null) > 0;
	  }

	// delete a event
	public boolean deleteEvents(String id) {
	    return database.delete(DbHelper.TABLE_EVENTS, DbHelper.COLUMN_ID + "=" + id, null) > 0;
	  }

	// fetch all event
	public Cursor fetchAllEvents() {
		Cursor cursor= database.query(DbHelper.TABLE_EVENTS, new String[] { DbHelper.COLUMN_ID, DbHelper.COLUMN_IMAGE,DbHelper.COLUMN_TITLE, DbHelper.COLUMN_DESCRIPTION, DbHelper.COLUMN_START_TIME, DbHelper.COLUMN_END_TIME ,DbHelper.COLUMN_LOCATION }, null, null,
				null, null, null);
		return cursor;
		
	}
	//fetch event by id
	public Cursor fetchEventById(String id){
		Cursor c= database.query(true, DbHelper.TABLE_EVENTS, new String[] { DbHelper.COLUMN_ID, DbHelper.COLUMN_IMAGE,DbHelper.COLUMN_TITLE, DbHelper.COLUMN_DESCRIPTION, DbHelper.COLUMN_START_TIME, DbHelper.COLUMN_END_TIME ,DbHelper.COLUMN_LOCATION },DbHelper.COLUMN_ID + "=" + id, null, null, null,null, null, null);
		return c;
	}
	

	// fetch events filter by a string
	public Cursor fetchEventsByFilter(String filter) {
		Cursor mCursor = database.query(true, DbHelper.TABLE_EVENTS,
				new String[] { DbHelper.COLUMN_ID, DbHelper.COLUMN_IMAGE,DbHelper.COLUMN_TITLE, DbHelper.COLUMN_DESCRIPTION, DbHelper.COLUMN_START_TIME, DbHelper.COLUMN_END_TIME ,DbHelper.COLUMN_LOCATION  },
				DbHelper.COLUMN_TITLE + " like '%" + filter + "%'", null, null, null, null,null);

		return mCursor;
	}

}
