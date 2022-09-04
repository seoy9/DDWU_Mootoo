package ddwucom.mobile.finalproject.ma01_20181033;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btn_allReview:
                intent = new Intent(this, AllReviewsActivity.class);
                break;
            case R.id.btn_addNewReview:
                intent = new Intent(this, SearchMovieActivity.class);
                break;
        }
        if (intent != null) startActivity(intent);
    }
}