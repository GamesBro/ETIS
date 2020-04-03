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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.MakeLinksClicable;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class TeachersMessagesFragment extends Fragment {

    public TeachersMessagesFragment() {
        // Required empty public constructor
    }

    private View root;

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_teachers_messages, container, false);

        new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                apiEtis my;
                SharedPreferences prefs = getActivity().getSharedPreferences("mysettings", MODE_PRIVATE);

                if(prefs.contains("session_id")) {
                    my = new apiEtis(prefs.getString("session_id", ""));
                    return  my.getTeacherMessages();
                }
                else
                    my = new apiEtis();

                try {
                    String session_id = my.auth(prefs.getString("surname", ""), prefs.getString("password", ""));
                    if(session_id != null){
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("session_id", session_id);
                        editor.apply();

                        return my.getTeacherMessages();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            protected void onPostExecute(String result)
            {
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