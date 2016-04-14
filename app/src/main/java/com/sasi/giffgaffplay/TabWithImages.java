package com.sasi.giffgaffplay;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TabWithImages extends AppCompatActivity implements OneFragment.OnFragmentInteractionListener,
        SecondFragment.OnFragmentInteractionListener, ThirdFragment.OnFragmentInteractionListener,
        FourthFragment.OnFragmentInteractionListener {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_with_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void setupTabIcons() {

//        tabLayout.getTabAt(0).setIcon(R.drawable.community_tap);

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("My account");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_active, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabSecond = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabSecond.setText("Ways to buy");
        tabSecond.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ways_to_buy_active, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabSecond);

        TextView tabThird = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThird.setText("Community");
        tabThird.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.community_active, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThird);

        TextView tabFourth = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFourth.setText("Contacts");
        tabFourth.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.contacts_active, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFourth);

    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "ONE");
        adapter.addFragment(new SecondFragment(), "TWO");
        adapter.addFragment(new ThirdFragment(), "THREE");
        adapter.addFragment(new FourthFragment(), "FOUR");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
