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
import java.util.Collection;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;
import th.or.dga.royaljitarsa.GalleryActivity;
import th.or.dga.royaljitarsa.Home;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.connection.LikeProjectAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.fragment.FragmentActivity;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.Utils;

/**
 * Created by Dooplus on 7/16/16 AD.
 */
public class FragmentActivityListAdapter extends BaseAdapter implements Filterable {

    private String TAG = FragmentActivityListAdapter.this.getClass().getSimpleName();

    private Activity activity;
    private static LayoutInflater inflater=null;
    //ImageLoader imageLoader;
    Typeface tf;

    private FragmentActivity fragment;

    private ArrayList<String> categoryIDList = null;
    private ArrayList<String> idList = null;
    private ArrayList<String> imageList = null;
    private ArrayList<String> nameList = null;
    private ArrayList<String> dateList = null;
    private ArrayList<String> provinceList = null;
    private ArrayList<String> placeList = null;
    private ArrayList<String> likeList = null;
    private ArrayList<String> descriptionList = null;
    private ArrayList<String> scheduleDateList = null;
    private ArrayList<String> calendarDateList = null;

    private ArrayList<String> tempCategoryIDList = null;
    private ArrayList<String> tempIDList = null;
    private ArrayList<String> tempImageList = null;
    private ArrayList<String> tempNameList = null;
    private ArrayList<String> tempDateList = null;
    private ArrayList<String> tempDescriptionList = null;
    private ArrayList<String> tempPlaceList = null;
    private ArrayList<String> tempProvinceList = null;
    private ArrayList<String> tempLikeList = null;
    private ArrayList<String> tempScheduleDateList = null;
    private ArrayList<String> tempCalendarDateList = null;

    private Utils utils;

    private HashMap<String, ArrayList<String>> imageMap;
    private HashMap<String, ArrayList<String>> imageCoverMap;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    //public LazyAdapter(Activity a, String[] d) {
    public FragmentActivityListAdapter(Activity a) {
        activity = a;
        //imageLoader = new ImageLoader(activity);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //tf = Typeface.createFromAsset(activity.getAssets(), "fonts/rsu-light.ttf");

        utils = new Utils(activity);
    }

    public void setFragment(FragmentActivity fragment) {
        this.fragment = fragment;
    }

    public void setImageMap(HashMap<String, ArrayList<String>> imageMap) {
        this.imageMap = imageMap;
    }

    public void setImageCoverMap(HashMap<String, ArrayList<String>> imageCoverMap) {
        this.imageCoverMap = imageCoverMap;
    }

    public void setCategoryIDList(ArrayList<String> categoryIDList) {
        this.categoryIDList = categoryIDList;
        this.tempCategoryIDList = new ArrayList<>(categoryIDList);
    }

    public void setIDList(ArrayList<String> idList) {
        this.idList = idList;
        this.tempIDList = new ArrayList<>(idList);
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
        this.tempImageList = new ArrayList<>(imageList);
    }

    public void setNameList(ArrayList<String> nameList) {
        this.nameList = nameList;
        this.tempNameList = new ArrayList<>(nameList);
        Log.d(TAG, "setNameList nameList.size(): " + nameList.size());
        Log.d(TAG, "setNameList tempNameList.size(): " + tempNameList.size());
    }

    public void setDateList(ArrayList<String> dateList) {
        this.dateList = dateList;
        this.tempDateList = new ArrayList<>(dateList);
        Log.d(TAG, "setNameList dateList.size(): " + dateList.size());
        Log.d(TAG, "setNameList tempDateList.size(): " + tempDateList.size());
    }

    public void setDescriptionList(ArrayList<String> descriptionList) {
        this.descriptionList = descriptionList;
        this.tempDescriptionList = new ArrayList<>(descriptionList);
    }

    public void setLikeList(ArrayList<String> likeList) {
        this.likeList = likeList;
        this.tempLikeList = new ArrayList<>(likeList);
    }

    public void setProvinceList(ArrayList<String> provinceList) {
        this.provinceList = provinceList;
        this.tempProvinceList = new ArrayList<>(provinceList);
    }

    public void setPlaceList(ArrayList<String> placeList) {
        this.placeList = placeList;
        this.tempPlaceList = new ArrayList<>(placeList);
    }

    public void setScheduleDateList(ArrayList<String> scheduleDateList) {
        this.scheduleDateList = scheduleDateList;
        this.tempScheduleDateList = new ArrayList<>(scheduleDateList);
    }

