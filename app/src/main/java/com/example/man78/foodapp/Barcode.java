/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.man78.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import cz.msebera.android.httpclient.Header;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class Barcode extends Activity implements View.OnClickListener {


    private static final String connection_url= "jdbc:mysql://frankencluster.com:3306/mdevteam7";
    private static final String user = "mdevteam7admin";
    private static final String pass = "jwH2ny7b.8s";

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView barcodeValue;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView)findViewById(R.id.status_message);
        barcodeValue = (TextView)findViewById(R.id.barcode_value);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.read_barcode).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
    }

    //Database connection and insert food to db
    private class insertFoodData extends AsyncTask <String, Void, Void> {

        private String queryResult;

        String  upcnumber;
        String title;
        String description;
        String brand;


        insertFoodData (String title, String upcnumber, String description, String brand) {
            // list all the parameters like in normal class define
            this.upcnumber = upcnumber;
            this.title = title;
            this.description = description;
            this.brand = brand;
        }


        protected Void doInBackground (String... strings){
            try{
                queryResult = "DB CONN SUCCESS";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(connection_url,user, pass);
                String queryString = "select * from food";
                Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                final ResultSet result = state.executeQuery(queryString);
                result.moveToInsertRow();
                result.updateString("food_upc_code", upcnumber);
                result.updateString("food_name", title);
                result.updateString("food_description", description);
                result.updateString("food_brand", brand);
                result.insertRow();
                result.moveToCurrentRow();
                conn.close();
                queryResult = "REGISTERED";
                //queryResult= result.toString();
            }catch (Exception e){
                e.printStackTrace();
                queryResult = "DIDN'T REGISTER "+ e.toString();
            }
            return null;
        }

        protected void onPostExecute(Void result){
            barcodeValue.setText(queryResult);
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {



        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    com.google.android.gms.vision.barcode.Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    statusMessage.setText(R.string.barcode_success);
                    barcodeValue.setText(barcode.displayValue);

                    AsyncHttpClient client = new AsyncHttpClient();
                    String barcode_id = barcodeValue.getText().toString();
                    String request_url = "https://api.upcdatabase.org/product/"+barcode_id+"/A76D7C543D11C4F5ED995EA21D1BD565";
                    client.get(request_url, new AsyncHttpResponseHandler() {
                        //private String queryResult;
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (responseBody != null){
                                assert barcodeValue != null;
                                String response = new String(responseBody);

                                try {
                                    JSONObject responseArray = new JSONObject(response);
                                    String upcnumber = responseArray.getString("upcnumber");
                                    String title = responseArray.getString("title");
                                    String description = responseArray.getString("description");
                                    String brand = responseArray.getString("brand");

                                    barcodeValue.setText(title+upcnumber+description+brand);

                                    new insertFoodData(title,upcnumber,description,brand).execute();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            //    v.setEnabled(true);
                        }
                    });

                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }
}
