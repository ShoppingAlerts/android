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
import android.widget.Toast;

import com.example.sofiya.smartshoppinglist.R;
import com.example.sofiya.smartshoppinglist.activities.IntroActivity;

/**
 * Created by sofiya on 3/13/15.
 */
public class CreateSearchDialogFragment extends DialogFragment implements TextView.OnEditorActionListener, View.OnClickListener{
    private EditText mEditText;

    private String mAlertKeywords;
    private boolean isCustomSearch;

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
        mDoneButton.setFocusable(true);

        mPrefilledKeywords.setText(prefill);
        mPrefilledKeywords.setFocusable(true);

        mPrefilledItemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCustomSearch = false;
                mAlertKeywords = mPrefilledItemTitle.getText().toString();
                enablePrefills();
                v.setSelected(true);
                mPrefilledKeywords.setSelected(false);
            }
        });
        mPrefilledKeywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCustomSearch = false;
                mAlertKeywords = mPrefilledKeywords.getText().toString();
                enablePrefills();
                v.setSelected(true);
                mPrefilledItemTitle.setSelected(false);
            }
        });
        mEditText.setOnEditorActionListener(this);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isCustomSearch = true;
                    disablePrefills();
                    mEditText.setAlpha((float) 1);
                    mEditText.setTextColor(getResources().getColor(R.color.edit_text_dark));
                }
                else {
                    ((IntroActivity)getActivity()).dismissKeyboard(mEditText);
                    if (isCustomSearch) {
                        mAlertKeywords = mEditText.getText().toString();
                    }
                }
            }
        });
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.clearFocus();
                if (mAlertKeywords == null) {
                    Toast.makeText(getActivity(), "Please set a keyword", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getActivity(), mAlertKeywords, Toast.LENGTH_SHORT).show();

//                applyContentChanges();
            }
        });

        return v;
    }

    private void disablePrefills() {
        mPrefilledKeywords.setAlpha((float) 0.5);
        mPrefilledItemTitle.setAlpha((float) 0.5);
    }

    private void enablePrefills() {
        mEditText.clearFocus();
        mEditText.setAlpha((float) 0.5);
        mEditText.setTextColor(getResources().getColor(R.color.fifty_percent_edit_text_dark));
        mPrefilledKeywords.setAlpha(1);
        mPrefilledItemTitle.setAlpha(1);
    }

    private void applyContentChanges() {
        ComposeSearchDialogListener listener = (ComposeSearchDialogListener) getActivity();

        listener.onFinishEditDialog(mEditText.getText().toString());
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
