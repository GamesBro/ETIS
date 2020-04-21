package com.example.myapplication.ui.curriculum;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.apiEtis;
import com.example.myapplication.ETISAsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CurriculumFragment extends Fragment {

    private View root;
    private CurriculumViewModel curriculumViewModel;

    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        curriculumViewModel =
                ViewModelProviders.of(this).get(CurriculumViewModel.class);
        root = inflater.inflate(R.layout.fragment_curriculum, container, false);
        /*final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        final SwipeRefreshLayout mSwipeRefreshLayout = root.findViewById(R.id.fresher);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new CurriculumETISAsyckTask(getActivity()).execute();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        new CurriculumETISAsyckTask(getActivity()).execute();

        return root;
    }

    class CurriculumETISAsyckTask extends ETISAsyncTask<String>{

        public CurriculumETISAsyckTask(FragmentActivity f) {
            super(f);
        }

        protected String doInBackgroundWithReauth(apiEtis ap){
            return ap.getCurriculumShort();
        }

        protected void onPostExecute(String result){
            if(result != null){

                Document doc = Jsoup.parse(result);
                Elements Headers = doc.getElementsByTag("h3");
                Elements tables = doc.getElementsByClass("common");

                //Сначала найдем в разметке активити саму таблицу по идентификатору
                TableLayout tableLayout = root.findViewById(R.id.tableCurriculum);
                LayoutInflater inflater = getActivity().getLayoutInflater();

                if(tableLayout.getChildCount() > 2)
                    tableLayout.removeViews(2, tableLayout.getChildCount()-2);

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

    }
}