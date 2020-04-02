package com.example.myapplication.ui.menu;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener{

    private MenuViewModel mViewModel;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    MyRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        Resources res = getResources();
        String[] sk_verb_array = res.getStringArray(R.array.menu); // Получаем массив из ресурсов

        // set up the RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.test);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), Arrays.asList(sk_verb_array));
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        NavController now = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        switch (position){
            case 0:
                now.navigate(R.id.teachersFragment, null);
                break;
            case 1:
                now.navigate(R.id.missedClassesFragment, null);
                break;
            case 2:
                now.navigate(R.id.ordersFragment, null);
                break;
            case 3:
                now.navigate(R.id.libraryFragment, null);
                break;
            case 4:
                now.navigate(R.id.electronicResourcesFragment, null);
                break;
            case 5:
                now.navigate(R.id.adsFragment, null);
                break;
        }
    }
}
