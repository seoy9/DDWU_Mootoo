package ddwucom.mobile.finalproject.ma01_20181033;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SearchReviewActivity extends AppCompatActivity {
    ListView lvContacts = null;
    ReviewDBHelper helper;
    ReviewDBManager manager;
    Cursor cursor;
    MyCursorAdapter adapter;
    final int UPDATE_CODE = 200;
    long iD;
    EditText etSearch;
    RadioGroup radioGroup;
    String what = ReviewDBHelper.COL_TITLE;
    String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_review);
        lvContacts = (ListView)findViewById(R.id.lvContacts3);

        helper = new ReviewDBHelper(this);
        manager = new ReviewDBManager(this);
        etSearch = findViewById(R.id.et_search);
        radioGroup = findViewById(R.id.radioGroup2);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_title2:
                        what = ReviewDBHelper.COL_TITLE;
                        break;
                    case R.id.rbtn_date2:
                        what = ReviewDBHelper.COL_DATE;
                        break;
                    case R.id.rbtn_genre2:
                        what = ReviewDBHelper.COL_GENRE;
                        break;
                    case R.id.rbtn_together2:
                        what = ReviewDBHelper.COL_TOGETHER;
                        break;
                    case R.id.rbtn_location2:
                        what = ReviewDBHelper.COL_ADDRESS;
                        break;
                }
            }
        });

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
                dto.setAddress(newCursor.getString(newCursor.getColumnIndex(ReviewDBHelper.COL_ADDRESS)));

                Intent updateIntent = new Intent(SearchReviewActivity.this, UpdateActivity.class);
                updateIntent.putExtra("dto", dto);
                startActivityForResult(updateIntent, UPDATE_CODE);
            }
        });

        lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                iD = id;
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchReviewActivity.this);
                builder.setTitle("삭제 확인")
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = helper.getWritableDatabase();
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

    public void onClick(View v) {
        search = etSearch.getText().toString();

        if (search.equals("")) {
            Toast.makeText(this, "검색어 입력이 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            switch (what) {
                case ReviewDBHelper.COL_TITLE:
                    cursor = manager.findReviewByTitle(this, search);

                    if(cursor != null) {
                        if (adapter == null) {
                            adapter = new MyCursorAdapter(this, R.layout.listview_layout, cursor);
                            lvContacts.setAdapter(adapter);
                        } else {
                            adapter.changeCursor(cursor);
                        }
                    }
                    break;
                case ReviewDBHelper.COL_DATE:
                    cursor = manager.findReviewByDate(this, search);

                    if(cursor != null) {
                        if (adapter == null) {
                            adapter = new MyCursorAdapter(this, R.layout.listview_layout, cursor);
                            lvContacts.setAdapter(adapter);
                        } else {
                            adapter.changeCursor(cursor);
                        }
                    }
                    break;
                case ReviewDBHelper.COL_GENRE:
                    cursor = manager.findReviewByGenre(this, search);

                    if(cursor != null) {
                        if (adapter == null) {
                            adapter = new MyCursorAdapter(this, R.layout.listview_layout, cursor);
                            lvContacts.setAdapter(adapter);
                        } else {
                            adapter.changeCursor(cursor);
                        }
                    }
                    break;
                case ReviewDBHelper.COL_TOGETHER:
                    cursor = manager.findReviewByTogether(this, search);

                    if(cursor != null) {
                        if (adapter == null) {
                            adapter = new MyCursorAdapter(this, R.layout.listview_layout, cursor);
                            lvContacts.setAdapter(adapter);
                        } else {
                            adapter.changeCursor(cursor);
                        }
                    }
                    break;
                case ReviewDBHelper.COL_ADDRESS:
                    cursor = manager.findReviewByLocation(this, search);

                    if(cursor != null) {
                        if (adapter == null) {
                            adapter = new MyCursorAdapter(this, R.layout.listview_layout, cursor);
                            lvContacts.setAdapter(adapter);
                        } else {
                            adapter.changeCursor(cursor);
                        }
                    }
                    break;
            }
        }
    }
}
