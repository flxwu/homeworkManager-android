package com.github.pl4gue;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 15.10.17.
 */

public class GSheetConstants {
    public static int ADDITIONAL_INFO_ROW = 13;

    public final static int INDEX_HOMEWORK_SUBJECT = 0;
    public final static int INDEX_HOMEWORK_ENTRY = 1;
    public final static int INDEX_HOMEWORK_DUE = 2;
    public final static int INDEX_HOMEWORK = 3;
    public final static int INDEX_LAB_SUBJECT = 4;

    public static void setAdditionalInfoRow(int additionalInfoRow) {
        ADDITIONAL_INFO_ROW = additionalInfoRow;
    }
}
