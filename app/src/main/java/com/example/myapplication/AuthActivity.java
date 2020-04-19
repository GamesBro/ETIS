package com.example.myapplication;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auth);
    }

    public void onMyButtonClick(View view) {
        new AsyncAuth().execute();
    }

    @SuppressLint("StaticFieldLeak")
    class AsyncAuth extends AsyncTask<Void,Void,apiEtis.ResultAuth> {
        private String surname, password;

        protected void onPreExecute() {
            super.onPreExecute();

            EditText EditSurname = findViewById(R.id.editText);
            EditText EditPassword = findViewById(R.id.editText2);
            this.surname =  EditSurname.getText().toString();
            this.password = EditPassword.getText().toString();
        }

        protected apiEtis.ResultAuth doInBackground(Void... params)
        {
            return new apiEtis().auth(surname, password);
        }

        protected void onPostExecute(apiEtis.ResultAuth resAuth)
        {
            if(!resAuth.error){
                SharedPreferences prefs = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("session_id", resAuth.token);
                editor.putString("surname", surname);
                editor.putString("password", password);
                editor.apply();

                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(intent);
                //finish();
            }
            else{
                AlertDialog.Builder alertDialog;
                alertDialog = new AlertDialog.Builder(AuthActivity.this);

                alertDialog.setMessage(resAuth.errorString).setTitle("Ошибка");

                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        }

    }
}