package com.example.myapplication.ui.menu.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.ui.menu.Fragments.LibraryFragments.RecommendedLiteratureFragment;
import com.example.myapplication.ui.menu.Fragments.LibraryFragments.IssuedBooksFragment;
import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;

public class LibraryFragment extends Fragment {

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        // Получаем ViewPager и устанавливаем в него адаптер
        ViewPager viewPager = root.findViewById(R.id.tadViewPager);
        viewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager()));

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = root.findViewById(R.id.tabLibrary);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    public static class MyAdapter extends FragmentPagerAdapter {

        MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new RecommendedLiteratureFragment();

                default:
                    return new IssuedBooksFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return "Рекомендации";

                default:
                    return "Выданные книги";
            }
        }
    }
}
