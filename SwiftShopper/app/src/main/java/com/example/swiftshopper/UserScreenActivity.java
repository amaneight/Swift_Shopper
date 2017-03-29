package com.example.swiftshopper;

/**
 * Created by AMAN-PC on 28-02-2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserScreenActivity extends Activity{

    Button btnScanProduct;
    Button btnCurrentCart;
    Button btnPreviousPurchases;
    Button btnLogout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);

        // Buttons
        btnScanProduct = (Button) findViewById(R.id.btnScanProduct);
        btnCurrentCart = (Button) findViewById(R.id.btnCurrentCart);
        btnPreviousPurchases = (Button) findViewById(R.id.btnPreviousPurchases);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        // view products click event
        btnScanProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), ScanActivity.class);
                startActivity(i);

            }
        });

        // view products click event
        btnCurrentCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), ViewCart.class);
                startActivity(i);

            }
        });
    }
}