package com.example.myapplication.ui.curriculum;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment {

    View root;
    private NotificationsViewModel notificationsViewModel;

    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        root = inflater.inflate(R.layout.fragment_curriculum, container, false);
        /*final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
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
                    title = my.getCurriculumShort();
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

                        title = my.getCurriculumShort();
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
                    Elements Headers = doc.getElementsByTag("h3");
                    Elements tables = doc.getElementsByClass("common");

                    //Сначала найдем в разметке активити саму таблицу по идентификатору
                    TableLayout tableLayout = root.findViewById(R.id.tableCurriculum);
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    for(int i=0; i < Headers.size(); i++){

                        TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_curriculum_title, null);
                        TextView tv1 = tr.findViewById(R.id.title);
                        tv1.setText(Headers.get(i).text());
                        tableLayout.addView(tr);

                        Elements rows = tables.get(i).getElementsByClass("cgrldatarow");
                        for(Element row: rows){
                            Elements ls = row.children();
                            TableRow tr2 = (TableRow) inflater.inflate(R.layout.table_row_curriculum, null);
                            TextView tv12 = tr2.findViewById(R.id.title);
                            tv12.setText((ls.size()==5 ? ls.get(0).text() : ls.get(1).text()));

                            tv12 = tr2.findViewById(R.id.reportType);
                            tv12.setText(row.getElementsByAttributeValue("align","center").get(0).text());

                            Elements right = row.getElementsByAttributeValue("align","right");

                            tv12 = tr2.findViewById(R.id.workLaboratory);
                            tv12.setText(right.get(0).text());

                            tv12 = tr2.findViewById(R.id.selfStudy);
                            tv12.setText(right.get(1).text());

                            tv12 = tr2.findViewById(R.id.all);
                            tv12.setText(right.get(2).text());

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