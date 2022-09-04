package ddwucom.mobile.finalproject.ma01_20181033;


import android.content.DialogInterface;
import android.content.Intent;

import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.FragmentActivity;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class SearchCinemaActivity  extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;
        private Geocoder geocoder;
        private Button button;
        private EditText editText;
        private String address;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search_cinema);
            editText = (EditText) findViewById(R.id.editText);
            button=(Button)findViewById(R.id.button);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        @Override
        public void onMapReady(final GoogleMap googleMap) {
            mMap = googleMap;
            geocoder = new Geocoder(this);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
                @Override
                public void onMapClick(LatLng point) {
                    MarkerOptions mOptions = new MarkerOptions();
                    mOptions.title("마커 좌표");
                    Double latitude = point.latitude;
                    Double longitude = point.longitude;
                    mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                    mOptions.position(new LatLng(latitude, longitude));

                    googleMap.addMarker(mOptions);
                }
            });

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchCinemaActivity.this);
                    builder.setTitle("영화관 확인")
                            .setMessage("선택하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra("address", address);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("취소", null)
                            .setCancelable(false)
                            .show();
                }
            });

            button.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String str = editText.getText().toString();

                    if (str.equals("")) {
                        Toast.makeText(SearchCinemaActivity.this, "검색어 입력이 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        List<Address> addressList = null;
                        try {
                            addressList = geocoder.getFromLocationName(
                                    str,
                                    10);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println(addressList.get(0).toString());

                        String[] splitStr = addressList.get(0).toString().split(",");
                        address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2);
                        System.out.println(address);

                        String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1);
                        String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1);
                        System.out.println(latitude);
                        System.out.println(longitude);


                        LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                        MarkerOptions mOptions2 = new MarkerOptions();
                        mOptions2.title("검색 결과");
                        mOptions2.snippet(address);
                        mOptions2.position(point);

                        mMap.addMarker(mOptions2);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 18));
                    }
                }
            });


            LatLng school = new LatLng(37.604094, 127.042463);
            mMap.addMarker(new MarkerOptions().position(school).title("Marker in School"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(school, 18));

        }
}
