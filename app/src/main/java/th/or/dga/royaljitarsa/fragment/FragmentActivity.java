package th.or.dga.royaljitarsa.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.adapter.FilterProvinceListAdapter;
import th.or.dga.royaljitarsa.adapter.FragmentActivityListAdapter;
import th.or.dga.royaljitarsa.connection.ProjectAPI;
import th.or.dga.royaljitarsa.connection.ProvinceAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;
import th.or.dga.royaljitarsa.utils.MyConfiguration;

public class FragmentActivity extends Fragment {

    private String TAG = FragmentActivity.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private LinearLayout layoutFilter;
    private SukhumvitEditText inputSearch;
    private ListView listView;
    private FragmentActivityListAdapter adapter;

    private LinearLayout layoutFilterDetail;
    private LinearLayout layoutFilterMenu;
    private SukhumvitTextView btnCalendar;
    private SukhumvitTextView btnLocation;
    private SukhumvitTextView btnProvince;

    private LinearLayout layoutFilterDetailCalendar;
    private CompactCalendarView compactCalendarView;

    private LinearLayout layoutFilterDetailLocation;
    private SukhumvitEditText inputFilterMapSearch;
    private MapView locationMapView;
    private GoogleMap googleMap;

    private LinearLayout layoutFilterDetailProvince;
    private SukhumvitEditText inputFilterProvinceSearch;
    private ListView provinceListView;
    private FilterProvinceListAdapter adapterProvinceList;
    private SukhumvitTextView txtCalendarDate;
    private ImageView btnPreviousMonth;
    private ImageView btnNextMonth;

    private ArrayList<String> provinceIDList;
    private ArrayList<String> provinceNameList;

    private ArrayList<String> categoryIDList;
    private ArrayList<String> idList;
    //private ArrayList<String> imageCoverList;
    private ArrayList<String> nameList;
    private ArrayList<String> dateList;
    private ArrayList<String> provinceList;
    private ArrayList<String> placeList;
    private ArrayList<String> likeList;
    private ArrayList<String> shortDescriptionList;
    private ArrayList<String> scheduleDateList;
    private ArrayList<String> calendarDateList;
    private ArrayList<String> latitudeList;
    private ArrayList<String> longitudeList;

    private HashMap<String, ArrayList<String>> imageCoverMap;
    private HashMap<String, ArrayList<String>> imageMap;
    private HashMap<String, ArrayList<String>> descriptionMap;
    private HashMap<String, ArrayList<String>> youtubeMap;
    private HashMap<String, ArrayList<String>> typeMap;
    private HashMap<String, ArrayList<Integer>> typeIDMap;

    private ProvinceAPI provinceAPI;
    private ProjectAPI projectAPI;
    private String categoryID = MyConfiguration.CATEGORY_ACTIVITY_ID;

    private int textSize8sp = 0;
    private String[] arrMonthEN;
    private String[] arrMonthTH;
    private ArrayList<String> monthENList;
    private ArrayList<String> monthTHList;

    private ArrayList<Marker> markerArray;

    public static FragmentActivity newInstance() {
        FragmentActivity fragment = new FragmentActivity();
        return fragment;
    }

