package th.or.dga.royaljitarsa.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import io.realm.Case;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.adapter.FragmentDisasteWithDatabaserListAdapter;
import th.or.dga.royaljitarsa.connection.ProjectAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.database.ContentDetail;
import th.or.dga.royaljitarsa.database.ContentImageCover;
import th.or.dga.royaljitarsa.database.ContentInfo;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;
import th.or.dga.royaljitarsa.utils.MyConfiguration;

public class FragmentDisasterWithDatabase extends Fragment {

    private String TAG = FragmentDisasterWithDatabase.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private MapView mMapView;
    private LinearLayout layoutMap;
    private LinearLayout layoutList;
    private ListView listView;
    private FragmentDisasteWithDatabaserListAdapter adapter;
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
    private ArrayList<String> imageCoverList;
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

    private Realm realm;
    private ArrayList<ContentInfo> contentInfoLists;
    private ArrayList<ContentImageCover> contentImageCoverLists;
    private ArrayList<ContentDetail> contentDetailLists;

    public static FragmentDisasterWithDatabase newInstance() {
        FragmentDisasterWithDatabase fragment = new FragmentDisasterWithDatabase();
        return fragment;
    }

    private View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_disaster, container, false);

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_disaster, container, false);

            mMapView = (MapView) rootView.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume();// needed to get the map to display immediately

            addLog();
            initValue();
            initUI(rootView);
            setUI();

            callProjectAPI();

            setListener();
        }

        return rootView;
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_disaster).toString());
    }

    private void initValue() {
        realm = Realm.getDefaultInstance();
        contentInfoLists = new ArrayList<>();
        contentImageCoverLists = new ArrayList<>();
        contentDetailLists = new ArrayList<>();

        imageList = new ArrayList<>();
        imageCoverList = new ArrayList<>();
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
        adapter = new FragmentDisasteWithDatabaserListAdapter(getActivity());
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
        adapter.setImageList(imageList);
        adapter.setImageCoverList(imageCoverList);
        //adapter.setImageList(imageCoverList);
        adapter.setNameList(nameList);
        adapter.setDateList(dateList);
        adapter.setDescriptionList(shortDescriptionList);
        adapter.setLikeList(likeList);
        adapter.setProvinceList(provinceList);

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
        //projectAPI.setDate("2018-11");
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
                        clearData();
                        deleteOldData();
                        readJSONContent(jObj);
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

    private void clearData() {
        categoryIDList.clear();
        idList.clear();
        imageList.clear();
        imageCoverList.clear();
        nameList.clear();
        dateList.clear();
        shortDescriptionList.clear();
        likeList.clear();
        provinceList.clear();
        adapter.notifyDataSetChanged();
    }

    private void deleteOldData() {
        realm.beginTransaction();
        realm.where(ContentInfo.class)
                .equalTo("category", "5", Case.INSENSITIVE)
                .findAll();
        realm.deleteAll();
        realm.commitTransaction();

        realm.beginTransaction();
        realm.where(ContentImageCover.class)
                .equalTo("contentCategoryID", "5", Case.INSENSITIVE)
                .findAll();
        realm.deleteAll();
        realm.commitTransaction();

        realm.beginTransaction();
        realm.where(ContentDetail.class)
                .equalTo("contentCategoryID", "5", Case.INSENSITIVE)
                .findAll();
        realm.deleteAll();
        realm.commitTransaction();
    }

    private void readJSONContent(JSONObject jObj) {
        JSONArray jArrayContent = jObj.optJSONArray("content");
        for(int x=0; x<jArrayContent.length(); x++) {
            String id = jArrayContent.optJSONObject(x).optString("id");
            String category = "" + jArrayContent.optJSONObject(x).optInt("category_id");
            String title = jArrayContent.optJSONObject(x).optString("title");
            String shortDescription = jArrayContent.optJSONObject(x).optString("short_description");
            String latitude = jArrayContent.optJSONObject(x).optString("latitude");
            String longitude = jArrayContent.optJSONObject(x).optString("longitude");
            String startDate = jArrayContent.optJSONObject(x).optString("start_date");
            String endDate = jArrayContent.optJSONObject(x).optString("end_date");
            String projectProvince = jArrayContent.optJSONObject(x).optString("project_province");
            String projectPlace = jArrayContent.optJSONObject(x).optString("project_place");
            String projectDate = jArrayContent.optJSONObject(x).optString("project_date");
            String schedultDate = jArrayContent.optJSONObject(x).optString("schedule_date");

            ArrayList<String> imageCoverIDList = new ArrayList<>();
            ArrayList<String> imageCoverURLList = new ArrayList<>();
            JSONArray jArrayImageCover = jArrayContent.optJSONObject(x).optJSONArray("image_cover");
            for(int y=0; y<jArrayImageCover.length(); y++) {
                imageCoverIDList.add("" + y);
                imageCoverURLList.add(jArrayImageCover.optJSONObject(y).optJSONObject("lg").optString("source"));
            }

            ArrayList<String> detailNoList = new ArrayList<>();
            ArrayList<String> detailTypeIDList = new ArrayList<>();
            ArrayList<String> detailTypeNameList = new ArrayList<>();
            ArrayList<String> detailContentList = new ArrayList<>();
            JSONArray jArrayDetail = jArrayContent.optJSONObject(x).optJSONArray("detail");
            for(int y=0; y<jArrayDetail.length(); y++) {
                String no = jArrayDetail.optJSONObject(y).optString("No");
                String typeID = "" + jArrayDetail.optJSONObject(y).optString("type_id");
                String typeName = jArrayDetail.optJSONObject(y).optString("type");
                String content = "";
                if(typeID.equals("1")) {
                    content = jArrayDetail.optJSONObject(y).optJSONObject("image").optJSONObject("lg").optString("source");
                } else if(typeID.equals("2")) {
                    content = jArrayDetail.optJSONObject(y).optString("youtube");
                } else {
                    content = jArrayDetail.optJSONObject(y).optString("discription");
                }
                detailNoList.add(no);
                detailTypeIDList.add(typeID);
                detailTypeNameList.add(typeName);
                detailContentList.add(content);
            }

            ContentInfo contentInfo = new ContentInfo();
            contentInfo.setId(id);
            contentInfo.setCategory(category);
            contentInfo.setTitle(title);
            contentInfo.setShortDescription(shortDescription);
            contentInfo.setLatitude(latitude);
            contentInfo.setLongitude(longitude);
            contentInfo.setStartDate(startDate);
            contentInfo.setEndDate(endDate);
            contentInfo.setProjectProvince(projectProvince);
            contentInfo.setProjectPlace(projectPlace);
            contentInfo.setProjectDate(projectDate);
            contentInfo.setScheduleDate(schedultDate);
            contentInfoLists.add(contentInfo);

            ContentImageCover contentImageCover = new ContentImageCover();
            for(int z=0; z<imageCoverIDList.size(); z++) {
                contentImageCover.setContentCategoryID(category);
                contentImageCover.setContentInfoID(id);
                contentImageCover.setImageCoverID(imageCoverIDList.get(z));
                contentImageCover.setImageCoverURL(imageCoverURLList.get(z));
                contentImageCoverLists.add(contentImageCover);
            }

            ContentDetail contentDetail = new ContentDetail();
            for(int z=0; z<detailNoList.size(); z++) {
                contentDetail.setContentCategoryID(category);
                contentDetail.setContentInfoID(id);
                contentDetail.setDetailNo(detailNoList.get(z));
                contentDetail.setDetailTypeID(detailTypeIDList.get(z));
                contentDetail.setDetailTypeName(detailTypeNameList.get(z));
                contentDetail.setDetailContent(detailContentList.get(z));
                contentDetailLists.add(contentDetail);
            }
        }

        addContentToDatabase();
    }

    private void addContentToDatabase() {
        realm.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (final ContentInfo contentInfoList : contentInfoLists) {
                            ContentInfo contentInfo = realm.createObject(ContentInfo.class);
                            contentInfo.setId(contentInfoList.getId());
                            contentInfo.setCategory(contentInfoList.getCategory());
                            contentInfo.setTitle(contentInfoList.getTitle());
                            contentInfo.setShortDescription(contentInfoList.getShortDescription());
                            contentInfo.setLatitude(contentInfoList.getLatitude());
                            contentInfo.setLongitude(contentInfoList.getLongitude());
                            contentInfo.setStartDate(contentInfoList.getStartDate());
                            contentInfo.setEndDate(contentInfoList.getEndDate());
                            contentInfo.setProjectProvince(contentInfoList.getProjectProvince());
                            contentInfo.setProjectPlace(contentInfoList.getProjectPlace());
                            contentInfo.setProjectDate(contentInfoList.getProjectDate());
                            contentInfo.setScheduleDate(contentInfoList.getScheduleDate());
                            Log.d(TAG, "Add Title Name = " + contentInfoList.getTitle());
                        }
                        for (final ContentImageCover contentImageCoverList : contentImageCoverLists) {
                            ContentImageCover contentImageCover = realm.createObject(ContentImageCover.class);
                            contentImageCover.setContentInfoID(contentImageCoverList.getContentInfoID());
                            contentImageCover.setImageCoverID(contentImageCoverList.getImageCoverID());
                            contentImageCover.setImageCoverURL(contentImageCoverList.getImageCoverURL());
                        }
                        for (final ContentDetail contentDetailList : contentDetailLists) {
                            ContentDetail contentDetail = realm.createObject(ContentDetail.class);
                            contentDetail.setContentInfoID(contentDetailList.getContentInfoID());
                            contentDetail.setDetailNo(contentDetailList.getDetailNo());
                            contentDetail.setDetailTypeID(contentDetailList.getDetailTypeID());
                            contentDetail.setDetailTypeName(contentDetailList.getDetailTypeName());
                            contentDetail.setDetailContent(contentDetailList.getDetailContent());
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                    }

                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        //error.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    }
                });
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
                //queryData(s.toString());
            } else {
                //getAllBranch();
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

    private void getAllContent() {
        RealmResults<ContentInfo> resultContentInfo = realm.where(ContentInfo.class).equalTo("category", "5").findAll();
        RealmResults<ContentImageCover> resultContentImageCover = realm.where(ContentImageCover.class).equalTo("contentCategoryID", "5").findAll();
        RealmResults<ContentDetail> resultContentDetail = realm.where(ContentDetail.class).equalTo("contentCategoryID", "5").findAll();

        clearData();
        //setContent(resultContentInfo, resultContentImageCover, resultContentDetail);
    }

    private void queryBranch(String s) {
        /*Log.d(TAG, "query: " + s.toString());

        clearData();
        RealmResults<DatabaseServiceLocationBranchList> result = realm.where(DatabaseServiceLocationBranchList.class)
                .contains("name", s, Case.INSENSITIVE)
                .or()
                .contains("address", s, Case.INSENSITIVE)
                .or()
                .contains("keyword", s, Case.INSENSITIVE)
                .or()
                .contains("stationKey", s, Case.INSENSITIVE)
                .findAllAsync();

        result.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<DatabaseServiceLocationBranchList>>() {
            @Override
            public void onChange(RealmResults<DatabaseServiceLocationBranchList> databaseServiceLocationBranchLists, OrderedCollectionChangeSet changeSet) {
                setBranch(databaseServiceLocationBranchLists);
            }
        });*/
    }

    private void setBranch(RealmResults<ContentInfo> resultContentInfo, RealmResults<ContentImageCover> resultContentImageCover, RealmResults<ContentDetail> resultContentDetail) {
        /*for (ContentInfo contentInfo : resultContentInfo) {
            idList.add(contentInfo.getId());
            latitudeList.add(contentInfo.getLatitude());
            longitudeList.add(contentInfo.getLongitude());
            typeList.add(contentInfo.getType());
            provinceIDList.add(contentInfo.getProvinceID());
            coolList.add(contentInfo.getCool());
            isCoolList.add(contentInfo.getIsCool());
            dryList.add(contentInfo.getDry());
            nameList.add(contentInfo.getName());
            telList.add(contentInfo.getTel());
            telPrimaryList.add(contentInfo.getTelPrimary());
            pptLogoList.add(contentInfo.getPptLogo());

            private ArrayList<String> nameList;
            private ArrayList<String> dateList;
            private ArrayList<String> provinceList;
            private ArrayList<String> placeList;
            private ArrayList<String> likeList;
            private ArrayList<String> latitudeList;
            private ArrayList<String> longitudeList;
            private ArrayList<String> descriptionList;

            timeOpenList.add(contentInfo.getTimeOpen());
            timeCloseList.add(branch.getTimeClose());
            dateOpenList.add(branch.getDateOpen());
            datePrimaryList.add(branch.getDatePrimary());
            dateTimeOpenList.add(branch.getDateTimeOpen());
            addressList.add(branch.getAddress());
            branchTypeList.add(branch.getBranchType());
            keywordList.add(branch.getKeyword());
            stationKeyList.add(branch.getStationKey());
            Log.d(TAG, "set Name: " + branch.getName());
        }
        adapter.notifyDataSetChanged();
        listView.invalidate();
        setBranchMarker();*/
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
}
