package th.or.dga.royaljitarsa.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;

import static com.bumptech.glide.request.RequestOptions.circleCropTransform;
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class FragmentMePostImageOrMessage extends Fragment {

    private String TAG = FragmentMePostImageOrMessage.this.getClass().getSimpleName();

    private ImageView imgUser;
    private SukhumvitTextView txtPostName;
    private SukhumvitTextView txtPostDate;
    private LinearLayout layoutAddImage;
    private LinearLayout layoutImageSelected;
    private ImageView img01;
    private ImageView img02;
    private ImageView img03;
    private ImageView img04;
    private ImageView img05;
    private ImageView btnClose01;
    private ImageView btnClose02;
    private ImageView btnClose03;
    private ImageView btnClose04;
    private ImageView btnClose05;
    private Spinner inputProvince;
    private Spinner inputActivity;
    private SukhumvitTextView btnPost;
    private SukhumvitEditText inputMessage;
    private ArrayList<ImageView> imageViewList;

    private AppPreference appPreference;
    private String urlImageProfile = "";
    private String name = "";

    private FragmentTransaction transaction;
    private Fragment selectedFragment = null;

    private static final int PICK_FROM_GALLARY = 1;
    private static final int PICK_FROM_CAMERA = 2;

    Uri outPutfileUri;
    boolean isSingleImage = false;
    int indexImage = 0;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static FragmentMePostImageOrMessage newInstance() {
        FragmentMePostImageOrMessage fragment = new FragmentMePostImageOrMessage();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me_post_image_or_message, container, false);

        initValue();
        initUI(rootView);
        setUI();
        setListener();



        return rootView;
    }

    private void initValue() {
        appPreference = new AppPreference(getActivity());
        urlImageProfile = appPreference.getProfileImage();
        name = appPreference.getFullname();

        imageViewList = new ArrayList<>();
    }

    private void initUI(View rootView) {
        imgUser = (ImageView) rootView.findViewById(R.id.imgUser);
        txtPostName = (SukhumvitTextView) rootView.findViewById(R.id.txtPostName);
        txtPostDate = (SukhumvitTextView) rootView.findViewById(R.id.txtPostDate);
        layoutAddImage = (LinearLayout) rootView.findViewById(R.id.layoutAddImage);
        layoutImageSelected = (LinearLayout) rootView.findViewById(R.id.layoutImageSelected);
        img01 = (ImageView) rootView.findViewById(R.id.img01);
        img02 = (ImageView) rootView.findViewById(R.id.img02);
        img03 = (ImageView) rootView.findViewById(R.id.img03);
        img04 = (ImageView) rootView.findViewById(R.id.img04);
        img05 = (ImageView) rootView.findViewById(R.id.img05);
        btnClose01 = (ImageView) rootView.findViewById(R.id.btnClose01);
        btnClose02 = (ImageView) rootView.findViewById(R.id.btnClose02);
        btnClose03 = (ImageView) rootView.findViewById(R.id.btnClose03);
        btnClose04 = (ImageView) rootView.findViewById(R.id.btnClose04);
        btnClose05 = (ImageView) rootView.findViewById(R.id.btnClose05);
        inputProvince = (Spinner) rootView.findViewById(R.id.inputProvince);
        inputActivity = (Spinner) rootView.findViewById(R.id.inputActivity);
        btnPost = (SukhumvitTextView) rootView.findViewById(R.id.btnPost);
        inputMessage = (SukhumvitEditText) rootView.findViewById(R.id.inputMessage);
    }

    private void setUI() {
        imageViewList.add(img01);
        imageViewList.add(img02);
        imageViewList.add(img03);
        imageViewList.add(img04);
        imageViewList.add(img05);

        if(urlImageProfile.equals("")) {
            Glide.with(this)
                    .load(R.mipmap.ic_launcher)
                    .apply(circleCropTransform()
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_background)
                            .priority(Priority.HIGH))
                    .into(imgUser);
        } else {
            Glide.with(this)
                    .load(urlImageProfile)
                    .apply(circleCropTransform()
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_background)
                            .priority(Priority.HIGH))
                    .into(imgUser);
        }

        txtPostName.setText(name);
    }

    private void setListener() {
        layoutAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMultiImage();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputMessage.getText().toString();
            }
        });

        img01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexImage = 0;
                chooseSingleImage();
            }
        });

        img02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexImage = 1;
                chooseSingleImage();
            }
        });

        img03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexImage = 2;
                chooseSingleImage();
            }
        });

        img04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexImage = 3;
                chooseSingleImage();
            }
        });

        img05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexImage = 4;
                chooseSingleImage();
            }
        });

        btnClose01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultImage(0);
            }
        });

        btnClose02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultImage(1);
            }
        });

        btnClose03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultImage(2);
            }
        });

        btnClose04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultImage(3);
            }
        });

        btnClose05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultImage(4);
            }
        });
    }

    public void displayFragment(Fragment selectedFragment) {
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutContent, selectedFragment);
        transaction.commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    //pic coming from camera
                    Bitmap bitmap=null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), outPutfileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;

            case PICK_FROM_GALLARY:

                if (resultCode == Activity.RESULT_OK) {
                    layoutAddImage.setVisibility(View.INVISIBLE);
                    layoutImageSelected.setVisibility(View.VISIBLE);
                    //pick image from gallery
                    try {
                        Log.d(TAG, "data.getClipData().getItemCount(): " + data.getClipData().getItemCount());
                        Log.d(TAG, "data.getClipData().getItemAt(0): " + data.getClipData().getItemAt(0));
                        Log.d(TAG, "data.getClipData(): " + data.getClipData());
                        for(int x=0; x<data.getClipData().getItemCount(); x++) {

                            Glide.with(this)
                                    .load(data.getClipData().getItemAt(x).getUri())
                                    .apply(fitCenterTransform()
                                            .placeholder(R.drawable.ic_launcher_foreground)
                                            .error(R.drawable.ic_launcher_background)
                                            .priority(Priority.HIGH))
                                    .into(imageViewList.get(x));
                        }
                    } catch(Exception e) {
                        Log.d(TAG, "data.getData(): " + data.getData());
                        Glide.with(this)
                                .load(data.getData())
                                .apply(fitCenterTransform()
                                        .placeholder(R.drawable.ic_launcher_foreground)
                                        .error(R.drawable.ic_launcher_background)
                                        .priority(Priority.HIGH))
                                .into(imageViewList.get(indexImage));
                    }
                    /*Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap bitmap=null;
                    bitmap = BitmapFactory.decodeFile(imgDecodableString);*/
                }
                break;
        }
    }

    private void chooseSingleImage() {
        isSingleImage = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }

    private void chooseMultiImage() {
        isSingleImage = false;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }

    private void setDefaultImage(int indexBtnClose) {
        Glide.with(this)
                .load(R.mipmap.post_add_picture)
                .apply(fitCenterTransform()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .priority(Priority.HIGH))
                .into(imageViewList.get(indexBtnClose));
    }
}
