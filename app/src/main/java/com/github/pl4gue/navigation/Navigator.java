package com.github.pl4gue.navigation;

import android.content.Context;
import android.content.Intent;

import com.github.pl4gue.mvp.view.activity.GSheetsActivity;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 14.10.17.
 */

public class Navigator {

    public void navigateToGSheetsPage(Context context) {
        if (context != null) {
            Intent intentToLaunch = GSheetsActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }
}
