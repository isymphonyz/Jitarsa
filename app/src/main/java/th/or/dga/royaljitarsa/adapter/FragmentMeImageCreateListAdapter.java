package th.or.dga.royaljitarsa.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.Utils;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

/**
 * Created by Dooplus on 7/16/16 AD.
 */
public class FragmentMeImageCreateListAdapter extends BaseAdapter {
    private String TAG = getClass().getSimpleName();

    private Activity activity;
    private static LayoutInflater inflater=null;
    //ImageLoader imageLoader;
    Typeface tf;

    private ArrayList<String> imageList = null;
    private ArrayList<String> nameList = null;
    private ArrayList<Boolean> selectStatusList = null;

    private Utils utils;

    //public LazyAdapter(Activity a, String[] d) {
    public FragmentMeImageCreateListAdapter(Activity a) {
        activity = a;
        //imageLoader = new ImageLoader(activity);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //tf = Typeface.createFromAsset(activity.getAssets(), "fonts/rsu-light.ttf");

        utils = new Utils(activity);
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public void setSelectStatusList(ArrayList<Boolean> selectStatusList) {
        this.selectStatusList = selectStatusList;
    }

    public void setNameList(ArrayList<String> nameList) {
        this.nameList = nameList;
    }

    public int getCount() {
        //return data.length;
        return nameList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        public RelativeLayout layout;
        public ImageView img;
        public LinearLayout layoutSelectImage;
        public SukhumvitTextView txtNumber;

        public int margin = 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        final ViewHolder holder;

        vi = inflater.inflate(R.layout.fragment_me_image_create_list_item, null);
        holder=new ViewHolder();
        holder.img = (ImageView) vi.findViewById(R.id.img);
        holder.layoutSelectImage = (LinearLayout) vi.findViewById(R.id.layoutSelectImage);

        if(selectStatusList.get(position)) {
            holder.layoutSelectImage.setVisibility(View.VISIBLE);
        } else {
            holder.layoutSelectImage.setVisibility(View.INVISIBLE);
        }

        Glide.with(activity)
                .load(imageList.get(position))
                .apply(fitCenterTransform()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .priority(Priority.HIGH)
                        .override(256,256))
                .into(holder.img);

        return vi;
    }
}
