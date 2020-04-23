package com.example.myapplication.ui.menu.Fragments.LibraryFragments;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import java.util.ArrayList;


public class IssuedBooksFragment extends Fragment {

    @Override @SuppressLint("StaticFieldLeak")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_issued_books, container, false);

        new ETISAsyncTask<ArrayList<apiEtis.ItemHistoryBook>>(getActivity()){

            @Override
            protected ArrayList<apiEtis.ItemHistoryBook> doInBackgroundWithReauth(apiEtis ap){
                return ap.getIssuedBooks();
            }

            @Override
            protected void onPostExecute(ArrayList<apiEtis.ItemHistoryBook> result) {
                if(result != null){
                    TableLayout tableLayout = root.findViewById(R.id.tableIssuedBooks);
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    for(apiEtis.ItemHistoryBook t : result){
                        System.out.println(t.title);
                        TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_issued_books, null);
                        TextView tv = tr.findViewById(R.id.title);
                        tv.setText(t.title);

                        tv = tr.findViewById(R.id.date_issue);
                        tv.setText(t.date_issue);

                        tv = tr.findViewById(R.id.date_planned_return);
                        tv.setText(t.date_planned_return);

                        tv = tr.findViewById(R.id.date_return);
                        tv.setText(t.date_return);

                        tableLayout.addView(tr);
                    }
                }
            }

        }.execute();

        return root;
    }

}
