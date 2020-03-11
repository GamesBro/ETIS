package com.example.myapplication.ui.home;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    View root;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/


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
                    title = my.getTimeTable(true, 28);
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

                        title = my.getTimeTable(true, 28);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            protected void onPostExecute(String result)
            {
                if(title != null){
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

                                tableLayout.addView(tr2);
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