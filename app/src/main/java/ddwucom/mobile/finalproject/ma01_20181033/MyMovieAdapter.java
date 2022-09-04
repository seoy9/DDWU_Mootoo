package ddwucom.mobile.finalproject.ma01_20181033;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyMovieAdapter extends BaseAdapter {

    public static final String TAG = "MyMovieAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<MovieDto> list;
    private NetworkManager networkManager = null;
    private ImageFileManager imageFileManager = null;

    public MyMovieAdapter(Context context, int resource, ArrayList<MovieDto> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        imageFileManager = new ImageFileManager(context);
        networkManager = new NetworkManager(context);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with position : " + position);

        View view = convertView;
        ViewHolder viewHolder = null;

        if(view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = view.findViewById(R.id.tvTitle);
            viewHolder.tvDirector = view.findViewById(R.id.tvDirector);
            viewHolder.tvActor = view.findViewById(R.id.tvActor);
            viewHolder.ivImage = view.findViewById(R.id.ivImage);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        MovieDto dto = list.get(position);

        viewHolder.tvTitle.setText(dto.getTitle());
        viewHolder.tvDirector.setText("감독: " + dto.getDirector());
        viewHolder.tvActor.setText("배우: " + dto.getActor());

        Bitmap savedBitmap = imageFileManager.getBitmapFromTemporary(dto.getImageLink());

        if(savedBitmap != null) {
            viewHolder.ivImage.setImageBitmap(savedBitmap);
            Log.d(TAG, "Image loading from file");
        } else {
            viewHolder.ivImage.setImageResource(R.mipmap.ic_launcher);
            new GetImageAsyncTask(viewHolder).execute(dto.getImageLink());
            Log.d(TAG, "Image loading from network");
        }
        return view;
    }

    public void setList(ArrayList<MovieDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        public TextView tvTitle = null;
        public TextView tvDirector = null;
        public TextView tvActor = null;
        public ImageView ivImage = null;
    }

    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        ViewHolder viewHolder;
        String imageAddress;

        public GetImageAsyncTask(ViewHolder holder) {
            viewHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            imageAddress = strings[0];
            Bitmap result = null;

            result = networkManager.downloadImage(imageAddress);

            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null) {
                viewHolder.ivImage.setImageBitmap(bitmap);
                imageFileManager.saveBitmapToTemporary(bitmap, imageAddress);
            }
        }
    }
}
