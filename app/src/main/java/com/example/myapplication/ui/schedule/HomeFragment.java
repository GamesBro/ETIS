package com.example.myapplication.ui.schedule;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    View root;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_schedule, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        WeekManager wm = new WeekManager();
        wm.add(19, 1);
        wm.add(20, 1);
        wm.add(21, 1);
        wm.add(22, 1);
        wm.add(23, 1);
        wm.add(24, 1);
        wm.add(25, 1);
        wm.add(26, 1);
        wm.add(27, 1);
        wm.add(28, 1);
        wm.add(29, 1);
        wm.add(30, 1);
        wm.add(31, 1);
        wm.add(32, 1);
        wm.add(33, 1);
        wm.add(35, 1);
        wm.add(36, 1);

        // Получаем ViewPager и устанавливаем в него адаптер
        ViewPager viewPager = root.findViewById(R.id.tabViewPager);
        MyAdapter adapter = new MyAdapter(getActivity().getSupportFragmentManager(), wm);
        viewPager.setAdapter(adapter);

        //Передаём ViewPager в TabLayout
        TabLayout tabLayout = root.findViewById(R.id.tabWeeks);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    class WeekManager{
        ArrayList<Week> weeks;
        int nowWeekIndex;

        public WeekManager(){
            weeks = new ArrayList<>();
        }

        public int getCount(){
            return weeks.size();
        }

        public void add(int number, int type){
            weeks.add(new Week(number, type));
        }

        public Week get(int index){
            return weeks.get(index);
        }

        public int getNowWeekIndex(){
            return nowWeekIndex;
        }

        class Week{
            int number;
            int type;

            Week(int number, int type){
                this.number = number;
                this.type = type;
            }
        }
    }


    public class MyAdapter extends FragmentPagerAdapter {
        WeekManager weekManager;

        MyAdapter(@NonNull FragmentManager fm, WeekManager weekManager) {
            super(fm);
            this.weekManager = weekManager;
        }

        @Override
        public int getCount() {
            return weekManager.getCount();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new WeeklyScheduleFragment(weekManager.get(position).number);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Integer.toString(weekManager.get(position).number);
        }
    }
}