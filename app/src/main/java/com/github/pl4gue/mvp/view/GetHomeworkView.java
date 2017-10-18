package com.github.pl4gue.mvp.view;

import com.github.pl4gue.data.entity.HomeWorkEntry;

import java.util.List;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 14.10.17.
 */

public interface GetHomeworkView extends View {
    void displayLoadingScreen();

    void hideLoadingScreen();

    void updateGSheetsResult(List<HomeWorkEntry> homeWorkEntryList);

    void fetchDataError();

}
