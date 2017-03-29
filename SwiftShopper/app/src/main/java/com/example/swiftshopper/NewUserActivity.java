package com.example.swiftshopper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AMAN-PC on 05-03-2015.
 */
public class NewUserActivity extends Activity{

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    EditText inputName;
    EditText inputUserName;
    EditText inputPassword;
    EditText inputContact;
    EditText inputAddress;


    // url to create new product
    private static String url_create_product = "http://www.amaneight.byethost3.com/create_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputUserName = (EditText) findViewById(R.id.inputUserName);
        inputPassword =(EditText) findViewById(R.id.inputPassword);
        inputContact = (EditText) findViewById(R.id.inputContact);
        inputAddress = (EditText) findViewById(R.id.inputAddress);


        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnRegister);

        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewProduct().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewUserActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String barcode = inputBarcode.getText().toString();
            String name = inputName.getText().toString();
            String brand = inputBrand.getText().toString();
            String price = inputPrice.getText().toString();
            String quantity = inputQty.getText().toString();
            String weight = inputWt.getText().toString();
            String description = inputDesc.getText().toString();


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("barcode", barcode));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("brand", brand));
            params.add(new BasicNameValuePair("price", price));
            params.add(new BasicNameValuePair("quantity", quantity));
            params.add(new BasicNameValuePair("weight", weight));
            params.add(new BasicNameValuePair("description", description));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), NewUserActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
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
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }

}
