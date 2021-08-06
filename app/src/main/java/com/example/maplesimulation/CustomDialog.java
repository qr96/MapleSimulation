package com.example.maplesimulation;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

public class CustomDialog extends Dialog {

    private Context context;
    private CustomDialogClickListener clickListener;
    private TextView textView;
    private Button positive, negative;

    public CustomDialog(@NonNull Context context, CustomDialogClickListener clickListener) {
        super(context);
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);

        textView = findViewById(R.id.customDialogMessage);
        positive = findViewById(R.id.customDialogPositive);
        negative = findViewById(R.id.customDialogNegative);

        Typeface face = ResourcesCompat.getFont(context, R.font.nexon_low);
        textView.setTypeface(face);
        positive.setTypeface(face);
        negative.setTypeface(face);

        positive.setOnClickListener(v -> {
            this.clickListener.onPositiveClick();
            dismiss();
        });

        negative.setOnClickListener(v -> {
            this.clickListener.onNegativeClick();
            dismiss();
        });
    }

    public void setMessage(String text) {
        textView.setText(text);
    }
}
