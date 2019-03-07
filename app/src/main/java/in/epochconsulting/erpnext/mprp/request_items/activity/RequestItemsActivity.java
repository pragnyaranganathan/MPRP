package in.epochconsulting.erpnext.mprp.request_items.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.activity.Home;
import in.epochconsulting.erpnext.mprp.common.BasicActivity;
import in.epochconsulting.erpnext.mprp.fragments.DatePickerFragment;
import in.epochconsulting.erpnext.mprp.implementation.ServiceLocatorImpl;
import in.epochconsulting.erpnext.mprp.model.pojo.SingletonData;
import in.epochconsulting.erpnext.mprp.request_items.adapter.RequestItemsAdapter;
import in.epochconsulting.erpnext.mprp.request_items.model.RequestItemList;
import in.epochconsulting.erpnext.mprp.request_items.model.RequestedItemDetailsModel;
import in.epochconsulting.erpnext.mprp.request_items.pojo.RequestItemsDetailsList;
import in.epochconsulting.erpnext.mprp.request_items.pojo.RequestedItemServerData;
import in.epochconsulting.erpnext.mprp.request_items.view.CustomRequestedItemsViewHolder;
import in.epochconsulting.erpnext.mprp.utils.AlertDialogHandler;
import in.epochconsulting.erpnext.mprp.utils.Constants;
import in.epochconsulting.erpnext.mprp.utils.CustomUrl;
import in.epochconsulting.erpnext.mprp.utils.Utility;

import static in.epochconsulting.erpnext.mprp.request_items.model.RequestItemsDataFactory.makeRequestItemsList;

public class RequestItemsActivity extends BasicActivity  implements DatePickerFragment.OnChildFragmentInteractionListener, CustomRequestedItemsViewHolder.RequestQntyEnteredListener {

    Button pickDate;
    TextView requiredByDate;
   // private List<String> mrequestedItemsList = new ArrayList<>();
    private RequestItemsAdapter mAdapter;
    Button saveORSubmitButton, addRowButton;
    Spinner requestItemsCodeList;
    private static RequestedItemDetailsModel itemSelectedFromDropDown;

  private RequestItemList requestItemList;
    private RequestItemList requestItemListForDropDown;
    RecyclerView mRecyclerView;


