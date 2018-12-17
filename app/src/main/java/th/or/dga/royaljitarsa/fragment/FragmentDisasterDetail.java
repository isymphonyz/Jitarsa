package th.or.dga.royaljitarsa.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class FragmentDisasterDetail extends Fragment {

    private ImageView img;
    private SukhumvitTextView txtName;
    private SukhumvitTextView txtDate;
    private SukhumvitTextView txtDescription;
    private SukhumvitTextView txtLike;
    private SukhumvitTextView btnShare;
    private ImageView btnSave;
    private LinearLayout layoutLike;
    private LinearLayout.LayoutParams params;

    private String image = "";
    private String name = "";
    private String date = "";
    private String description = "";
    private String like = "";

    private int margin8dp = 0;

    public static FragmentDisasterDetail newInstance() {
        FragmentDisasterDetail fragment = new FragmentDisasterDetail();
        return fragment;
    }

    public static FragmentDisasterDetail newInstance(Bundle bundle) {
        FragmentDisasterDetail fragment = new FragmentDisasterDetail();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list_item, container, false);


        initValue();
        initUI(rootView);
        setUI();
        setListener();

        return rootView;
    }

    private void initValue() {
        Resources r = getActivity().getResources();
        margin8dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                r.getDisplayMetrics()
        );

        image = getArguments().getString("image");
        name = getArguments().getString("name");
        date = getArguments().getString("date");
        description = getArguments().getString("description");
        like = getArguments().getString("like");
    }

    private void initUI(View rootView) {
        
        img = (ImageView) rootView.findViewById(R.id.img);
        txtName = (SukhumvitTextView) rootView.findViewById(R.id.txtName);
        txtDate = (SukhumvitTextView) rootView.findViewById(R.id.txtDate);
        txtDescription = (SukhumvitTextView) rootView.findViewById(R.id.txtDescription);
        txtLike = (SukhumvitTextView) rootView.findViewById(R.id.txtLike);
        btnShare = (SukhumvitTextView) rootView.findViewById(R.id.btnShare);
        btnSave = (ImageView) rootView.findViewById(R.id.btnSave);
        layoutLike = (LinearLayout) rootView.findViewById(R.id.layoutLike);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin8dp, margin8dp, margin8dp, margin8dp);
    }

    private void setUI() {
        Glide.with(getActivity())
                .load(image)
                .apply(fitCenterTransform()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .priority(Priority.HIGH))
                .into(img);

        txtName.setText(name);
        txtDate.setText(date);
        //txtDescription.setText(description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtDescription.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtDescription.setText(Html.fromHtml(description));
        }
        txtLike.setText(like);
    }

    private void setListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getText(R.string.app_name));
                sendIntent.putExtra(Intent.EXTRA_TEXT, name);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                sendIntent.setType("text/plain");
                getActivity().startActivity(sendIntent);
            }
        });

        layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
