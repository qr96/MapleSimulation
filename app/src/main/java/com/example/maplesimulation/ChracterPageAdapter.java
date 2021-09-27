package com.example.maplesimulation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ChracterPageAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Character> characterArrayList;

    public ChracterPageAdapter(Context context, ArrayList<Character> characterArrayList)
    {
        this.mContext = context;
        this.characterArrayList = characterArrayList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_character_info, null);

        TextView textView = view.findViewById(R.id.character_info_number);
        textView.setText(position+1+"/"+getCount());

        TextView textView1 = view.findViewById(R.id.character_info);
        textView1.setText(characterArrayList.get(position).getInfo());

        container.addView(view);

        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return characterArrayList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View)o);
    }
}
