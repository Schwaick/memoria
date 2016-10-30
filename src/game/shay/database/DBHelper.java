package game.shay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "game";
	public static final int DB_VERSION = 1;
	
	public static final String TABLE_SOUND = "sound";
	public static final String TABLE_ROUNDS = "rounds";
	
	public static final String COLUMN_ID = "_id";
	
	public static final String COLUMN_ENABLED_SOUND = "enabled";
	
	public static final String COLUMN_DIFFICULTY_ROUNDS = "difficulty";
	public static final String COLUMN_HITS_ROUNDS = "hits";
	public static final String COLUMN_TOTAL_ROUNDS = "total";

	private static final String CREATE_SQL = 
			"CREATE TABLE "+TABLE_SOUND+" ("+COLUMN_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + 
					COLUMN_ENABLED_SOUND+" INTEGER NOT NULL)";
	
	private static final String CREATE_SQL2 = 
			"CREATE TABLE "+TABLE_ROUNDS+" ("+COLUMN_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + 
					COLUMN_DIFFICULTY_ROUNDS+" TEXT NOT NULL, "+COLUMN_HITS_ROUNDS+" INTEGER NOT NULL, " +
					COLUMN_TOTAL_ROUNDS+" INTEGER NOT NULL)";

	private static final String DELETE_SQL = 
			"DROP TABLE " + TABLE_SOUND;
	
	private static final String DELETE_SQL2 = 
			"DROP TABLE " + TABLE_ROUNDS;
		
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_SQL);
		db.execSQL(CREATE_SQL2);
		
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ID, 1);
		cv.put(COLUMN_ENABLED_SOUND, 1);
		db.insert(TABLE_SOUND, null, cv);
		
		cv = new ContentValues();
		cv.put(COLUMN_ID, 1);
		cv.put(COLUMN_DIFFICULTY_ROUNDS, "veryeasy");
		cv.put(COLUMN_HITS_ROUNDS, 0);
		cv.put(COLUMN_TOTAL_ROUNDS, 0);
		db.insert(TABLE_ROUNDS, null, cv);
		
		cv = new ContentValues();
		cv.put(COLUMN_ID, 2);
		cv.put(COLUMN_DIFFICULTY_ROUNDS, "easy");
		cv.put(COLUMN_HITS_ROUNDS, 0);
		cv.put(COLUMN_TOTAL_ROUNDS, 0);
		db.insert(TABLE_ROUNDS, null, cv);
		
		cv = new ContentValues();
		cv.put(COLUMN_ID, 3);
		cv.put(COLUMN_DIFFICULTY_ROUNDS, "medium");
		cv.put(COLUMN_HITS_ROUNDS, 0);
		cv.put(COLUMN_TOTAL_ROUNDS, 0);
		db.insert(TABLE_ROUNDS, null, cv);
		
		cv = new ContentValues();
		cv.put(COLUMN_ID, 4);
		cv.put(COLUMN_DIFFICULTY_ROUNDS, "hard");
		cv.put(COLUMN_HITS_ROUNDS, 0);
		cv.put(COLUMN_TOTAL_ROUNDS, 0);
		db.insert(TABLE_ROUNDS, null, cv);
		
		cv = new ContentValues();
		cv.put(COLUMN_ID, 5);
		cv.put(COLUMN_DIFFICULTY_ROUNDS, "veryhard");
		cv.put(COLUMN_HITS_ROUNDS, 0);
		cv.put(COLUMN_TOTAL_ROUNDS, 0);
		db.insert(TABLE_ROUNDS, null, cv);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DELETE_SQL);
		db.execSQL(DELETE_SQL2);
		db.execSQL(CREATE_SQL);
		db.execSQL(CREATE_SQL2);
	}
	
	public boolean isSoundEnabled() {
		SQLiteDatabase myDB = this.getReadableDatabase();
		
		Cursor myCursor = myDB.query(TABLE_SOUND, null, COLUMN_ID+"=1", null, null, null, null);
		if(myCursor.moveToFirst()) {
			int index = myCursor.getColumnIndex(COLUMN_ENABLED_SOUND);
			if(myCursor.getInt(index) == 1) {
				myDB.close();
        		return true;
			} else {
				myDB.close();
        		return false;
			}
		}
		myDB.close();
        return false;
	}
	
	public void updateSound(int currentTileIndex) {
		SQLiteDatabase myDB = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ENABLED_SOUND, currentTileIndex == 0 ? 1 : 0);
		myDB.update(TABLE_SOUND, cv, COLUMN_ID+"=1", null);
		
		myDB.close();
	}

	public void addHits(int id) {
		int hits = getHits(id);
		SQLiteDatabase myDB = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(COLUMN_HITS_ROUNDS, hits+1);
		myDB.update(TABLE_ROUNDS, cv, COLUMN_ID+"=?", new String[]{String.valueOf(id)});

		myDB.close();
	}

	public void addTotal(int id) {
		int total = getTotal(id);
		SQLiteDatabase myDB = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_TOTAL_ROUNDS, total+1);
		myDB.update(TABLE_ROUNDS, cv, COLUMN_ID+"=?", new String[]{String.valueOf(id)});
		
		myDB.close();
	}
	
	public int getHits(int id) {
		SQLiteDatabase myDB = this.getReadableDatabase();
		
		Cursor myCursor = myDB.query(TABLE_ROUNDS, null, COLUMN_ID+"=?", new String[]{String.valueOf(id)}, null, null, null);
		if(myCursor.moveToFirst()) {
			int index = myCursor.getColumnIndex(COLUMN_HITS_ROUNDS);
			int value = myCursor.getInt(index);
			myDB.close();
        	return value;
		}
		
		myDB.close();
        return -1;
	}
	
	public int getTotal(int id) {
		SQLiteDatabase myDB = this.getReadableDatabase();
		
		Cursor myCursor = myDB.query(TABLE_ROUNDS, null, COLUMN_ID+"=?", new String[]{String.valueOf(id)}, null, null, null);
		if(myCursor.moveToFirst()) {
			int index = myCursor.getColumnIndex(COLUMN_TOTAL_ROUNDS);
			int value = myCursor.getInt(index);
			myDB.close();
        	return value;
		}
		
		myDB.close();
        return -1;
	}
	
	/*
	public boolean isLevelLocked(int id) {
		SQLiteDatabase myDB = this.getReadableDatabase();
        
        Cursor myCursor = myDB.query(TABLE_NAME, null, COLUMN_ID+"=?", new String[]{String.valueOf(id)}, null, null, null);
        if(myCursor.moveToFirst()) {
        	int index = myCursor.getColumnIndex(COLUMN_LOCKED);
        	if(myCursor.getInt(index) == 1) {
        		myDB.close();
        		return true;
        	} else {
        		myDB.close();
        		return false;
        	}
        }
        myDB.close();
        return false;
	}
	
	public void unlockLevel(int level) {
		SQLiteDatabase myDB = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_LOCKED, 0);
		myDB.update(TABLE_NAME, cv, COLUMN_LVL+"=?", new String[]{String.valueOf(level)});
		
		myDB.close();
	}
	*/
	
}
