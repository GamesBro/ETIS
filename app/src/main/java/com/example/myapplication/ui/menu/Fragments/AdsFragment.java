package com.example.myapplication.ui.menu.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.MakeLinksClicable;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AdsFragment extends Fragment {

    public AdsFragment() {
        // Required empty public constructor
    }

    private View root;

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_ads, container, false);

        androidx.appcompat.widget.Toolbar mainToolbar = root.findViewById(R.id.toolbar);
        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        new ETISAsyncTask<ArrayList<String>>(getActivity()){

            protected ArrayList<String> doInBackgroundWithReauth(apiEtis ap){
                try {
                    return ap.getAnnounce();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @SuppressLint("ResourceAsColor")
            protected void onPostExecute(ArrayList<String> result) {

                if(result != null){
                    LinearLayout Layout = root.findViewById(R.id.adsList);

                    for(String ads : result){
                        TextView textView = new TextView(getContext());
                        textView.setBackgroundResource(R.drawable.ads);
                        textView.setText(Html.fromHtml(ads));
                        textView.setLinkTextColor(R.color.colorLinks);
                        textView.setLinksClickable(true);
                        textView.setMovementMethod(LinkMovementMethod.getInstance());

                        CharSequence text = textView.getText();
                        if (text instanceof Spannable)
                            textView.setText((new MakeLinksClicable("https://student.psu.ru/pls/stu_cus_et/")).reformatText(text));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 10, 0, 10);

                        Layout.addView(textView, params);
                    }
                }
                else {
                    System.out.println("Err load");
                }
            }

        }.execute();

        return root;
    }
}
