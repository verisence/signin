package com.verisence.signin.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.verisence.signin.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmailCheckActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.emailEditText)
    EditText emailET;
    @BindView(R.id.emailCheckBtn)
    Button emailCheckBtn;
    @BindView(R.id.methods)
    TextView methods;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_check);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        emailCheckBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==emailCheckBtn){
            checkEmail();
        }
    }

    private void checkEmail() {
        auth.fetchSignInMethodsForEmail(emailET.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).isEmpty();

                        if (check){
                            Intent intent = new Intent(EmailCheckActivity.this, CreateAccountActivity.class);
                            intent.putExtra("email", emailET.getText().toString());
                            startActivity(intent);
                        }else if(task.getResult().getSignInMethods().contains("google.com")){
                            Intent intent = new Intent(EmailCheckActivity.this, GoogleAccountActivity.class);
                            intent.putExtra("email", emailET.getText().toString());
                            startActivity(intent);
                        }else if(task.getResult().getSignInMethods().contains("password")){
                            Intent intent = new Intent(EmailCheckActivity.this, EmailAccountActivity.class);
                            intent.putExtra("email", emailET.getText().toString());
                            startActivity(intent);
                        }
                    }
                });
    }
}
