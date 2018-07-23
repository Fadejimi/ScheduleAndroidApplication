package app.view;

import android.content.Context;

public interface LoginView {
    void startMainActivity();
    Context getContext();
    void showToast(String mssg);
}
