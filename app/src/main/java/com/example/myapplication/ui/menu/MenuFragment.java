package com.example.myapplication.ui.menu;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.AuthActivity;
import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener{

    private MenuViewModel mViewModel;
    private View root;
    private MyRecyclerViewAdapter adapter;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }


    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_menu, container, false);

        Button im = root.findViewById(R.id.button);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.settingFragment);
            }
        });

        // set up the RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.test);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String[] sk_verb_array = getResources().getStringArray(R.array.menu); // Получаем массив из ресурсов
        adapter = new MyRecyclerViewAdapter(getContext(), Arrays.asList(sk_verb_array));
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // cached value
        SharedPreferences prefs = getActivity().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        ((TextView)root.findViewById(R.id.fio)).setText(prefs.getString("user_fio", ""));
        ((TextView)root.findViewById(R.id.direction)).setText(prefs.getString("user_direction", ""));

        new ETISAsyncTask<apiEtis.UserInfo>(getActivity()){

            @Override
            protected apiEtis.UserInfo doInBackgroundWithReauth(apiEtis ap){
                return ap.getUserInfo();
            }

            @Override
            protected void onPostExecute(apiEtis.UserInfo result) {
                if(result != null){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("user_fio", result.fio);
                    editor.putString("user_direction", result.direction);
                    editor.apply();

                    TextView textViewFio = (TextView)root.findViewById(R.id.fio);
                    if(!textViewFio.getText().equals(result.fio))
                        textViewFio.setText(result.fio);

                    TextView textViewDirection = (TextView)root.findViewById(R.id.direction);
                    if(!textViewDirection.getText().equals(result.direction))
                        textViewDirection.setText(result.direction);

                    adapter.setItem(2, String.format("%s (%d)", adapter.getItem(2), result.cntMissedClassed));
                }
            }

        }.execute();

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
                now.navigate(R.id.teachersMessagesFragment, null);
                break;
            case 2:
                now.navigate(R.id.missedClassesFragment, null);
                break;
            case 3:
                now.navigate(R.id.ordersFragment, null);
                break;
            case 4:
                now.navigate(R.id.libraryFragment, null);
                break;
            case 5:
                now.navigate(R.id.electronicResourcesFragment, null);
                break;
            case 6:
                now.navigate(R.id.adsFragment, null);
                break;
        }
    }
}