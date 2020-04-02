package com.example.myapplication.ui.menu.Fragments;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.MakeLinksClicable;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;


public class OrdersFragment extends Fragment {

    private View root;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_orders, container, false);

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
                    title = my.getOrders();
                    return null;
                }
                else
                    my = new apiEtis();

                try {
                    String session_id = my.auth(prefs.getString("surname", ""), prefs.getString("password", ""));
                    if(session_id != null){
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("session_id", session_id);
                        editor.apply();

                        title = my.getOrders();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            protected void onPostExecute(String result)
            {
                if(title != null){
                    LinearLayout Layout = root.findViewById(R.id.listOrders);
                    Document doc = Jsoup.parse(title);
                    Elements orders = doc.getElementsByClass("ord-name");
                    for(Element order : orders){
                        Element a = order.getElementsByTag("a").get(0);
                        a.attr("href", "https://student.psu.ru/pls/stu_cus_et/"+a.attr("href"));

                        TextView tv = new TextView(getContext());
                        tv.setText(Html.fromHtml(order.html()));
                        tv.setLinksClickable(true);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        CharSequence text = tv.getText();
                        if (text instanceof Spannable)
                            tv.setText(MakeLinksClicable.reformatText(text));
                        Layout.addView(tv);
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
