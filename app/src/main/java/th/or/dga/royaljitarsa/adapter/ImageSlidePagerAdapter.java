package th.or.dga.royaljitarsa.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.R;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

/**
 * Created by Dooplus on 11/26/15 AD.
 */

public class ImageSlidePagerAdapter extends PagerAdapter {

    private String TAG = ImageSlidePagerAdapter.class.getSimpleName();

    private ArrayList<Integer> imageList;
    private LayoutInflater inflater;
    private Context context;

    public ImageSlidePagerAdapter(Context context, ArrayList<Integer> imageList) {
        this.context = context;
        this.imageList = imageList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.image_sliding, view, false);

        assert imageLayout != null;
        RelativeLayout layout = (RelativeLayout) imageLayout.findViewById(R.id.layout);
        try {
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageView);
            Glide.with(context)
                    .load(imageList.get(position))
                    .apply(fitCenterTransform()
                            //.placeholder(R.mipmap.ic_launcher)
                            //.error(R.mipmap.ic_launcher)
                            .priority(Priority.HIGH))
                    .into(imageView);
            imageView.setVisibility(View.VISIBLE);
        } catch(Exception e) {
            Log.d(TAG, "Exception: " + e.toString());
            ImageView mImageView = new ImageView(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            mImageView.setLayoutParams(params);
            mImageView.setAdjustViewBounds(true);
            mImageView.setImageResource(imageList.get(position));
            layout.addView(mImageView);
        }

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
