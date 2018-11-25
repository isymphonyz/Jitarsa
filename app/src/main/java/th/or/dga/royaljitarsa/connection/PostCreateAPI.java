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

public class PostCreateAPI extends AsyncTask<String, Void, String> {

    public interface PostCreateAPIListener {
        void onPostCreateAPIPreExecuteConcluded();
        void onPostCreateAPIPostExecuteConcluded(String result);
    }

    private String TAG = PostCreateAPI.this.getClass().getSimpleName();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient okHttpClient;
    private String url = MyConfiguration.DOMAIN + MyConfiguration.URL_POST_CREATE;
    private String userID = "";
    private String title = "";
    private String provinceID = "";
    private String postDate = "";
    private String description = "";
    private String postYoutube = "";

    private PostCreateAPIListener mListener;

    final public void setListener(PostCreateAPIListener listener) {
        mListener = listener;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostYoutube(String postYoutube) {
        this.postYoutube = postYoutube;
    }

    @Override
    protected void onPreExecute() {
        okHttpClient = new OkHttpClient();

        Log.d(TAG, "onPostExecute");

        if (mListener != null) {
            mListener.onPostCreateAPIPreExecuteConcluded();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userID", userID)
                .addFormDataPart("title", title)
                .addFormDataPart("province_id", provinceID)
                .addFormDataPart("post_date", postDate)
                .addFormDataPart("discription", description)
                .addFormDataPart("post_youtube", postYoutube)
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
            mListener.onPostCreateAPIPreExecuteConcluded();

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        Log.d(TAG, "onPostExecute: " + result);

        if (mListener != null) {
            mListener.onPostCreateAPIPostExecuteConcluded(result);
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
