package com.example.myapplication.ui.grades;

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

public class FragmentDiplome extends Fragment {

    View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_diplome, container, false);

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
                    title = my.getRatingDiplom();
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

                        title = my.getRatingDiplom();
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

                    TableLayout tableLayout = root.findViewById(R.id.tableDiplom);
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    Elements rows = doc.getElementsByClass("common").get(0).getElementsByTag("tr");
                    for(Element row: rows){
                        Elements ls = row.children();
                        if(ls.size() == 3){
                            TableRow tr2 = (TableRow) inflater.inflate(R.layout.table_row_diplom, null);
                            TextView tv12 = tr2.findViewById(R.id.title);
                            tv12.setText(ls.get(0).text());

                            tv12 = tr2.findViewById(R.id.mark);
                            tv12.setText(ls.get(1).text());

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