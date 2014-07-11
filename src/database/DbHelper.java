package database;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_EVENTS = "Events"; 
	 public static final String COLUMN_TABLEID = "idtable"; 
	 public static final String COLUMN_ID = "id"; 
	 public static final String COLUMN_IMAGE = "image"; 
	 public static final String COLUMN_TITLE = "title"; 
	 public static final String COLUMN_DESCRIPTION = "description";
	 public static final String COLUMN_START_TIME = "startTime";
	 public static final String COLUMN_END_TIME = "endTime";
	 public static final String COLUMN_LOCATION = "location";
	
	
	
	 
	 
	 private static final String DATABASE_NAME = "events.db"; 
	 private static final int DATABASE_VERSION = 4; 
	 
	 // Database creation sql statement 
	 private static final String DATABASE_CREATE = "create table " 
	 + TABLE_EVENTS + "( " 
	 + COLUMN_TABLEID + " integer primary key autoincrement, "
	 + COLUMN_ID + " integer not null, " 
	 + COLUMN_IMAGE + " text ," 
	 + COLUMN_TITLE + " text ," 
	 + COLUMN_DESCRIPTION + " text ," 
	 + COLUMN_START_TIME + " text," //+ COLUMN_START_TIME + " text not null," 
	 + COLUMN_END_TIME + " text ,"
	 + COLUMN_LOCATION + " text);";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE); 

		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	
			 Log.w(DbHelper.class.getName(),"Upgrading database from version " + oldVersion + " to "+ newVersion + ", which will destroy all old data"); 
			 db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS); 
			 onCreate(db); 

		
	}

}
