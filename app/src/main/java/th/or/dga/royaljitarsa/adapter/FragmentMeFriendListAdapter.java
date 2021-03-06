package th.or.dga.royaljitarsa.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.Utils;

/**
 * Created by Dooplus on 7/16/16 AD.
 */
public class FragmentMeFriendListAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater=null;
    //ImageLoader imageLoader;
    Typeface tf;

    private ArrayList<String> imageList = null;
    private ArrayList<String> nameList = null;
    private ArrayList<String> ageList = null;

    private Utils utils;

    //public LazyAdapter(Activity a, String[] d) {
    public FragmentMeFriendListAdapter(Activity a) {
        activity = a;
        //imageLoader = new ImageLoader(activity);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //tf = Typeface.createFromAsset(activity.getAssets(), "fonts/rsu-light.ttf");

        utils = new Utils(activity);
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public void setNameList(ArrayList<String> nameList) {
        this.nameList = nameList;
    }

    public void setAgeList(ArrayList<String> ageList) {
        this.ageList = ageList;
    }

    public int getCount() {
        //return data.length;
        return ageList.size();
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
        public SukhumvitTextView txtName;
        public SukhumvitTextView txtAge;

        public int margin = 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;

        vi = inflater.inflate(R.layout.fragment_me_friend_list_item, null);
        holder=new ViewHolder();
        holder.img = (ImageView) vi.findViewById(R.id.img);
        holder.txtName = (SukhumvitTextView) vi.findViewById(R.id.txtName);
        holder.txtAge = (SukhumvitTextView) vi.findViewById(R.id.txtAge);

        holder.txtName.setText(nameList.get(position));
        holder.txtAge.setText(ageList.get(position));

        return vi;
    }
}
