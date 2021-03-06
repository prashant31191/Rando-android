package com.github.randoapp.auth;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.github.randoapp.R;
import com.github.randoapp.db.RandoDAO;
import com.github.randoapp.fragment.AuthFragment;
import com.github.randoapp.fragment.EmptyHomeWallFragment;
import com.github.randoapp.service.SyncService;
import com.github.randoapp.view.Progress;

public abstract class BaseAuth implements View.OnClickListener {

    protected final AuthFragment authFragment;

    public BaseAuth(AuthFragment authFragment) {
        this.authFragment = authFragment;
    }

    public static void done(FragmentActivity fragmentActivity) {
        Progress.hide();

        hideSoftKeyboard(fragmentActivity);
        clearDBForChangeAccount(fragmentActivity.getApplicationContext());

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_screen, new EmptyHomeWallFragment()).commit();

        SyncService.run();
    }

    private static void hideSoftKeyboard(FragmentActivity fragmentActivit) {
        InputMethodManager inputManager = (InputMethodManager) fragmentActivit.getSystemService(Context.INPUT_METHOD_SERVICE);
        View textFieldWithSoftKeyboard = fragmentActivit.getCurrentFocus();
        if (textFieldWithSoftKeyboard != null) {
            inputManager.hideSoftInputFromWindow(textFieldWithSoftKeyboard.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private static void clearDBForChangeAccount(Context context) {
        RandoDAO randoDAO = new RandoDAO(context);
        randoDAO.clearRandoPairs();
        randoDAO.close();
    }

}