    private View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_activity, container, false);

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception: " + e.toString());
        }

        GoogleApiAvailability googleApiAvailability= GoogleApiAvailability.getInstance();

        int status = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
        Log.d(TAG, "ConnectionResult.SUCCESS): " + ConnectionResult.SUCCESS);
        Log.d(TAG, "status: " + status);

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_activity, container, false);

            locationMapView = (MapView) rootView.findViewById(R.id.mapView);
            locationMapView.onCreate(savedInstanceState);

            locationMapView.onResume();// needed to get the map to display immediately

            addLog();
            initValue();
            initUI(rootView);
            setUI();
            setListener();

            callProjectAPI();
        }

        return rootView;
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_activity).toString());
    }

    private void initValue() {
        Resources r = getActivity().getResources();
        textSize8sp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                4,
                r.getDisplayMetrics()
        );

        provinceIDList = new ArrayList<>();
        provinceNameList = new ArrayList<>();

        categoryIDList = new ArrayList<>();
        idList = new ArrayList<>();
        //imageCoverList = new ArrayList<>();
        nameList = new ArrayList<>();
        dateList = new ArrayList<>();
        provinceList = new ArrayList<>();
        placeList = new ArrayList<>();
        likeList = new ArrayList<>();
        shortDescriptionList = new ArrayList<>();
        scheduleDateList = new ArrayList<>();
        calendarDateList = new ArrayList<>();
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();

        imageCoverMap = new HashMap<>();
        imageMap = new HashMap<>();
        descriptionMap = new HashMap<>();
        youtubeMap = new HashMap<>();
        typeIDMap = new HashMap<>();
        typeMap = new HashMap<>();

        arrMonthEN = getResources().getStringArray(R.array.month_en);
        arrMonthTH = getResources().getStringArray(R.array.month_th);

        monthENList = new ArrayList<>(Arrays.asList(arrMonthEN));
        monthTHList = new ArrayList<>(Arrays.asList(arrMonthTH));

        markerArray = new ArrayList<Marker>();

        callProvinceAPI();
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        layoutFilter = (LinearLayout) rootView.findViewById(R.id.layoutFilter);
        inputSearch = (SukhumvitEditText) rootView.findViewById(R.id.inputSearch);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new FragmentActivityListAdapter(getActivity());

        layoutFilterMenu = (LinearLayout) rootView.findViewById(R.id.layoutFilterMenu);
        btnCalendar = (SukhumvitTextView) rootView.findViewById(R.id.btnCalendar);
        btnLocation = (SukhumvitTextView) rootView.findViewById(R.id.btnLocation);
        btnProvince = (SukhumvitTextView) rootView.findViewById(R.id.btnProvince);
        layoutFilterDetail = (LinearLayout) rootView.findViewById(R.id.layoutFilterDetail);
        layoutFilterDetailCalendar = (LinearLayout) rootView.findViewById(R.id.layoutFilterDetailCalendar);
        compactCalendarView = (CompactCalendarView) rootView.findViewById(R.id.calendarView);
        layoutFilterDetailLocation = (LinearLayout) rootView.findViewById(R.id.layoutFilterDetailLocation);
        inputFilterMapSearch = (SukhumvitEditText) rootView.findViewById(R.id.inputFilterMapSearch);
        layoutFilterDetailProvince = (LinearLayout) rootView.findViewById(R.id.layoutFilterDetailProvince);
        inputFilterProvinceSearch = (SukhumvitEditText) rootView.findViewById(R.id.inputFilterProvinceSearch);
        provinceListView = (ListView) rootView.findViewById(R.id.provinceListView);
        adapterProvinceList = new FilterProvinceListAdapter(getActivity());
        txtCalendarDate = (SukhumvitTextView) rootView.findViewById(R.id.txtCalendarDate);
        btnPreviousMonth = (ImageView) rootView.findViewById(R.id.btnPreviousMonth);
        btnNextMonth = (ImageView) rootView.findViewById(R.id.btnNextMonth);

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception: " + e.toString());
        }

        locationMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                MapsInitializer.initialize(getActivity().getApplicationContext());

                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                /*
                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                */
            }
        });
    }

    private void setUI() {
        adapter.setFragment(this);
        adapter.setCategoryIDList(categoryIDList);
        adapter.setIDList(idList);
        adapter.setImageMap(imageMap);
        adapter.setImageCoverMap(imageCoverMap);
        //adapter.setImageList(imageCoverList);
        adapter.setNameList(nameList);
        adapter.setDateList(dateList);
        adapter.setDescriptionList(shortDescriptionList);
        adapter.setProvinceList(provinceList);
        adapter.setPlaceList(placeList);
        adapter.setLikeList(likeList);
        adapter.setScheduleDateList(scheduleDateList);
        adapter.setCalendarDateList(calendarDateList);

        listView.setAdapter(adapter);

        String[] dayColumnNames = {"อา", "จ", "อ", "พ", "พฤ", "ศ", "ส"};
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        setTextCalendarDate(compactCalendarView.getFirstDayOfCurrentMonth());
        compactCalendarView.setDayColumnNames(dayColumnNames);
    }

    private void setListener() {
        inputSearch.addTextChangedListener(myTextWatcher);
        inputFilterProvinceSearch.addTextChangedListener(provinceTextWatcher);
        inputFilterMapSearch.addTextChangedListener(mapTextWatcher);

        layoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutFilterDetail.setVisibility(View.VISIBLE);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int x, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("title", getText(R.string.fragment_activity_detail_txt_title).toString());
                bundle.putString("name", nameList.get(i));
                bundle.putString("date", dateList.get(i));
                bundle.putString("like", likeList.get(i));
                bundle.putStringArrayList("imageList", imageMap.get(idList.get(i)));
                bundle.putStringArrayList("youtubeList", youtubeMap.get(idList.get(i)));
                bundle.putStringArrayList("descriptionList", descriptionMap.get(idList.get(i)));
                bundle.putIntegerArrayList("typeIDList", typeIDMap.get(idList.get(i)));
                bundle.putStringArrayList("typeList", typeMap.get(idList.get(i)));
                //displayFragment(FragmentSearch.newInstance(bundle));

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, FragmentActivityDetail.newInstance(bundle));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        layoutFilterMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        layoutFilterDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutFilterDetail.setVisibility(View.GONE);
                inputSearch.setText("");
            }
        });

        layoutFilterDetailCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCalendar.setTextSize(textSize8sp);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilterViewVisible(layoutFilterDetailCalendar);
            }
        });

        btnLocation.setTextSize(textSize8sp);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilterViewVisible(layoutFilterDetailLocation);
            }
        });

        btnProvince.setTextSize(textSize8sp);
        btnProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilterViewVisible(layoutFilterDetailProvince);
            }
        });

        btnPreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();
            }
        });

        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();
            }
        });

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                inputSearch.setText("");
                setCalendarDateFilter(dateClicked);
                layoutFilterDetail.setVisibility(View.GONE);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
                setTextCalendarDate(firstDayOfNewMonth);
            }
        });

        provinceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inputSearch.setText(provinceNameList.get(position));
                layoutFilterDetail.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        locationMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        locationMapView.onLowMemory();
    }

    private void callProvinceAPI() {
        provinceAPI = new ProvinceAPI();
        provinceAPI.setListener(new ProvinceAPI.ProvinceAPIListener() {
            @Override
            public void onProvinceAPIPreExecuteConcluded() {

            }

            @Override
            public void onProvinceAPIPostExecuteConcluded(String result) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    int status = jObj.optInt("status");
                    String statusDetail = jObj.optString("status_detail");

                    Log.d(TAG, "statusDetail: " + statusDetail);
                    if(status == 200) {
                        JSONArray jArrayProvince = jObj.optJSONArray("province");
                        for(int x=0; x<jArrayProvince.length(); x++) {
                            provinceIDList.add(jArrayProvince.optJSONObject(x).optString("PROVINCE_ID"));
                            provinceNameList.add(jArrayProvince.optJSONObject(x).optString("PROVINCE_NAME"));
                        }
                    } else {
                        Toast.makeText(getActivity(), statusDetail, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapterProvinceList.setProvinceIDList(provinceIDList);
                adapterProvinceList.setProvinceNameList(provinceNameList);
                adapterProvinceList.notifyDataSetChanged();
                provinceListView.setAdapter(adapterProvinceList);
                provinceListView.invalidate();
            }
        });
        provinceAPI.execute("");
    }
    
    private void callProjectAPI() {
        projectAPI = new ProjectAPI();
        projectAPI.setCategoryID(categoryID);
        projectAPI.setUserID(AppPreference.getInstance(getActivity().getApplicationContext()).getUserID());
        projectAPI.setLimit("10");
        projectAPI.setOffset("0");
        //projectAPI.setDate(getDate());
        projectAPI.setDate("");
        projectAPI.setListener(new ProjectAPI.ProjectAPIListener() {
            @Override
            public void onProjectAPIPreExecuteConcluded() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProjectAPIPostExecuteConcluded(String result) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(result);
                    int status = jObj.optInt("status");
                    String statusDetail = jObj.optString("status_detail");

                    Log.d(TAG, "statusDetail: " + statusDetail);
                    if(status == 200) {
                        JSONArray jArrayContent = jObj.optJSONArray("content");
                        for(int x=0; x<jArrayContent.length(); x++) {
                            categoryIDList.add("" + jArrayContent.optJSONObject(x).optInt("category_id"));
                            idList.add("" + jArrayContent.optJSONObject(x).optInt("id"));
                            nameList.add(jArrayContent.optJSONObject(x).optString("title"));
                            dateList.add(jArrayContent.optJSONObject(x).optString("project_date"));
                            provinceList.add(jArrayContent.optJSONObject(x).optJSONArray("province").optJSONObject(0).optString("province_name"));
                            placeList.add(jArrayContent.optJSONObject(x).optString("place"));
                            likeList.add(jArrayContent.optJSONObject(x).optString("like_count"));
                            shortDescriptionList.add(jArrayContent.optJSONObject(x).optString("short_description"));
                            scheduleDateList.add(jArrayContent.optJSONObject(x).optString("start_date"));
                            calendarDateList.add(convertScheduleDateToCalendarDate(jArrayContent.optJSONObject(x).optString("start_date")));
                            latitudeList.add(jArrayContent.optJSONObject(x).optString("latitude"));
                            longitudeList.add(jArrayContent.optJSONObject(x).optString("longitude"));

                            ArrayList<String> imageCoverList = new ArrayList<>();
                            JSONArray jArrayImageCover = jArrayContent.optJSONObject(x).optJSONArray("image_cover");
                            for(int y=0; y<jArrayImageCover.length(); y++) {
                                imageCoverList.add(jArrayImageCover.optJSONObject(y).optJSONObject("lg").optString("source"));
                            }
                            imageCoverMap.put(idList.get(x), imageCoverList);

                            ArrayList<Integer> typeIDList = new ArrayList<>();
                            ArrayList<String> typeList = new ArrayList<>();
                            ArrayList<String> imageList = new ArrayList<>();
                            ArrayList<String> youtubeList = new ArrayList<>();
                            ArrayList<String> descriptionList = new ArrayList<>();
                            JSONArray jArrayDetail = jArrayContent.optJSONObject(x).optJSONArray("detail");
                            for(int z=0; z<jArrayDetail.length(); z++) {
                                typeIDList.add(jArrayDetail.optJSONObject(z).optInt("type_id"));
                                typeList.add(jArrayDetail.optJSONObject(z).optString("type"));
                                try {
                                    //imageList.add(jArrayDetail.optJSONObject(z).optJSONArray("image").optJSONObject(0).optJSONObject("lg").optString("source"));
                                    imageList.add(jArrayDetail.optJSONObject(z).optJSONObject("image").optJSONObject("lg").optString("source"));
                                } catch(Exception e) {
                                    imageList.add("");
                                }
                                youtubeList.add(jArrayDetail.optJSONObject(z).optString("youtube"));
                                descriptionList.add(jArrayDetail.optJSONObject(z).optString("discription"));
                            }
                            typeIDMap.put(idList.get(x), typeIDList);
                            typeMap.put(idList.get(x), typeList);
                            imageMap.put(idList.get(x), imageList);
                            youtubeMap.put(idList.get(x), youtubeList);
                            descriptionMap.put(idList.get(x), descriptionList);
                        }
                    } else {
                        Toast.makeText(getActivity(), statusDetail, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.setCategoryIDList(categoryIDList);
                adapter.setIDList(idList);
                adapter.setImageMap(imageMap);
                adapter.setImageCoverMap(imageCoverMap);
                //adapter.setImageList(imageCoverList);
                adapter.setNameList(nameList);
                adapter.setDateList(dateList);
                adapter.setDescriptionList(shortDescriptionList);
                adapter.setProvinceList(provinceList);
                adapter.setPlaceList(placeList);
                adapter.setLikeList(likeList);
                adapter.setScheduleDateList(scheduleDateList);
                adapter.setCalendarDateList(calendarDateList);
                adapter.notifyDataSetChanged();

                setCalendarEvent(scheduleDateList);
                //setActivityMarker();
            }
        });
        projectAPI.execute("");
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String date = year + "-" + month;
        return date;
    }

    public void goToDetail(int i) {
        listView.performItemClick(
                listView.getAdapter().getView(i, null, null),
                i,
                listView.getAdapter().getItemId(i));
    }

    private int i;
    public void goToDetail(int x, String name) {
        i = nameList.indexOf(name);
        listView.performItemClick(
                listView.getAdapter().getView(x, null, null),
                x,
                listView.getAdapter().getItemId(x));
    }

    public TextWatcher provinceTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() > 0) {
                adapterProvinceList.getFilter().filter(s);
            } else {
                adapterProvinceList.setProvinceIDList(provinceIDList);
                adapterProvinceList.setProvinceNameList(provinceNameList);
                adapterProvinceList.notifyDataSetChanged();
                provinceListView.invalidate();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher mapTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() > 0) {
                adapter.getFilter().filter(s);
            } else {
                adapter.setCategoryIDList(categoryIDList);
                adapter.setIDList(idList);
                adapter.setImageMap(imageMap);
                adapter.setImageCoverMap(imageCoverMap);
                //adapter.setImageList(imageCoverList);
                adapter.setNameList(nameList);
                adapter.setDateList(dateList);
                adapter.setDescriptionList(shortDescriptionList);
                adapter.setProvinceList(provinceList);
                adapter.setPlaceList(placeList);
                adapter.setLikeList(likeList);
                adapter.setScheduleDateList(scheduleDateList);
                adapter.setCalendarDateList(calendarDateList);
                adapter.notifyDataSetChanged();
                listView.invalidate();
            }
            mapMarkerFilter(s);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher myTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() > 0) {
                adapter.getFilter().filter(s);
            } else {
                adapter.setCategoryIDList(categoryIDList);
                adapter.setIDList(idList);
                adapter.setImageMap(imageMap);
                adapter.setImageCoverMap(imageCoverMap);
                //adapter.setImageList(imageCoverList);
                adapter.setNameList(nameList);
                adapter.setDateList(dateList);
                adapter.setDescriptionList(shortDescriptionList);
                adapter.setProvinceList(provinceList);
                adapter.setPlaceList(placeList);
                adapter.setLikeList(likeList);
                adapter.setScheduleDateList(scheduleDateList);
                adapter.setCalendarDateList(calendarDateList);
                adapter.notifyDataSetChanged();
                listView.invalidate();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setFilterViewVisible(View v) {
        layoutFilterDetailCalendar.setVisibility(View.GONE);
        layoutFilterDetailLocation.setVisibility(View.GONE);
        layoutFilterDetailProvince.setVisibility(View.GONE);
        v.setVisibility(View.VISIBLE);
    }

    private void setTextCalendarDate(Date firstDateOfMonth) {
        String[] date = firstDateOfMonth.toString().split(" ");
        int len = date.length;
        String month = monthTHList.get(monthENList.indexOf(date[1]));
        int year = Integer.parseInt(date[len-1]) + 543;
        txtCalendarDate.setText(month + " " + year);
    }

    private void setCalendarEvent(ArrayList<String> calendarDateList) {
        for(int x=0; x<calendarDateList.size(); x++) {
            Event ev3 = new Event(getResources().getColor(R.color.fragment_activity_filter_calendar_event), convertDateToMilliSeconds(calendarDateList.get(x)));
            compactCalendarView.addEvent(ev3);
        }
    }

    private void setCalendarDateFilter(Date dateClicked) {
        String[] date = dateClicked.toString().split(" ");
        int len = date.length;
        String day = date[2];
        String month = monthTHList.get(monthENList.indexOf(date[1]));
        int year = Integer.parseInt(date[len-1]) + 543;
        inputSearch.setText(day + " " + month + " " + year);
    }

    private String convertScheduleDateToCalendarDate(String scheduleDate) {
        String[] date = scheduleDate.split(" ")[0].split("-");
        String day = date[2];
        String month = monthTHList.get(Integer.parseInt(date[1])-1);
        int year = Integer.parseInt(date[0]) + 543;
        String calendarDate = day + " " + month + " " + year;
        Log.d(TAG, "scheduleDate: " + scheduleDate);
        Log.d(TAG, "calendarDate: " + calendarDate);
        return calendarDate;
    }

    private long convertDateToMilliSeconds(String date) {
        long timeInMilliseconds = 0L;
        //String givenDateString = "dd-MMM-yyy Apr 23 16:08:28 GMT+05:30 2013";
        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        try {
            Date mDate = sdf.parse(date);
            timeInMilliseconds = mDate.getTime();
            Log.d(TAG, "Date in milli :: " + timeInMilliseconds);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return timeInMilliseconds;
    }

    private void setActivityMarker() {
        //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.pin_intersection_1);
        int height = 100;
        int width = (height*79)/100;
        //BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.pin_intersection_1);
        //Bitmap b = bitmapdraw.getBitmap();
        //Bitmap b =  BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.pin_intersection_1);
        Bitmap b = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.pin_intersection_1);
        //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.pin_intersection_1);

        Drawable circleDrawable = getResources().getDrawable(R.mipmap.pin_intersection_1);
        BitmapDescriptor icon = getMarkerIconFromDrawable(circleDrawable);
        //BitmapDescriptor icon = new BitmapDescriptor(getActivity());

        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        for(int x=0; x<latitudeList.size(); x++) {
            // For dropping a marker at a point on the Map
            LatLng disaster = new LatLng(Double.parseDouble(latitudeList.get(x)), Double.parseDouble(longitudeList.get(x)));
            markerArray.add(googleMap.addMarker(new MarkerOptions().position(disaster).title(nameList.get(x)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));

            if(x == 0) {
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(disaster).zoom(5).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            Log.d(TAG, "latitude: " + latitudeList.get(x));
            Log.d(TAG, "longitude: " + longitudeList.get(x));
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                /*int index = nameList.indexOf(marker.getTitle());
                Bundle bundle = new Bundle();
                bundle.putString("name", nameList.get(index));
                bundle.putString("date", dateList.get(index));
                bundle.putString("description", descriptionList.get(index));
                bundle.putString("like", likeList.get(index));
                bundle.putString("img", imageList.get(index));
                //displayFragment(FragmentSearch.newInstance(bundle));

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, FragmentDisasterDetail.newInstance(bundle));
                transaction.addToBackStack(null);
                transaction.commit();*/

                int i = nameList.indexOf(marker.getTitle());
                Bundle bundle = new Bundle();
                bundle.putString("title", getText(R.string.fragment_activity_detail_txt_title).toString());
                bundle.putString("name", nameList.get(i));
                bundle.putString("date", dateList.get(i));
                bundle.putString("like", likeList.get(i));
                bundle.putStringArrayList("imageList", imageMap.get(idList.get(i)));
                bundle.putStringArrayList("youtubeList", youtubeMap.get(idList.get(i)));
                bundle.putStringArrayList("descriptionList", descriptionMap.get(idList.get(i)));
                bundle.putIntegerArrayList("typeIDList", typeIDMap.get(idList.get(i)));
                bundle.putStringArrayList("typeList", typeMap.get(idList.get(i)));
                //displayFragment(FragmentSearch.newInstance(bundle));

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, FragmentActivityDetail.newInstance(bundle));
                transaction.addToBackStack(null);
                transaction.commit();
                return false;
            }
        });
    }

    private void mapMarkerFilter(CharSequence s) {
        for(int x=0; x<nameList.size(); x++) {
            if(nameList.get(x).contains(s)) {
                markerArray.get(x).setVisible(true);
            } else {
                markerArray.get(x).setVisible(false);
            }
        }
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
