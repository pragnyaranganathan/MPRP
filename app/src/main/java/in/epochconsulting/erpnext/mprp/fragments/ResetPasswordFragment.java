package in.epochconsulting.erpnext.mprp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.activity.Login;
import in.epochconsulting.erpnext.mprp.common.BaseFragment;
import in.epochconsulting.erpnext.mprp.utils.CustomUrl;
import in.epochconsulting.erpnext.mprp.utils.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends BaseFragment {
    Button sendPwd;
    EditText mailid;
    private Login activity;

    String stringArray = null;
    JSONArray jsonArray = null;


    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View resetpasswordfragment= inflater.inflate(R.layout.fragment_reset_password, container, false);
        this.activity = (Login) this.getActivity();
        this.context = activity.getApplicationContext();
        mailid = (EditText) resetpasswordfragment.findViewById(R.id.email_address);
        sendPwd = (Button) resetpasswordfragment.findViewById(R.id.sendpassword);

        sendPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                requestSendPwd();
            }
        });
        return resetpasswordfragment;
    }
    private void requestSendPwd() {

        Map<String, String> map = new HashMap<>();
        map.put("user", mailid.getText().toString());

        String myUrl = Utility.getInstance().buildUrl(CustomUrl.API_METHOD, map,CustomUrl.RESEND_THE_PASS_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, myUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    hideProgress();
                    JSONObject obj = new JSONObject(response);
                    stringArray = obj.getString("_server_messages");
                    jsonArray = new JSONArray(stringArray);
                    JSONObject object = new JSONObject(jsonArray.getString(0));

                    if (object.has("message")) {
//view contains the value of a message ,That view you can toast to user
                        String finalResult = object.getString("message");
                        Toast.makeText(ResetPasswordFragment.super.getContext(), finalResult, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callLoginPage();

            }
        }//end of sucess responseP
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Toast.makeText(ResetPasswordFragment.super.getContext(), "Server denied request. Please ensure you enter a registered email address. ", Toast.LENGTH_LONG).show();


            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ResetPasswordFragment.super.getContext());
        requestQueue.add(stringRequest);
    }


    private void callLoginPage() {
        Intent intent = new Intent(ResetPasswordFragment.super.getContext(), Login.class);
        startActivity(intent);
    }

}
