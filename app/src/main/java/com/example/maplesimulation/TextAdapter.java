package com.example.maplesimulation;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//참고:https://recipes4dev.tistory.com/154
public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder>
implements OnItemClickListener{

    private ArrayList<String> mData = null ;
    private OnItemClickListener mListener;

    @Override
    public void onItemClick(ViewHolder viewHolder, View view, int position) {
        if(mListener != null){
            mListener.onItemClick(viewHolder, view, position);
        }
    }

    public void setOnItemClicklistener(OnItemClickListener listener){
        this.mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView ;

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView = itemView.findViewById(R.id.recyclerText) ;

            // 이벤트리스너 추가
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(ViewHolder.this, v, pos);
                    }
                }
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    TextAdapter(ArrayList<String> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public TextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false) ;
        TextAdapter.ViewHolder viewHolder = new TextAdapter.ViewHolder(view, this) ;

        return viewHolder;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(TextAdapter.ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.textView.setText(text) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
