package ddwucom.mobile.finalproject.ma01_20181033;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReviewDBHelper extends SQLiteOpenHelper {
	
	private final static String DB_NAME = "review_db";
	public final static String TABLE_NAME = "review_table";
	public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";
    public final static String COL_DATE = "date";
	public final static String COL_GENRE = "genre";
	public final static String COL_TOGETHER = "together";
	public final static String COL_RATING = "rating";
    public final static String COL_MEMO = "memo";
    public final static String COL_IMAGELINK = "imagelink";
    public final static String COL_IMAGEFILENAME = "imagefilename";
    public final static String COL_ADDRESS = "address";

	public ReviewDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createSql = "create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement, "
				+ COL_TITLE + " TEXT, " + COL_DATE + " TEXT, " + COL_GENRE + " TEXT, " + COL_TOGETHER + " TEXT, "
				+ COL_RATING + " integer, " + COL_MEMO + " TEXT, " + COL_IMAGELINK + " TEXT, " + COL_IMAGEFILENAME + " TEXT, "
				+ COL_ADDRESS + " TEXT);";

		db.execSQL(createSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
	}

}
