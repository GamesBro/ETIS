package com.example.myapplication.ui.menu.Fragments;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.MakeLinksClicable;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class OrdersFragment extends Fragment {

    private View root;

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_orders, container, false);

        androidx.appcompat.widget.Toolbar mainToolbar = root.findViewById(R.id.toolbar);
        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        new ETISAsyncTask<String>(getActivity()){

            protected String doInBackgroundWithReauth(apiEtis ap){
                return ap.getOrders();
            }

            @SuppressLint("ResourceAsColor")
            protected void onPostExecute(String result) {

                if(result != null){
                    LinearLayout Layout = root.findViewById(R.id.listOrders);
                    Document doc = Jsoup.parse(result);
                    Elements orders = doc.getElementsByClass("ord-name");
                    for(Element order : orders){
                        Element a = order.getElementsByTag("a").get(0);
                        a.attr("href", "https://student.psu.ru/pls/stu_cus_et/"+a.attr("href"));
                        TextView tv = new TextView(getContext());
                        tv.setText(Html.fromHtml(order.html()));
                        tv.setLinkTextColor(R.color.colorLinks);
                        tv.setLinksClickable(true);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        CharSequence text = tv.getText();
                        if (text instanceof Spannable)
                            tv.setText((new MakeLinksClicable()).reformatText(text));
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
