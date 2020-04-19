package com.example.myapplication.ui.grades;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import java.io.IOException;
import java.util.ArrayList;

public class RatingFragment extends Fragment {

    public RatingFragment() {
        // Required empty public constructor
    }

    private View root;

    @Override
    @SuppressLint("StaticFieldLeak")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_rating, container, false);

        new ETISAsyncTask<ArrayList<apiEtis.Rating>>(getActivity()){

            protected ArrayList<apiEtis.Rating> doInBackgroundWithReauth(apiEtis ap){
                try {
                    return ap.getRating();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(ArrayList<apiEtis.Rating> result) {

                if(result != null){
                    //Сначала найдем в разметке активити саму таблицу по идентификатору
                    TableLayout tableLayout = root.findViewById(R.id.tableRating);

                    for(apiEtis.Rating r : result){
                        TableRow tr2 = new TableRow(getContext());
                        TextView tv2 = new TextView(getContext());
                        tv2.setText(r.title);
                        tr2.addView(tv2);
                        tableLayout.addView(tr2);

                        for(apiEtis.Rating.RatingRow rr : r.rows){
                            tr2 = new TableRow(getContext());

                            tv2 = new TextView(getContext());
                            tv2.setText(rr.combination);
                            tr2.addView(tv2);

                            tv2 = new TextView(getContext());
                            tv2.setText(rr.ranking);
                            tr2.addView(tv2);

                            tableLayout.addView(tr2);
                        }
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