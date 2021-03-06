package com.example.rudnev.remindme.adapter;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.dto.RemindDTO;

import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TodayListAdapter extends RecyclerView.Adapter<TodayListAdapter.RemindViewHolder> {

    private List<RemindDTO> data;
    private static RemindItemClickListener itemClickListener;

    public TodayListAdapter(List<RemindDTO> data, RemindItemClickListener remindItemClickListener/*,Cursor cursor*/){
        this.data = data;
        itemClickListener = remindItemClickListener;
    }

    public TodayListAdapter(RemindItemClickListener remindItemClickListener){
        itemClickListener = remindItemClickListener;

    }

    @Override
    public RemindViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_item, parent, false);
        return new RemindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RemindViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        holder.title.setText(data.get(position).getTitle());
        holder.date.setText(sdf.format(data.get(position).getDate().toDate()));
    }


    @Override
    public int getItemCount() {
        if(data!=null)
            return data.size();
        else
            return 0;
    }

    public static class RemindViewHolder extends CommonViewHolder implements View.OnClickListener{

        CardView cardView;
        TextView title;
        TextView date;
        public RelativeLayout viewBackground, viewForeground;

        public RemindViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);

            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

        }
        @Override
        public void onClick(View v)
        {
            itemClickListener.remindListOpenClicked(v, this.getLayoutPosition());
        }

        @Override
        public View getForegroundView() {
            return viewForeground;
        }

        @Override
        public View getBackgroundView() {
            return viewBackground;
        }
    }

    public void setData(List<RemindDTO> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public RemindDTO getItemById(int id){
        return data.get(id);
    }

    public String getTitle(int position){

        return data.get(position).getTitle();
    }
    public String getNote(int position){

        return data.get(position).getNote();
    }
    public LocalDateTime getDate(int position){

        return data.get(position).getDate();
    }

}
