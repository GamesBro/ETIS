package com.example.myapplication.ui.menu.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import java.io.IOException;

public class OrderViewFragment extends Fragment {

    @Override @SuppressLint("StaticFieldLeak")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_order_view, container, false);
        final Bundle bundle = getArguments();

        if (bundle != null) {
            new ETISAsyncTask<String>(getActivity()){
                String url;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    url = bundle.getString("url");
                }

                @Override
                protected String doInBackgroundWithReauth(apiEtis ap){
                    try {
                        return ap.getPage("https://student.psu.ru/pls/stu_cus_et/"+url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    if(result != null){
                        WebView wv = root.findViewById(R.id.webViewOrder);
                        wv.getSettings().setBuiltInZoomControls(true);
                        wv.loadData(result, "text/html", "UTF-8");
                    }
                }
            }.execute();
        }
        return root;
    }
}