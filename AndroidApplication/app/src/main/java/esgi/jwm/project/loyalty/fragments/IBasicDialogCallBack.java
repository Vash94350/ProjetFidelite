package esgi.jwm.project.loyalty.fragments;

import android.app.AlertDialog;
import android.view.View;

/**
 * Created by wmorgado on 07/03/2018.
 */

public interface IBasicDialogCallBack {
    void onClickButton1(View view, AlertDialog dialogBox);
    void onClickButton2(View view, String res, AlertDialog dialogBox);
}
