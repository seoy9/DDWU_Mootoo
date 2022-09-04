package ddwucom.mobile.finalproject.ma01_20181033;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.ResultReceiver;

import java.util.ArrayList;

public class ReviewDBManager {
    ReviewDBHelper reviewDBHelper = null;
    Cursor cursor = null;

    public ReviewDBManager(Context context) {
        reviewDBHelper = new ReviewDBHelper(context);
    }

    public boolean addNewReview(ReviewDto newReview) {
        SQLiteDatabase db = reviewDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReviewDBHelper.COL_TITLE, newReview.getTitle());
        values.put(ReviewDBHelper.COL_DATE, newReview.getDate());
        values.put(ReviewDBHelper.COL_GENRE, newReview.getGenre());
        values.put(ReviewDBHelper.COL_TOGETHER, newReview.getTogether());
        values.put(ReviewDBHelper.COL_RATING, newReview.getRating());
        values.put(ReviewDBHelper.COL_MEMO, newReview.getMemo());
        values.put(ReviewDBHelper.COL_IMAGELINK, newReview.getImageLink());
        values.put(ReviewDBHelper.COL_IMAGEFILENAME, newReview.getImageFileName());
        values.put(ReviewDBHelper.COL_ADDRESS, newReview.getAddress());

        long count = db.insert(ReviewDBHelper.TABLE_NAME, null, values);

        if(count > 0) return true;
        return false;
    }

    public boolean updateReview(ReviewDto updateReview) {
        SQLiteDatabase db = reviewDBHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(ReviewDBHelper.COL_DATE, updateReview.getDate());
        row.put(ReviewDBHelper.COL_GENRE, updateReview.getGenre());
        row.put(ReviewDBHelper.COL_TOGETHER, updateReview.getTogether());
        row.put(ReviewDBHelper.COL_RATING, updateReview.getRating());
        row.put(ReviewDBHelper.COL_MEMO, updateReview.getMemo());
        row.put(ReviewDBHelper.COL_ADDRESS, updateReview.getAddress());

        String whereClause = ReviewDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(updateReview.getId()) };

        int result = db.update(ReviewDBHelper.TABLE_NAME, row, whereClause, whereArgs);
        reviewDBHelper.close();

        if(result > 0) return true;
        return false;
    }

    public Cursor getAllReviewList(Context context) {
        reviewDBHelper = new ReviewDBHelper(context);

        SQLiteDatabase db = reviewDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME, null);

        return cursor;
    }

    public Cursor findReviewByTitle(Context context, String title) {
        reviewDBHelper = new ReviewDBHelper(context);

        SQLiteDatabase db = reviewDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " where " + ReviewDBHelper.COL_TITLE + " LIKE '%" + title + "%'", null);

        return cursor;
    }

    public Cursor findReviewByDate(Context context, String date) {
        reviewDBHelper = new ReviewDBHelper(context);

        SQLiteDatabase db = reviewDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " where " + ReviewDBHelper.COL_DATE + " LIKE '%" + date + "%'", null);

        return cursor;
    }

    public Cursor findReviewByGenre(Context context, String genre) {
        reviewDBHelper = new ReviewDBHelper(context);

        SQLiteDatabase db = reviewDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " where " + ReviewDBHelper.COL_GENRE + " LIKE '%" + genre + "%'", null);

        return cursor;
    }

    public Cursor findReviewByTogether(Context context, String together) {
        reviewDBHelper = new ReviewDBHelper(context);

        SQLiteDatabase db = reviewDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " where " + ReviewDBHelper.COL_TOGETHER + " LIKE '%" + together + "%'", null);

        return cursor;
    }

    public Cursor findReviewByLocation(Context context, String location) {
        reviewDBHelper = new ReviewDBHelper(context);

        SQLiteDatabase db = reviewDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " where " + ReviewDBHelper.COL_ADDRESS + " LIKE '%" + location + "%'", null);

        return cursor;
    }

    public ArrayList<String> findReviewImageList(Context context) {
        reviewDBHelper = new ReviewDBHelper(context);
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = reviewDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select " + ReviewDBHelper.COL_IMAGEFILENAME + " from " + ReviewDBHelper.TABLE_NAME, null);

        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }

        return list;
    }
}
