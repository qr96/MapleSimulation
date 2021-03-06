package com.example.maplesimulation;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

public class CustomNotice extends Dialog {

    private Context context;
    private TextView title, content;
    private Button button;

    public CustomNotice(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notice);

        title = findViewById(R.id.popupTitle);
        content = findViewById(R.id.popupContent);
        button = findViewById(R.id.popupClose);

        title.setVisibility(View.GONE);

        Typeface face = ResourcesCompat.getFont(context, R.font.nexon_low);
        //textView.setTypeface(face);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setTitle(String text) {
        title.setVisibility(View.VISIBLE);
        title.setText(text);
    }

    public void setContent(String text) {
        if(text.length()>220) {
            ScrollView scrollView = findViewById(R.id.noticeScrollview);
            ViewGroup.LayoutParams params = scrollView.getLayoutParams();
            params.height = 1300;
            scrollView.setLayoutParams(params);
        }
        content.setText(text);
    }
}
