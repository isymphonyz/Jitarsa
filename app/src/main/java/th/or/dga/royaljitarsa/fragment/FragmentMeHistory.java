package th.or.dga.royaljitarsa.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import th.or.dga.royaljitarsa.EditUserData;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.UpdateUserData;
import th.or.dga.royaljitarsa.adapter.FragmentMeHistoryListAdapter;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class FragmentMeHistory extends Fragment {

    private CircleImageView imgProfile;
    private SukhumvitTextView txtName;
    private SukhumvitTextView txtAge;
    private LinearLayout layoutEditProfile;
    private LinearLayout layoutProofJitarsa;
    private ListView listView;
    private FragmentMeHistoryListAdapter adapter;

    private ArrayList<String> dateList;
    private ArrayList<String> projectList;
    private String urlImageProfile = "";
    private String name = "";
    private String age = "";

    public static FragmentMeHistory newInstance() {
        FragmentMeHistory fragment = new FragmentMeHistory();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me_history, container, false);

        initValue();
        initUI(rootView);
        setUI();
        setListener();

        return rootView;
    }

    private void initValue() {
        urlImageProfile = AppPreference.getInstance(getActivity()).getProfileImage();
        name = AppPreference.getInstance(getActivity()).getFullname();
        age = AppPreference.getInstance(getActivity()).getAge();

        projectList = new ArrayList<>();
        dateList = new ArrayList<>();

        for(int x=0; x<10; x++) {
            projectList.add("Project: " + x);
            dateList.add("Date: " + x);
        }
    }

    private void initUI(View rootView) {
        imgProfile = (CircleImageView) rootView.findViewById(R.id.imgProfile);
        txtName = (SukhumvitTextView) rootView.findViewById(R.id.txtName);
        txtAge = (SukhumvitTextView) rootView.findViewById(R.id.txtAge);
        layoutEditProfile = (LinearLayout) rootView.findViewById(R.id.layoutEditProfile);
        layoutProofJitarsa = (LinearLayout) rootView.findViewById(R.id.layoutProofJitarsa);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new FragmentMeHistoryListAdapter(getActivity());
    }

    private void setUI() {
        Glide.with(this)
                .load(urlImageProfile)
                .apply(centerCropTransform()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .priority(Priority.HIGH))
                .into(imgProfile);

        txtName.setText(name);
        txtAge.setText(getText(R.string.fragment_me_history_txt_age) + " " + age + " " + getText(R.string.fragment_me_history_txt_year));

        adapter.setDateList(dateList);
        adapter.setProjectList(projectList);
        listView.setAdapter(adapter);
        listView.invalidateViews();
        adapter.notifyDataSetChanged();
        //setListViewHeightBasedOnChildren(listView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        } else {
            setListViewHeightBasedOnChildren(listView);
        }
    }

    private void setListener() {
        layoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditUserData.class);
                startActivity(intent);
            }
        });
        layoutProofJitarsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), UpdateUserData.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public static void setListViewHeightBasedOnChildren
            (ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            //view.measure(View.MeasureSpec.makeMeasureSpec(desiredWidth, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight()*(listAdapter.getCount()-1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
