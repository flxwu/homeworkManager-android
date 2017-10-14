package com.github.pl4gue.mvp.presenter;

import com.github.pl4gue.data.entity.HomeWorkEntry;
import com.github.pl4gue.mvp.view.GSheetsView;
import com.github.pl4gue.mvp.view.View;

import java.util.List;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 14.10.17.
 */

public class GSheetsPresenter implements Presenter {

    GSheetsView mView;

    public void showNext(List<HomeWorkEntry> homeWorkEntryList) {
        mView.displayLoadingScreen();
        nextRepoList(homeWorkEntryList);
    }

    private void nextRepoList(List<HomeWorkEntry> homeWorkEntryList) {
        mView.hideLoadingScreen();
        mView.updateGSheetsResult(homeWorkEntryList);
    }

    @Override
    public void attachView(View v) {
        mView = (GSheetsView) v;
    }
}
