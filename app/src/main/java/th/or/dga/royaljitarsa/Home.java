package th.or.dga.royaljitarsa;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.firebase.analytics.FirebaseAnalytics;

import de.hdodenhof.circleimageview.CircleImageView;
import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.fragment.FragmentAbout;
import th.or.dga.royaljitarsa.fragment.FragmentActivity;
import th.or.dga.royaljitarsa.fragment.FragmentAnnouncement;
import th.or.dga.royaljitarsa.fragment.FragmentChangePassword;
import th.or.dga.royaljitarsa.fragment.FragmentDisaster;
import th.or.dga.royaljitarsa.fragment.FragmentHome;
import th.or.dga.royaljitarsa.fragment.FragmentHowToPlay;
import th.or.dga.royaljitarsa.fragment.FragmentJitarsa;
import th.or.dga.royaljitarsa.fragment.FragmentMe;
import th.or.dga.royaljitarsa.fragment.FragmentNews;
import th.or.dga.royaljitarsa.fragment.FragmentPost;
import th.or.dga.royaljitarsa.fragment.FragmentQRCode;
import th.or.dga.royaljitarsa.fragment.FragmentSave;
import th.or.dga.royaljitarsa.fragment.FragmentSearch;
import th.or.dga.royaljitarsa.fragment.FragmentSetting;
import th.or.dga.royaljitarsa.fragment.FragmentUnderConstruction;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.BottomNavigationViewHelper;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class Home extends AppCompatActivity {

    private String TAG = Home.this.getClass().getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;
    FirebaseLogTracking firebaseLogTracking;

    public static Home instance;

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBar actionbar;

    private BottomNavigationView bottomNavigationView;

    private ImageView btnNotice;
    private ImageView btnSearch;
    private ImageView btnBack;
    private LinearLayout layoutSearch;
    private SukhumvitEditText inputSearch;
    private SukhumvitTextView txtVersion;

    private CircleImageView imgProfile;
    private SukhumvitTextView txtName;

    public FragmentTransaction transaction;
    private boolean isCallFragment = false;

    private AppPreference appPreference;
    private String urlImageProfile = "";
    private String name = "";
    private String appVersion = "";

    public static Home getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            instance = new Home();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        initValue();
        initUI();
        setUI();
        setListener();

        askPermission();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        Log.d(TAG, "getLoginStatus: " + appPreference.getLoginStatus());

        Menu nav_Menu = navigationView.getMenu();
        /*if(appPreference.getLoginStatus() == 200) {
            nav_Menu.findItem(R.id.menuSignIn).setVisible(false);
            nav_Menu.findItem(R.id.menuSignOut).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.menuSignIn).setVisible(true);
            nav_Menu.findItem(R.id.menuSignOut).setVisible(false);
        }*/

        initValue();
        setUI();
        bottomNavigationView.setEnabled(false);
        bottomNavigationView.setFocusable(false);
        bottomNavigationView.setFocusableInTouchMode(false);
        bottomNavigationView.setClickable(false);
        //bottomNavigationView.setContextClickable(false);
        bottomNavigationView.setOnClickListener(null);
    }

    private void initValue() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);

        appPreference = new AppPreference(this);
        urlImageProfile = appPreference.getProfileImage();
        name = appPreference.getFullname();

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        btnNotice = (ImageView) findViewById(R.id.btnNotice);
        btnSearch = (ImageView) findViewById(R.id.btnSearch);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        layoutSearch = (LinearLayout) findViewById(R.id.layoutSearch);
        inputSearch = (SukhumvitEditText) findViewById(R.id.inputSearch);

        //txtVersion = (SukhumvitTextView) findViewById(R.id.txtVersion);
        //txtVersion.setText(appVersion);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        NavigationView navigation2 = (NavigationView) navigationView.findViewById(R.id.navigation2);
        View view = navigation2.inflateHeaderView(R.layout.version);
        txtVersion = (SukhumvitTextView) view.findViewById(R.id.txtVersion);
        txtVersion.setText("Ver. " + appVersion);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.burger_menu);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        //BottomNavigationViewHelper bottomNavigationViewHelper = new BottomNavigationViewHelper();
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        hView.setVisibility(View.GONE);
        imgProfile = (CircleImageView) hView.findViewById(R.id.imgProfile);
        txtName = (SukhumvitTextView) hView.findViewById(R.id.txtName);

        //Manually displaying the first fragment - one time only
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, FragmentHome.newInstance());
        transaction.commit();*/
        displayFragment(FragmentHome.newInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUI() {
        Log.d(TAG, "urlImageProfile: " + urlImageProfile);
        Log.d(TAG, "name: " + name);
        Glide.with(this)
                .load(urlImageProfile)
                .apply(fitCenterTransform()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .priority(Priority.HIGH))
                .into(imgProfile);

        txtName.setText(name);
        inputSearch.setSingleLine(true);
        inputSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);

        /*BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.text);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }*/

        //BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        //BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        menuView.setVisibility(View.GONE);
    }
    
    private void setListener() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        //Toast.makeText(getApplicationContext(), "Menu ID: " + menuItem.getItemId(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), "R.id.qr: " + R.id.menuQR, Toast.LENGTH_SHORT).show();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        Fragment selectedFragment = null;
                        switch (menuItem.getItemId()) {
                            case R.id.menuVolunteer:
                                selectedFragment = FragmentJitarsa.newInstance();
                                isCallFragment = true;
                                break;
                            case R.id.menuInformation:
                                selectedFragment = FragmentAnnouncement.newInstance();
                                isCallFragment = true;
                                break;
                            case R.id.menuActivity:
                                selectedFragment = FragmentActivity.newInstance();
                                isCallFragment = true;
                                break;
                            case R.id.menuNews:
                                selectedFragment = FragmentNews.newInstance();
                                isCallFragment = true;
                                break;
                            case R.id.menuSave:
                                selectedFragment = FragmentSave.newInstance();
                                isCallFragment = true;
                                break;
                            case R.id.menuDisaster:
                                selectedFragment = FragmentDisaster.newInstance();
                                isCallFragment = true;
                                break;
                            case R.id.menuQR:
                                //selectedFragment = FragmentQRCode.newInstance();
                                //isCallFragment = false;

                                Intent intentQRCode = new Intent(getApplicationContext(), QRCodeV2.class);
                                intentQRCode.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                                intentQRCode.putExtra(BarcodeCaptureActivity.UseFlash, false);
                                intentQRCode.putExtra("isFromHome", true);
                                startActivity(intentQRCode);

                                break;
                            case R.id.menuSetting:
                                selectedFragment = FragmentSetting.newInstance();
                                isCallFragment = true;
                                break;
                            case R.id.menuHowToPlay:
                                selectedFragment = FragmentHowToPlay.newInstance();
                                isCallFragment = false;

                                Intent intentHowToPlay = new Intent(getApplicationContext(), HowToPlay.class);
                                intentHowToPlay.putExtra("isFromHome", true);
                                startActivity(intentHowToPlay);

                                break;
                            case R.id.menuChangePassword:
                                selectedFragment = FragmentChangePassword.newInstance();
                                isCallFragment = false;

                                Intent intentChangePassword = new Intent(getApplicationContext(), ChangePassword.class);
                                intentChangePassword.putExtra("isFromHome", true);
                                startActivity(intentChangePassword);

                                break;
                            case R.id.menuAbout:
                                selectedFragment = FragmentAbout.newInstance();
                                isCallFragment = true;
                                break;
                            case R.id.menuSignOut:
                                selectedFragment = FragmentMe.newInstance();
                                isCallFragment = false;
                                logoutDialog();
                                break;
                            case R.id.menuSignIn:
                                isCallFragment = false;
                                Intent intentLogin = new Intent(getApplicationContext(), Login.class);
                                intentLogin.putExtra("isFromHome", true);
                                startActivity(intentLogin);
                                break;
                        }

                        if(isCallFragment) {
                            displayFragment(selectedFragment);
                        }

                        return true;
                    }
                });

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        bottomNavigationView.getMenu().removeItem(R.id.menuPost);
        bottomNavigationView.getMenu().removeItem(R.id.menuSave);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.menuHome:
                                selectedFragment = FragmentHome.newInstance();
                                break;
                            case R.id.menuPost:
                                selectedFragment = FragmentPost.newInstance();
                                selectedFragment = FragmentUnderConstruction.newInstance();
                                break;
                            case R.id.menuSave:
                                selectedFragment = FragmentSave.newInstance();
                                selectedFragment = FragmentUnderConstruction.newInstance();
                                break;
                            case R.id.menuMe:
                                selectedFragment = FragmentMe.newInstance();
                                break;
                        }
                        //selectedFragment = FragmentUnderConstruction.newInstance();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSearch.setVisibility(View.VISIBLE);
                actionbar.setDisplayHomeAsUpEnabled(false);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layoutSearch.getVisibility() == View.VISIBLE) {
                    layoutSearch.setVisibility(View.GONE);
                    actionbar.setDisplayHomeAsUpEnabled(true);
                } else {
                    /*Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                    if(fragment instanceof FragmentAnnouncementDetail) {
                        displayFragment(FragmentAnnouncement.newInstance());
                    } else if(fragment instanceof FragmentActivityDetail) {
                        displayFragment(FragmentActivity.newInstance());
                    } else if(fragment instanceof FragmentDisasterDetail) {
                        displayFragment(FragmentDisaster.newInstance());
                    } else if(fragment instanceof FragmentDisasterDetail) {
                        displayFragment(FragmentDisaster.newInstance());
                    } else if(fragment instanceof FragmentDisasterDetail) {
                        displayFragment(FragmentDisaster.newInstance());
                    } else if(fragment instanceof FragmentSettingAlertFormat) {
                        displayFragment(FragmentSetting.newInstance());
                    } else if(fragment instanceof FragmentSettingShare) {
                        displayFragment(FragmentSettingAlertFormat.newInstance());
                    } else if(fragment instanceof FragmentSettingLanguage) {
                        displayFragment(FragmentSettingShare.newInstance());
                    } else if(fragment instanceof FragmentMePost) {
                        displayFragment(FragmentMe.newInstance());
                    } else if(fragment instanceof FragmentMeHistory) {
                        displayFragment(FragmentMe.newInstance());
                    } else if(fragment instanceof FragmentMeImage) {
                        displayFragment(FragmentMe.newInstance());
                    } else if(fragment instanceof FragmentMeImageCreate) {
                        displayFragment(FragmentMeImage.newInstance());
                    } else if(fragment instanceof FragmentMeFriend) {
                        displayFragment(FragmentMe.newInstance());
                    } else if(fragment instanceof FragmentMeFriendAddFriend) {
                        displayFragment(FragmentMeFriend.newInstance());
                    } else if(fragment instanceof FragmentMeFriendAddFriendQR) {
                        displayFragment(FragmentMeFriend.newInstance());
                    } else if(fragment instanceof FragmentMeFriendAddFriendScan) {
                        displayFragment(FragmentMeFriend.newInstance());
                    } else if(fragment instanceof FragmentMeFriendAddFriendInvite) {
                        displayFragment(FragmentMeFriend.newInstance());
                    } else if(fragment instanceof FragmentMeFriendAddFriendResponse) {
                        displayFragment(FragmentMeFriendAddFriend.newInstance());
                    } else {
                        //displayFragment(FragmentHome.newInstance());
                        getFragmentManager().popBackStackImmediate();
                    }*/
                    onBackPressed();
                }
            }
        });

        inputSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d(TAG, "onKey: " + i + " == " + KeyEvent.KEYCODE_ENTER);

                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }

                Bundle bundle = new Bundle();
                bundle.putString("keyword", inputSearch.getText().toString());
                //displayFragment(FragmentSearch.newInstance(bundle));

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, FragmentSearch.newInstance(bundle));
                transaction.commit();

                return false;
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here is your code
                Log.d(TAG, "onKey: " + s);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void displayFragment(Fragment selectedFragment) {
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void displayFragment(FragmentTransaction transaction, Fragment selectedFragment) {
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 20);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant

                return;
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 20);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant

                return;
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 20);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant

                return;
            }
        }
    }

    public void logoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getText(R.string.dialog_logout_txt_title))
        //.setMessage("Note that nuking planet Jupiter will destroy everything in there.")
        .setPositiveButton(getText(R.string.dialog_logout_btn_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "setPositiveButton");
                appPreference.setLoginStatus(0);
                appPreference.setFullname("");
                appPreference.setAge("");
                appPreference.setUsername("");
                appPreference.setProfileImage("");

                //LoginManager.getInstance().logOut();
                if (AccessToken.getCurrentAccessToken() == null) {
                    return; // already logged out
                }

                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {

                        LoginManager.getInstance().logOut();

                    }
                }).executeAsync();
                onResume();
            }
        })
        .setNegativeButton(getText(R.string.dialog_logout_btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}
