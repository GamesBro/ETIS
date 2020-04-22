package com.example.myapplication.ui.menu.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.AuthActivity;
import com.example.myapplication.ETISAsyncTask;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.apiEtis;

public class SettingFragment extends Fragment {

    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_setting, container, false);

        androidx.appcompat.widget.Toolbar mainToolbar = root.findViewById(R.id.toolbar);
        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        Button changePassword = root.findViewById(R.id.buttonEditPassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override @SuppressLint("StaticFieldLeak")
            public void onClick(View v) {
                new ETISAsyncTask<String>(getActivity()){
                    String oldPass;
                    String newPass;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                        EditText EToldPass = root.findViewById(R.id.editTextOldPassword);
                        EditText ETnewPass = root.findViewById(R.id.editTextNewPassword);
                        EditText ETnewPass2 = root.findViewById(R.id.editTextNewPassword2);
                        if(ETnewPass.getText().toString().equals(ETnewPass2.getText().toString()) && ETnewPass.getText().length() > 0 && EToldPass.getText().length() > 0){
                            this.oldPass = EToldPass.getText().toString();
                            this.newPass = ETnewPass.getText().toString();
                        }
                        else{
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("Форма заполнена неверно")
                                    .setTitle("Ошибка")
                                    .create().show();
                            this.cancel(false);
                        }
                    }

                    @Override
                    protected String doInBackgroundWithReauth(apiEtis ap){
                        return ap.changePassword(oldPass, newPass);
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if(result != null){
                            new AlertDialog.Builder(getActivity())
                                    .setMessage(result)
                                    .setTitle("")
                                    .create().show();
                        }
                    }
                }.execute();
            }
        });

        Button Quit = root.findViewById(R.id.buttonQuit);
        Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSharedPreferences("mysettings", Context.MODE_PRIVATE).edit().clear().apply();
                startActivity(new Intent(getActivity(), AuthActivity.class));
            }
        });

        return root;
    }
}
