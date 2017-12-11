package com.example.rudnev.remindme;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateItemDialog extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditTextTitle;
    private EditText mEditTextNote;
    private Button mOkBtn;
    private Button mCloseBtn;
    private Calendar date;
    private Date formatDate;
    private long itemID;
    private boolean fromEditDialog;

    public interface EditNameDialogListener {
        void onFinishEditDialog(long itemID, String inputText, String note, Date date, boolean fromEditDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_item_dialog, container);
        //date = CalendarDay.today();
        date = Calendar.getInstance();

        mEditTextTitle = (EditText) view.findViewById(R.id.titleText);
        mEditTextNote = (EditText) view.findViewById(R.id.noteText);
        mOkBtn = (Button) view.findViewById(R.id.createBtn);
        mCloseBtn = (Button) view.findViewById(R.id.closeBtn);

        mEditTextTitle.setBackgroundResource(R.drawable.edit_text_bg);
        mEditTextNote.setBackgroundResource(R.drawable.edit_text_bg);
        if(getArguments()!=null){
            mEditTextTitle.setText(getArguments().getString("title"));
            mEditTextNote.setText(getArguments().getString("note"));
            //formatDate = getArguments().getString("date");
            //itemID = getArguments().getLong("itemID");
            fromEditDialog = true;
        }else{
            //formatDate = date.getYear() + "/" + (date.getMonth() + 1) + "/" + date.getDay();
            formatDate = date.getTime();
            fromEditDialog = false;
        }

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getArguments()!=null) {
                    Intent intent = new Intent();
                    intent.putExtra("title", mEditTextTitle.getText().toString());
                    intent.putExtra("note", mEditTextNote.getText().toString());
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                }else{
                    EditNameDialogListener activity = (EditNameDialogListener) getActivity();
                    activity.onFinishEditDialog(itemID, mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), formatDate, fromEditDialog);
                }
                dismiss();
            }
        });

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        long pseudoID = 0;
        if (EditorInfo.IME_ACTION_DONE == i) {
            EditNameDialogListener activity = (EditNameDialogListener) getActivity();
            if(getArguments()!=null){
                pseudoID = getArguments().getLong("itemID");
            }
            activity.onFinishEditDialog(pseudoID, mEditTextTitle.getText().toString(), mEditTextNote.getText().toString(), formatDate, fromEditDialog);
            this.dismiss();
            return true;
        }
        return false;
    }
}
