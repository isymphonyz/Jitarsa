package th.or.dga.royaljitarsa.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.github.chrisbanes.photoview.PhotoView;
import com.jsibbold.zoomage.ZoomageView;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.GalleryActivity;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.utils.AppPreference;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

/**
 * Created by Dooplus on 11/26/15 AD.
 */

public class ImageSlidePagerStringAdapter extends PagerAdapter {

    private String TAG = ImageSlidePagerStringAdapter.class.getSimpleName();

    private ImageView imageView;
    private ZoomageView myZoomageView;
    private PhotoView photoView;
    private ArrayList<String> imageList;
    private LayoutInflater inflater;
    private Context context;
    private boolean zoomImageEnable = false;

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    public ImageSlidePagerStringAdapter(Context context, ArrayList<String> imageList) {
        this.context = context;
        this.imageList = imageList;
        inflater = LayoutInflater.from(context);
    }

    public ImageSlidePagerStringAdapter(Context context, ArrayList<String> imageList, boolean zoomImageEnable) {
        this.context = context;
        this.imageList = imageList;
        this.zoomImageEnable = zoomImageEnable;
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

        int width = AppPreference.getInstance(context).getScreenWidth();
        int height = (AppPreference.getInstance(context).getScreenWidth()/16)*9;

        assert imageLayout != null;

        myZoomageView = (ZoomageView) imageLayout.findViewById(R.id.myZoomageView);
        photoView = (PhotoView) imageLayout.findViewById(R.id.photoView);

        imageView = (ImageView) imageLayout.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "imageView.setOnClickListener: ");
                Intent intent = new Intent(context, GalleryActivity.class);
                intent.putStringArrayListExtra("imageList", imageList);
                context.startActivity(intent);
            }
        });

        /*FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        params.width = 320;
        params.height = 180;
        imageView.setLayoutParams(params);*/

        Glide.with(context)
                .load(imageList.get(position))
                .apply(fitCenterTransform()
                        //.placeholder(R.mipmap.ic_launcher)
                        //.error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH))
                .into(imageView);

        Glide.with(context)
                .load(imageList.get(position))
                .apply(fitCenterTransform()
                        //.placeholder(R.mipmap.ic_launcher)
                        //.error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH))
                .into(myZoomageView);

        Glide.with(context)
                .load(imageList.get(position))
                .apply(fitCenterTransform()
                        //.placeholder(R.mipmap.ic_launcher)
                        //.error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH))
                .into(photoView);

        if(zoomImageEnable) {
            imageView.setVisibility(View.GONE);
            myZoomageView.setVisibility(View.GONE);
            photoView.setVisibility(View.VISIBLE);
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

}
