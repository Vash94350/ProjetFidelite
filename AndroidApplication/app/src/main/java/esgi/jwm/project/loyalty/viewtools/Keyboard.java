package esgi.jwm.project.loyalty.viewtools;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by wmorgado on 09/03/2018.
 */

public abstract class Keyboard {
    public static void hide(Context context, View v){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }
}
