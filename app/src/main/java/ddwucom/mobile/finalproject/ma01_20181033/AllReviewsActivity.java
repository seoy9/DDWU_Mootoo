package ddwucom.mobile.finalproject.ma01_20181033;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AllReviewsActivity extends AppCompatActivity {
	
	ListView lvContacts = null;
	ReviewDBHelper helper;
	ReviewDBManager manager;
	Cursor cursor;
	MyCursorAdapter adapter;
	final int UPDATE_CODE = 200;
	long iD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_reviews);
		lvContacts = (ListView)findViewById(R.id.lvContacts);

		helper = new ReviewDBHelper(this);
		manager = new ReviewDBManager(this);

		cursor = manager.getAllReviewList(this);

		adapter = new MyCursorAdapter(this, R.layout.listview_layout, cursor);

		lvContacts.setAdapter(adapter);

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				iD = id;

				SQLiteDatabase db = helper.getWritableDatabase();
				Cursor newCursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " where " + ReviewDBHelper.COL_ID + "=" + iD, null);

				newCursor.moveToNext();

				ReviewDto dto = new ReviewDto();
				dto.setId(newCursor.getLong(newCursor.getColumnIndex(ReviewDBHelper.COL_ID)));
				dto.setTitle(newCursor.getString(newCursor.getColumnIndex(ReviewDBHelper.COL_TITLE)));
				dto.setDate(newCursor.getString(newCursor.getColumnIndex(ReviewDBHelper.COL_DATE)));
				dto.setMemo(newCursor.getString(newCursor.getColumnIndex(ReviewDBHelper.COL_MEMO)));
				dto.setGenre(newCursor.getString(newCursor.getColumnIndex(ReviewDBHelper.COL_GENRE)));
				dto.setTogether(newCursor.getString(newCursor.getColumnIndex(ReviewDBHelper.COL_TOGETHER)));
				dto.setRating(newCursor.getInt(newCursor.getColumnIndex(ReviewDBHelper.COL_RATING)));
				dto.setImageLink(newCursor.getString(newCursor.getColumnIndex(ReviewDBHelper.COL_IMAGELINK)));
				dto.setImageFileName(newCursor.getString(newCursor.getColumnIndex(ReviewDBHelper.COL_IMAGEFILENAME)));
				dto.setAddress(newCursor.getString(newCursor.getColumnIndex(ReviewDBHelper.COL_ADDRESS)));

				Intent updateIntent = new Intent(AllReviewsActivity.this, UpdateActivity.class);
				updateIntent.putExtra("dto", dto);
				startActivityForResult(updateIntent, UPDATE_CODE);
            }
        });

		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				iD = id;
				AlertDialog.Builder builder = new AlertDialog.Builder(AllReviewsActivity.this);
				builder.setTitle("삭제 확인")
						.setMessage("삭제하시겠습니까?")
						.setPositiveButton("확인", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								SQLiteDatabase db = helper.getWritableDatabase();
								Cursor cursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " where " + ReviewDBHelper.COL_ID + "=" + iD, null);
								cursor.moveToNext();
								ImageFileManager imageFileManager = new ImageFileManager(AllReviewsActivity.this);
								imageFileManager.removeImageFromExt(cursor.getString(cursor.getColumnIndex(ReviewDBHelper.COL_IMAGEFILENAME)));

								String whereClause = ReviewDBHelper.COL_ID + "=?";
								String[] whereArgs = new String[] { String.valueOf(iD) };
								int result = db.delete(ReviewDBHelper.TABLE_NAME, whereClause, whereArgs);
								Cursor newCursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME, null);
								if(result > 0) adapter.changeCursor(newCursor);
							}
						})
						.setNegativeButton("취소", null)
						.setCancelable(false)
						.show();
				return true;
			}
		});


	}

	@Override
	protected void onResume() {
		super.onResume();
        helper.close();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (cursor != null) cursor.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == UPDATE_CODE) {
			switch (resultCode) {
				case RESULT_OK:
					SQLiteDatabase db = helper.getReadableDatabase();
					Cursor newCursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME, null);
					adapter.changeCursor(newCursor);
					break;
				case RESULT_CANCELED:
					break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	public void onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_new:
				item.setChecked(true);

				SQLiteDatabase db = helper.getWritableDatabase();
				Cursor newCursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " order by " + ReviewDBHelper.COL_ID + " DESC", null);
				adapter.changeCursor(newCursor);
				break;
			case R.id.menu_old:
				item.setChecked(true);

				SQLiteDatabase db2 = helper.getWritableDatabase();
				Cursor newCursor2 = db2.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " order by " + ReviewDBHelper.COL_ID, null);
				adapter.changeCursor(newCursor2);
				break;
			case R.id.menu_top:
				item.setChecked(true);

				SQLiteDatabase db3 = helper.getWritableDatabase();
				Cursor newCursor3 = db3.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " order by " + ReviewDBHelper.COL_RATING + " DESC", null);
				adapter.changeCursor(newCursor3);
				break;
			case R.id.menu_low:
				item.setChecked(true);

				SQLiteDatabase db4 = helper.getWritableDatabase();
				Cursor newCursor4 = db4.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " order by " + ReviewDBHelper.COL_RATING, null);
				adapter.changeCursor(newCursor4);
				break;
			case R.id.menu_location:
				item.setChecked(true);

				SQLiteDatabase db5 = helper.getWritableDatabase();
				Cursor newCursor5 = db5.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME + " order by " + ReviewDBHelper.COL_ADDRESS, null);
				adapter.changeCursor(newCursor5);
				break;
			case R.id.menu_search:
				Intent intent = new Intent(AllReviewsActivity.this, SearchReviewActivity.class);
				startActivity(intent);
				break;
		}
	}
}




