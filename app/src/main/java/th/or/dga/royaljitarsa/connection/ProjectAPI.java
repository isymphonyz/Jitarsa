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

public class ProjectAPI extends AsyncTask<String, Void, String> {

    public interface ProjectAPIListener {
        void onProjectAPIPreExecuteConcluded();
        void onProjectAPIPostExecuteConcluded(String result);
    }

    private String TAG = ProjectAPI.this.getClass().getSimpleName();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient okHttpClient;
    private String url = MyConfiguration.DOMAIN + MyConfiguration.URL_PROJECT;
    private String userID = "";
    private String categoryID = "";
    private String limit = "";
    private String offset = "";
    private String date = "";

    private ProjectAPIListener mListener;

    final public void setListener(ProjectAPIListener listener) {
        mListener = listener;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
    public void setLimit(String limit) {
        this.limit = limit;
    }
    public void setOffset(String offset) {
        this.offset = offset;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    protected void onPreExecute() {
        okHttpClient = new OkHttpClient();

        Log.d(TAG, "onPostExecute");

        if (mListener != null) {
            mListener.onProjectAPIPreExecuteConcluded();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        /*RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userID", userID)
                .addFormDataPart("category_id", categoryID)
                .addFormDataPart("limit", limit)
                .addFormDataPart("offset", offset)
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
            mListener.onProjectAPIPreExecuteConcluded();

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        Log.d(TAG, "onPostExecute: " + result);

        if (mListener != null) {
            mListener.onProjectAPIPostExecuteConcluded(result);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

    private String setParameter() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("userID", userID);
            jsonObject.accumulate("category_id",  categoryID);
            jsonObject.accumulate("limit",  limit);
            jsonObject.accumulate("offset",  offset);
            jsonObject.accumulate("date",  date);
        } catch (Exception e) {

        }

        Log.d(TAG, "Parameter: " + jsonObject.toString());
        return jsonObject.toString();
    }
}
