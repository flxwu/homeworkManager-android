package com.github.pl4gue.mvp.view;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 16.10.17.
 */

public interface AddHomeworkView extends View {
    void displayLoadingScreen();

    void hideLoadingScreen();

    void fetchDataError();

}
