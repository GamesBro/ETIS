package com.example.myapplication.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.myapplication.ui.grades.FragmentDiplome;
import com.example.myapplication.ui.grades.FragmentSession;
import com.example.myapplication.ui.grades.FragmentTrimester;
import com.example.myapplication.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentSession fragmentSession;
    private FragmentDiplome fragmentDiplome;
    private FragmentTrimester fragmentTrimester;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        fragmentSession = new FragmentSession();
        fragmentDiplome = new FragmentDiplome();
        fragmentTrimester = new FragmentTrimester();

        Spinner spinner = root.findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        setFragment(fragmentSession);
                        break;
                    case 1:
                        setFragment(fragmentTrimester);
                        break;
                    case 2:
                        setFragment(fragmentDiplome);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return root;
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}