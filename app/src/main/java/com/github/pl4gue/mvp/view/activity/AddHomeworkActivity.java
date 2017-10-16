package com.github.pl4gue.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.github.pl4gue.R;
import com.github.pl4gue.data.entity.HomeWorkEntry;
import com.github.pl4gue.mvp.view.AddHomeworkView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 16.10.17.
 */

public class AddHomeworkActivity extends BaseActivity implements AddHomeworkView {

    @BindView(R.id.addHomeworkEditText)
    EditText mHomeworkEditText;

    @BindView(R.id.addHomeworkSubjectEditText)
    EditText mHomeworkSubjectEditText;

    @BindView(R.id.addHomeworkDueDateEditText)
    EditText mHomeworkDueDateEditText;

    @org.jetbrains.annotations.Contract("_ -> !null")
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AddHomeworkActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.addHomeworkDueDateEditText)
    public void selectDueDate() {
        datePickerDialog(Calendar.getInstance(),AddHomeworkActivity.this,mHomeworkDueDateEditText);
    }


    @Override
    public void displayLoadingScreen() {
        ProgressDialogManager.startProgressDialog(getString(R.string.loading));
    }

    @Override
    public void hideLoadingScreen() {
        ProgressDialogManager.stopProgressDialog();
    }

    @Override
    public void updateGSheetsBy(HomeWorkEntry homeWorkEntry) {

    }

    @Override
    public void fetchDataError() {
    }
}
