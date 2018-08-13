package sg.edu.rp.c346.smsapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etTo;
    EditText etContent;
    Button btnSend;
    Button btnViaMsg;
    BroadcastReceiver br = new MessageReceiver(); // refer to the java class (AnotherBroadcastReceiver)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etTo = findViewById(R.id.editTextTo);
        etContent = findViewById(R.id.editTextContent);
        btnSend = findViewById(R.id.button);
        btnViaMsg = findViewById(R.id.buttonviaMsg);

        checkPermission();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = etTo.getText().toString();
                String content = etContent.getText().toString();

                    String [] lstnum  = to.split(", ");

                    for (int i = 0 ; i<lstnum.length; i++){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(lstnum[i], null, content, null, null);

                    }





                etContent.setText(null);
                Toast.makeText(getApplicationContext(),"Message Sent!", Toast.LENGTH_LONG).show();
            }
        });

        btnViaMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = etTo.getText().toString();
                String content = etContent.getText().toString();


                Intent it = new Intent(Intent.ACTION_SENDTO);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.setData(Uri.parse("smsto:"+to));
                it.putExtra("sms_body", content);
                startActivity(it);

            }
        });
    }

    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }

}
