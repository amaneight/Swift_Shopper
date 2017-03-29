package com.example.swiftshopper;
/**
 * Created by AMAN-PC on 28-02-2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ScanActivity extends Activity  {

    private Button scanBtn;
    private TextView formatTxt, contentTxt;
    private static final String TAG_BARCODE = "barcode";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.scan_code);
      /*  scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);*/

        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }





 /*   public void onClick(View v){
//respond to clicks
      //  if(v.getId()==R.id.scan_button){
//scan
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
       // }

    }*/
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
//we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
        /*    formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
*/
            Intent in = new Intent(getApplicationContext(),
                    ViewProductActivity.class);
            // sending pid to next activity
            in.putExtra(TAG_BARCODE, scanContent);

            // starting new activity and expecting some response back
            startActivityForResult(in, 100);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
