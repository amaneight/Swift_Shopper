package com.example.swiftshopperadmin;

/**
 * Created by AMAN-PC on 28-02-2015.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity {


    // Progress Dialog
    private ProgressDialog pDialog;
    private TextView txtLoginStatus;
    JSONParser jsonParser = new JSONParser();

   /* Calendar c = Calendar.getInstance();
    Integer seconds = c.get(Calendar.SECOND);
    Integer minute = c.get(Calendar.MINUTE);
    Integer hour = c.get(Calendar.HOUR_OF_DAY);
    Integer day = c.get(Calendar.DAY_OF_YEAR);*/


    public static String username;

    EditText inputUsername;
    EditText inputPassword;
    private static final String TAG_PID = "pid";
    private static final String TAG_USERNAME = "username";



    // url for login

    private static String url_login = "http://www.amaneight.byethost3.com/user_login.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        // Edit Text
        inputUsername = (EditText) findViewById(R.id.inputUsername);
        inputPassword = (EditText) findViewById(R.id.inputPassword);


        // Create button
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        // button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CheckCredentials().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CheckCredentials extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Checking Credentials..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            username = inputUsername.getText().toString();
            String password = inputPassword.getText().toString();



            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_login,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product

                    // Starting new intent
                    username = inputUsername.getText().toString();;
                    Intent login = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // sending pid to next activity
                    login.putExtra(TAG_USERNAME, username);

                    // starting new activity and expecting some response back
                    startActivityForResult(login, 100);

                    Intent i = new Intent(getApplicationContext(), ScanActivity.class);
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

            txtLoginStatus = (TextView) findViewById(R.id.viewLoginStatus);
            txtLoginStatus.setVisibility(View.VISIBLE);

            inputPassword = (EditText) findViewById(R.id.inputPassword);
            inputPassword.setText("");

            inputUsername = (EditText) findViewById(R.id.inputUsername);
            inputUsername.setText("");
        }

    }

}
