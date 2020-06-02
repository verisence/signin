package com.verisence.signin.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.verisence.signin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmailAccountActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    ProgressDialog pd;

    @BindView(R.id.registeredEmail)
    TextView registeredEmail;
    @BindView(R.id.emailAccountBtn)
    Button emailAccountBtn;
    @BindView(R.id.emailPassword)
    EditText emailPassword;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;


    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_account);
        ButterKnife.bind(this);

        emailAccountBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        email = getIntent().getStringExtra("email");

        createProgressDialog();

        registeredEmail.setText("You've already used "+email+" to sign in. Enter your password for that account.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(EmailAccountActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    private void createProgressDialog() {
        pd = new ProgressDialog(this);
        pd.setTitle("Authenticating");
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if (v==emailAccountBtn){
            loginWithPassword();
        }
    }

    private void loginWithPassword() {
        String password = emailPassword.getText().toString().trim();
        if (password.equals("")) {
            emailPassword.setError("Password cannot be blank");
            return;
        }

        pd.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(EmailAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