    public void setCalendarDateList(ArrayList<String> calendarDateList) {
        this.calendarDateList = calendarDateList;
        this.tempCalendarDateList = new ArrayList<>(calendarDateList);
    }

    public int getCount() {
        //return data.length;
        //return nameList.size();
        return tempNameList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        public ImageView img;
        public LinearLayout layout;
        public SukhumvitTextView txtName;
        public SukhumvitTextView txtDate;
        public SukhumvitTextView txtProvince;
        public SukhumvitTextView txtPlace;
        public SukhumvitTextView txtLike;
        public SukhumvitTextView btnShare;
        public LinearLayout layoutLike;
        public SukhumvitTextView btnDescription;

        public ViewPager viewPager;
        public CircleIndicator indicator;

        public int margin = 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        final ViewHolder holder = new ViewHolder();;

        vi = inflater.inflate(R.layout.fragment_activity_list_item, null);
        //holder = new ViewHolder();
        holder.layout = (LinearLayout) vi.findViewById(R.id.layout);
        holder.img = (ImageView) vi.findViewById(R.id.img);
        holder.txtName = (SukhumvitTextView) vi.findViewById(R.id.txtName);
        holder.txtDate = (SukhumvitTextView) vi.findViewById(R.id.txtDate);
        holder.txtProvince = (SukhumvitTextView) vi.findViewById(R.id.txtProvince);
        holder.txtPlace = (SukhumvitTextView) vi.findViewById(R.id.txtPlace);
        holder.txtLike = (SukhumvitTextView) vi.findViewById(R.id.txtLike);
        holder.btnShare = (SukhumvitTextView) vi.findViewById(R.id.btnShare);
        holder.layoutLike = (LinearLayout) vi.findViewById(R.id.layoutLike);
        holder.btnDescription = (SukhumvitTextView) vi.findViewById(R.id.btnDescription);

        holder.viewPager = (ViewPager) vi.findViewById(R.id.viewPager);
        holder.indicator = (CircleIndicator) vi.findViewById(R.id.indicator);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.viewPager.getLayoutParams();
        params.width = AppPreference.getInstance(activity).getScreenWidth();
        params.height = (AppPreference.getInstance(activity).getScreenWidth()/16)*9;
        holder.viewPager.setLayoutParams(params);

        //holder.img.setImageResource(R.mipmap.ic_launcher);
        //holder.txtName.setText(nameList.get(position));
        //holder.txtProject.setText(projectList.get(position));
        //holder.txtDate.setText(dateList.get(position));

        /*Glide.with(activity)
                .load(imageList.get(position))
                .apply(fitCenterTransform()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .priority(Priority.HIGH))
                .into(holder.img);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txtName.setText(Html.fromHtml(tempNameList.get(position), Html.FROM_HTML_MODE_LEGACY));
            holder.txtDate.setText(Html.fromHtml(tempDateList.get(position), Html.FROM_HTML_MODE_LEGACY));
            holder.txtProvince.setText(Html.fromHtml(activity.getText(R.string.fragment_activity_txt_province) + tempProvinceList.get(position), Html.FROM_HTML_MODE_LEGACY));
            holder.txtPlace.setText(Html.fromHtml(activity.getText(R.string.fragment_activity_txt_place) + tempPlaceList.get(position), Html.FROM_HTML_MODE_LEGACY));
            holder.txtLike.setText(Html.fromHtml(tempLikeList.get(position), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.txtName.setText(Html.fromHtml(tempNameList.get(position)));
            holder.txtDate.setText(Html.fromHtml(tempDateList.get(position)));
            holder.txtProvince.setText(Html.fromHtml(activity.getText(R.string.fragment_activity_txt_province) + tempProvinceList.get(position)));
            holder.txtPlace.setText(Html.fromHtml(activity.getText(R.string.fragment_activity_txt_place) + tempPlaceList.get(position)));
            holder.txtLike.setText(Html.fromHtml(tempLikeList.get(position)));
        }
        holder.txtDate.setTypeface(Typeface.DEFAULT_BOLD);

        holder.layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikeProjectAPI likeProjectAPI = new LikeProjectAPI();
                likeProjectAPI.setUserID(AppPreference.getInstance(activity).getUserID());
                likeProjectAPI.setCategoryID(tempCategoryIDList.get(position));
                likeProjectAPI.setProjectID(tempIDList.get(position));
                likeProjectAPI.setListener(new LikeProjectAPI.LikeProjectAPIListener() {
                    @Override
                    public void onLikeProjectAPIPreExecuteConcluded() {

                    }

                    @Override
                    public void onLikeProjectAPIPostExecuteConcluded(String result) {
                        try {
                            JSONObject jObj = new JSONObject(result);
                            int status = jObj.optInt("status");
                            String statusDetail = jObj.optString("status_detail");

                            Log.d(TAG, "statusDetail: " + statusDetail);
                            if(status == 200) {
                                holder.txtLike.setText("" + Integer.parseInt(holder.txtLike.getText().toString().replace(",",""))+1);
                            } else {
                                Toast.makeText(activity, statusDetail, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                likeProjectAPI.execute("");
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "layout.setOnClickListener");
                fragment.goToDetail(position, tempNameList.get(position));
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getText(R.string.app_name));
                sendIntent.putExtra(Intent.EXTRA_TEXT, tempNameList.get(position));
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                sendIntent.setType("text/plain");
                activity.startActivity(sendIntent);
                //activity.startActivity(Intent.createChooser(sendIntent, "Share link!"));

            }
        });

        holder.btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnDescription.setOnClickListener");
                fragment.goToDetail(position, tempNameList.get(position));
            }
        });

        holder.viewPager.setAdapter(new ImageSlidePagerStringAdapter(activity, imageCoverMap.get(tempIDList.get(position))));
        holder.indicator.setViewPager(holder.viewPager);
        holder.viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, GalleryActivity.class);
                intent.putStringArrayListExtra("imageList", imageCoverMap.get(tempIDList.get(position)));
                activity.startActivity(intent);
            }
        });

        holder.indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

        holder.txtName.setTypeface(Typeface.BOLD);

        return vi;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                Log.d(TAG, "publishResults constraint: " + constraint);

                tempNameList = (ArrayList<String>) results.values;
                tempCategoryIDList.clear();
                tempIDList.clear();
                //tempNameList.clear();
                tempDateList.clear();
                tempDescriptionList.clear();
                tempLikeList.clear();
                tempProvinceList.clear();
                tempPlaceList.clear();
                tempScheduleDateList.clear();
                tempCalendarDateList.clear();
                if(tempNameList.size() == 0) {
                    //tempNameList = new ArrayList<>(nameList);
                } else {
                    for(int x=0; x<nameList.size(); x++) {
                        for(int y=0; y<tempNameList.size(); y++) {
                            if(nameList.get(x).equals(tempNameList.get(y))) {
                                tempCategoryIDList.add(categoryIDList.get(x));
                                tempIDList.add(idList.get(x));
                                tempDateList.add(dateList.get(x));
                                tempDescriptionList.add(descriptionList.get(x));
                                tempLikeList.add(likeList.get(x));
                                tempProvinceList.add(provinceList.get(x));
                                tempPlaceList.add(placeList.get(x));
                                tempScheduleDateList.add(scheduleDateList.get(x));
                                tempCalendarDateList.add(calendarDateList.get(x));
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                Log.d(TAG, "performFiltering constraint: " + constraint);
                Log.d(TAG, "tempNameList.size():  " + tempNameList.size());
                FilterResults results = new FilterResults();
                ArrayList<String> FilteredArrayNames = new ArrayList<String>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase().replace("   ", "").replace("  ", "");
                for (int i = 0; i < tempNameList.size(); i++) {
                    String dataNames = tempNameList.get(i);
                    String dataContent = tempDescriptionList.get(i);
                    String dataDate = tempDateList.get(i);
                    String dataScheduleDate = tempScheduleDateList.get(i);
                    String dataProvince = tempProvinceList.get(i);
                    String dataCalendar = tempCalendarDateList.get(i);
                    Log.d(TAG, "constraint.toString():  " + constraint.toString());
                    Log.d(TAG, "dataProvince:  " + dataProvince.toLowerCase());
                    if (dataNames.toLowerCase().contains(constraint.toString()) || dataContent.toLowerCase().contains(constraint.toString())
                            || dataDate.toLowerCase().contains(constraint.toString()) || dataScheduleDate.toLowerCase().contains(constraint.toString())
                            || dataProvince.toLowerCase().contains(constraint.toString()) || dataCalendar.toLowerCase().contains(constraint.toString()))  {
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
