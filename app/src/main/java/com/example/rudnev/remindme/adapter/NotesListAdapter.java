package com.example.rudnev.remindme.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.dto.Notes;

import java.util.ArrayList;
import java.util.List;


public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteViewHolder> implements Filterable {
    private List<Notes> data;
    private List<Notes> filteredData;
    private static RemindItemClickListener itemClickListener;

    public NotesListAdapter(RemindItemClickListener remindItemClickListener){
        itemClickListener = remindItemClickListener;
    }

    @Override
    public NotesListAdapter.NoteViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_item, parent, false);
        return new NotesListAdapter.NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesListAdapter.NoteViewHolder holder, int position) {

        holder.title.setText(data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if(data!=null)
            return data.size();
        else
            return 0;
    }

    public static class NoteViewHolder extends CommonViewHolder implements View.OnClickListener{

        CardView cardView;
        TextView title;
        TextView date;
        public RelativeLayout viewBackground, viewForeground;

        public NoteViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);

            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }

        @Override
        public View getForegroundView() {
            return viewForeground;
        }

        @Override
        public View getBackgroundView() {
            return viewBackground;
        }

        @Override
        public void onClick(View v)
        {
            itemClickListener.remindListOpenClicked(v, this.getLayoutPosition());
        }

    }

    public void setData(List<Notes> data) {
        this.data = data;
        this.filteredData = data;
        notifyDataSetChanged();
    }

    public Notes getItemById(int id){
        return filteredData.get(id);
    }

    public String getTitle(int position){

        return filteredData.get(position).getTitle();
    }
    public String getNote(int position){

        return filteredData.get(position).getNote();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredData = data;
                } else {
                    List<Notes> filteredList = new ArrayList<>();
                    for (Notes row : data) {

                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredData = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredData = (ArrayList<Notes>) filterResults.values;
                setData(filteredData);
            }
        };
    }

}