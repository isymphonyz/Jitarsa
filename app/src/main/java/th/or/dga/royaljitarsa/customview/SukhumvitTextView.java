package th.or.dga.royaljitarsa.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import th.or.dga.royaljitarsa.R;

/**
 * Created by Dooplus on 11/26/15 AD.
 */

public class SukhumvitTextView extends AppCompatTextView {

    Typeface tf;

    public SukhumvitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.text_size));
    }

    public SukhumvitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.text_size));
    }

    public SukhumvitTextView(Context context) {
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
}
