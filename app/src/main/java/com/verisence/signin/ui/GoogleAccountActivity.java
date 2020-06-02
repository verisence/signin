package com.verisence.signin.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.verisence.signin.R;

import butterknife.BindView;

public class GoogleAccountActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.registeredGoogle)
    TextView registeredTV;
    @BindView(R.id.googleAccountBtn)
    Button googleAccountBtn;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_account);

        email = getIntent().getStringExtra("email");

        registeredTV.setText("You've already used "+email+", Sign in with Google to continue.");
    }

    @Override
    public void onClick(View v) {

    }
}
