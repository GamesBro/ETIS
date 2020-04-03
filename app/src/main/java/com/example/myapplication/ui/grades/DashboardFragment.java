package com.example.myapplication.ui.grades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;


import com.example.myapplication.ui.grades.Fragments.FragmentDiplome;
import com.example.myapplication.ui.grades.Fragments.FragmentSession;
import com.example.myapplication.ui.grades.Fragments.FragmentTrimester;
import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_grades, container, false);

        toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        // Получаем ViewPager и устанавливаем в него адаптер
        ViewPager viewPager = root.findViewById(R.id.tabViewPager);
        viewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager()));

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = root.findViewById(R.id.tabGrades);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.grades_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.rating)
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.ratingFragment, null);
        return true;
    }

    public static class MyAdapter extends FragmentPagerAdapter {

        MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new FragmentTrimester();
                case 2:
                    return new FragmentDiplome();
                default:
                    return new FragmentSession();

            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return "в триместре";
                case 2:
                    return "в диплом";
                default:
                    return "за сессии";
            }
        }
    }
}