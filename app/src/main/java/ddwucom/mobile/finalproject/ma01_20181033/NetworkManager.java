package ddwucom.mobile.finalproject.ma01_20181033;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkManager {

    private Context context;
    private String clientId;
    private String clientSecret;

    public NetworkManager(Context context) {
        this.context = context;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String downloadContents(String address) {
        HttpURLConnection conn = null;
        InputStream stream = null;
        String result = null;

        if(!isOnline()) return null;

        try {
            URL url = new URL(address);
            conn = (HttpURLConnection)url.openConnection();
            stream = getNetworkConnection(conn);
            result = readStreamToString(stream);
            if(stream != null) stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) conn.disconnect();
        }

        return result;
    }

    public Bitmap downloadImage(String address) {
        HttpURLConnection conn = null;
        InputStream stream = null;
        Bitmap result = null;

        try {
            URL url = new URL(address);
            conn = (HttpURLConnection)url.openConnection();
            stream = getNetworkConnection(conn);
            result = readStreamToBitmap(stream);
            if(stream != null) stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) conn.disconnect();
        }
        return result;
    }

    private Bitmap readStreamToBitmap(InputStream stream) {
        return BitmapFactory.decodeStream(stream);
    }

    private String readStreamToString(InputStream stream) {
        StringBuilder result = new StringBuilder();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readLine = bufferedReader.readLine();

            while (readLine != null) {
                result.append(readLine + "\n");
                readLine = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private InputStream getNetworkConnection(HttpURLConnection conn) throws Exception {
        conn.setReadTimeout(3000);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        if(clientId != null && clientSecret != null) {
            conn.setRequestProperty("X-Naver-Client-Id", clientId);
            conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);
        }

        if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + conn.getResponseCode());
        }

        return conn.getInputStream();
    }
}