    int iCurrentSelection;
    String rowTitle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_items);
        saveORSubmitButton = (Button)findViewById(R.id.requestItems_saveorsubmit_button);
        saveORSubmitButton.setEnabled(false);
        saveORSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constructMessageForDialogBox(requestItemList);

            }
        });
        addRowButton = (Button) findViewById(R.id.request_items_addRow_button);
        addRowButton.setEnabled(false);
        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRowForRequestedItem();
            }
        });
        this.setTitle(getString(R.string.request_items));
        requiredByDate = (TextView) findViewById(R.id.edit_text_date);
        String defaultDate = getDefaultDate();
        if(defaultDate!=null)
            requiredByDate.setText(getDefaultDate());
        else
            requiredByDate.setText(" ");
        pickDate = (Button) findViewById(R.id.pick_date_button);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });



        //do this methods equivalent in my custom class
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_requested_items);

        populatetheDataModel(SingletonData.getInstance().getMloggedInUserName());
    }

    private void addRowForRequestedItem() {
        LayoutInflater inflater = this.getLayoutInflater() ;
        View dialogiew = inflater.inflate(R.layout.dialog_info_content,null);
        TextView title = (TextView) dialogiew.findViewById(R.id.dialog_info_title);
        title.setText(getResources().getString(R.string.choose_item_to_be_transformed_str));
        TextView message = (TextView) dialogiew.findViewById(R.id.dialog_info_message) ;
        message.setText(getResources().getString(R.string.add_request_item_row));
        LinearLayout layout = (LinearLayout) dialogiew.findViewById(R.id.dialog_layout);
        requestItemsCodeList = new Spinner(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        requestItemsCodeList.setLayoutParams(lp);
        layout.addView(requestItemsCodeList);

        iCurrentSelection = -1;
        populateTheItemCodeListDropDown(getRequestItemListForDropDown());

        showAlertDialog(null,null , false, getResources().getString(R.string.ok_str), getString(R.string.cancel_str),null, dialogiew, new AlertDialogHandler() {
            @Override
            public void onPositiveButtonClicked() {

                if (getSelectedRequestedItemFromList()!=null && rowTitle!=null) {
                    //do something here
                    mAdapter.addRow(getSelectedRequestedItemFromList(),requestItemList.getRequestedItemDetailsModelList().size());

                    //TODO remove this item from the spinner so that user doesnt select this again
                    getRequestItemListForDropDown().getRequestedItemDetailsModelList().remove(getSelectedRequestedItemFromList());




                } else {

                    showErrorDialog  (getString(R.string.no_more__request_items_to_be_selected ));
                }


            }

            @Override
            public void onNegativeButtonClicked() {

                //do nothing on cancel
                dialogbox.dismiss();



            }
        });


    }
    private void populateTheItemCodeListDropDown(final RequestItemList itemList) {
        List<String> array = new ArrayList<>();
        rowTitle = null;

        for (RequestedItemDetailsModel iteminList : itemList.getRequestedItemDetailsModelList()) {
            if(iteminList!=null)
            {
                String name =  iteminList.getmItemCode();

                array.add(name);
            }



        }
        //now set the adapter

        ArrayAdapter<String> spinnersadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array);
        spinnersadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestItemsCodeList.setAdapter(spinnersadapter);

        requestItemsCodeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (iCurrentSelection != position && selectedItem!=null) {
                    //set the item_code here
                    rowTitle = selectedItem;
                    RequestedItemDetailsModel itemSelected = itemList.getRequestedItemDetailsModelList().get(position);

                    //get the itemMade object at selected position
                    setSelectedRequestedItemFromList(itemSelected);

                }
                iCurrentSelection = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing here because we are just staying here in the same activity if nothing is selected

                rowTitle = null;
                setSelectedRequestedItemFromList(null);


            }
        });

    }

    private static void setSelectedRequestedItemFromList(RequestedItemDetailsModel itemSelected) {
        itemSelectedFromDropDown = itemSelected;
    }
    private static RequestedItemDetailsModel getSelectedRequestedItemFromList(){
        return itemSelectedFromDropDown;
    }


    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");

    }

    private String getDefaultDate() {
        try{

            String formattedDate;
            Date parsedDate;

            Date date = Calendar.getInstance().getTime();
            String input = date.toString();
            Toast.makeText(getApplicationContext(),"Today's date is :- "+input,Toast.LENGTH_LONG).show();
            SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            parsedDate= parser.parse(input);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            formattedDate = formatter.format(parsedDate);
            return formattedDate;

        }
        catch (ParseException e){
            Log.i("date_format_error","There is a Parse exception when getting the date object");
        }
        return null;
    }

    private void populatetheDataModel(String username) {
        Map<String, String> param = new HashMap<>() ;
        param.put("loggedInUser",username);
        String url = Utility.getInstance().buildUrl(CustomUrl.API_METHOD,null,CustomUrl.GET_REQUESTED_ITEM_DETAILS);
        if(this.getApplicationContext()!= null){
            ServiceLocatorImpl.getInstance().executeGetVolleyRequest(this.getApplicationContext(), url, RequestItemsDetailsList.class, param, getHeaders(), new Response.Listener<RequestItemsDetailsList>() {
                @Override
                public void onResponse(RequestItemsDetailsList response) {
                    if(response!= null)
                    {
                        //populate the model for the adapter and then set the adapter here
                        SingletonData.getInstance().setRequestedItemsDetailsList(response);
                        List<RequestedItemServerData> requestedItemServerDataList = SingletonData.getInstance().getRequestedItemsDetailsList().getItemsDataList();
                        if(requestedItemServerDataList!=null) {
                            setupModelForAdapter();

                            setRequestItemListForDropDown(makeRequestItemsList(requestedItemServerDataList));
                            addRowButton.setEnabled(true);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),getString(R.string.requested_items_not_found_on_server_str),Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),getString(R.string.requested_items_not_found_on_server_str),Toast.LENGTH_SHORT).show();
                    }

                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Toast.makeText(getApplicationContext(), "Server denied request with error" + error.toString(), Toast.LENGTH_LONG).show();

                }
            });

        }

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

    private void setupModelForAdapter() {


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        requestItemList = new RequestItemList();
        //send an empty list at first
        mAdapter = new RequestItemsAdapter(requestItemList,this.getLayoutInflater(), this);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    private void constructMessageForDialogBox(RequestItemList listToSendToServer) {
        if (listToSendToServer.getRequestedItemDetailsModelList().isEmpty()) {
            //no contents at all, throw the errorDialog
            String errorMsg = "You are not requesting any items. Please select at least one item and provide a non zero quantity in the required quantity list";
            showErrorDialog(errorMsg);
        } else {
            StringBuilder messageStr = new StringBuilder(" You are requesting for these items" + "\n");
            boolean zeroQntyFlag = false;
            for (int i = 0; i < listToSendToServer.getRequestedItemDetailsModelList().size(); i++) {
                if(listToSendToServer.getRequestedItemDetailsModelList().get(i).getmRequiredQnty().equals("0")||listToSendToServer.getRequestedItemDetailsModelList().get(i).getmRequiredQnty().equals("0.0"))
                {
                    //a zero quantity was found flag error
                    zeroQntyFlag = true;
                    break;
                }
                messageStr.append(listToSendToServer.getRequestedItemDetailsModelList().get(i).getmItemCode() + " : " + listToSendToServer.getRequestedItemDetailsModelList().get(i).getmRequiredQnty());
                messageStr.append("\n");
            }
            String errmsg = "";
            boolean errFound = false;
            if(zeroQntyFlag)
            {
                 errmsg = "You are trying to request an item without specifying its quantity. Please specify a non zero value and try again ";
                errFound = true;
            }
            else {
                String expDate = requiredByDate.getText().toString();

                if(expDate.matches("")|| expDate.matches(" "))
                {
                    errmsg = "You have not set an expiry date, please specify the expiry date in the yyyy-mm-dd format";
                    errFound = true;

                }
                else if(expDate.length()!=10)
                {
                    errmsg = "The date format is incorrect. Please enter the expiry date in the yyyy-mm-dd format";
                    errFound = true;
                }
                else if (expDate.substring(0,4).length()!=4|| expDate.substring(5,7).length()!=2 || expDate.substring(8,10).length()!=2)
                {
                    errmsg = "The date format is incorrect.  Please enter the expiry date in yyyy-mm-dd format";
                    errFound = true;

                }
                else if (expDate.split("-",3).length == 3)
                {
                    //now have the yyyy,mm,dd
                    String splitDate[] = expDate.split("-",3);
                    if(Integer.valueOf(splitDate[1])>12 || Integer.valueOf(splitDate[1])<=0)
                    {
                        errmsg = "Please enter a month between 1 and 12 only";
                        errFound = true;
                    }
                    if(Integer.valueOf(splitDate[2])>31 || Integer.valueOf(splitDate[2])<=0 ){
                        errmsg = "Please enter a date between 1 and 31";
                        errFound = true;
                    }

                }

            }
            if(errFound)
            {
                showErrorDialog(errmsg);
            }
            else {
                showRequestedItemsDialog(messageStr.toString(), listToSendToServer.getRequestedItemDetailsModelList());
            }
        }
    }
    public void constructJsonArrayList(List<RequestedItemDetailsModel> tempList) {
        JSONArray jsonArray = new JSONArray();

        for(int j =0; j<tempList.size();j++){

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("item_code",tempList.get(j).getmItemCode());
                jsonObject.put("reqd_qnty",tempList.get(j).getmRequiredQnty());
                jsonObject.put("avail_qnty",tempList.get(j).getmAvailableQnty());
                jsonObject.put("uom",tempList.get(j).getmUOM());
                jsonObject.put("src_warehouse", tempList.get(j).getmSrcWarehouse());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        JSONObject reqdItems = new JSONObject();
        try {
            reqdItems.put("requiredItems",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendReqdItemsToApi(reqdItems);
    }
    private void sendReqdItemsToApi(JSONObject reqdItems) {
        Map<String, String> param = new HashMap<>();
        param.put("loggedInUser",SingletonData.getInstance().getMloggedInUserName());
        param.put("reqdByDate",requiredByDate.getText().toString());
        param.put("reqdItemsList", reqdItems.toString());

        String urlStr = Utility.getInstance().buildUrl(CustomUrl.API_METHOD, param, CustomUrl.SEND_REQUESTED_ITEMS_LIST);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String returnmsg = "";

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    returnmsg = jsonObject.getString("message");
                    if(returnmsg.contains("Success")) {
                        showSuccessDialog(returnmsg);
                    }
                    else
                    {
                        showErrorDialog(returnmsg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }//end of sucess response
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showErrorDialog(error.toString());



            }
        })
                //end of error response and stringRequest params
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
                String userId = prefs.getString(Constants.USER_ID, null);
                String sid = prefs.getString(Constants.SESSION_ID, null);

                headers.put("user_id", userId);
                headers.put("sid", sid);

                return headers;
            }

        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(stringRequest);
    }


    @Override
    protected void autoLogout() {

    }


    @Override
    public void onRequiredQntyEntered(String reqdQnty, int itemSelectedPosition) {

        requestItemList.getRequestedItemDetailsModelList().get(itemSelectedPosition).setmRequiredQnty(reqdQnty);
        mAdapter.notifyItemChanged(itemSelectedPosition);
        saveORSubmitButton.setEnabled(true);

    }

    @Override
    public void messageFromChildToParent(String myString) {
        requiredByDate.setText(myString);

    }

    //Dailog Boxes to be shown
    public void showErrorDialog(String str) {
        LayoutInflater dialogViewinflator = this.getLayoutInflater();
        View dialogView = dialogViewinflator.inflate(R.layout.dialog_error_content,null);
        TextView title = (TextView) dialogView.findViewById(R.id.dialog_error_title) ;

        title.setText(getResources().getString(R.string.error_string));
        TextView message = (TextView) dialogView.findViewById(R.id.dialog_error_message) ;
        message.setText(str);


        showAlertDialog(null, null, false, getResources().getString(R.string.ok_str), null, null,dialogView, new AlertDialogHandler() {
            @Override
            public void onPositiveButtonClicked() {


                dialogbox.dismiss();

            }

            @Override
            public void onNegativeButtonClicked() {
                //do nothing


            }

        });

    }
    public void showSuccessDialog(String str) {
        LayoutInflater dialogViewinflator = this.getLayoutInflater();
        View dialogView = dialogViewinflator.inflate(R.layout.dialog_success_content,null);
        TextView title = (TextView) dialogView.findViewById(R.id.dialog_success_title) ;

        title.setText(getResources().getString(R.string.success_string));
        TextView message = (TextView) dialogView.findViewById(R.id.dialog_success_message) ;
        message.setText(str);


        showAlertDialog(null, null, false, getResources().getString(R.string.ok_str), null, null,dialogView, new AlertDialogHandler() {
            @Override
            public void onPositiveButtonClicked() {


                dialogbox.dismiss();
                // loadSelectedFragment(R.id.nav_select_task);
                loadAppropriateMenuItem(R.id.nav_select_task);

            }

            @Override
            public void onNegativeButtonClicked() {
                //do nothing


            }

        });

    }
    public void showRequestedItemsDialog(String data, final List<RequestedItemDetailsModel> listToSendToServer) {
        LayoutInflater dialogViewinflator = this.getLayoutInflater();
        View dialogView = dialogViewinflator.inflate(R.layout.dialog_info_content,null);
        TextView title =  dialogView.findViewById(R.id.dialog_info_title) ;
        title.setText(R.string.requested_items_title_str);
        TextView message = dialogView.findViewById(R.id.dialog_info_message) ;
        message.setText(data);


        this.showAlertDialog(null,null , true, getResources().getString(R.string.ok_str), getString(R.string.no_str),getResources().getString(R.string.cancel_str), dialogView, new AlertDialogHandler() {
            @Override
            public void onPositiveButtonClicked() {


                    constructJsonArrayList(listToSendToServer);


            }

            @Override
            public void onNegativeButtonClicked() {
                //do nothing
                dialogbox.dismiss();
                //should take me back to home screeen

                //loadSelectedFragment(R.id.nav_select_task);
                loadAppropriateMenuItem(R.id.nav_select_task);


            }

        });



    }
    public void loadAppropriateMenuItem(int menuId)
    {
        Intent homeActivity = new Intent(this, Home.class);
        homeActivity.putExtra("NavigateToSelectTask",menuId);
        startActivity(homeActivity);
        finish();

    }

    public RequestItemList getRequestItemListForDropDown() {
        return requestItemListForDropDown;
    }

    public void setRequestItemListForDropDown(RequestItemList requestItemList) {
        this.requestItemListForDropDown = requestItemList;
    }
}
