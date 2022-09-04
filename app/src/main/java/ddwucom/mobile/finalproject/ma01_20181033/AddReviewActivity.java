package ddwucom.mobile.finalproject.ma01_20181033;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddReviewActivity extends AppCompatActivity {

	ImageView imageView;
	TextView tvTitle;
	EditText etDate;
	EditText etGenre;
	EditText etTogether;
	EditText etMemo;
	RatingBar ratingBar;
	TextView tvCinema;

	String title;
	String imageLink;
	String imageFileName;
	String address;
	final int FIND_CODE = 100;

	ReviewDBManager manager;
	ImageFileManager imageFileManager;
	NetworkManager networkManager;
	Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_review);

		imageFileManager = new ImageFileManager(this);
		networkManager = new NetworkManager(this);

		title = getIntent().getStringExtra("title");
		imageLink = getIntent().getStringExtra("imageLink");

		imageView = findViewById(R.id.img_movie);

		bitmap = imageFileManager.getBitmapFromTemporary(imageLink);

		if(bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.mipmap.ic_launcher);
		}

		tvTitle = findViewById(R.id.tv_title);
		tvTitle.setText(title);

		etDate = findViewById(R.id.et_date);
		etGenre = findViewById(R.id.et_genre);
		etTogether = findViewById(R.id.et_together);
		etMemo = findViewById(R.id.et_meno);
		ratingBar = findViewById(R.id.ratingBar);
		manager = new ReviewDBManager(this);
		tvCinema = findViewById(R.id.tv_cinema);
	}
	
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_save:
			String date = etDate.getText().toString();
			String genre = etGenre.getText().toString();
			String together = etTogether.getText().toString();
			String memo = etMemo.getText().toString();
			int rating = (int) ratingBar.getRating();
			boolean result;

			imageFileName = imageFileManager.saveBitmapToTemporary(bitmap, imageLink);

			if(title.equals("") || date.equals("") || genre.equals("") || together.equals("") || rating == 0) {
				result = false;
			} else {
				result = manager.addNewReview(new ReviewDto(title, date, genre, together, rating, memo, imageLink, imageFileName, address));
			}

			if(result) {
				Toast.makeText(this, "저장 완료!", Toast.LENGTH_SHORT).show();
				finish();
			} else
				Toast.makeText(this, "필수 항목 미작성!", Toast.LENGTH_SHORT).show();

			break;
		case R.id.btn_cancle:
			finish();
			break;

		case R.id.tv_cinema:
			Intent intent = new Intent(AddReviewActivity.this, SearchCinemaActivity.class);
			startActivityForResult(intent, FIND_CODE);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FIND_CODE) {
			switch (resultCode) {
				case RESULT_OK:
					address = data.getStringExtra("address");

					tvCinema.setText(address);

					break;
				case RESULT_CANCELED:
					break;
			}
		}
	}
}
