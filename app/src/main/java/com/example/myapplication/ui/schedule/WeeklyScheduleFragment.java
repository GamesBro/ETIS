package com.example.myapplication.ui.schedule;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

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

public class WeeklyScheduleFragment extends Fragment {

    private View root;
    private int number;

    WeeklyScheduleFragment(int number) {
        this.number = number;
    }

    private int pxFromDp(int dp) {
        return (int)(dp * getContext().getResources().getDisplayMetrics().density);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_weekly_schedule, container, false);

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
                    title = my.getTimeTable(true, number);
                    return null;
                }
                else
                    my = new apiEtis();

                try {
                    String session_id = my.auth(prefs.getString("surname", ""), prefs.getString("password", ""));
                    if(session_id != null){
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("session_id", session_id);
                        editor.apply();

                        title = my.getTimeTable(true, number);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            protected void onPostExecute(String result)
            {
                System.out.println(
                        title.length()
                );
                if(title != null && title.length()>0){
                    Document doc = Jsoup.parse(title);
                    Elements days = doc.getElementsByClass("day");

                    TableLayout tableLayout = root.findViewById(R.id.table);
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    for (Element day : days) {
                        TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_schedule_title, null);
                        TextView tv1 = tr.findViewById(R.id.title);
                        tv1.setText(day.getElementsByTag("h3").text());
                        tableLayout.addView(tr);

                        Elements table = day.getElementsByTag("tr");
                        if (table.isEmpty()) {
                            tr = (TableRow) inflater.inflate(R.layout.table_row_schedule_title, null);
                            tv1 = tr.findViewById(R.id.title);
                            tv1.setText("Пар нет!");
                            tableLayout.addView(tr);
                        } else {
                            boolean first = true;
                            for (Element row : table) {
                                TableRow tr2 = (TableRow) inflater.inflate(R.layout.table_row_schedule, null);
                                TextView tv12 = tr2.findViewById(R.id.pair);
                                tv12.setText(row.getElementsByClass("pair_num").first().ownText());

                                tv12 = tr2.findViewById(R.id.time);
                                tv12.setText(row.getElementsByClass("eval").first().text());

                                if(row.getElementsByClass("pair_info").get(0).childrenSize() > 0) {
                                    tv12 = tr2.findViewById(R.id.title);
                                    tv12.setText(row.getElementsByClass("dis").first().text());

                                    tv12 = tr2.findViewById(R.id.teacher);
                                    tv12.setText(row.getElementsByClass("teacher").get(0).getElementsByTag("a").get(0).text());

                                    tv12 = tr2.findViewById(R.id.classroom);
                                    tv12.setText(row.getElementsByClass("aud").get(0).text());
                                }

                                TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                                int px = pxFromDp(1);
                                params.setMargins(px,first ? px : 0,px,px);
                                tableLayout.addView(tr2, params);
                                first = false;
                            }
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
