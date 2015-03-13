package com.example.sofiya.smartshoppinglist.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.example.sofiya.smartshoppinglist.R;
import com.example.sofiya.smartshoppinglist.fragments.CreateSearchDialogFragment;
import com.example.sofiya.smartshoppinglist.fragments.ShoppingListFragment;


public class ListsActivity extends ActionBarActivity implements CreateSearchDialogFragment.ComposeSearchDialogListener {

    private ShoppingListFragment mShoppingListFragment;
    private CreateSearchDialogFragment mComposeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lists);

        ShoppingListFragment shoppingListFragment = new ShoppingListFragment();
        Bundle args = new Bundle();
//        if (getIntent()!=null) {
//            args.putString("shoppingitem", getIntent().getExtras().get("shoppingitem").toString());
//            shoppingListFragment.setArguments(args);
//        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.shopping_list_placeholder, shoppingListFragment, "shoppinglistfragment").addToBackStack("shoppinglistfragment").commit();
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        if (inputText.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.edit_empty_string_error), Toast.LENGTH_SHORT).show();
        } else {
//            createSearch(inputText);
//            mShoppingListFragment.clearAll();
//            makeRequest(0);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getFragmentManager().getBackStackEntryCount() == 0) finish();
            }
        });
        super.onBackPressed();
    }

    private void showComposeDialog() {
        mComposeDialog = CreateSearchDialogFragment.newInstance(getResources().getString(R.string.compose_search));
        mComposeDialog.show(getSupportFragmentManager(), "fragment_compose_search");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

