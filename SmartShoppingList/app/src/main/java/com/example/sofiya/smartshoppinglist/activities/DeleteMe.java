package com.example.sofiya.smartshoppinglist.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.example.sofiya.smartshoppinglist.R;
import com.example.sofiya.smartshoppinglist.fragments.CreateSearchDialogFragment;
import com.example.sofiya.smartshoppinglist.fragments.ShoppingListFragment;
import com.example.sofiya.smartshoppinglist.models.SearchItem;

import static com.example.sofiya.smartshoppinglist.activities.IntroActivity.persistSearch;


public class DeleteMe extends android.support.v4.app.FragmentActivity implements CreateSearchDialogFragment.ComposeSearchDialogListener {

    public static final int INTRO_CODE = 123;
    private ShoppingListFragment mShoppingListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_lists);

        mShoppingListFragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        if (getIntent().hasExtra("shoppingitem")) {
            args.putString("shoppingitem", getIntent().getExtras().get("shoppingitem").toString());
            mShoppingListFragment.setArguments(args);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.shopping_list_placeholder, mShoppingListFragment, "shoppinglistfragment").addToBackStack(null).commit();
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        if (inputText.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.edit_empty_string_error), Toast.LENGTH_SHORT).show();
        } else {

            persistSearch(new SearchItem(inputText));
            mShoppingListFragment.retrieveSearchesFromDB();
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}

