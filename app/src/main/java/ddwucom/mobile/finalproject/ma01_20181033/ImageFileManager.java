package ddwucom.mobile.finalproject.ma01_20181033;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ImageFileManager {

    final static String TAG = "ImageFileManager";
    final static String IMG_EXT = ".jpg";

    private Context context;

    public ImageFileManager(Context context) {
        this.context = context;
    }

    public String saveBitmapToTemporary(Bitmap bitmap, String url) {
        String fileName = null;

        try {
            fileName = getFileNameFromUrl(url);

            File saveFile = new File(context.getFilesDir(), fileName);
            FileOutputStream fos = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fileName = null;
        } catch (IOException e) {
            e.printStackTrace();
            fileName = null;
        }

        return fileName;
    }

    public Bitmap getBitmapFromTemporary(String url) {
        String fileName = getFileNameFromUrl(url);
        String path = context.getFilesDir().getPath() + "/" + fileName;

        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(path);
        Log.i(TAG, path);

        return bitmap;
    }

    public void clearTemporaryFiles(ArrayList<String> list) {
        File internalStoragefiles = context.getFilesDir();
        File[] files = internalStoragefiles.listFiles();

        int isChecked;

        for(File target : files) {
            isChecked = 0;
            for(String name : list) {
                if (target.getName().equals(name)) {
                    isChecked = 1;
                }
            }
            if(isChecked == 0)
                target.delete();
        }
    }

    public boolean removeImageFromExt(String fileName) {
        if(!isExtStorageWritable()) return false;
        File targetFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        return targetFile.delete();
    }

    public String getFileNameFromUrl(String url) {
        String fileName = Uri.parse(url).getLastPathSegment();
        return fileName.replace("\n", "");
    }

    private boolean isExtStorageWritable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
