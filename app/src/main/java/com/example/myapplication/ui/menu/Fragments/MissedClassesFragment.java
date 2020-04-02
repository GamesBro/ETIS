package com.example.myapplication.ui.menu.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class MissedClassesFragment extends Fragment {
    private View root;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String normalize(String fio){
        String[] temp = fio.split(" ");
        temp[1] = temp[1].substring(0, 1)+".";
        temp[2] = temp[2].substring(0, 1)+".";
        return String.join(" ", temp);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_missed_classes, container, false);

        new AsyncTask<Void, Void, ArrayList<apiEtis.MissedClasses>>()
        {
            @Override
            protected ArrayList<apiEtis.MissedClasses> doInBackground(Void... params)
            {
                apiEtis my;
                SharedPreferences prefs = getActivity().getSharedPreferences("mysettings", MODE_PRIVATE);

                if(prefs.contains("session_id")) {
                    my = new apiEtis(prefs.getString("session_id", ""));
                    try {
                        return my.getMissedClasses();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //return null;
                }
                else
                    my = new apiEtis();

                try {
                    String session_id = my.auth(prefs.getString("surname", ""), prefs.getString("password", ""));
                    if(session_id != null){
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("session_id", session_id);
                        editor.apply();

                        return my.getMissedClasses();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            protected void onPostExecute(ArrayList<apiEtis.MissedClasses> result)
            {
                if(result != null){
                    //Сначала найдем в разметке активити саму таблицу по идентификатору
                    TableLayout tableLayout = root.findViewById(R.id.tableMissedLessons);
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    for(apiEtis.MissedClasses tt : result){
                        TableRow tr2 = (TableRow) inflater.inflate(R.layout.table_row_missed_classes_title, null);
                        TextView tv2 = tr2.findViewById(R.id.title);
                        tv2.setText(tt.title);
                        tableLayout.addView(tr2);

                        for(apiEtis.absence ttt : tt.absences){
                            TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_missed_classes, null);
                            TextView tv = tr.findViewById(R.id.num);
                            tv.setText(Integer.toString(ttt.n));

                            tv = tr.findViewById(R.id.date);
                            tv.setText(String.join("\n", ttt.dates));

                            tv = tr.findViewById(R.id.discipline);
                            tv.setText(ttt.discipline);

                            //tv = tr.findViewById(R.id.typeWork);

                            tv = tr.findViewById(R.id.lecturer);
                            tv.setText(normalize(ttt.lecturer));

                            tableLayout.addView(tr);
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
