package com.example.sofiya.smartshoppinglist.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.robototextview.widget.RobotoTextView;
import com.example.sofiya.smartshoppinglist.EbayItemsArrayAdapter;
import com.example.sofiya.smartshoppinglist.EbayRequests;
import com.example.sofiya.smartshoppinglist.R;
import com.example.sofiya.smartshoppinglist.activities.IntroActivity;
import com.example.sofiya.smartshoppinglist.models.EbayItem;
import com.example.sofiya.smartshoppinglist.models.SearchItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultsListFragment extends Fragment {

    protected EbayItemsArrayAdapter mEbayItemsArrayAdapter;
    private ArrayList<EbayItem> ebayItems;
    private ListView lvResultsList;
    private EditText keywordsEditText, filterEditText;
//    private Button saveButton;
    protected SwipeRefreshLayout mSwipeContainer;
    protected View mCategories, mClearEditText, mProgressBar;
    protected RobotoTextView mBreadCrumb;

    protected View mFashion, mHome, mCollectibles, mMotors, mCamping, mElectronics, mPlainTextSearch;

    protected ImageButton mBackButton;
    private String filter;

    public static SearchItem sItemToAdd;

    private boolean addingItem = false;
    private int mCurrentPage;




    private String keywords;
    private String mPaginatedUrl;

    public static String sBestPrice;
    public static String sBestPriceUrl;
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static ResultsListFragment newInstance(int page, String title) {
        ResultsListFragment fragmentFirst = new ResultsListFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ebayItems = new ArrayList<EbayItem>();
//        startup = true;

        mEbayItemsArrayAdapter = new EbayItemsArrayAdapter(getActivity(), ebayItems);
        mCurrentPage = 1;
        if (getArguments()!=null) {
            keywords = getArguments().getString("shoppingitem");
            filter = getArguments().getString("pricefilter");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("debug","oncreateview resultslistfragment");
        View v = inflater.inflate(R.layout.fragment_results_list, container, false);
        prepareEditText(v);
        prepareFilters(v);
        mSwipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        mCategories = v.findViewById(R.id.categories);
        mBackButton = (ImageButton) v.findViewById(R.id.back_button);
        mClearEditText = v.findViewById(R.id.calc_clear_txt_Prise);
        mBreadCrumb = (RobotoTextView) v.findViewById(R.id.breadcrumb);
        mPlainTextSearch = v.findViewById(R.id.plain_text_search);
        mProgressBar = v.findViewById(R.id.progress_bar);

        mFashion = v.findViewById(R.id.fashion);
        mFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchForCategory((IntroActivity) getActivity(), EbayRequests.sFashionCategoryId, "Fashion");
            }
        });
        mCollectibles = v.findViewById(R.id.collectibles);
        mCollectibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchForCategory((IntroActivity) getActivity(), EbayRequests.sCollectiblesCategoryId, "Collectibles");

            }
        });
        mCamping = v.findViewById(R.id.camping);
        mCamping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchForCategory((IntroActivity) getActivity(), EbayRequests.sCampingCategoryId, "Camping");

            }
        });
        mElectronics = v.findViewById(R.id.electronics);
        mElectronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchForCategory((IntroActivity) getActivity(), EbayRequests.sElectronicsCategoryId, "Electronics");

            }
        });
        mHome = v.findViewById(R.id.home);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchForCategory((IntroActivity) getActivity(), EbayRequests.sHomeCategoryId, "Home");

            }
        });
        mMotors = v.findViewById(R.id.motors);
        mMotors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchForCategory((IntroActivity) getActivity(), EbayRequests.sMotorsCategoryId, "Motors");

            }
        });

        final Animation enterAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.enter);
        final Animation leaveAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.leave);

        mClearEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordsEditText.setText("");
                keywords = "";
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategories.setVisibility(View.VISIBLE);
                mCategories.startAnimation(enterAnimation);
                mSwipeContainer.startAnimation(leaveAnimation);
                mSwipeContainer.setVisibility(View.GONE);
                mBackButton.setVisibility(View.GONE);
                mBreadCrumb.setVisibility(View.GONE);
                mPlainTextSearch.setVisibility(View.VISIBLE);
            }
        });
