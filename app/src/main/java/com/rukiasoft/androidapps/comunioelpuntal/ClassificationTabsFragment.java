package com.rukiasoft.androidapps.comunioelpuntal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.Serializable;


public class ClassificationTabsFragment extends Fragment implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String TAG = "ClassificationTabsFragment";
    private static FragmentTabHost mTabHost;
    private ViewPager mViewPager;
    private static ClassificationFragment fGeneral = null;
    private static ClassificationFragment fLastRound = null;
    private static ClassificationFragment fRound = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        if (fGeneral == null)
            fGeneral = new ClassificationFragment();
        Log.d(TAG, "creandoGeneral");

        fGeneral.setGamerList(MainActivity.getGamers(),
                ClassificationFragment.OrderType.GENERAL);
        Log.d(TAG, "creadofLastRound");
        if (fLastRound == null)
            fLastRound = new ClassificationFragment();
        fLastRound.setGamerList(MainActivity.getGamers(),
                ClassificationFragment.OrderType.LAST_ROUND);
        Log.d(TAG, "creadofRound");
        if (fRound == null)
            fRound = new ClassificationFragment();
        fRound.setGamerList(MainActivity.getGamers(),
                ClassificationFragment.OrderType.ROUND);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.classification_tabs_fragment, container, false);


        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.classification_tabs_tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator(getResources().getString(R.string.general_without_colon)),
                Fragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator(getResources().getString(R.string.last_round)),
                Fragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentd").setIndicator(getResources().getString(R.string.other_round)),
                Fragment.class, null);
        mTabHost.setClickable(true);

        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                mViewPager.setCurrentItem(mTabHost.getCurrentTab());
            }
        });

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

            final TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i)
                    .findViewById(android.R.id.title);

            // Look for the title view to ensure this is an indicator and not a divider.(I didn't know, it would return divider too, so I was getting an NPE)
            if (tv != null)
                tv.setTextColor(getResources().getColor(R.color.color_tabs_text));
        }

        mViewPager = (ViewPager) rootView.findViewById(R.id.classification_tabs_viewpager);
        AppSectionsPagerAdapter mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                mTabHost.setCurrentTab(position);
            }
        });

        return rootView;
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        final FragmentManager fragmentManager;

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        @Override
        public Fragment getItem(int i) {
            //Log.d(TAG, "onTabSelected");
            Fragment fragment = new Fragment();
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    fragment = fGeneral;
                    break;
                case 1:
                    fragment = fLastRound;
                    break;
                case 2:
                    fragment = fRound;
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }
}

    
