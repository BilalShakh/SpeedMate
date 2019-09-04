package com.example.speedmate.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.speedmate.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.speedmate.R.string.tab_text_3;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, tab_text_3};
    private final Context mContext;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitle = new ArrayList<>();

    public void addFragment(Fragment fragment,String title){
        mFragmentList.add(fragment);
        mFragmentTitle.add(title);
    }

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return mFragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitle.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return mFragmentList.size();
    }
}