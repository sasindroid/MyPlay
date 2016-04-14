package com.sasi.giffgaffplay;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabFragment3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment3 newInstance(String param1, String param2) {
        TabFragment3 fragment = new TabFragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TabFragment3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_third, container, false);

        View view = inflater.inflate(R.layout.tab_layout, null);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager();

        tabLayout.setupWithViewPager(viewPager);

//        setupTabIcons();
//        setupTabIconsOnly();

        return view;
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new OneFragment(), "My account");
        adapter.addFragment(new SecondFragment(), "My payback");
        adapter.addFragment(new ThirdFragment(), "Ways to buy");
        adapter.addFragment(new FourthFragment(), "Community");
        adapter.addFragment(new FifthFragment(), "Contacts");
        viewPager.setAdapter(adapter);
    }


    private void setupTabIconsOnly() {
        tabLayout.getTabAt(0).setIcon(R.drawable.account_active);
        tabLayout.getTabAt(1).setIcon(R.drawable.contacts_active);
        tabLayout.getTabAt(2).setIcon(R.drawable.ways_to_buy_active);
        tabLayout.getTabAt(3).setIcon(R.drawable.community_active);
        tabLayout.getTabAt(4).setIcon(R.drawable.payback_active);
    }

    private void setupTabIcons() {

//        tabLayout.getTabAt(0).setIcon(R.drawable.community_tap);

        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabOne.setText("My account");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_active, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabSecond = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabSecond.setText("My payback");
        tabSecond.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.contacts_active, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabSecond);

        TextView tabThird = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabThird.setText("Ways to buy");
        tabThird.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ways_to_buy_active, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThird);

        TextView tabFourth = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabFourth.setText("Community");
        tabFourth.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.community_active, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFourth);

        TextView tabFifth = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabFifth.setText("Contacts");
        tabFifth.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.payback_active, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFifth);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
//            return null;
            return mFragmentTitleList.get(position);
        }
    }

}
