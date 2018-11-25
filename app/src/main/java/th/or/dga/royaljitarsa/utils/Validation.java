package th.or.dga.royaljitarsa.utils;

import android.text.TextUtils;

public class Validation {

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void passwordValidation() {

    }

}
