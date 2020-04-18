package com.example.myapplication.ui.grades.Fragments;

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
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class FragmentTrimester extends Fragment {
    View root;

    private int pxFromDp(double dp) {
        return (int)(dp * getContext().getResources().getDisplayMetrics().density);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_trimester, container, false);

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
                    title = my.getRatingCurrent();
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

                        title = my.getRatingCurrent();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @SuppressLint("ResourceAsColor")
            protected void onPostExecute(String result)
            {
                if(title != null){

                    Document doc = Jsoup.parse(title);
                    Elements Headers = doc.getElementsByTag("h3");
                    Elements Tables =  doc.getElementsByClass("common");

                    //Сначала найдем в разметке активити саму таблицу по идентификатору
                    TableLayout tableLayout = root.findViewById(R.id.tableTrimester);
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    for(int i=0; i < Headers.size(); i++){
                        TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_trimester_title, null);
                        TextView tv1 = tr.findViewById(R.id.title);
                        tv1.setText(Headers.get(i).text());
                        tableLayout.addView(tr);
                        System.out.println('\t' + Headers.get(i).text());

                        for(Element row: Tables.get(i).getElementsByTag("tr")) {
                            Elements cell = row.getElementsByTag("td");
                            if(cell.size() == 9) {
                                TableRow tr2 = (TableRow) inflater.inflate(R.layout.table_row_trimester, null);

                                TextView tvTitle = tr2.findViewById(R.id.title);
                                tvTitle.setText(cell.get(0).text());

                                TextView tvType = tr2.findViewById(R.id.typeControl);
                                tvType.setText(cell.get(2).text().replace(" ", "\n"));
                                if(row.attr("style").equals("color:red;"))
                                    tvType.setTextColor(getResources().getColor(R.color.colorRed));

                                TextView tv12 = tr2.findViewById(R.id.typeWork);
                                tv12.setText(cell.get(1).text());
                                if(row.attr("style").equals("color:red;"))
                                    tv12.setTextColor(getResources().getColor(R.color.colorRed));

                                tv12 = tr2.findViewById(R.id.min);
                                tv12.setText(cell.get(4).text());
                                if(row.attr("style").equals("color:red;"))
                                    tv12.setTextColor(getResources().getColor(R.color.colorRed));

                                tv12 = tr2.findViewById(R.id.now);
                                tv12.setText(
                                        cell.get(3).text() + (!cell.get(3).text().equals(cell.get(5).text()) && cell.get(5).text().length() > 0 ? "("+cell.get(5).text()+")" : "")
                                );
                                if(row.attr("style").equals("color:red;"))
                                    tv12.setTextColor(getResources().getColor(R.color.colorRed));

                                tv12 = tr2.findViewById(R.id.max);
                                tv12.setText(cell.get(6).text());
                                if(row.attr("style").equals("color:red;"))
                                    tv12.setTextColor(getResources().getColor(R.color.colorRed));

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
