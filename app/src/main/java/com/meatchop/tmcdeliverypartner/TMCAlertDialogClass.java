package com.meatchop.tmcdeliverypartner;
/*$Id$*/

//import android.support.v7.app.AlertDialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Gravity;
import android.widget.TextView;

public class TMCAlertDialogClass extends AlertDialog.Builder {
    public TMCAlertDialogClass(Context context, int title, int message, int btn1Caption, int btn2Caption, final AlertListener listener) {
        super(context, AlertDialog.THEME_HOLO_LIGHT);
     /* if (title != 0) {
            setTitle(context.getResources().getString(title));
        } */
        TextView titleView = new TextView(context);
        titleView.setPadding(30,40,30,40);
        titleView.setText(message);
        titleView.setTextColor(context.getResources().getColor(android.R.color.black));

        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(17);
        setView(titleView);
        //setMessage(context.getResources().getString(message));
        setPositiveButton(context.getResources().getString(btn1Caption), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onYes();
            }
        });

        if (btn2Caption != 0) {
            setNegativeButton(context.getResources().getString(btn2Caption), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onNo();
                }
            });
        }
        show();
    }

    public TMCAlertDialogClass(Context context, String title, String message, String btn1Caption, String btn2Caption, final AlertListener listener) {
        super(context, AlertDialog.THEME_HOLO_LIGHT);


        // super(context, R.style.AlertDialogTheme1);
     /* if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        } */

        TextView titleView = new TextView(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            titleView.setPadding(30, 40, 30, 40);
        } else {
            titleView.setPadding(10,20,10,10);
        }
        titleView.setText(message);
        if (message.length() > 50) {
            titleView.setGravity(Gravity.LEFT);
        } else {
            titleView.setGravity(Gravity.CENTER);
        }

        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.GINGERBREAD_MR1){
            titleView.setTextColor(context.getResources().getColor(android.R.color.black));
        }else{
            titleView.setTextColor(context.getResources().getColor(android.R.color.white));
        }
        titleView.setTextSize(17);
        setView(titleView);
        //setMessage(message);
        setPositiveButton(btn1Caption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onYes();
            }
        });

        if (!btn2Caption.equals("")) {
            setNegativeButton(btn2Caption, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onNo();
                }
            });
        }
        show();
    }

    public TMCAlertDialogClass(Context context, int title, int message, int btn1Caption, int btn2Caption, int btn3Caption, final AlertListener2 listener) {
        super(context);
     /* if (title != 0) {
            setTitle(context.getResources().getString(title));
        } */
        TextView titleView = new TextView(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            titleView.setPadding(30,40,30,40);
        } else {
            titleView.setPadding(10,20,10,10);
        }
        titleView.setText(message);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            titleView.setTextColor(context.getResources().getColor(android.R.color.black));
        } else {
            titleView.setTextColor(context.getResources().getColor(android.R.color.white));
        }

        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(16);
        setView(titleView);
        setPositiveButton(context.getResources().getString(btn1Caption), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onYes();
            }
        });

        if (btn2Caption != 0) {
            setNegativeButton(context.getResources().getString(btn2Caption), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onNo();
                }
            });
        }

        if (btn3Caption != 0) {
            setNeutralButton(context.getResources().getString(btn3Caption), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onNeutral();
                }
            });
        }
        show();
    }
    public interface AlertListener {
        public void onYes();

        public void onNo();
    }

    public interface AlertListener2 {
        public void onYes();

        public void onNo();

        public void onNeutral();
    }
}