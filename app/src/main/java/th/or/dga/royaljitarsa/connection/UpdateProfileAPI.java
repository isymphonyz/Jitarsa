package th.or.dga.royaljitarsa.connection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import th.or.dga.royaljitarsa.utils.MyConfiguration;

public class UpdateProfileAPI extends AsyncTask<String, Void, String> {

    public interface GetProfileAPIListener {
        void onUpdateProfileAPIPreExecuteConcluded();
        void onUpdateProfileAPIPostExecuteConcluded(String result);
    }

    private String TAG = UpdateProfileAPI.this.getClass().getSimpleName();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient okHttpClient;
    private String url = MyConfiguration.DOMAIN + MyConfiguration.URL_UPDATE_PROFILE;
    private String userID = "";
    private String identificationCard = "";
    private String fullName = "";
    private String laserCode = "";
    private String birthDate = "";
    private String phone = "";
    private String address = "";
    private String provinceID = "";
    private String districtID = "";
    private String subDistrictID = "";
    private String zipCode = "";

    private GetProfileAPIListener mListener;

    final public void setListener(GetProfileAPIListener listener) {
        mListener = listener;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setIdentificationCard(String identificationCard) {
        this.identificationCard = identificationCard;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setLaserCode(String laserCode) {
        this.laserCode = laserCode;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public void setSubDistrictID(String subDistrictID) {
        this.subDistrictID = subDistrictID;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    protected void onPreExecute() {
        okHttpClient = new OkHttpClient();

        Log.d(TAG, "onPostExecute");

        if (mListener != null) {
            mListener.onUpdateProfileAPIPreExecuteConcluded();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userID", userID)
                .addFormDataPart("identification_card", identificationCard)
                .addFormDataPart("fullname", fullName)
                .addFormDataPart("laser_code", laserCode)
                .addFormDataPart("birthdate", birthDate)
                .addFormDataPart("phone", phone)
                .addFormDataPart("address", address)
                .addFormDataPart("province_id", provinceID)
                .addFormDataPart("district_id", districtID)
                .addFormDataPart("sub_district_id", subDistrictID)
                .addFormDataPart("zip_code", zipCode)
                //.addFormDataPart("image", "logo-square.png", RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        Log.d(TAG, "url: " + url);
        Request request = new Request.Builder()
                //.header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(url)
                .post(requestBody)
                .build();

        /*RequestBody body = RequestBody.create(JSON, setParameter());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();*/

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

        if (mListener != null)
            mListener.onUpdateProfileAPIPreExecuteConcluded();

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        Log.d(TAG, "onPostExecute: " + result);

        if (mListener != null) {
            mListener.onUpdateProfileAPIPostExecuteConcluded(result);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

    /*private String setParameter() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("userpassword", password);
            jsonObject.accumulate("fullname",  fullName);
            jsonObject.accumulate("email",  email);
            jsonObject.accumulate("token_id",  tokenID);
        } catch (Exception e) {

        }

        return jsonObject.toString();
    }*/
}
