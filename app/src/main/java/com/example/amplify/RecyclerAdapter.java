package com.example.amplify;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Data> mData = new ArrayList<>();


    // 아이템 뷰를 저장하는 viewholder 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;     // 영화이름
        TextView textView2;     // 누적관객수
        TextView textView3;     // 영화개봉일



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);

        }

        void onBind(Data data1) {
            textView1.setText(data1.getimage());
            textView2.setText(data1.gettime());
            textView3.setText(data1.getserial());
        }
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 return
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_recycler_view, parent, false);
        RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {

        holder.onBind(mData.get(position));

    }


    // 사이즈
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // data 모델의 객체들을 list에 저장
    public void setmovieList(ArrayList<Data> list) {
        this.mData = list;
        notifyDataSetChanged();
    }
}