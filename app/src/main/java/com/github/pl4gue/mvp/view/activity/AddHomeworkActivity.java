package com.github.pl4gue.mvp.view.activity;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.pl4gue.R;
import com.github.pl4gue.data.entity.HomeWorkEntry;
import com.github.pl4gue.mvp.presenter.AddHomeworkPresenter;
import com.github.pl4gue.mvp.view.AddHomeworkView;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.pl4gue.GSheetConstants.PREF_ACCOUNT_NAME;
import static com.github.pl4gue.GSheetConstants.REQUEST_ACCOUNT_PICKER;
import static com.github.pl4gue.GSheetConstants.REQUEST_AUTHORIZATION;
import static com.github.pl4gue.GSheetConstants.REQUEST_GOOGLE_PLAY_SERVICES;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 16.10.17.
 */

public class AddHomeworkActivity extends BaseActivity implements AddHomeworkView {

    GoogleAccountCredential mCredential;

    HomeWorkEntry entryToAdd;

    AddHomeworkPresenter mAddHomeworkPresenter;

    @BindView(R.id.addHomeworkEditText)
    EditText mHomeworkEditText;
    @BindView(R.id.addHomeworkSubjectAutoCompleteText)
    EditText mHomeworkSubjectEditText;
    @BindView(R.id.addHomeworkDueDateEditText)
    EditText mHomeworkDueDateEditText;
    @BindView(R.id.addHomeworkCommentEditText)
    EditText mHomeworkCommentEditText;

    @BindView(R.id.addHomeworkSubmitButton)
    Button mHomeworkSubmitButton;

    @BindView(R.id.addHomeworkLinearLayout)
    LinearLayout mHomeworkLinearLayout;

    @org.jetbrains.annotations.Contract("_ -> !null")
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AddHomeworkActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);
        ButterKnife.bind(this);
        mAddHomeworkPresenter = new AddHomeworkPresenter();
        mAddHomeworkPresenter.attachView(this);
        mAddHomeworkPresenter.initialize(this,mHomeworkLinearLayout);
        //TODO Autocomplete Adapter

        DialogManagers.ProgressDialogManager.setUpProgressDialog(AddHomeworkActivity.this);
    }

    @OnClick(R.id.addHomeworkDueDateEditText)
    public void selectDueDate() {
        DialogManagers.DatePickerManager.datePickerDialog(Calendar.getInstance(), AddHomeworkActivity.this, mHomeworkDueDateEditText);
    }

    @OnClick(R.id.addHomeworkSubmitButton)
    public void submitHomework() {
        if (!checkRequiredFilled()) {
            showError("Please fill in all required fields",this);
        } else {
            entryToAdd = getNewHomeworkEntry();
            // Initialize credentials and service object.
            mAddHomeworkPresenter.next(entryToAdd);
        }
    }

    private boolean checkRequiredFilled() {
        if (mHomeworkDueDateEditText.getText().toString().trim().equals("")
                || mHomeworkSubjectEditText.getText().toString().trim().equals("")
                || mHomeworkEditText.getText().toString().trim().equals("")) {
            return false;
        }
        return true;
    }

    private HomeWorkEntry getNewHomeworkEntry() {
        String myFormat = "EEE, dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);
        String entryDate = sdf.format(new Date());

        String subject = mHomeworkSubjectEditText.getText().toString();
        String homework = mHomeworkEditText.getText().toString();
        String dueDate = mHomeworkDueDateEditText.getText().toString();
        String comment = mHomeworkCommentEditText.getText().toString().trim().equals("") ? " - " : mHomeworkCommentEditText.getText().toString();
        return new HomeWorkEntry(entryDate, subject, homework, dueDate, comment);
    }

    @Override
    public void displayLoadingScreen() {
        DialogManagers.ProgressDialogManager.startProgressDialog(getString(R.string.loading));
    }

    @Override
    public void hideLoadingScreen() {
        DialogManagers.ProgressDialogManager.stopProgressDialog();
    }

    @Override
    public void fetchDataError() {
    }


    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    showError(" This app requires Google Play Services. Please install Google Play Services on your device and relaunch this app.",this);
                } else {
                    mAddHomeworkPresenter.next(entryToAdd);
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        mAddHomeworkPresenter.next(entryToAdd);
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    mAddHomeworkPresenter.next(entryToAdd);
                }
                break;
            default:
                showError("Unknown Error",this);
        }
    }

    public void disableFields() {
        mHomeworkSubjectEditText.setEnabled(false);
        mHomeworkSubjectEditText.setFocusable(false);
        mHomeworkDueDateEditText.setEnabled(false);
        mHomeworkDueDateEditText.setFocusable(false);
        mHomeworkEditText.setEnabled(false);
        mHomeworkEditText.setFocusable(false);
        mHomeworkCommentEditText.setEnabled(false);
        mHomeworkCommentEditText.setFocusable(false);
        mHomeworkSubmitButton.setEnabled(false);
    }

    public void enableFields() {
        mHomeworkSubjectEditText.setEnabled(true);
        mHomeworkSubjectEditText.setFocusable(true);
        mHomeworkDueDateEditText.setEnabled(true);
        mHomeworkDueDateEditText.setFocusable(true);
        mHomeworkEditText.setEnabled(true);
        mHomeworkEditText.setFocusable(true);
        mHomeworkCommentEditText.setEnabled(true);
        mHomeworkCommentEditText.setFocusable(true);
        mHomeworkSubmitButton.setEnabled(true);
    }
}
