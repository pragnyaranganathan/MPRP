package in.epochconsulting.erpnext.mprp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.common.BasicActivity;
import in.epochconsulting.erpnext.mprp.fragments.SelectTaskFragment;

import in.epochconsulting.erpnext.mprp.implementation.CustomEventListener;
import in.epochconsulting.erpnext.mprp.model.pojo.SingletonData;

import in.epochconsulting.erpnext.mprp.request_items.model.RequestItemList;
import in.epochconsulting.erpnext.mprp.utils.Constants;
import in.epochconsulting.erpnext.mprp.utils.CustomUrl;
import in.epochconsulting.erpnext.mprp.utils.Utility;

public class Home extends BasicActivity
        implements NavigationView.OnNavigationItemSelectedListener, CustomEventListener {

    TextView toolBarUserName;
    TextView toolBarUserRoleProfile;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolBarUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        toolBarUserRoleProfile = navigationView.getHeaderView(0).findViewById(R.id.nav_user_role);

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            int navigationSelected = extras.getInt("NavigateToSelectTask");
             MenuItem menuItem = (MenuItem)navigationView.getMenu().findItem(navigationSelected);
            menuItem.setChecked(true);
            onNavigationItemSelected(menuItem);
        }





        getLoggedInUserData();
        loadAppropriateMenuItem(R.id.nav_select_task); //load the select task fragment
    }

    private void getLoggedInUserData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String myUrl = Utility.getInstance().buildUrl(CustomUrl.API_METHOD, null, CustomUrl.GET_LOGGED_USER);
        //StringRequest stringRequest1 = new StringRequest()

        StringRequest stringRequest = new StringRequest(Request.Method.GET, myUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    String loggedInUser = object.getString("message");

                    toolBarUserName.setText(loggedInUser);
                    SingletonData.getInstance().setLoggedInUserName(loggedInUser);

                    } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }//end of sucess responseP
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Home.this, error.toString(), Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return Home.this.getHeaders();
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }




    public Map<String, String> getHeaders () {
        Map<String, String> headers = new HashMap<>();
        SharedPreferences prefs = this.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        String userId = prefs.getString(Constants.USER_ID, null);
        String sid = prefs.getString(Constants.SESSION_ID, null);
        headers.put("user_id", userId);
        headers.put("sid", sid);

        return headers;
    }




    private void loadSelectedFragment(int itemId) {
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_select_task:
                fragment = new SelectTaskFragment();
                break;

            case R.id.nav_logout:

                logout();
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_to_be_displayed, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        loadSelectedFragment(item.getItemId());

        return true;
    }

    @Override
    protected void autoLogout() {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //showprogress method call
            }
        });
        destroyTimer();




        if (dialogbox != null) {
            dialogbox.dismiss();
        }
    }



    public void loadAppropriateMenuItem(int menuId)
    {
        MenuItem menuItem = (MenuItem)navigationView.getMenu().findItem(menuId);
        menuItem.setChecked(true);
        onNavigationItemSelected(menuItem);
    }


    @Override
    public void showDialog(String data, RequestItemList list) {

    }

    @Override
    public void showErrorDialog(String message) {

    }

    @Override
    public void showSuccessDialog(String message) {

    }

    @Override
    public void loadAppropriateFragment(int fragmentId) {

    }
}
