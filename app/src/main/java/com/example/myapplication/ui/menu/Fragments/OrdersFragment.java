package com.example.myapplication.ui.menu.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import java.util.ArrayList;


public class OrdersFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_orders, container, false);

        androidx.appcompat.widget.Toolbar mainToolbar = root.findViewById(R.id.toolbar);
        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        new ETISAsyncTask<ArrayList<apiEtis.Order>>(getActivity()){

            protected ArrayList<apiEtis.Order> doInBackgroundWithReauth(apiEtis ap){
                return ap.getOrders();
            }

            @SuppressLint("ResourceAsColor")
            protected void onPostExecute(ArrayList<apiEtis.Order> result) {
                if(result != null){
                    LinearLayout Layout = root.findViewById(R.id.listOrders);
                    for(apiEtis.Order ord : result){
                        TextView tv = new TextView(getContext());
                        tv.setText(ord.title);
                        tv.setTextColor(R.color.colorLinks);
                        tv.setLinksClickable(true);
                        tv.setOnClickListener(new cliker(ord.url));
                        Layout.addView(tv);
                    }
                }
                else {
                    System.out.println("Err load");
                }
            }

        }.execute();

        return root;
    }

    class cliker implements View.OnClickListener {
        Bundle bundle;
        cliker(String url){
            bundle = new Bundle();
            bundle.putString("url", url);
        }

        @Override
        public void onClick(View v) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.orderViewFragment, bundle);
        }
    };

}
