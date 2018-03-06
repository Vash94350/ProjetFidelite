package esgi.jwm.project.loyalty.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import esgi.jwm.project.loyalty.R;


public abstract class BasicDialog {
    public static AlertDialog create(String msg, String msgbtn1, String msgbtn2, View v , Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//        v.findViewById(R.id.button)
        TextView textView = v.findViewById(R.id.dialogMessage);
        textView.setText(msg);

        Button button1 = v.findViewById(R.id.buttonOk);
        button1.setText(msgbtn1);

        Button button2 = v.findViewById(R.id.buttonCancel);
        button2.setText(msgbtn2);
        return null;
    }
}
