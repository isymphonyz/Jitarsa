package th.or.dga.royaljitarsa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import th.or.dga.royaljitarsa.connection.GetProfileAPI;
import th.or.dga.royaljitarsa.connection.LoginAPI;
import th.or.dga.royaljitarsa.connection.LoginFacebookAPI;
import th.or.dga.royaljitarsa.connection.RegisterFacebookAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;

public class Login extends AppCompatActivity implements LoginAPI.LoginAPIListener {

    private String TAG = Login.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private SukhumvitEditText inputUsername;
    private SukhumvitEditText inputPassword;
    private SukhumvitTextView btnSignIn;
    private SukhumvitTextView txtForgetPassword;
    private SukhumvitTextView txtRegister;
    private SukhumvitTextView txtSkip;
    private LoginButton btnFacebook;

    private AccessToken mAccessToken;
    private CallbackManager callbackManager;

    private OkHttpClient okHttpClient;
    private RegisterFacebookAPI registerFacebookApi;
    private LoginFacebookAPI loginFacebookApi;
    private LoginAPI loginApi;
    private LoginAPI.LoginAPIListener loginAPIListener;
    private GetProfileAPI getProfileAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        initUI();
        initValue();
        setListener();
        //facebookLoginCallback();

        /*LoginAPI aTask = new LoginAPI();
        aTask.setListener(this);
        aTask.execute();*/

    }

    private void initValue() {
        callbackManager = CallbackManager.Factory.create();
        okHttpClient = new OkHttpClient();
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputUsername = (SukhumvitEditText) findViewById(R.id.inputUsername);
        inputPassword = (SukhumvitEditText) findViewById(R.id.inputPassword);
        btnSignIn = (SukhumvitTextView) findViewById(R.id.btnSignIn);
        txtForgetPassword = (SukhumvitTextView) findViewById(R.id.txtForgetPassword);
        txtRegister = (SukhumvitTextView) findViewById(R.id.txtRegister);
        txtSkip = (SukhumvitTextView) findViewById(R.id.txtSkip);

        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        btnFacebook.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/sukhumvitset.ttf"));
        btnFacebook.setTextSize(TypedValue.COMPLEX_UNIT_SP, this.getResources().getDimension(R.dimen.short_description_text_size));
        btnFacebook.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLoginCallback();
            }
        });
        // If using in a fragment
        //btnFacebook.setFragment(this);
    }

    private void setListener() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = inputUsername.getText().toString();
                final String password = inputPassword.getText().toString();

                //username = "aaa@gmail.com";
                //password = "aaa";

                //executeLogin(username, password);
                //new LoginAPI().execute("");

                loginApi = new LoginAPI();
                loginApi.setUsername(username);
                loginApi.setPassword(password);
                loginApi.setListener(new LoginAPI.LoginAPIListener() {
                    @Override
                    public void onLoginAPIPreExecuteConcluded() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoginAPIPostExecuteConcluded(String result) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jObj = new JSONObject(result);
                            int status = jObj.optInt("status");
                            String statusDetail = jObj.optString("status_detail");
                            String userID = jObj.optString("userID");
                            AppPreference.getInstance(getApplicationContext()).setUserID(userID);
                            AppPreference.getInstance(getApplicationContext()).setUsername(username);
                            AppPreference.getInstance(getApplicationContext()).setPassword(password);
                            AppPreference.getInstance(getApplicationContext()).setLoginStatus(status);

                            Log.d(TAG, "statusDetail: " + statusDetail);
                            if(status == 200) {
                                getProfileAPI = new GetProfileAPI();
                                getProfileAPI.setUserID(userID);
                                getProfileAPI.setListener(new GetProfileAPI.GetProfileAPIListener() {
                                    @Override
                                    public void onGetProfileAPIPreExecuteConcluded() {
                                        progressBar.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onGetProfileAPIPostExecuteConcluded(String result) {
                                        progressBar.setVisibility(View.GONE);
                                        readUserProfile(result);
                                        Intent intent = new Intent(getApplicationContext(), Home.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                getProfileAPI.execute("");
                            } else {
                                Toast.makeText(getApplicationContext(), statusDetail, Toast.LENGTH_SHORT).show();
                                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(500);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                loginApi.execute("");
            }
        });

        txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
                startActivity(intent);
                //finish();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
        });
    }
    
    private void facebookLoginCallback() {
        // Callback registration
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "btnFacebook: onSuccess");
                AccessToken accessToken = loginResult.getAccessToken();
                final String facebookToken = accessToken.getToken();
                final String faceboookUserID = accessToken.getUserId();
                String facebookApplicationID = accessToken.getApplicationId();

                Log.d(TAG, "facebookToken: " + facebookToken);
                Log.d(TAG, "faceboookUserID: " + faceboookUserID);
                Log.d(TAG, "facebookApplicationID: " + facebookApplicationID);

                AppPreference.getInstance(getApplicationContext()).setFacebookToken(facebookToken);
                AppPreference.getInstance(getApplicationContext()).setFacebookUserID(faceboookUserID);
                AppPreference.getInstance(getApplicationContext()).setFacebookApplicationID(facebookApplicationID);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v(TAG, response.toString());

                                // Application code
                                try {
                                    final String email = object.getString("email");
                                    final String name = object.getString("name");
                                    String birthday = object.getString("birthday"); // 01/31/1980 format

                                    Log.d(TAG, "email: " + email);
                                    Log.d(TAG, "birthday: " + birthday);

                                    registerFacebookApi = new RegisterFacebookAPI();
                                    registerFacebookApi.setEmail(email);
                                    registerFacebookApi.setFacebookID(faceboookUserID);
                                    registerFacebookApi.setFullName(name);
                                    registerFacebookApi.setTokenID(facebookToken);
                                    registerFacebookApi.setListener(new RegisterFacebookAPI.RegisterFacebookAPIListener() {
                                        @Override
                                        public void onRegisterFacebookAPIPreExecuteConcluded() {
                                            progressBar.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onRegisterFacebookAPIPostExecuteConcluded(String result) {
                                            progressBar.setVisibility(View.GONE);
                                            try {
                                                JSONObject jObj = new JSONObject(result);
                                                int status = jObj.optInt("status");
                                                String statusDetail = jObj.optString("status_detail");

                                                Log.d(TAG, "statusDetail: " + statusDetail);
                                                loginFacebookApi = new LoginFacebookAPI();
                                                loginFacebookApi.setEmail(email);
                                                loginFacebookApi.setFacebookID(faceboookUserID);
                                                loginFacebookApi.setListener(new LoginFacebookAPI.LoginFacebookAPIListener() {
                                                    @Override
                                                    public void onLoginFacebookAPIPreExecuteConcluded() {
                                                        progressBar.setVisibility(View.VISIBLE);
                                                    }

                                                    @Override
                                                    public void onLoginFacebookAPIPostExecuteConcluded(String result) {
                                                        progressBar.setVisibility(View.GONE);
                                                        try {
                                                            JSONObject jObj = new JSONObject(result);
                                                            int status = jObj.optInt("status");
                                                            String statusDetail = jObj.optString("status_detail");
                                                            String userID = jObj.optString("userID");
                                                            AppPreference.getInstance(getApplicationContext()).setUserID(userID);
                                                            AppPreference.getInstance(getApplicationContext()).setUsername(email);
                                                            AppPreference.getInstance(getApplicationContext()).setLoginStatus(status);

                                                            Log.d(TAG, "statusDetail: " + statusDetail);
                                                            if(status == 200) {
                                                                getProfileAPI = new GetProfileAPI();
                                                                getProfileAPI.setUserID(userID);
                                                                getProfileAPI.setListener(new GetProfileAPI.GetProfileAPIListener() {
                                                                    @Override
                                                                    public void onGetProfileAPIPreExecuteConcluded() {
                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                    }

                                                                    @Override
                                                                    public void onGetProfileAPIPostExecuteConcluded(String result) {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        readUserProfile(result);
                                                                        Intent intent = new Intent(getApplicationContext(), Home.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                });
                                                                getProfileAPI.execute("");
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), statusDetail, Toast.LENGTH_SHORT).show();
                                                                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                                                v.vibrate(500);
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                loginFacebookApi.execute("");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    registerFacebookApi.execute("");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "JSONException: " + e.toString());
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday, first_name, last_name, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "btnFacebook: onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "btnFacebook: onError");
            }
        });

        /*LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d(TAG, "LoginManager: onSuccess");
                        AccessToken accessToken = loginResult.getAccessToken();
                        final String facebookToken = accessToken.getToken();
                        final String faceboookUserID = accessToken.getUserId();
                        String facebookApplicationID = accessToken.getApplicationId();

                        Log.d(TAG, "facebookToken: " + facebookToken);
                        Log.d(TAG, "faceboookUserID: " + faceboookUserID);
                        Log.d(TAG, "facebookApplicationID: " + facebookApplicationID);

                        AppPreference.getInstance(getApplicationContext()).setFacebookToken(facebookToken);
                        AppPreference.getInstance(getApplicationContext()).setFacebookUserID(faceboookUserID);
                        AppPreference.getInstance(getApplicationContext()).setFacebookApplicationID(facebookApplicationID);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v(TAG, response.toString());

                                        // Application code
                                        try {
                                            final String email = object.getString("email");
                                            final String name = object.getString("name");
                                            String birthday = object.getString("birthday"); // 01/31/1980 format

                                            Log.d(TAG, "email: " + email);
                                            Log.d(TAG, "birthday: " + birthday);

                                            registerFacebookApi = new RegisterFacebookAPI();
                                            registerFacebookApi.setEmail(email);
                                            registerFacebookApi.setFacebookID(faceboookUserID);
                                            registerFacebookApi.setFullName(name);
                                            registerFacebookApi.setTokenID(facebookToken);
                                            registerFacebookApi.setListener(new RegisterFacebookAPI.RegisterFacebookAPIListener() {
                                                @Override
                                                public void onRegisterFacebookAPIPreExecuteConcluded() {
                                                    progressBar.setVisibility(View.VISIBLE);
                                                }

                                                @Override
                                                public void onRegisterFacebookAPIPostExecuteConcluded(String result) {
                                                    progressBar.setVisibility(View.GONE);
                                                    try {
                                                        JSONObject jObj = new JSONObject(result);
                                                        int status = jObj.optInt("status");
                                                        String statusDetail = jObj.optString("status_detail");

                                                        Log.d(TAG, "statusDetail: " + statusDetail);
                                                        loginFacebookApi = new LoginFacebookAPI();
                                                        loginFacebookApi.setEmail(email);
                                                        loginFacebookApi.setFacebookID(faceboookUserID);
                                                        loginFacebookApi.setListener(new LoginFacebookAPI.LoginFacebookAPIListener() {
                                                            @Override
                                                            public void onLoginFacebookAPIPreExecuteConcluded() {
                                                                progressBar.setVisibility(View.VISIBLE);
                                                            }

                                                            @Override
                                                            public void onLoginFacebookAPIPostExecuteConcluded(String result) {
                                                                progressBar.setVisibility(View.GONE);
                                                                try {
                                                                    JSONObject jObj = new JSONObject(result);
                                                                    int status = jObj.optInt("status");
                                                                    String statusDetail = jObj.optString("status_detail");
                                                                    String userID = jObj.optString("userID");
                                                                    AppPreference.getInstance(getApplicationContext()).setUserID(userID);
                                                                    AppPreference.getInstance(getApplicationContext()).setUsername(email);
                                                                    AppPreference.getInstance(getApplicationContext()).setLoginStatus(status);

                                                                    Log.d(TAG, "statusDetail: " + statusDetail);
                                                                    if(status == 200) {
                                                                        getProfileAPI = new GetProfileAPI();
                                                                        getProfileAPI.setUserID(userID);
                                                                        getProfileAPI.setListener(new GetProfileAPI.GetProfileAPIListener() {
                                                                            @Override
                                                                            public void onGetProfileAPIPreExecuteConcluded() {
                                                                                progressBar.setVisibility(View.VISIBLE);
                                                                            }

                                                                            @Override
                                                                            public void onGetProfileAPIPostExecuteConcluded(String result) {
                                                                                progressBar.setVisibility(View.GONE);
                                                                                readUserProfile(result);
                                                                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            }
                                                                        });
                                                                        getProfileAPI.execute("");
                                                                    } else {
                                                                        Toast.makeText(getApplicationContext(), statusDetail, Toast.LENGTH_SHORT).show();
                                                                        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                                                        v.vibrate(500);
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                        loginFacebookApi.execute("");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            registerFacebookApi.execute("");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d(TAG, "JSONException: " + e.toString());
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, name, email, gender, birthday, first_name, last_name, location");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d(TAG, "LoginManager: onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d(TAG, "LoginManager: onError");
                    }
                });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String executeLogin(String username, String password) {
        String result = "";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", "bunhan321@gmail.com")
                .addFormDataPart("userpassword", "123456")
                //.addFormDataPart("image", "logo-square.png", RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        Request request = new Request.Builder()
                //.header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("http://demo.360innovative.com/2018/360jitarsa/user/login")
                .post(requestBody)
                .build();

        try {

            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                //throw new IOException("Unexpected code " + response);
                result = response.body().string();
                Log.d(TAG, "response.isSuccessful(): " + result);
            } else {
                Log.d(TAG, "response.Fail: " + response.body().string());
            }
            //result = response.body().string();
        } catch(IOException e) {

        }

        return result;
    }

    private void run() throws Exception {
        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userID", "4")
                //.addFormDataPart("image", "logo-square.png", RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        Request request = new Request.Builder()
                //.header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("http://demo.360innovative.com/2018/360jitarsa/user/getprofile")
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            //throw new IOException("Unexpected code " + response);
            Log.d(TAG, "response.isSuccessful(): " + response.body().string());
        } else {
            Log.d(TAG, "response.Fail: " + response.body().string());
        }

        //System.out.println(response.body().string());
    }

    @Override
    public void onLoginAPIPreExecuteConcluded() {
        Log.d(TAG, "onLoginAPIPreExecuteConcluded");
    }

    @Override
    public void onLoginAPIPostExecuteConcluded(String result) {
        Log.d(TAG, "onLoginAPIPostExecuteConcluded");
    }

    private void readUserProfile(String profile) {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(profile);
            int status = jObj.optInt("status");
            String statusDetail = jObj.optString("status_detail");
            String fullname = jObj.optString("fullname");
            String identification_card = jObj.optString("identification_card");
            String laser_code = jObj.optString("laser_code");
            String email = jObj.optString("email");
            String birthdate = jObj.optString("birthdate");
            String phone = jObj.optString("phone");
            String profile_image = jObj.optString("profile_image");
            String qrcode = jObj.optString("qrcode");
            String address = jObj.optString("address");
            String province_id = jObj.optString("province_id");
            String district_id = jObj.optString("district_id");
            String sub_district_id = jObj.optString("sub_district_id");
            String zip_code = jObj.optString("zip_code");
            String status_user = jObj.optString("status_user");
            String language = jObj.optString("language");
            AppPreference.getInstance(getApplicationContext()).setFullname(fullname);
            AppPreference.getInstance(getApplicationContext()).setDistrictID(identification_card);
            AppPreference.getInstance(getApplicationContext()).setLaserCode(laser_code);
            AppPreference.getInstance(getApplicationContext()).setEmail(email);
            AppPreference.getInstance(getApplicationContext()).setBirthDate(birthdate);
            AppPreference.getInstance(getApplicationContext()).setAge("" + getAge(birthdate));
            AppPreference.getInstance(getApplicationContext()).setPhone(phone);
            AppPreference.getInstance(getApplicationContext()).setProfileImage(profile_image);
            AppPreference.getInstance(getApplicationContext()).setQRCode(qrcode);
            AppPreference.getInstance(getApplicationContext()).setAddress(address);
            AppPreference.getInstance(getApplicationContext()).setProvinceID(province_id);
            AppPreference.getInstance(getApplicationContext()).setDistrictID(district_id);
            AppPreference.getInstance(getApplicationContext()).setSubDistrictID(sub_district_id);
            AppPreference.getInstance(getApplicationContext()).setZipCode(zip_code);
            AppPreference.getInstance(getApplicationContext()).setStatusUser(status_user);
            AppPreference.getInstance(getApplicationContext()).setLanguage(language);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getAge(String dobString){

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age;
    }
}
