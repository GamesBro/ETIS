package com.example.myapplication.ui.menu.Fragments;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;


public class TeachersFragment extends Fragment {

    private View root;

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_teachers, container, false);

        new ETISAsyncTask<String>(getActivity()){

            protected String doInBackgroundWithReauth(apiEtis ap){
                return ap.getTeachers();
            }

            protected void onPostExecute(String result) {

                if(result != null){
                    TableLayout tableLayout = root.findViewById(R.id.teacherList);
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    Document doc = Jsoup.parse(result);
                    Elements rows = doc.getElementsByClass("teacher_info");
                    for (Element row: rows) {
                        TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_teachers, null);
                        ImageView tPhoto = tr.findViewById(R.id.teacher_photo);

                        new DownloadImageTask(tPhoto)
                                .execute("https://student.psu.ru/pls/stu_cus_et/"+row.getElementsByTag("img").get(0).attr("src"));

                        TextView tv = tr.findViewById(R.id.teacher_name);
                        tv.setText(row.getElementsByClass("teacher_name").get(0).ownText());

                        tv = tr.findViewById(R.id.cafedra);
                        tv.setText(row.getElementsByClass("chair").get(0).ownText());

                        tv = tr.findViewById(R.id.discipline);
                        tv.setText(row.getElementsByClass("dis").get(0).ownText());

                        tableLayout.addView(tr);
                    }
                }
                else {
                    System.out.println("Err load");
                }
            }

        }.execute();

        /*
        androidx.appcompat.widget.Toolbar toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        int count = ((AppCompatActivity)getActivity()).getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) getActivity()).onBackPressed();
                }
            });
        }
        */
        return root;
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @SuppressLint("StaticFieldLeak")
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Ошибка передачи изображения", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
