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


/**
 * SwitchButton.
 */
public class RegisterFacebookAPI extends AsyncTask<String, Void, String> {

    public interface RegisterFacebookAPIListener {
        void onRegisterFacebookAPIPreExecuteConcluded();
        void onRegisterFacebookAPIPostExecuteConcluded(String result);
    }

    private String TAG = RegisterFacebookAPI.this.getClass().getSimpleName();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient okHttpClient;
    private String url = MyConfiguration.DOMAIN + MyConfiguration.URL_REGISTER_FACEBOOK;
    private String fullName = "";
    private String facebookID = "";
    private String email = "";
    private String tokenID = "";

    private RegisterFacebookAPIListener mListener;

    final public void setListener(RegisterFacebookAPIListener listener) {
        mListener = listener;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    @Override
    protected void onPreExecute() {
        okHttpClient = new OkHttpClient();

        Log.d(TAG, "onPostExecute");

        if (mListener != null) {
            mListener.onRegisterFacebookAPIPreExecuteConcluded();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        /*RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("facebook_id", facebookID)
                .addFormDataPart("fullname", fullName)
                .addFormDataPart("email", email)
                .addFormDataPart("token_id", tokenID)
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
            mListener.onRegisterFacebookAPIPreExecuteConcluded();

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        Log.d(TAG, "onPostExecute: " + result);

        if (mListener != null) {
            mListener.onRegisterFacebookAPIPostExecuteConcluded(result);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

    private String setParameter() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("facebook_id", facebookID);
            jsonObject.accumulate("fullname",  fullName);
            jsonObject.accumulate("email",  email);
            jsonObject.accumulate("token_id",  tokenID);
        } catch (Exception e) {

        }

        return jsonObject.toString();
    }
}
