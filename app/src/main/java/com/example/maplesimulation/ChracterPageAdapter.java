package com.example.maplesimulation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        String info = "이름 : " + characterArrayList.get(position).getName() + "\n" +
                "레벨 : " + characterArrayList.get(position).getLevel() + "\n" +
                "직업 : " + characterArrayList.get(position).getJob();
        textView1.setText(info);

        ImageView imageview = view.findViewById(R.id.job_thumnail);
        int rid = R.drawable.job_newbie;
        String job = characterArrayList.get(position).getJob();
        if(job.equals("아델")) rid = R.drawable.job_adele;
        else if(job.equals("호영")) rid = R.drawable.job_hoyoung;
        else if(job.equals("패스파인더")) rid = R.drawable.job_pathfinder;
        imageview.setImageResource(rid);

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
