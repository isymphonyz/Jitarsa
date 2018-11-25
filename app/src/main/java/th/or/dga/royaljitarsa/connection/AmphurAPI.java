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

public class AmphurAPI extends AsyncTask<String, Void, String> {

    public interface ProjectAPIListener {
        void onAmphurAPIPreExecuteConcluded();
        void onAmphurAPIPostExecuteConcluded(String result);
    }

    private String TAG = AmphurAPI.this.getClass().getSimpleName();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient okHttpClient;
    private String url = MyConfiguration.DOMAIN + MyConfiguration.URL_AMPHUR;
    private String userID = "";
    private String categoryID = "";
    private String limit = "";
    private String offset = "";

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

    @Override
    protected void onPreExecute() {
        okHttpClient = new OkHttpClient();

        Log.d(TAG, "onPostExecute");

        if (mListener != null) {
            mListener.onAmphurAPIPreExecuteConcluded();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        RequestBody requestBody = new MultipartBody.Builder()
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
            mListener.onAmphurAPIPreExecuteConcluded();

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        Log.d(TAG, "onPostExecute: " + result);

        if (mListener != null) {
            mListener.onAmphurAPIPostExecuteConcluded(result);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

}
