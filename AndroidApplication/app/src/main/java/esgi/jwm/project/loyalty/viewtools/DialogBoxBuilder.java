package esgi.jwm.project.loyalty.viewtools;

import android.app.AlertDialog;
import android.content.Context;
import android.nfc.Tag;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import esgi.jwm.project.loyalty.R;
import esgi.jwm.project.loyalty.fragments.IBasicDialogCallBack;

/**
 * Created by wmorgado on 09/03/2018.
 */

public abstract class DialogBoxBuilder {


    
    public static final int BASIC_DIALOG = 1;
    public static final int NO_YES_DIALOG = 2;


    public DialogBoxBuilder() {
    }

    public static AlertDialog build(Context context, String dialogBoxTitle,
                             String dialogBoxMessage, String button1Label,
                             String button2Label, LayoutInflater layoutInflater,
                             int typeDialog, IBasicDialogCallBack dialogCallBack){


        View view = getViewByDialogType(typeDialog, layoutInflater);

        if(view != null){

//            Instantiate the dialogBox Object
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();

//            setup the View (set text, onclickListener and callback
            setupView(view, dialogBoxTitle, dialogBoxMessage, button1Label, button2Label, typeDialog, dialogCallBack, alertDialog);

//            attach the view to the dialogBox Object
            alertDialog.setView(view);



            return alertDialog;
        } else {
            return null;
        }


    }

    public static View getViewByDialogType(int typeDialog, LayoutInflater layoutInflater){

        switch (typeDialog){
            case BASIC_DIALOG:
                return layoutInflater.inflate(R.layout.basic_dialog, null);
            case NO_YES_DIALOG:
                return layoutInflater.inflate(R.layout.no_yes_dialog, null);
            default:
                return null;
        }
    }

    public static void setupView(View view, String dialogBoxTitle,
                     String dialogBoxMessage, String button1Label,
                     String button2Label, int typeDialog,
                     IBasicDialogCallBack dialogCallBack, AlertDialog alertDialog){

        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);
        button1.setText(button1Label);
        button2.setText(button2Label);

        TextView dialogMessage = view.findViewById(R.id.dialogMessage);
        TextView dialogTitle = view.findViewById(R.id.titleDialog);
        dialogMessage.setText(dialogBoxMessage);
        dialogTitle.setText(dialogBoxTitle);

        button1.setOnClickListener(v -> dialogCallBack.onClickButton1(v, alertDialog));


        button2.setOnClickListener(v -> {
            if(typeDialog == BASIC_DIALOG){
                TextInputEditText textInputEditText = view.findViewById(R.id.email);
                String mail =  String.valueOf(textInputEditText.getText());
                dialogCallBack.onClickButton2(v, mail, alertDialog);
            }
            if(typeDialog == NO_YES_DIALOG){
                dialogCallBack.onClickButton2(v, "", alertDialog);
            }

        });
    }


}
