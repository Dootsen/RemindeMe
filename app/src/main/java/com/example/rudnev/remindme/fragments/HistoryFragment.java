package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.adapter.RemindListAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.history_fragment;

    private List<RemindDTO> data;
    private RemindListAdapter adapter;

    public static HistoryFragment getInstance(Context context, List<RemindDTO> datas){

        Bundle args = new Bundle();
        HistoryFragment historyFragment = new HistoryFragment();
        historyFragment.setArguments(args);
        historyFragment.setData(datas);
        historyFragment.setContext(context);
        historyFragment.setTitle(context.getString(R.string.history_tab));
        return historyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RemindListAdapter(data);
        rv.setAdapter(adapter);
        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    public void setData(List<RemindDTO> data) {
        this.data = data;
    }

    public void refreshData(List<RemindDTO>data){
        //adapter = new RemindListAdapter(data);
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }
}
