package com.github.pl4gue.mvp.view.activity;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.github.pl4gue.R;
import com.github.pl4gue.adapters.AddHomeworkAdapter;
import com.github.pl4gue.data.entity.HomeWorkEntry;
import com.github.pl4gue.mvp.presenter.AddHomeworkPresenter;
import com.github.pl4gue.mvp.view.GetHomeworkView;
import com.github.pl4gue.mvp.view.util.DividerItemDecoration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.github.pl4gue.GSheetConstants.ADDITIONAL_INFO_ROW;
import static com.github.pl4gue.GSheetConstants.INDEX_HOMEWORK;
import static com.github.pl4gue.GSheetConstants.INDEX_HOMEWORK_DUE;
import static com.github.pl4gue.GSheetConstants.INDEX_HOMEWORK_ENTRY;
import static com.github.pl4gue.GSheetConstants.INDEX_HOMEWORK_SUBJECT;
import static com.github.pl4gue.GSheetConstants.PREF_ACCOUNT_NAME;
import static com.github.pl4gue.GSheetConstants.REQUEST_ACCOUNT_PICKER;
import static com.github.pl4gue.GSheetConstants.REQUEST_AUTHORIZATION;
import static com.github.pl4gue.GSheetConstants.REQUEST_GOOGLE_PLAY_SERVICES;
import static com.github.pl4gue.GSheetConstants.REQUEST_PERMISSION_GET_ACCOUNTS;
import static com.github.pl4gue.GSheetConstants.SCOPES;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 14.10.17.
 */

public class GetHomeworkActivity extends BaseActivity implements GetHomeworkView, EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;

    AddHomeworkPresenter mPresenter;
    AddHomeworkAdapter mAdapter;

    private List<HomeWorkEntry> mHomeworkList;

    @BindView(R.id.getHomeworkRecyclerView)
    RecyclerView mHomeworkListRecyclerView;

    @org.jetbrains.annotations.Contract("_ -> !null")
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, GetHomeworkActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_homework);
        ButterKnife.bind(this);
        mPresenter = new AddHomeworkPresenter();

        ProgressDialogManager.setUpProgressDialog(GetHomeworkActivity.this);
        mPresenter.attachView(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mHomeworkListRecyclerView.setLayoutManager(mLayoutManager);
        mHomeworkListRecyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider)));
        mAdapter = new AddHomeworkAdapter(mHomeworkList);
        mHomeworkListRecyclerView.setAdapter(mAdapter);

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        getResultsFromApi();
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
    public void updateGSheetsResult(List<HomeWorkEntry> homeWorkEntryList) {
        mHomeworkList = homeWorkEntryList;
        mAdapter = new AddHomeworkAdapter(mHomeworkList);
        mHomeworkListRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void fetchDataError() {
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */

    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            showError("No network connection available.");
        } else {
            new GetHomeworkActivity.MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
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
                    showError(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
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
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
            default:
                Toast.makeText(this, "Unknown Error", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     *
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                GetHomeworkActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<HashMap<Integer, String>>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Homework Manager")
                    .build();
        }

        /**
         * Background task to call Google Sheets API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<HashMap<Integer, String>> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         *
         * @return List of names and majors
         * @throws IOException
         */
        private List<HashMap<Integer, String>> getDataFromApi() throws IOException {
            String spreadsheetId = "1XxkZd4iFSV-itiArqJl9ALh_f1ELzTf1nvH97KbOV70";
            //String range = "Class Data!A2:E";
            String range = "Tabellenblatt1";
            ValueRange response = this.mService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            List<HashMap<Integer, String>> results = new ArrayList<>();
            if (values != null) {
                //First row, Column titles
                List<Object> firstrow = values.remove(0);
                HashMap<Integer, String> temp = new HashMap<>();
                temp.put(INDEX_HOMEWORK_SUBJECT, getString(R.string.defaultSubject));
                temp.put(INDEX_HOMEWORK, getString(R.string.defaultHomework));
                temp.put(INDEX_HOMEWORK_ENTRY, getString(R.string.defaultEntryDate));
                temp.put(INDEX_HOMEWORK_DUE, getString(R.string.defaultDueDate));
                results.add(temp);
                //
                for (List<Object> row : values) {
                    String additionalInfo = row.get(ADDITIONAL_INFO_ROW).toString();
                    String entrydate = row.get(0).toString();
                    String duedate = "", subject = "", homework = "", lab = "";
                    for (int i = 1; i < ADDITIONAL_INFO_ROW; i += 3) {
                        homework = row.get(i).toString();
                        subject = firstrow.get(i).toString();
                        duedate = getString(R.string.defaultDueDate_nextLesson);
                        newEntry(results,subject,entrydate,duedate,homework);
                        lab = row.get(i + 1).toString();
                        subject = firstrow.get(i + 1).toString();
                        duedate = row.get(i + 2).toString();
                        newEntry(results,subject,entrydate,duedate,lab);
                    }
                }
            }
            return results;
        }

        @Override
        protected void onPreExecute() {
            showError("");
            displayLoadingScreen();
        }

        @Override
        protected void onPostExecute(List<HashMap<Integer, String>> output) {
            hideLoadingScreen();
            if (output == null || output.size() == 0) {
                showError("No results returned.");
            } else {
                showHomework(output);
            }
        }

        @Override
        protected void onCancelled() {
            hideLoadingScreen();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    showError("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                showError("Request cancelled.");
            }
        }
    }

    private void newEntry(List<HashMap<Integer, String>> results,String subject,String entrydate,String duedate,String homework) {
        if (homework != null && !homework.equals("")) {
            HashMap<Integer,String> temp = new HashMap<>();
            temp.put(INDEX_HOMEWORK, homework);
            temp.put(INDEX_HOMEWORK_DUE, duedate);
            temp.put(INDEX_HOMEWORK_ENTRY, entrydate);
            temp.put(INDEX_HOMEWORK_SUBJECT, subject);
            results.add(temp);
        }
    }

    private void showError(String msg) {
        List<HomeWorkEntry> list = new ArrayList<>();
        HomeWorkEntry entry = new HomeWorkEntry();
        entry.setHomeworkSubject(msg);
        entry.setHomeworkDueDate("");
        entry.setHomework("");
        entry.setHomeworkEntryDate("");
        list.add(entry);
        mPresenter.showNext(list);
    }

    private void showHomework(List<HashMap<Integer, String>> list) {
        List<HomeWorkEntry> temp = new ArrayList<>();
        for (HashMap<Integer, String> homeworkEntrylist : list) {
            HomeWorkEntry entry = new HomeWorkEntry();
            entry.setHomeworkSubject(homeworkEntrylist.get(INDEX_HOMEWORK_SUBJECT));
            entry.setHomeworkEntryDate(homeworkEntrylist.get(INDEX_HOMEWORK_ENTRY));
            entry.setHomeworkDueDate(homeworkEntrylist.get(INDEX_HOMEWORK_DUE));
            entry.setHomework(homeworkEntrylist.get(INDEX_HOMEWORK));
            temp.add(entry);
        }
        mPresenter.showNext(temp);
    }
}
