package com.example.swiftshopper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewProductActivity extends Activity {


    TextView txtBarcode;
    TextView txtName;
    TextView txtBrand;
    TextView txtPrice;
    TextView txtQty;
    TextView txtWt;
    TextView txtDesc;
    TextView txtCreatedAt;

    EditText txtReqQty;
    Button btnAdd;
    Button btnCart;

    String pid;
    String barcode;
    String username = LoginActivity.username;
   // String username = "aman";


    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_product_details = "http://www.amaneight.byethost3.com/get_scanned_product_details.php";
    private static final String url_add_product_tocart = "http://www.amaneight.byethost3.com/add_product_tocart.php";
    // url to update product
    private static final String url_update_product = "http://www.amaneight.byethost3.com/update_product.php";

    // url to delete product
    private static final String url_delete_product = "http://www.amaneight.byethost3.com/delete_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_CARTID = "cartid";
    private static final String TAG_REQQTY = "reqqty";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_BARCODE = "barcode";
    private static final String TAG_NAME = "name";
    private static final String TAG_BRAND = "brand";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_PRICE = "price";
    private static final String TAG_QUANTITY = "quantity";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PRODUCT_NAME = "product_name";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // save button
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnCart = (Button) findViewById(R.id.btnPlaceOrder);

       // txtReqQty = (EditText) findViewById(R.id.inputReqQty);

        Intent login = getIntent();

        // getting product id (pid) from intent
        //username = login.getStringExtra(TAG_USERNAME);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent

       barcode = i.getStringExtra(TAG_BARCODE);

        // Getting complete product details in background thread
        new GetProductDetails().execute();

        // save button click event

      btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new AddProductToCart().execute();
            }
        });
/*
        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                new DeleteProduct().execute();
            }
            /////////////////////////////////////
        });*/

    }

    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewProductActivity.this);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("barcode",barcode));
                       // params.add(new BasicNameValuePair("pid", pid));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text
                            txtBarcode = (TextView) findViewById(R.id.viewBarcode);
                            txtName = (TextView) findViewById(R.id.viewName);
                            txtBrand = (TextView) findViewById(R.id.viewBrand);
                            txtPrice = (TextView) findViewById(R.id.viewPrice);
                            txtQty = (TextView) findViewById(R.id.viewQuantity);
                            txtWt = (TextView) findViewById(R.id.viewWeight);
                            txtDesc = (TextView) findViewById(R.id.viewDesc);



                            // display product data in EditText

                            txtBarcode.setText(product.getString(TAG_BARCODE));
                            txtName.setText(product.getString(TAG_NAME));
                            txtBrand.setText(product.getString(TAG_BRAND));
                            txtPrice.setText(product.getString(TAG_PRICE));
                            txtQty.setText(product.getString(TAG_QUANTITY));
                            txtWt.setText(product.getString(TAG_WEIGHT));
                            txtDesc.setText(product.getString(TAG_DESCRIPTION));
                            //txtReqQty.setText('1');

                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    /**
     * Background Async Task to  Save product Details
     * */
    class  AddProductToCart extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

         @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewProductActivity.this);
            pDialog.setMessage("Adding product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String cartid = "123";
           // String barcode = txtBarcode.getText().toString();
            String product_name = txtName.getText().toString();
            String brand = txtBrand.getText().toString();
            String price = txtPrice.getText().toString();
            String quantity = txtQty.getText().toString();
            //String reqqty = txtReqQty.getText().toString();
            String weight = txtWt.getText().toString();
            String description = txtDesc.getText().toString();

            txtReqQty = (EditText) findViewById(R.id.inputReqQty);
            String reqqty = txtReqQty.getText().toString();



            String s1 = txtPrice.getText().toString();
            Float pro_price= Float.parseFloat(s1);

            String s2 = txtReqQty.getText().toString();
            Float pro_reqqty= Float.parseFloat(s2);


            Float pro_amount= pro_price*pro_reqqty;


            String amount = pro_amount.toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_USERNAME, username));
            params.add(new BasicNameValuePair(TAG_CARTID, cartid));
            params.add(new BasicNameValuePair(TAG_BARCODE, barcode));
            params.add(new BasicNameValuePair(TAG_PRODUCT_NAME, product_name));
            params.add(new BasicNameValuePair(TAG_REQQTY,reqqty));
            params.add(new BasicNameValuePair(TAG_AMOUNT, amount));


            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_add_product_tocart,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated

                    Intent i = new Intent(getApplicationContext(), ViewCart.class);
                    startActivity(i);

                    //Intent i = getIntent();
                    // send result code 100 to notify about product update
                    //setResult(100, i);
                    finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product updated
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeleteProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewProductActivity.this);
            pDialog.setMessage("Deleting Product...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pid", pid));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_product, "POST", params);

                // check your log for json response
                Log.d("Delete Product", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();

        }

    }
}