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

public class LikeProjectAPI extends AsyncTask<String, Void, String> {

    public interface LikeProjectAPIListener {
        void onLikeProjectAPIPreExecuteConcluded();
        void onLikeProjectAPIPostExecuteConcluded(String result);
    }

    private String TAG = LikeProjectAPI.this.getClass().getSimpleName();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient okHttpClient;
    private String url = MyConfiguration.DOMAIN + MyConfiguration.URL_LIKE_PROJECT;
    private String userID = "";
    private String projectID = "";
    private String categoryID = "";

    private LikeProjectAPIListener mListener;

    final public void setListener(LikeProjectAPIListener listener) {
        mListener = listener;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    @Override
    protected void onPreExecute() {
        okHttpClient = new OkHttpClient();

        Log.d(TAG, "onPostExecute");

        if (mListener != null) {
            mListener.onLikeProjectAPIPreExecuteConcluded();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        /*RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userID", userID)
                .addFormDataPart("project_id", projectID)
                .addFormDataPart("category_id", categoryID)
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
            mListener.onLikeProjectAPIPreExecuteConcluded();

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        Log.d(TAG, "onPostExecute: " + result);

        if (mListener != null) {
            mListener.onLikeProjectAPIPostExecuteConcluded(result);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

    private String setParameter() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("userID", userID);
            jsonObject.accumulate("category_id",  categoryID);
            jsonObject.accumulate("project_id",  projectID);
        } catch (Exception e) {

        }

        return jsonObject.toString();
    }
}
