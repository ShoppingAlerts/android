package com.sophiataskova.apps.smartshoppinglist.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateSearchDialogFragment extends DialogFragment implements TextView.OnEditorActionListener, View.OnClickListener{
    private EditText mEditText;
    private Button mDoneButton;

    static CreateSearchDialogFragment newInstance(String title) {
        CreateSearchDialogFragment createSearchDialogFragment = new CreateSearchDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
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
        View v = inflater.inflate(com.sophiataskova.apps.smartshoppinglist.R.layout.compose_dialog, container, false);
        mEditText = (EditText) v.findViewById(com.sophiataskova.apps.smartshoppinglist.R.id.txt_new_content);
        mDoneButton = (Button) v.findViewById(com.sophiataskova.apps.smartshoppinglist.R.id.done_button);
        String title = getArguments().getString("title", getResources().getString(com.sophiataskova.apps.smartshoppinglist.R.string.compose_search));
        String prefill = getArguments().getString("prefill", "");

        getDialog().setTitle(title);
        mEditText.setText(prefill);
        mEditText.requestFocus();
        mEditText.setOnEditorActionListener(this);
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
