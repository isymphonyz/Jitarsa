package th.or.dga.royaljitarsa.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import th.or.dga.royaljitarsa.R;

/**
 * Created by Dooplus on 11/26/15 AD.
 */

public class SukhumvitZoomableV2TextView extends AppCompatTextView implements View.OnTouchListener {

    private static final String TAG = SukhumvitZoomableV2TextView.class.getSimpleName();

    final static float STEP = 200;
    float mRatio = 1.0f;
    int mBaseDist;
    float mBaseRatio;
    float fontsize = 13;

    Typeface tf;

    public SukhumvitZoomableV2TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.text_size));
    }

    public SukhumvitZoomableV2TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.text_size));
    }

    public SukhumvitZoomableV2TextView(Context context) {
        super(context);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.text_size));
    }

    public void setTypeface(int style) {
        Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/sukhumvitset.ttf");
        Typeface boldTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/sukhumvitset-bold.ttf");

        if (style == Typeface.BOLD) {
            super.setTypeface(boldTypeface/*, -1*/);
        } else {
            super.setTypeface(normalTypeface/*, -1*/);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            int action = event.getAction();
            int pureaction = action & MotionEvent.ACTION_MASK;
            if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
                mBaseDist = getDistance(event);
                mBaseRatio = mRatio;
            } else {
                float delta = (getDistance(event) - mBaseDist) / STEP;
                float multi = (float) Math.pow(2, delta);
                mRatio = Math.min(1024.0f, Math.max(0.1f, mBaseRatio * multi));
                this.setTextSize(mRatio + 13);
            }
        }
        return true;
    }

    int getDistance(MotionEvent event) {
        int dx = (int) (event.getX(0) - event.getX(1));
        int dy = (int) (event.getY(0) - event.getY(1));
        return (int) (Math.sqrt(dx * dx + dy * dy));
    }

    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
