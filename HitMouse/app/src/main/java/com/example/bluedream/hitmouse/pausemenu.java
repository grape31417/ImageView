package com.example.bluedream.hitmouse;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by USER on 2018/1/24.
 */

public class pausemenu implements DialogInterface.OnCancelListener, View.OnClickListener {
    @BindView(R.id.resume)
    ImageButton resume;
    @BindView(R.id.set)
    ImageButton set;
    @BindView(R.id.exit)
    ImageButton exit;
    private Context mContext;
    private int mResId;
    private Dialog mDialog;
    private ImageView dialog_image;
    private Button dialog_btn;

    pausemenu(Context context) {
        this.mContext = context;
    }


    public pausemenu show() {
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.pausemenu);
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);


        mDialog.setOnCancelListener(this);
        mDialog.show();

        return this;
    }


    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    @Override
    public void onClick(View view) {

    }

    @OnClick({R.id.resume, R.id.set, R.id.exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.resume:
                mDialog.dismiss();
                break;
            case R.id.set:
                break;
            case R.id.exit:
                break;
        }
    }
}
