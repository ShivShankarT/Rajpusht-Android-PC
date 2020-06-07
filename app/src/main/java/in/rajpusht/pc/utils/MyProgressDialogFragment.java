package in.rajpusht.pc.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;


import java.util.Objects;

import in.rajpusht.pc.R;

public class MyProgressDialogFragment extends Dialog implements DialogInterface.OnDismissListener {


    private ProgressBar progress_bar;

    public MyProgressDialogFragment(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loader);
        progress_bar = findViewById(R.id.progress_bar);
        setCancelable(false);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        setOnDismissListener(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        progress_bar.setVisibility(View.GONE);
    }
}


