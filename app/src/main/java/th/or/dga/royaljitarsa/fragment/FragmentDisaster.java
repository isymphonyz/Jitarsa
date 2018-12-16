package th.or.dga.royaljitarsa.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.adapter.FragmentDisasterListAdapter;
import th.or.dga.royaljitarsa.connection.ProjectAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;
import th.or.dga.royaljitarsa.utils.MyConfiguration;

public class FragmentDisaster extends Fragment {

    private String TAG = FragmentDisaster.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private MapView mMapView;
    private LinearLayout layoutMap;
    private LinearLayout layoutList;
    private ListView listView;
    private FragmentDisasterListAdapter adapter;
    private GoogleMap googleMap;
    private SukhumvitTextView txtTitle;
    private SukhumvitEditText inputSearch;
    private ImageView imgUp;
    private ImageView imgDown;

    private ArrayList<String> categoryIDList;
    private ArrayList<String> idList;
    //private ArrayList<String> imageCoverList;
    private ArrayList<String> shortDescriptionList;

    private HashMap<String, ArrayList<String>> imageCoverMap;
    private HashMap<String, ArrayList<String>> imageMap;
    private HashMap<String, ArrayList<String>> descriptionMap;
    private HashMap<String, ArrayList<String>> youtubeMap;
    private HashMap<String, ArrayList<String>> typeMap;
    private HashMap<String, ArrayList<Integer>> typeIDMap;

    private ArrayList<String> imageList;
    private ArrayList<String> nameList;
    private ArrayList<String> dateList;
    private ArrayList<String> provinceList;
    private ArrayList<String> placeList;
    private ArrayList<String> likeList;
    private ArrayList<String> latitudeList;
    private ArrayList<String> longitudeList;
    private ArrayList<String> descriptionList;

    private ArrayList<Marker> markerArray;

    private ProjectAPI projectAPI;
    private String categoryID = MyConfiguration.CATEGORY_DISASTER_ID;

    public static FragmentDisaster newInstance() {
        FragmentDisaster fragment = new FragmentDisaster();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_disaster, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        addLog();
        initValue();
        initUI(rootView);
        setUI();

        callProjectAPI();

        setListener();

        return rootView;
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_disaster).toString());
    }

    private void initValue() {
        imageList = new ArrayList<>();
        nameList = new ArrayList<>();
        dateList = new ArrayList<>();
        provinceList = new ArrayList<>();
        placeList = new ArrayList<>();
        likeList = new ArrayList<>();
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();
        descriptionList = new ArrayList<>();

        categoryIDList = new ArrayList<>();
        idList = new ArrayList<>();
        //imageCoverList = new ArrayList<>();
        shortDescriptionList = new ArrayList<>();

        imageCoverMap = new HashMap<>();
        imageMap = new HashMap<>();
        descriptionMap = new HashMap<>();
        youtubeMap = new HashMap<>();
        typeIDMap = new HashMap<>();
        typeMap = new HashMap<>();

        markerArray = new ArrayList<Marker>();
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        txtTitle = (SukhumvitTextView) rootView.findViewById(R.id.txtTitle);
        layoutMap = (LinearLayout) rootView.findViewById(R.id.layoutMap);
        layoutList = (LinearLayout) rootView.findViewById(R.id.layoutList);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new FragmentDisasterListAdapter(getActivity());
        inputSearch = (SukhumvitEditText) rootView.findViewById(R.id.inputSearch);
        imgUp = (ImageView) rootView.findViewById(R.id.imgUp);
        imgDown = (ImageView) rootView.findViewById(R.id.imgDown);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
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
        adapter.setLikeList(likeList);

        listView.setAdapter(adapter);
    }

    private void setListener() {
        inputSearch.addTextChangedListener(myTextWatcher);
        layoutMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        });

        layoutList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });

        /*listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(totalItemCount != listView.getLastVisiblePosition() + 1){
                    imgUp.setVisibility(View.GONE);
                    imgDown.setVisibility(View.VISIBLE);
                }else{
                    imgUp.setVisibility(View.VISIBLE);
                    imgDown.setVisibility(View.GONE);
                }
            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("title", getText(R.string.fragment_disaster_txt_title_detail).toString());
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void callProjectAPI() {
        projectAPI = new ProjectAPI();
        projectAPI.setCategoryID(categoryID);
        projectAPI.setUserID(AppPreference.getInstance(getActivity().getApplicationContext()).getUserID());
        projectAPI.setLimit("10");
        projectAPI.setOffset("0");
        projectAPI.setDate(getDate());
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
                            imageList.add(jArrayContent.optJSONObject(x).optJSONArray("detail").optJSONObject(0).optString("image"));
                            nameList.add(jArrayContent.optJSONObject(x).optString("title"));
                            dateList.add(jArrayContent.optJSONObject(x).optString("project_date"));
                            provinceList.add(jArrayContent.optJSONObject(x).optJSONArray("province").optJSONObject(0).optString("province_name"));
                            placeList.add(jArrayContent.optJSONObject(x).optString("place"));
                            likeList.add(jArrayContent.optJSONObject(x).optString("like_count"));
                            latitudeList.add(jArrayContent.optJSONObject(x).optString("latitude"));
                            longitudeList.add(jArrayContent.optJSONObject(x).optString("longitude"));
                            descriptionList.add(jArrayContent.optJSONObject(x).optJSONArray("detail").optJSONObject(0).optString("discription"));

                            categoryIDList.add("" + jArrayContent.optJSONObject(x).optInt("category_id"));
                            idList.add("" + jArrayContent.optJSONObject(x).optInt("id"));
                            shortDescriptionList.add(jArrayContent.optJSONObject(x).optString("short_description"));

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
                setDisasterMarker();
                adapter.notifyDataSetChanged();
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

    private void setDisasterMarker() {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.pin_intersection_1);
        int height = 100;
        int width = (height*79)/100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.mipmap.pin_intersection_1);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        for(int x=0; x<latitudeList.size(); x++) {
            // For dropping a marker at a point on the Map
            LatLng disaster = new LatLng(Double.parseDouble(latitudeList.get(x)), Double.parseDouble(longitudeList.get(x)));
            markerArray.add(googleMap.addMarker(new MarkerOptions().position(disaster).title(nameList.get(x)).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))));

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
                bundle.putString("title", getText(R.string.fragment_disaster_txt_title_detail).toString());
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

    public void goToDetail(int i) {
        listView.performItemClick(
                listView.getAdapter().getView(i, null, null),
                i,
                listView.getAdapter().getItemId(i));
    }

    public TextWatcher myTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() > 0) {
                adapter.getFilter().filter(s);
            } else {
                adapter.setNameList(nameList);
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

    private void mapMarkerFilter(CharSequence s) {
        for(int x=0; x<nameList.size(); x++) {
            if(nameList.get(x).contains(s)) {
                markerArray.get(x).setVisible(true);
            } else {
                markerArray.get(x).setVisible(false);
            }
        }
    }
}
