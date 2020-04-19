package com.example.myapplication.ui.menu.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.MakeLinksClicable;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ElectronicResourcesFragment extends Fragment {

    private View root;

    public ElectronicResourcesFragment() {
        // Required empty public constructor
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_electronic_resources, container, false);

        new ETISAsyncTask<String>(getActivity()){

            protected String doInBackgroundWithReauth(apiEtis ap){
                return ap.getElectricRes();
            }

            protected void onPostExecute(String result)
            {
                if(result != null){
                    //Сначала найдем в разметке активити саму таблицу по идентификатору
                    TableLayout tableLayout = root.findViewById(R.id.elecRes);
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    Document doc = Jsoup.parse(result);
                    Elements rows = doc.getElementsByClass("slimtab_nice").get(0).getElementsByTag("tr");
                    for (Element row: rows) {
                        Elements els = row.getElementsByTag("td");
                        if(els.size() == 3){
                            TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_electronic_resources, null);

                            TextView tv = tr.findViewById(R.id.resources);
                            tv.setText(Html.fromHtml(els.get(0).html()));
                            tv.setLinksClickable(true);
                            tv.setMovementMethod(LinkMovementMethod.getInstance());
                            CharSequence text = tv.getText();
                            if (text instanceof Spannable)
                                tv.setText((new MakeLinksClicable()).reformatText(text));

                            tv = tr.findViewById(R.id.login);
                            tv.setText(els.get(1).text());

                            tv = tr.findViewById(R.id.password);
                            tv.setText(els.get(2).text());

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
