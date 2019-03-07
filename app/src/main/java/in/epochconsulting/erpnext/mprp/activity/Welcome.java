package in.epochconsulting.erpnext.mprp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.epochconsulting.erpnext.mprp.R;

public class Welcome extends AppCompatActivity {

    String my_PREFS_NAME;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);

        SharedPreferences prefs = getSharedPreferences(my_PREFS_NAME, MODE_PRIVATE);
        boolean isServeraddressPresent = prefs.getBoolean("ServeraddressFound_MPRP", false);
        if (isServeraddressPresent) {
            intent = new Intent(Welcome.this, Login.class);
            startActivity(intent);

        } else {
            intent = new Intent(Welcome.this, Configuration.class);
            startActivity(intent);

        }
    }
}
