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

	public boolean updateEvent(String id,String image,String title, String description,
			String start_time, String end_time, String location,String my,String latitude,String longitude) {
		 ContentValues updateValues = createContentValues(id, image, title, description,
				 start_time,end_time,location,my,latitude,longitude);
		return database.update(DbHelper.TABLE_EVENTS, updateValues, id +"="+ DbHelper.COLUMN_ID, null) > 0;
	  }


	
	private ContentValues createContentValues(String id, String image,
			String title, String description, String start_time,
			String end_time, String location, String my, String latitude,
			String longitude) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.COLUMN_ID, id);
		values.put(DbHelper.COLUMN_IMAGE, image);
		values.put(DbHelper.COLUMN_TITLE, title);
		values.put(DbHelper.COLUMN_DESCRIPTION, description);
		values.put(DbHelper.COLUMN_START_TIME, start_time);
		values.put(DbHelper.COLUMN_END_TIME, end_time);
		values.put(DbHelper.COLUMN_LOCATION, location);
		values.put(DbHelper.COLUMN_MY, my);
		values.put(DbHelper.COLUMN_LATITUDE, latitude);
		values.put(DbHelper.COLUMN_LONGITUDE, longitude);

		return values;
	}

	// create a event
	public long createEvents(String id, String image, String title,
			String description, String start_time, String end_time,
			String location, String my, String latitude, String longitude) {
		ContentValues initialValues = createContentValues(id, image, title,
				description, start_time, end_time, location, my, latitude,
				longitude);
		return database.insert(DbHelper.TABLE_EVENTS, null, initialValues);

	}

	// update a event
	public boolean updateEvents(String id, String image, String title,
			String description, String start_time, String end_time,
			String location, String my, String latitude, String longitude) {
		ContentValues updateValues = createContentValues(id, image, title,
				description, start_time, end_time, location, my, latitude,
				longitude);
		return database.update(DbHelper.TABLE_EVENTS, updateValues,
				DbHelper.COLUMN_ID + "=" + id, null) > 0;
	}

	// delete a event
	public boolean deleteEvents(String id) {
		return database.delete(DbHelper.TABLE_EVENTS, DbHelper.COLUMN_ID + "="
				+ id, null) > 0;
	}

	// fetch all event
	public Cursor fetchAllEvents() {
		Cursor cursor = database.query(DbHelper.TABLE_EVENTS, new String[] {
				DbHelper.COLUMN_ID, DbHelper.COLUMN_IMAGE,
				DbHelper.COLUMN_TITLE, DbHelper.COLUMN_DESCRIPTION,
				DbHelper.COLUMN_START_TIME, DbHelper.COLUMN_END_TIME,
				DbHelper.COLUMN_LOCATION, DbHelper.COLUMN_MY,
				DbHelper.COLUMN_LATITUDE, DbHelper.COLUMN_LONGITUDE }, null,
				null, null, null, null);
		return cursor;

	}

	// fetch event by id
	public Cursor fetchEventById(String id) {
		Cursor c = database.query(true, DbHelper.TABLE_EVENTS, new String[] {
				DbHelper.COLUMN_ID, DbHelper.COLUMN_IMAGE,
				DbHelper.COLUMN_TITLE, DbHelper.COLUMN_DESCRIPTION,
				DbHelper.COLUMN_START_TIME, DbHelper.COLUMN_END_TIME,
				DbHelper.COLUMN_LOCATION, DbHelper.COLUMN_MY,
				DbHelper.COLUMN_LATITUDE, DbHelper.COLUMN_LONGITUDE },
				DbHelper.COLUMN_ID + "=" + id, null, null, null, null, null,
				null);
		return c;
	}

	// fetch event by is myevent
	public Cursor fetchEventByMy(String my) {
		Cursor c = database.query(true, DbHelper.TABLE_EVENTS, new String[] {
				DbHelper.COLUMN_ID, DbHelper.COLUMN_IMAGE,
				DbHelper.COLUMN_TITLE, DbHelper.COLUMN_DESCRIPTION,
				DbHelper.COLUMN_START_TIME, DbHelper.COLUMN_END_TIME,
				DbHelper.COLUMN_LOCATION, DbHelper.COLUMN_MY,
				DbHelper.COLUMN_LATITUDE, DbHelper.COLUMN_LONGITUDE },
				DbHelper.COLUMN_MY + "=" + my, null, null, null, null, null,
				null);
		return c;
	}

	// fetch events filter by a string
	public Cursor fetchEventsByFilter(String filter) {
		Cursor mCursor = database.query(true, DbHelper.TABLE_EVENTS,
				new String[] { DbHelper.COLUMN_ID, DbHelper.COLUMN_IMAGE,
						DbHelper.COLUMN_TITLE, DbHelper.COLUMN_DESCRIPTION,
						DbHelper.COLUMN_START_TIME, DbHelper.COLUMN_END_TIME,
						DbHelper.COLUMN_LOCATION, DbHelper.COLUMN_MY,
						DbHelper.COLUMN_LATITUDE, DbHelper.COLUMN_LONGITUDE },
				DbHelper.COLUMN_TITLE + " like '%" + filter + "%'", null, null,
				null, null, null);

		return mCursor;
	}

}
