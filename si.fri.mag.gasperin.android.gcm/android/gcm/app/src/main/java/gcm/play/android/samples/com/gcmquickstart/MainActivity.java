/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gcm.play.android.samples.com.gcmquickstart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private static TextView mInformationTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonDeleteHistory = (Button) findViewById(R.id.delete_history_button);
        buttonDeleteHistory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SQLiteDatabase mydatabase = openOrCreateDatabase("DB", MODE_PRIVATE, null);
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS GCMMessages(_id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR);");
                mydatabase.execSQL("DROP TABLE GCMMessages;");
                ListView gcmListView = (ListView) findViewById( R.id.gcmListView );
                gcmListView.setAdapter(null);
            }

        });

        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    mInformationTextView.setText("History:");
                } else {
                    mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };

        mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

        SQLiteDatabase mydatabase = openOrCreateDatabase("DB", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS GCMMessages(_id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR);");

        Cursor c = mydatabase.rawQuery("SELECT message FROM GCMMessages ORDER BY _id DESC", new String[]{});

        ListView gcmListView = (ListView) findViewById( R.id.gcmListView );

        // Create ArrayAdapter using the planet list.
        HistoryAdapter listAdapter = new HistoryAdapter(this);
        while (c.moveToNext()) {
            String msg = c.getString(c.getColumnIndex("message"));
            try {
                JSONObject jsonValue = new JSONObject(msg);

                listAdapter.add(new SensorData(
                        jsonValue.get("title").toString(),
                        jsonValue.get("systolic").toString(),
                        jsonValue.get("diastolic").toString(),
                        jsonValue.get("x").toString(),
                        jsonValue.get("y").toString(),
                        jsonValue.get("z").toString())
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Set the ArrayAdapter as the ListView's adapter.
        gcmListView.setAdapter( listAdapter );

        c.close();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }



    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
