package ddwucom.mobile.finalproject.ma01_20181033;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {
    LayoutInflater inflater;
    int layout;
    ImageFileManager imageFileManager;

    public MyCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        imageFileManager = new ImageFileManager(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.tvMovieTitle == null) {
            holder.ivMoviePoster = view.findViewById(R.id.ivPoster);
            holder.tvMovieTitle = view.findViewById(R.id.tvMovieTitle);
            holder.tvReviewDate = view.findViewById(R.id.tvReviewDate);
            holder.tvReviewTogether = view.findViewById(R.id.tvReviewTogether);
        }

        Bitmap savedBitmap = imageFileManager.getBitmapFromTemporary(cursor.getString(cursor.getColumnIndex(ReviewDBHelper.COL_IMAGELINK)));

        if(savedBitmap != null) {
            holder.ivMoviePoster.setImageBitmap(savedBitmap);
        } else {
            holder.ivMoviePoster.setImageResource(R.mipmap.ic_launcher);
        }

        holder.tvMovieTitle.setText(cursor.getString(cursor.getColumnIndex(ReviewDBHelper.COL_TITLE)));
        holder.tvReviewDate.setText(cursor.getString(cursor.getColumnIndex(ReviewDBHelper.COL_DATE)));
        holder.tvReviewTogether.setText(cursor.getString(cursor.getColumnIndex(ReviewDBHelper.COL_TOGETHER)));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder();
        view.setTag(holder);
        return view;
    }

    static class ViewHolder {

        public ViewHolder() {
            ivMoviePoster = null;
            tvMovieTitle = null;
            tvReviewDate = null;
            tvReviewTogether = null;
        }

        ImageView ivMoviePoster;
        TextView tvMovieTitle;
        TextView tvReviewDate;
        TextView tvReviewTogether;
    }
}
