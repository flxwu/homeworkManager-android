package com.github.pl4gue.mvp.view.activity;

import android.os.Bundle;

import com.github.pl4gue.R;
import com.github.pl4gue.navigation.Navigator;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    Navigator navigator = new Navigator();

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.showHomeworkButton)
    public void onShowHomework() {
        navigator.navigateToShowHomeworkPage(this);
    }

    @OnClick(R.id.addHomeworkButton)
    public void onAddHomework() {
        navigator.navigateToAddHomeworkPage(this);
    }
}