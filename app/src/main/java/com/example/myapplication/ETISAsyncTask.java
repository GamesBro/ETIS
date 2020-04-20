package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentActivity;

import static android.content.Context.MODE_PRIVATE;

public abstract class ETISAsyncTask<T> extends AsyncTask<Void, Void, T> {
    protected SharedPreferences prefs;

    public ETISAsyncTask(FragmentActivity f){
        prefs = f.getSharedPreferences("mysettings", MODE_PRIVATE);
    }

    protected abstract T doInBackgroundWithReauth(apiEtis ap);

    @Override
    protected T doInBackground(Void... params){

        if(prefs.contains("session_id")) {
            T temp = doInBackgroundWithReauth(new apiEtis(prefs.getString("session_id", "")));
            if(temp != null)
                return temp;
        }

        apiEtis my = new apiEtis();
        apiEtis.ResultAuth resAuth  = my.auth(prefs.getString("surname", ""), prefs.getString("password", ""));
        if(!resAuth.error){
            prefs.edit().putString("session_id", resAuth.token).apply();
            return doInBackgroundWithReauth(my);
        }
        else{
            // обработка неудачной авторизации
        }
        return null;
    }
}
