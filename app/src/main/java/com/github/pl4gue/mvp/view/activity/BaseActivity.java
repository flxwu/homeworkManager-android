package com.github.pl4gue.mvp.view.activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.github.pl4gue.R;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 14.10.17.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private int counter = 0;

    protected void setUpProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(100);
    }

    protected void startProgressDialog(String title) {
        if (mProgressDialog != null) {
            mProgressDialog.setMessage(title);
            mProgressDialog.show();
            progressBarStatus = 0;
            counter = 0;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progressBarStatus < 100) {
                        progressBarStatus = counter;
                        counter += 1;
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progressBarHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mProgressDialog.setProgress(progressBarStatus);
                                                    }
                                                }
                        );
                    }
                }
            }).start();
        }
    }

    protected void stopProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

}
