package th.or.dga.royaljitarsa.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;
import th.or.dga.royaljitarsa.GalleryActivity;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.connection.LikeProjectAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.fragment.FragmentActivity;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.Utils;

/**
 * Created by Dooplus on 7/16/16 AD.
 */
public class FilterProvinceListAdapter extends BaseAdapter implements Filterable {

    private String TAG = FilterProvinceListAdapter.this.getClass().getSimpleName();

    private Activity activity;
    private static LayoutInflater inflater=null;
    //ImageLoader imageLoader;
    Typeface tf;

    private FragmentActivity fragment;

    private ArrayList<String> provinceIDList = null;
    private ArrayList<String> provinceNameList = null;

    private ArrayList<String> tempProvinceIDList = null;
    private ArrayList<String> tempProvinceNameList = null;

    public FilterProvinceListAdapter(Activity a) {
        activity = a;
        //imageLoader = new ImageLoader(activity);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //tf = Typeface.createFromAsset(activity.getAssets(), "fonts/rsu-light.ttf");
    }

    public void setFragment(FragmentActivity fragment) {
        this.fragment = fragment;
    }

    public void setProvinceIDList(ArrayList<String> provinceIDList) {
        this.provinceIDList = provinceIDList;
        this.tempProvinceIDList = new ArrayList<>(provinceIDList);
    }

    public void setProvinceNameList(ArrayList<String> provinceNameList) {
        this.provinceNameList = provinceNameList;
        this.tempProvinceNameList = new ArrayList<>(provinceNameList);
    }

    public int getCount() {
        //return data.length;
        //return nameList.size();
        return tempProvinceNameList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        public RelativeLayout layout;
        public SukhumvitTextView txtProvinceName;

        public int margin = 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        final ViewHolder holder = new ViewHolder();;

        vi = inflater.inflate(R.layout.filter_province_list_item, null);
        //holder = new ViewHolder();
        holder.layout = (RelativeLayout) vi.findViewById(R.id.layout);
        holder.txtProvinceName = (SukhumvitTextView) vi.findViewById(R.id.txtProvinceName);
        holder.txtProvinceName.setText(provinceNameList.get(position));

        return vi;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                Log.d(TAG, "publishResults constraint: " + constraint);

                tempProvinceNameList = (ArrayList<String>) results.values;
                tempProvinceIDList.clear();
                if(tempProvinceNameList.size() == 0) {
                    //tempProvinceNameList = new ArrayList<>(nameList);
                } else {
                    for(int x=0; x<provinceNameList.size(); x++) {
                        for(int y=0; y<tempProvinceNameList.size(); y++) {
                            if(provinceNameList.get(x).equals(tempProvinceNameList.get(y))) {
                                tempProvinceIDList.add(provinceIDList.get(x));
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                Log.d(TAG, "performFiltering constraint: " + constraint);
                Log.d(TAG, "tempProvinceNameList.size():  " + tempProvinceNameList.size());
                FilterResults results = new FilterResults();
                ArrayList<String> FilteredArrayNames = new ArrayList<String>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < tempProvinceNameList.size(); i++) {
                    String dataNames = tempProvinceNameList.get(i);
                    Log.d(TAG, "dataNames.toLowerCase(): " + dataNames.toLowerCase());
                    Log.d(TAG, "constraint.toString(): " + constraint.toString());
                    if (dataNames.toLowerCase().contains(constraint.toString()))  {
                        FilteredArrayNames.add(dataNames);
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e(TAG, "VALUES: " + results.values.toString());

                return results;
            }
        };

        return filter;
    }
}
