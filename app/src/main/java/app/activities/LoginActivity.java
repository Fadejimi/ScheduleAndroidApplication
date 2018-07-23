package app.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.common.SignInButton;


import app.model.UserInfo;
import app.presenter.LoginPresenter;
import app.presenter.SignInPresenter;
import app.scheduler.R;
import app.view.LoginView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginView {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private View mProgressView;

    private SignInButton googleBtn;
    private SignInPresenter signInPresenter;
    private UserInfo userModelSingelton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userModelSingelton = UserInfo.getInstance();
        //userModelSingelton.readCash(this);

        bindViews();
        signInPresenter = new LoginPresenter(this);
        signInPresenter.createGoogleClient(this);

        googleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signInPresenter.signIn(LoginActivity.this);
            }
        });
    }



    private void bindViews() {
        googleBtn = findViewById(R.id.google_sign_in);
        googleBtn.setSize(SignInButton.SIZE_STANDARD);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public void showToast(String mssg) {
        Toast.makeText(this, mssg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        signInPresenter.onDestroy();
    }
}

