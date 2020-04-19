package com.example.myapplication;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.IOException;

public class Auth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auth);

    }

    @SuppressLint("StaticFieldLeak")
    public void onMyButtonClick(View view) {
        new AsyncAuth().execute();
    }

    class AsyncAuth extends AsyncTask<Void,Void,String> {
        private String surname, password;

        protected void onPreExecute(){
            EditText EditSurname = findViewById(R.id.editText);
            EditText EditPassword = findViewById(R.id.editText2);
            this.surname =  EditSurname.getText().toString();
            this.password = EditPassword.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params)
        {
            apiEtis my = new apiEtis();
            try {
                return my.auth(surname, password);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String session_id)
        {
            if(session_id != null){
                SharedPreferences prefs = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("session_id", session_id);
                editor.putString("surname", surname);
                editor.putString("password", password);
                editor.apply();

                Intent intent = new Intent(Auth.this, MainActivity.class);
                startActivity(intent);
                //finish();
            }
        }
    }
}
