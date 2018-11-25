package th.or.dga.royaljitarsa.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by Dooplus on 11/26/15 AD.
 */

public class SukhumvitEditText extends AppCompatEditText {

    Typeface tf;

    public SukhumvitEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
    }

    public SukhumvitEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
    }

    public SukhumvitEditText(Context context) {
        super(context);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/sukhumvitset.ttf");
        this.setTypeface(tf);
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
