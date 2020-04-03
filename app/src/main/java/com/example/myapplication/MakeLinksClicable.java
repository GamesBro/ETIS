package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;

public class MakeLinksClicable
{
    private static final String LOG = MakeLinksClicable.class.getSimpleName();
    private static String def;

    public MakeLinksClicable(){}

    public MakeLinksClicable(String def){
        this.def = def;
    }

    public class CustomerTextClick extends ClickableSpan
    {
        String mUrl;

        public CustomerTextClick(String url)
        {
            if(!url.matches("^((https?)\\:\\/\\/)?([a-z0-9]{1})((\\.[a-z0-9-])|([a-z0-9-]))*\\.([a-z]{2,6})(\\/?)$"))
                mUrl = def;
            mUrl += url;
        }

        @Override
        public void onClick(View widget)
        {
            //Тут можно как-то обработать нажатие на ссылку
            //Сейчас же мы просто открываем браузер с ней
            //Log.i(LOG, "url clicked: " + this.mUrl);

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mUrl));
            widget.getContext().startActivity(i);
        }
    }

    public SpannableStringBuilder reformatText(CharSequence text)
    {
        int end = text.length();
        Spannable sp = (Spannable) text;
        URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        for (URLSpan url : urls)
        {
            style.removeSpan(url);
            MakeLinksClicable.CustomerTextClick click = new MakeLinksClicable.CustomerTextClick(url.getURL());
            style.setSpan(click, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return style;
    }
}
