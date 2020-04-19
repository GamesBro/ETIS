package com.example.myapplication.ui.menu.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.MakeLinksClicable;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;


public class TeachersMessagesFragment extends Fragment {

    private View root;

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_teachers_messages, container, false);

        new ETISAsyncTask<String>(getActivity()){

            protected String doInBackgroundWithReauth(apiEtis ap){
                return ap.getTeacherMessages();
            }

            protected void onPostExecute(String result) {

                if(result != null){
                    LinearLayout Layout = root.findViewById(R.id.teacherMessagesList);

                    Document doc = Jsoup.parse(result);
                    Elements messages = doc.getElementsByClass("msg");
                    for(Element msg : messages){
                        TextView textView = new TextView(getContext());
                        textView.setBackgroundResource(R.drawable.ads);
                        textView.setText(Html.fromHtml(
                                Pattern.compile("[(?:<br>)\\s]+$").matcher( // delete hyphens at the end
                                        msg.html()
                                ).replaceAll("")
                        ));
                        textView.setLinksClickable(true);
                        textView.setMovementMethod(LinkMovementMethod.getInstance());

                        CharSequence text = textView.getText();
                        if (text instanceof Spannable)
                            textView.setText((new MakeLinksClicable()).reformatText(text));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 10, 0, 10);

                        Layout.addView(textView, params);
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