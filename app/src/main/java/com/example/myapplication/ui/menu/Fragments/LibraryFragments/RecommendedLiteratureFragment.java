package com.example.myapplication.ui.menu.Fragments.LibraryFragments;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

public class RecommendedLiteratureFragment extends Fragment {

    @Override @SuppressLint("StaticFieldLeak")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_recommended_literature, container, false);

        new ETISAsyncTask<apiEtis.DisciplineLiterature[]>(getActivity()){

            @Override
            protected apiEtis.DisciplineLiterature[] doInBackgroundWithReauth(apiEtis ap){
                return ap.getRecommendedLiterature();
            }

            @Override
            protected void onPostExecute(apiEtis.DisciplineLiterature[] result) {
                if(result != null){
                    LinearLayout tableLayout = root.findViewById(R.id.tableRecommended);
                    for(apiEtis.DisciplineLiterature t : result){

                        TextView title = new TextView(getActivity());
                        title.setText(t.title);
                        tableLayout.addView(title);

                        title = new TextView(getActivity());
                        title.setText("Обязательная");
                        tableLayout.addView(title);

                        for(apiEtis.Book b : t.mandatoryLiterature){
                            title = new TextView(getActivity());
                            title.setText(b.title);
                            tableLayout.addView(title);
                            /*
                            for(apiEtis.StorageLocation s : b.storageLocations){
                                System.out.println("\t\t"+s.location+"\t"+s.count);
                            }
                             */
                        }

                        title = new TextView(getActivity());
                        title.setText("Дополнительная");
                        tableLayout.addView(title);

                        for(apiEtis.Book b : t.additionalLiterature){
                            title = new TextView(getActivity());
                            title.setText(b.title);
                            tableLayout.addView(title);
                            /*
                            for(apiEtis.StorageLocation s : b.storageLocations){
                                System.out.println("\t\t"+s.location+"\t"+s.count);
                            }
                            */
                        }
                    }
                }
            }

        }.execute();

        return root;
    }

}
