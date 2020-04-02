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

        new AsyncTask<Void, Void, String>()
        {
            String title;

            @Override
            protected String doInBackground(Void... params)
            {
                apiEtis my;
                SharedPreferences prefs = getActivity().getSharedPreferences("mysettings", MODE_PRIVATE);

                if(prefs.contains("session_id")) {
                    my = new apiEtis(prefs.getString("session_id", ""));
                    title = my.getRatingSession();
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

                        title = my.getAbsence(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            protected void onPostExecute(String result)
            {
                if(title != null){

                    Document doc = Jsoup.parse(title);
                    Elements tables = doc.getElementsByClass("slimtab_nice").get(0).getElementsByTag("tr");

                    //Сначала найдем в разметке активити саму таблицу по идентификатору
                    TableLayout tableLayout = root.findViewById(R.id.tableMissedLessons);
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    for (Element row : tables){
                        Elements els = row.getElementsByTag("td");
                        if(els.size() == 5){
                            TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_missed_classes, null);
                            TextView tv = tr.findViewById(R.id.num);
                            tv.setText(els.get(0).text());

                            tv = tr.findViewById(R.id.date);
                            tv.setText(els.get(1).text().replace(" ", "\n"));

                            tv = tr.findViewById(R.id.discipline);
                            tv.setText(els.get(2).text());

                            //tv = tr.findViewById(R.id.typeWork);
                            //tv.setText(els.get(3).text());

                            tv = tr.findViewById(R.id.lecturer);
                            tv.setText(normalize(els.get(4).text()));

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
