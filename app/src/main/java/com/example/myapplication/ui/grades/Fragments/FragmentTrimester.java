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

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class FragmentTrimester extends Fragment {
    private View root;

    private int pxFromDp(double dp) {
        return (int)(dp * getContext().getResources().getDisplayMetrics().density);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_trimester, container, false);

        new ETISAsyncTask<String>(getActivity()){

            protected String doInBackgroundWithReauth(apiEtis ap){
                return ap.getRatingCurrent();
            }

            @SuppressLint("ResourceAsColor")
            protected void onPostExecute(String result)
            {
                if(result != null){

                    Document doc = Jsoup.parse(result);
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
