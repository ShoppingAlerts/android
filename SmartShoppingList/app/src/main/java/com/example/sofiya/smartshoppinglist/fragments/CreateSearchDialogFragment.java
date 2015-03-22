package com.example.sofiya.smartshoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sofiya.smartshoppinglist.R;

/**
 * Created by sofiya on 3/13/15.
 */
public class CreateSearchDialogFragment extends DialogFragment implements TextView.OnEditorActionListener, View.OnClickListener{
    private EditText mEditText;

    private TextView mPrefilledKeywords;
    private TextView mPrefilledItemTitle;
    private Button mDoneButton;

    public static CreateSearchDialogFragment newInstance(String title, String keywords) {
        CreateSearchDialogFragment createSearchDialogFragment = new CreateSearchDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("prefill", keywords);
        createSearchDialogFragment.setArguments(args);
        return createSearchDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.compose_dialog, container, false);
        mEditText = (EditText) v.findViewById(R.id.txt_new_content);
        mPrefilledKeywords = (TextView) v.findViewById(R.id.original_keywords);
        mPrefilledItemTitle = (TextView) v.findViewById(R.id.original_item_title);
        mDoneButton = (Button) v.findViewById(R.id.done_button);
        String title = getArguments().getString("title", "");
        String prefill = getArguments().getString("prefill", "");

        getDialog().setTitle(getResources().getString(R.string.create_alert_for));

        mPrefilledItemTitle.setText(title);
        mPrefilledItemTitle.setFocusable(true);

        mPrefilledKeywords.setText(prefill);
        mPrefilledKeywords.setFocusable(true);

        mPrefilledItemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.clearFocus();
                mEditText.setAlpha((float) 0.5);
                mEditText.setTextColor(getResources().getColor(R.color.fifty_percent_edit_text_dark));
                v.setFocusable(true);
                v.setSelected(true);
                mPrefilledItemTitle.setAlpha(1);
                mPrefilledKeywords.setAlpha(1);
                mPrefilledKeywords.setSelected(false);
            }
        });
        mPrefilledKeywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setAlpha((float) 0.5);
                mEditText.setTextColor(getResources().getColor(R.color.fifty_percent_edit_text_dark));
                mEditText.clearFocus();
                v.setSelected(true);
                mPrefilledItemTitle.setAlpha(1);
                mPrefilledKeywords.setAlpha(1);
                mPrefilledItemTitle.setSelected(false);
            }
        });
        mEditText.setOnEditorActionListener(this);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPrefilledKeywords.setAlpha((float) 0.5);
                    mPrefilledItemTitle.setAlpha((float) 0.5);
                    mEditText.setAlpha((float) 1);
                    mEditText.setTextColor(getResources().getColor(R.color.edit_text_dark));
//                    mPrefilledKeywords.setEnabled(false);
//                    mPrefilledItemTitle.setEnabled(false);
                }
//                else if (v.equals(mPrefilledItemTitle) || v.equals(mPrefilledKeywords)){
//                    mPrefilledKeywords.setAlpha((float) 1);
//                    mPrefilledItemTitle.setAlpha((float) 1);
//                    mEditText.setAlpha((float)0.8);
////                    mPrefilledKeywords.setEnabled(true);
////                    mPrefilledItemTitle.setEnabled(true);
//                }
            }
        });
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyContentChanges();
            }
        });

        return v;
    }

    private void applyContentChanges() {
        ComposeSearchDialogListener listener = (ComposeSearchDialogListener) getActivity();

        listener.onFinishEditDialog(mEditText.getText().toString());
//        getActivity().dismissKeyboard();
        dismiss();

    }

    public interface ComposeSearchDialogListener {
        void onFinishEditDialog(String inputText);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