//        saveButton = (Button) v.findViewById(R.id.save_search);
        lvResultsList = (ListView) v.findViewById(R.id.lv_results_list);
        lvResultsList.setAdapter(mEbayItemsArrayAdapter);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");

        // Applying font
        keywordsEditText.setTypeface(tf);
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), keywordsEditText.getText() +" selected",Toast.LENGTH_SHORT).show();
//                startSearchForKeywords((IntroActivity)getActivity() , keywordsEditText.getText().toString(), filterEditText.getText().toString());
//            }
//        });

//        lvResultsList.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                customLoadMoreDataFromApi(page);
//            }
//        });

        return v;
    }



    private void prepareFilters(View container) {
//        filterEditText = (EditText) container.findViewById(R.id.max_price_text);
//        filterEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
//        filterEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
//                    String filterText = filterEditText.getText().toString();
//                    startSearchForKeywords((IntroActivity)getActivity() , keywordsEditText.getText().toString(), filterText);
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    private void prepareEditText(View container) {
        keywordsEditText = (EditText) container.findViewById(R.id.i_need);
        keywordsEditText.setFocusableInTouchMode(true);
        keywordsEditText.requestFocus();
        keywordsEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
        keywordsEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    ((IntroActivity)getActivity()).dismissKeyboard(keywordsEditText);
                    keywords = keywordsEditText.getText().toString();
                    if (keywords.contains("\\")) {
                        Toast.makeText(getActivity(), "Couldn't run search: check your input for backslashes!", Toast.LENGTH_SHORT).show();
                    } else {
                        startSearchForKeywords((IntroActivity)getActivity(), keywordsEditText.getText().toString());
                        mSwipeContainer.setVisibility(View.VISIBLE);
                        mBackButton.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public EbayItemsArrayAdapter getmEbayItemsArrayAdapter() {
        return mEbayItemsArrayAdapter;
    }



    public void customLoadMoreDataFromApi(int offset) {
        mCurrentPage = offset + 1;
        makeRequest(offset, keywords);
    }

    public void makeRequest(final int offset, final String searchKeywords) {
//        Log.i("debug", "making request with keywords "+ searchKeywords + " because startup is " +startup);
        mEbayItemsArrayAdapter.clear();
        mCurrentPage = offset;

        mPaginatedUrl = EbayRequests.sSearchByKeywordUrl.concat(searchKeywords + EbayRequests.sPageNumberUrl + 1 + EbayRequests.sPaginationUrl + 10);
        if (filter != null) {
            mEbayItemsArrayAdapter.setPriceFilter(filter);
            mPaginatedUrl = mPaginatedUrl.concat("&itemFilter(0).name=MaxPrice&itemFilter(0).value=" + filter + "&itemFilter(0).paramName=Currency&itemFilter(0).paramValue=USD&sortOrder=PricePlusShippingLowest");
        }
        Log.i("INFO", "searchUrl is " + mPaginatedUrl);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        if (((IntroActivity)getActivity()).isNetworkAvailable()){

            asyncHttpClient.get(mPaginatedUrl, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("Debug", "error is "+ errorResponse);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    mProgressBar.setVisibility(View.GONE);
                    Log.d("DEBUG", response.toString());
                    JSONArray ebayItemsResult;
                    try {
                        ebayItemsResult = response.getJSONArray("findItemsByKeywordsResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
//                        if (mCurrentPage == 0) {
//                            imageResults.clear();
//                        }
                        if (addingItem) {
                            EbayItem bestPriceItem = null;
                            for (int i = 0; i< ebayItemsResult.length(); i++) {
                                if (EbayItem.fromJsonArray(ebayItemsResult).get(i).getUrl() != null) {
                                    bestPriceItem = EbayItem.fromJsonArray(ebayItemsResult).get(i);
                                    break;
                                }
                            }

                            if (bestPriceItem != null) {
                            sBestPrice = String.valueOf(bestPriceItem.getPrice());
                            sBestPriceUrl = bestPriceItem.getUrl();
                            addingItem = false;
                            }
                            else {
                                Toast.makeText(getActivity(), "No results for these keywords", Toast.LENGTH_SHORT).show();
                            }
                        }

                        mEbayItemsArrayAdapter.addAll(EbayItem.fromJsonArray(ebayItemsResult));
                        if (mEbayItemsArrayAdapter.isEmpty()) {

//                            getView().findViewById(R.id.no_results).setVisibility(View.VISIBLE);
//                            gvResults.setVisibility(View.GONE);
                        } else {
//                            findViewById(R.id.no_results).setVisibility(View.GONE);
//                            gvResults.setVisibility(View.VISIBLE);
                            mSwipeContainer.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Network problem with fetching the data", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
//                    Log.i("INFO", imageResults.toString());
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "No network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    public void startSearchForKeywords(IntroActivity context, String searchKeywords) {
        if (mCategories != null) {
            mCategories.setVisibility(View.GONE);
        }
        mProgressBar.setVisibility(View.VISIBLE);
        FragmentPagerAdapter fragmentPagerAdapter = context.getAdapterViewPager();
        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            String name = makeFragmentName(context.getViewPager().getId(), i);
            Fragment viewPagerFragment = context.getSupportFragmentManager().findFragmentByTag(name);
            if (viewPagerFragment != null) {
                if (viewPagerFragment instanceof ResultsListFragment && viewPagerFragment.isResumed()) {
                    this.addingItem = true;
                    ((ResultsListFragment) viewPagerFragment).makeRequest(0, searchKeywords);

                }
            }
        }
    }

    public void startSearchForCategory(IntroActivity context, String categoryId, String categoryText) {
        if (mCategories != null) {
            mCategories.setVisibility(View.GONE);
        }
        keywordsEditText.setText("");
        keywords = "";
        mProgressBar.setVisibility(View.VISIBLE);
        FragmentPagerAdapter fragmentPagerAdapter = context.getAdapterViewPager();
        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            String name = makeFragmentName(context.getViewPager().getId(), i);
            Fragment viewPagerFragment = context.getSupportFragmentManager().findFragmentByTag(name);
            if (viewPagerFragment != null) {
                if (viewPagerFragment instanceof ResultsListFragment && viewPagerFragment.isResumed()) {
                    this.addingItem = true;
                    ((ResultsListFragment) viewPagerFragment).makeRequestByCategory(0, categoryId);
                    mSwipeContainer.setVisibility(View.VISIBLE);
                    mPlainTextSearch.setVisibility(View.GONE);
                    mBackButton.setVisibility(View.VISIBLE);
                    mBreadCrumb.setVisibility(View.VISIBLE);
                    mBreadCrumb.setText(categoryText);
                }
            }
        }
    }

    public void startSearchForKeywords(IntroActivity context, String searchKeywords, String filter) {
//        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                mEbayItemsArrayAdapter.clear();
//                if (startup || keywords != null) {
//                    makeRequest(0, keywords);
//                    startup = false;
//                }
//            }
//        });
//        if (startup || keywords!=null){
//            makeRequest(0, keywords);
//            startup = false;
//        }
        FragmentPagerAdapter fragmentPagerAdapter = context.getAdapterViewPager();
        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            String name = makeFragmentName(context.getViewPager().getId(), i);
            Fragment viewPagerFragment = context.getSupportFragmentManager().findFragmentByTag(name);
            if (viewPagerFragment != null) {
                if (viewPagerFragment instanceof ResultsListFragment && viewPagerFragment.isResumed()) {
                    this.addingItem = true;
                    ((ResultsListFragment) viewPagerFragment).setFilter(filter);
                    ((ResultsListFragment) viewPagerFragment).makeRequest(0, searchKeywords);
                }
            }
        }
    }

    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }



    public void makeRequestByCategory(final int offset, final String categoryId) {
//        Log.i("debug", "making request with keywords "+ categoryId + " because startup is " +startup);
        mEbayItemsArrayAdapter.clear();
        mCurrentPage = offset;

        mPaginatedUrl = EbayRequests.sSearchByCategoryUrl.concat(categoryId + EbayRequests.sPageNumberUrl + 1 + EbayRequests.sPaginationUrl + 10);
        if (filter != null) {
            mEbayItemsArrayAdapter.setPriceFilter(filter);
            mPaginatedUrl = mPaginatedUrl.concat("&itemFilter(0).name=MaxPrice&itemFilter(0).value=" + filter + "&itemFilter(0).paramName=Currency&itemFilter(0).paramValue=USD&sortOrder=PricePlusShippingLowest");
        }
        Log.i("INFO", "searchUrl is " + mPaginatedUrl);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        if (((IntroActivity)getActivity()).isNetworkAvailable()){

            asyncHttpClient.get(mPaginatedUrl, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("Debug", "error is "+ errorResponse);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());
                    JSONArray ebayItemsResult;
                    try {
                        ebayItemsResult = response.getJSONArray("findItemsByCategoryResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
//                        if (mCurrentPage == 0) {
//                            imageResults.clear();
//                        }
                        if (addingItem) {
                            EbayItem bestPriceItem = null;
                            for (int i = 0; i< ebayItemsResult.length(); i++) {
                                if (EbayItem.fromJsonArray(ebayItemsResult).get(i).getUrl() != null) {
                                    bestPriceItem = EbayItem.fromJsonArray(ebayItemsResult).get(i);
                                    break;
                                }
                            }

                            if (bestPriceItem != null) {
                                sBestPrice = String.valueOf(bestPriceItem.getPrice());
                                sBestPriceUrl = bestPriceItem.getUrl();

//                            sItemToAdd = new SearchItem(keywordsEditText.getText().toString(), filterEditText.getText().toString(), "", "");
//                            if (sItemToAdd != null) {
//                                sItemToAdd.setBestPrice(sBestPrice);
//                                sItemToAdd.setBestPriceUrl(sBestPriceUrl);
//                                persistSearch(sItemToAdd);
//                            }
//                            ((IntroActivity) getActivity()).getmShoppingListFragment().retrieveSearchesFromDB();
//                            ((IntroActivity) getActivity()).getViewPager().setCurrentItem(1);
                                addingItem = false;
                            }
                            else {
                                Toast.makeText(getActivity(), "No results for these keywords", Toast.LENGTH_SHORT).show();
                            }
                        }
                        mProgressBar.setVisibility(View.GONE);
                        mEbayItemsArrayAdapter.addAll(EbayItem.fromJsonArray(ebayItemsResult));
                        if (mEbayItemsArrayAdapter.isEmpty()) {

//                            getView().findViewById(R.id.no_results).setVisibility(View.VISIBLE);
//                            gvResults.setVisibility(View.GONE);
                        } else {
//                            findViewById(R.id.no_results).setVisibility(View.GONE);
//                            gvResults.setVisibility(View.VISIBLE);
                            mSwipeContainer.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Network problem with fetching the data", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
//                    Log.i("INFO", imageResults.toString());
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "No network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFilter() {
        return filter;
    }
    public void setAddingItem(boolean addingItem) {
        this.addingItem = addingItem;
    }
    public String getKeywords() {
        return keywords;
    }
    public void setFilter(String filter) {
        this.filter = filter;
    }
}
