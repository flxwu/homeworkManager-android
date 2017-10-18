package com.github.pl4gue.mvp.view;

import com.github.pl4gue.data.entity.HomeWorkEntry;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 16.10.17.
 */

public interface AddHomeworkView extends View {
    void displayLoadingScreen();

    void hideLoadingScreen();

    void updateGSheetsBy(HomeWorkEntry homeWorkEntry);

    void fetchDataError();

    void showError();

}
