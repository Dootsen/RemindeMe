package com.example.rudnev.remindme.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.CreateItemDialog;
import com.example.rudnev.remindme.MainActivity;
import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.adapter.RemindListAdapter;
import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.sql.RemindDBAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TodayFragment extends AbstractTabFragment implements RemindItemClickListener,
        TabFragmentAdapter.TabSelectedListener, AbstractTabFragment.UpdateFragmentsLists,
        CreateItemDialog.EditNameDialogListener{

    private static final int LAYOUT = R.layout.today_fragment;
    private static final int REQUEST_TODAY = 1;


    private FloatingActionButton fab;
    private List<RemindDTO> datas;
    private RemindListAdapter adapter;
    RecyclerView rv;
    private RemindDBAdapter dbAdapter;
    private long mItemID;

    public static TodayFragment getInstance(Context context, List<RemindDTO> datas){

        Bundle args = new Bundle();
        TodayFragment todayFragment = new TodayFragment();
        todayFragment.setArguments(args);
        todayFragment.setData(datas);
        todayFragment.setContext(context);
        todayFragment.setTitle(context.getString(R.string.today_tab));
        return todayFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new RemindDBAdapter(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(LAYOUT, container, false);

        initFAB(view);
        rv = (RecyclerView)view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RemindListAdapter(datas, this);
        rv.setAdapter(adapter);
        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public void setData(List<RemindDTO> data) {
        this.datas = data;
    }

    private void initFAB(View view) {
        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.setTargetFragment(TodayFragment.this, REQUEST_TODAY);
        createItemDialog.show(fm, "create_item_dialog");
    }

    @Override
    public void remindListRemoveClicked(View v, int position) {
        dbAdapter = new RemindDBAdapter(context);
        dbAdapter.removeItem(datas.get(position).getId());
        update();
        ((MainActivity)getActivity()).updateTabFragmentList();

    }

    @Override
    public void remindListUpdateClicked(View v, int position) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.setTargetFragment(this, REQUEST_TODAY);
        Bundle args = new Bundle();
        mItemID = datas.get(position).getId();
        args.putString("title", datas.get(position).getTitle());
        args.putString("note", datas.get(position).getNote());
        //args.putString("date", data.get(position).getDate());
        //args.putLong("itemID", datas.get(position).getId());
        createItemDialog.setArguments(args);
        createItemDialog.show(fm, "create_item_dialog");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String title = data.getStringExtra("title");
            String note = data.getStringExtra("note");
            Date date = new Date();
            date.setTime(data.getLongExtra("date", 0));
            CreateItemDialog.EditNameDialogListener activity = (CreateItemDialog.EditNameDialogListener) getActivity();
            activity.onFinishEditDialog(mItemID, title, note, date, true);
            //int weight = data.getIntExtra(WeightDialogFragment.TAG_WEIGHT_SELECTED, -1);
        }
    }

    @Override
    public void popupMenuItemClicked(final View view, final int position) {
        // pass the imageview id
        View menuItemView = view.findViewById(R.id.ib_popup_menu);
        PopupMenu popup = new PopupMenu(view.getContext(), menuItemView);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.popup_cardview_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        remindListUpdateClicked(view, position);
                        break;
                    case R.id.delete:
                        remindListRemoveClicked(view, position);
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onFragmentBecomesCurrent(boolean current) {

        if(adapter!=null){
            adapter.setData(datas);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void update() {
        dbAdapter = new RemindDBAdapter(context);
        datas = dbAdapter.getAllItems(1, null);
        setData(datas);
        if(adapter!=null){
            adapter.setData(datas);
            adapter.notifyDataSetChanged();
        }else{
            adapter = new RemindListAdapter(datas, this);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFinishEditDialog(long itemID, String inputText, String note, Date date, boolean fromEditDialog) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        if(fromEditDialog){
            dbAdapter.updateItem(itemID, inputText, note, sdf.format(date));
        }else{
            dbAdapter.addItem(inputText, note, sdf.format(date));
        }
        //adapter.setDatas(dbAdapter.getAllItems(1, date));
        ((MainActivity)getActivity()).updateTabFragmentList();
        adapter.notifyDataSetChanged();
    }
}
