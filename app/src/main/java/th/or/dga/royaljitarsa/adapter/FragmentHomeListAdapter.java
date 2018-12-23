package th.or.dga.royaljitarsa.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;
import ss.com.bannerslider.Slider;
import th.or.dga.royaljitarsa.GalleryActivity;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.connection.LikeProjectAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.fragment.FragmentHome;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.Utils;

/**
 * Created by Dooplus on 7/16/16 AD.
 */
public class FragmentHomeListAdapter extends BaseAdapter {
    private String TAG = FragmentHomeListAdapter.this.getClass().getSimpleName();

    private Activity activity;
    private static LayoutInflater inflater=null;
    //ImageLoader imageLoader;
    Typeface tf;

    private FragmentHome fragment;

    private ArrayList<String> categoryIDList = null;
    private ArrayList<String> idList = null;
    //private ArrayList<String> imageList = null;
    private ArrayList<String> nameList = null;
    private ArrayList<String> dateList = null;
    private ArrayList<String> descriptionList = null;
    private ArrayList<String> likeList = null;

    private Utils utils;

    private HashMap<String, ArrayList<String>> imageMap;
    private HashMap<String, ArrayList<String>> imageCoverMap;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    //public LazyAdapter(Activity a, String[] d) {
    public FragmentHomeListAdapter(Activity a) {
        Log.d(TAG, "FragmentHomeListAdapter Create");
        activity = a;
        //imageLoader = new ImageLoader(activity);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //tf = Typeface.createFromAsset(activity.getAssets(), "fonts/rsu-light.ttf");

        utils = new Utils(activity);

        //Slider.init(new PicassoImageLoadingService(activity));
    }

    public void setFragment(FragmentHome fragment) {
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
    }

    public void setIDList(ArrayList<String> idList) {
        this.idList = idList;
    }

    /*public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }*/

    public void setNameList(ArrayList<String> nameList) {
        this.nameList = nameList;
    }

    public void setDateList(ArrayList<String> dateList) {
        this.dateList = dateList;
    }

    public void setDescriptionList(ArrayList<String> descriptionList) {
        this.descriptionList = descriptionList;
    }

    public void setLikeList(ArrayList<String> likeList) {
        this.likeList = likeList;
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
        public ImageView img;
        public LinearLayout layout;
        public SukhumvitTextView txtName;
        public SukhumvitTextView txtPostDate;
        public SukhumvitTextView txtDescription;
        public SukhumvitTextView txtLike;
        public SukhumvitTextView btnShare;
        public ImageView btnSave;
        public LinearLayout layoutLike;
        public SukhumvitTextView btnDescription;

        public Slider slider;
        public ViewPager viewPager;
        public CircleIndicator indicator;

        public int margin = 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        final ViewHolder holder = new ViewHolder();;

        vi = inflater.inflate(R.layout.fragment_home_list_item, null);
        //holder = new ViewHolder();
        holder.layout = (LinearLayout) vi.findViewById(R.id.layout);
        holder.img = (ImageView) vi.findViewById(R.id.img);
        holder.txtName = (SukhumvitTextView) vi.findViewById(R.id.txtName);
        holder.txtPostDate = (SukhumvitTextView) vi.findViewById(R.id.txtPostDate);
        holder.txtDescription = (SukhumvitTextView) vi.findViewById(R.id.txtDescription);
        holder.txtLike = (SukhumvitTextView) vi.findViewById(R.id.txtLike);
        holder.btnShare = (SukhumvitTextView) vi.findViewById(R.id.btnShare);
        holder.btnSave = (ImageView) vi.findViewById(R.id.btnSave);
        holder.layoutLike = (LinearLayout) vi.findViewById(R.id.layoutLike);
        holder.btnDescription = (SukhumvitTextView) vi.findViewById(R.id.btnDescription);

        //holder.slider = (Slider) vi.findViewById(R.id.slider);
        //holder.slider.setAdapter(new MainSliderAdapter());
        holder.viewPager = (ViewPager) vi.findViewById(R.id.viewPager);
        holder.indicator = (CircleIndicator) vi.findViewById(R.id.indicator);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.viewPager.getLayoutParams();
        params.width = AppPreference.getInstance(activity).getScreenWidth();
        params.height = (AppPreference.getInstance(activity).getScreenWidth()/16)*9;
        holder.viewPager.setLayoutParams(params);

        //holder.img.setImageResource(R.mipmap.ic_launcher);
        //holder.txtName.setText(nameList.get(position));
        //holder.txtProject.setText(projectList.get(position));
        //holder.txtPostDate.setText(dateList.get(position));

        /*Glide.with(activity)
                .load(imageList.get(position))
                .apply(fitCenterTransform()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .priority(Priority.HIGH))
                .into(holder.img);*/

        //holder.txtDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        holder.txtDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, activity.getResources().getDimension(R.dimen.short_description_text_size));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txtName.setText(Html.fromHtml(nameList.get(position), Html.FROM_HTML_MODE_LEGACY));
            holder.txtPostDate.setText(Html.fromHtml(dateList.get(position), Html.FROM_HTML_MODE_LEGACY));
            holder.txtDescription.setText(Html.fromHtml(descriptionList.get(position), Html.FROM_HTML_MODE_LEGACY));
            holder.txtLike.setText(Html.fromHtml(likeList.get(position), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.txtName.setText(Html.fromHtml(nameList.get(position)));
            holder.txtPostDate.setText(Html.fromHtml(dateList.get(position)));
            holder.txtDescription.setText(Html.fromHtml(descriptionList.get(position)));
            holder.txtLike.setText(Html.fromHtml(likeList.get(position)));
        }

        holder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getText(R.string.app_name));
                sendIntent.putExtra(Intent.EXTRA_TEXT, nameList.get(position));
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                sendIntent.setType("text/plain");
                activity.startActivity(sendIntent);
            }
        });

        holder.layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikeProjectAPI likeProjectAPI = new LikeProjectAPI();
                likeProjectAPI.setUserID(AppPreference.getInstance(activity).getUserID());
                likeProjectAPI.setCategoryID(categoryIDList.get(position));
                likeProjectAPI.setProjectID(idList.get(position));
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
                                holder.txtLike.setText("" + (Integer.parseInt(holder.txtLike.getText().toString().replace(",",""))+1));
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
                fragment.goToDetail(position);
            }
        });

        holder.txtDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "layout.setOnClickListener");
                fragment.goToDetail(position);
            }
        });

        holder.btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnDescription.setOnClickListener");
                fragment.goToDetail(position);
            }
        });

        holder.viewPager.setAdapter(new ImageSlidePagerStringAdapter(activity, imageCoverMap.get(idList.get(position))));
        holder.indicator.setViewPager(holder.viewPager);
        holder.viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "viewPager.setOnClickListener: " + position);
                Intent intent = new Intent(activity, GalleryActivity.class);
                intent.putStringArrayListExtra("imageList", imageCoverMap.get(idList.get(position)));
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
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0) {
        return true;
    }
}
