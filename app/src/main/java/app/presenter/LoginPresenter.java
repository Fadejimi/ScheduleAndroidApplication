package app.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import app.activities.LoginActivity;
import app.model.Constants;
import app.model.UserInfo;
import app.rest.APIClient;
import app.rest.UserService;
import app.rest.model.User;
import app.view.LoginView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements SignInPresenter, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LoginPresenter.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private UserInfo userModelSingleton;
    private LoginView fieldLoginView;

    public LoginPresenter(LoginView view) {
        this.fieldLoginView = view;
    }

    @Override
    public void createGoogleClient(LoginActivity loginView) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(loginView)
                .enableAutoManage(loginView, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Log.d(TAG, "createGoogleClient()");
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void signIn(LoginActivity loginView) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        loginView.startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG, "signIn()");
    }

    private void handleSignInResult(GoogleSignInResult result, LoginActivity loginView) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            final String personName = acct.getDisplayName();
            final String personEmail = acct.getEmail();
            final String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            userModelSingleton = UserInfo.getInstance();
            userModelSingleton.setName(personName);
            userModelSingleton.setEmail(personEmail);
            userModelSingleton.setAvatar(personPhoto.toString());
            userModelSingleton.setToken(personId);
            userModelSingleton.setLoginStatus(Constants.LOGIN_IN);

            User user = new User();
            user.setName(personName);
            user.setEmail(personEmail);

            UserService userService = APIClient.createService(UserService.class, personId);
            Call<Void> call = userService.addUser(user);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d(TAG, "Name: " + personName + "\nEmail: " + personEmail + "\nId: " +
                        personId);
                    fieldLoginView.startMainActivity();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    //progressDialog.dismiss();
                    fieldLoginView.showToast("Could not login for the user");
                }
            });

        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
            fieldLoginView.showToast("Could not sign in ");
        }
    }


    @Override
    public void onActivityResult(LoginActivity loginView, int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result, loginView);
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroy() {
        fieldLoginView = null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
