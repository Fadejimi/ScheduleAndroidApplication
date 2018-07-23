package app.presenter;

import android.content.Intent;

import app.activities.LoginActivity;

public interface SignInPresenter {
    void createGoogleClient (LoginActivity loginView);
    void onStart();
    void signIn(LoginActivity loginView);
    void onActivityResult (LoginActivity loginView, int requestCode, int resultCode, Intent data);
    void onStop ();
    void onDestroy();
}
