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
public class FragmentSearchListAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater=null;
    //ImageLoader imageLoader;
    Typeface tf;

    private ArrayList<String> projectList = null;
    private ArrayList<String> dateList = null;
    private ArrayList<String> imageList = null;

    private Utils utils;

    //public LazyAdapter(Activity a, String[] d) {
    public FragmentSearchListAdapter(Activity a) {
        activity = a;
        //imageLoader = new ImageLoader(activity);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //tf = Typeface.createFromAsset(activity.getAssets(), "fonts/rsu-light.ttf");

        utils = new Utils(activity);
    }

    public void setProjectList(ArrayList<String> projectList) {
        this.projectList = projectList;
    }

    public void setDateList(ArrayList<String> dateList) {
        this.dateList = dateList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public int getCount() {
        //return data.length;
        return dateList.size();
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
        public SukhumvitTextView txtProject;
        public SukhumvitTextView txtDate;

        public int margin = 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;

        vi = inflater.inflate(R.layout.fragment_search_list_item, null);
        holder=new ViewHolder();
        holder.img = (ImageView) vi.findViewById(R.id.img);
        holder.txtProject = (SukhumvitTextView) vi.findViewById(R.id.txtProject);
        holder.txtDate = (SukhumvitTextView) vi.findViewById(R.id.txtDate);

        //holder.img.setImageResource(R.mipmap.ic_launcher);
        holder.txtProject.setText(projectList.get(position));
        holder.txtDate.setText(dateList.get(position));

        return vi;
    }
}
