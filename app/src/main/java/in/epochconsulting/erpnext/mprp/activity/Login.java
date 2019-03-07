package in.epochconsulting.erpnext.mprp.activity;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.common.BasicActivity;
import in.epochconsulting.erpnext.mprp.utils.CustomUrl;
import in.epochconsulting.erpnext.mprp.fragments.LoginFragment;

public class Login extends BasicActivity {

    String my_PREFS_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs = getSharedPreferences(my_PREFS_NAME, MODE_PRIVATE);
        String serverAddress = prefs.getString("serverAddress_MPRP", null);

        if(serverAddress!=null){
            CustomUrl.setServerAddress(serverAddress);
        }

        callloginFragment();

    }

    @Override
    protected void autoLogout() {
        //do nothng
    }

    private void callloginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        addFragment(loginFragment);

    }


    public void addFragment(Fragment fragment1) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.login_content, fragment1);
        fragmentTransaction.commitAllowingStateLoss();

    }
}
