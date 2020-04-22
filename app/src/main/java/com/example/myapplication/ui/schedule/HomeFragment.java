package com.example.myapplication.ui.schedule;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;

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

        new ETISAsyncTask<apiEtis.WeekManager>(getActivity()){
            @Override
            protected apiEtis.WeekManager doInBackgroundWithReauth(apiEtis ap){
                return ap.getWeeksManager();
            }

            @Override
            protected void onPostExecute(apiEtis.WeekManager result) {
                if(result != null){
                    // Получаем ViewPager и устанавливаем в него адаптер
                    ViewPager viewPager = root.findViewById(R.id.tabViewPager);
                    MyAdapter adapter = new MyAdapter(getActivity().getSupportFragmentManager(), result);
                    viewPager.setAdapter(adapter);
                    //viewPager.setCurrentItem(result.getNowWeekIndex());

                    //Передаём ViewPager в TabLayout
                    TabLayout tabLayout = root.findViewById(R.id.tabWeeks);
                    tabLayout.setupWithViewPager(viewPager);
                }
            }
        }.execute();

        return root;
    }

    public class MyAdapter extends FragmentStatePagerAdapter {
        apiEtis.WeekManager weekManager;


        MyAdapter(@NonNull FragmentManager fm, apiEtis.WeekManager weekManager) {
            super(fm);
            this.weekManager = weekManager;
        }

        @Override
        public int getCount() {
            return weekManager.getCount();
        }

        @Override @NonNull
        public Fragment getItem(int position) {
            return new WeeklyScheduleFragment(weekManager.get(position).number);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Integer.toString(weekManager.get(position).number);
        }
    }
}