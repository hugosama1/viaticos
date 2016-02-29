package com.coppel.viaticos;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        final EditText txtPassword =(EditText)view.findViewById(R.id.txtPassword);
        final EditText txtEmail =(EditText)view.findViewById(R.id.txtEmail);
        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                FetchViaticosTask task = new FetchViaticosTask(getContext());
                String user = txtEmail.getText().toString();
                String pass = txtPassword.getText().toString();
                String action = "login";
                task.execute(action,user,pass);
                return false;
            }
        });
        return view;
    }
}
