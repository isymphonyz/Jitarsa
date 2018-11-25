package th.or.dga.royaljitarsa.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.utils.AppPreference;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

/**
 * Created by Dooplus on 11/26/15 AD.
 */

public class ImageSlidePagerStringAdapter extends PagerAdapter {

    private ArrayList<String> imageList;
    private LayoutInflater inflater;
    private Context context;

    public ImageSlidePagerStringAdapter(Context context, ArrayList<String> imageList) {
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

        int width = AppPreference.getInstance(context).getScreenWidth();
        int height = (AppPreference.getInstance(context).getScreenWidth()/16)*9;

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageView);

        /*FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        params.width = 320;
        params.height = 180;
        imageView.setLayoutParams(params);*/

        Glide.with(context)
                .load(imageList.get(position))
                .apply(fitCenterTransform()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .priority(Priority.HIGH))
                .into(imageView);

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
