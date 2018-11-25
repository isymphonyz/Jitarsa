package th.or.dga.royaljitarsa.connection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import th.or.dga.royaljitarsa.utils.MyConfiguration;

public class InvestigateAPI extends AsyncTask<String, Void, String> {

    public interface InvestigateAPIListener {
        void onInvestigateAPIPreExecuteConcluded();
        void onInvestigateAPIPostExecuteConcluded(String result);
    }

    private String TAG = InvestigateAPI.this.getClass().getSimpleName();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient okHttpClient;
    private String url = MyConfiguration.DOMAIN + MyConfiguration.URL_INVESTIGATE;
    private String userID = "";
    private String identificationCard = "";
    private String fullName = "";
    private String laserCode = "";
    private String birthDate = "";
    private String phone = "";
    private String dateJitarsa = "";

    private InvestigateAPIListener mListener;

    final public void setListener(InvestigateAPIListener listener) {
        mListener = listener;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setDateJitarsa(String dateJitarsa) {
        this.dateJitarsa = dateJitarsa;
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

    @Override
    protected void onPreExecute() {
        okHttpClient = new OkHttpClient();

        Log.d(TAG, "onPostExecute");

        if (mListener != null) {
            mListener.onInvestigateAPIPreExecuteConcluded();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        /*RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userID", userID)
                .addFormDataPart("identification_card", identificationCard)
                .addFormDataPart("fullname", fullName)
                .addFormDataPart("laser_code", laserCode)
                .addFormDataPart("birthdate", birthDate)
                .addFormDataPart("phone", phone)
                .addFormDataPart("date_jitarsa", dateJitarsa)
                //.addFormDataPart("image", "logo-square.png", RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        Log.d(TAG, "url: " + url);
        Request request = new Request.Builder()
                //.header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(url)
                .post(requestBody)
                .build();*/

        RequestBody body = RequestBody.create(JSON, setParameter());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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

        if (mListener != null)
            mListener.onInvestigateAPIPreExecuteConcluded();

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        Log.d(TAG, "onPostExecute: " + result);

        if (mListener != null) {
            mListener.onInvestigateAPIPostExecuteConcluded(result);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

    private String setParameter() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("userID", userID);
            jsonObject.accumulate("identification_card", identificationCard);
            jsonObject.accumulate("laser_code", laserCode);
        } catch (Exception e) {

        }

        return jsonObject.toString();
    }
}
