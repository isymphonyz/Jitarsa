package th.or.dga.royaljitarsa.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import th.or.dga.royaljitarsa.R;

/**
 * Created by Dooplus on 11/26/15 AD.
 */

public class SukhumvitTextView extends AppCompatTextView {
    private static final String TAG = SukhumvitTextView.class.getSimpleName();
    private ScaleGestureDetector mScaleDetector;

    private float mScaleFactor = 1.f;
    private float defaultSize;

    private float zoomLimit = 3.0f;

    Typeface tf;

    public SukhumvitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.text_size));
        initialize();
    }

    public SukhumvitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.text_size));
        initialize();
    }

    public SukhumvitTextView(Context context) {
        super(context);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.text_size));
        this.setTextColor(Color.BLACK);
        initialize();
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

    private void initialize() {
        defaultSize = getTextSize();
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    /***
     * @param zoomLimit
     * Default value is 3, 3 means text can zoom 3 times the default size
     */

    public void setZoomLimit(float zoomLimit) {
        this.zoomLimit = zoomLimit;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        super.onTouchEvent(ev);
        mScaleDetector.onTouchEvent(ev);
        return true;
    }

    /*Scale Gesture listener class,
    mScaleFactor is getting the scaling value
    and mScaleFactor is mapped between 1.0 and and zoomLimit
    that is 3.0 by default. You can also change it. 3.0 means text
    can zoom to 3 times the default value.*/

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, zoomLimit));
            setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSize * mScaleFactor);
            Log.e(TAG, String.valueOf(mScaleFactor));
            return true;
        }
    }
}
