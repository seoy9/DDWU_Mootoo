package ddwucom.mobile.finalproject.ma01_20181033;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchMovieActivity extends AppCompatActivity {

    public static final String TAG = "SearchMovieActivity";

    EditText editText;
    ListView lvList;
    String apiAddress;
    
    String query;
    
    MyMovieAdapter adapter;
    ArrayList<MovieDto> resultList;
    MovieXmlParser parser;
    NetworkManager networkManager;
    ImageFileManager imageFileManager;
    ReviewDBManager reviewDBManager;
    MovieDto dto;
    int pos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        
        editText = findViewById(R.id.etMovie);
        lvList = findViewById(R.id.lvList);
        
        resultList = new ArrayList<>();
        adapter = new MyMovieAdapter(this, R.layout.listview_movie, resultList);
        lvList.setAdapter(adapter);
        
        apiAddress = getResources().getString(R.string.api_url);
        parser = new MovieXmlParser();
        networkManager = new NetworkManager(this);
        networkManager.setClientId(getResources().getString(R.string.client_id));
        networkManager.setClientSecret(getResources().getString(R.string.client_secret));
        imageFileManager = new ImageFileManager(this);
        reviewDBManager = new ReviewDBManager(this);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(SearchMovieActivity.this);
                builder.setTitle("선택 확인")
                        .setMessage("리뷰를 작성하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dto = resultList.get(pos);
                                Intent addIntent = new Intent(SearchMovieActivity.this, AddReviewActivity.class);
                                addIntent.putExtra("imageLink", dto.getImageLink());
                                addIntent.putExtra("title", dto.getTitle());
                                startActivity(addIntent);

                                finish();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ArrayList<String> list = reviewDBManager.findReviewImageList(this);
        imageFileManager.clearTemporaryFiles(list);
    }
    
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_movie:
                query = editText.getText().toString();
                
                try {
                    new NetworkAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(SearchMovieActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = null;

            result = networkManager.downloadContents(address);
            if(result == null) return "Error!";

            Log.d(TAG, result);

            resultList = parser.parse(result);

            for(MovieDto dto : resultList) {
                Bitmap bitmap = networkManager.downloadImage(dto.getImageLink());

                if(bitmap != null) {
                    imageFileManager.saveBitmapToTemporary(bitmap, dto.getImageLink());
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            adapter.setList(resultList);
            progressDlg.dismiss();
        }
    }
}
