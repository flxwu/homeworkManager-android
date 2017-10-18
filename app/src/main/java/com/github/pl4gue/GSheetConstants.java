package com.github.pl4gue;

import com.google.api.services.sheets.v4.SheetsScopes;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 15.10.17.
 */

public class GSheetConstants {
    public static final String[] SCOPES = {SheetsScopes.SPREADSHEETS_READONLY};

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    public static final String PREF_ACCOUNT_NAME = "accountName";

    //Indexes of columns
    public final static int COLUMN_ENTRY = 0;
    public final static int COLUMN_SUBJECT = 1;
    public final static int COLUMN_HOMEWORK = 2;
    public final static int COLUMN_DUEDATE = 3;
    public final static int COLUMN_COMMENTS = 4;


    //HashMap Keys for homeworkentries
    public final static int KEY_HOMEWORK_SUBJECT = 0;
    public final static int KEY_HOMEWORK_ENTRY = 1;
    public final static int KEY_HOMEWORK_DUE = 2;
    public final static int KEY_HOMEWORK = 3;

}
