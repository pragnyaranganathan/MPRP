package in.epochconsulting.erpnext.mprp.issue_materials.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.activity.Home;
import in.epochconsulting.erpnext.mprp.common.BasicActivity;
import in.epochconsulting.erpnext.mprp.implementation.ServiceLocatorImpl;
import in.epochconsulting.erpnext.mprp.issue_materials.adapter.IssueMaterialFromStockAdapter;
import in.epochconsulting.erpnext.mprp.issue_materials.implementation.IssueMaterialFromStockListener;
import in.epochconsulting.erpnext.mprp.issue_materials.model.IssueMaterialFromStockDetailsModel;
import in.epochconsulting.erpnext.mprp.issue_materials.model.IssueMaterialFromStockList;
import in.epochconsulting.erpnext.mprp.issue_materials.pojo.IssueMaterialFromStockItemsDetailsList;
import in.epochconsulting.erpnext.mprp.issue_materials.pojo.IssueMaterialFromStockServerData;
import in.epochconsulting.erpnext.mprp.model.pojo.SingletonData;
import in.epochconsulting.erpnext.mprp.transform_items.activity.BatchNoSelectionActivity;
import in.epochconsulting.erpnext.mprp.transform_items.model.BatchNoModel;
import in.epochconsulting.erpnext.mprp.transform_items.model.ItemConsumedModel;
import in.epochconsulting.erpnext.mprp.transform_items.model.ItemMadeList;
import in.epochconsulting.erpnext.mprp.transform_items.model.ItemMadeModel;
import in.epochconsulting.erpnext.mprp.transform_items.model.MultiCheckBatchList;
import in.epochconsulting.erpnext.mprp.transform_items.model.MultiCheckList;
import in.epochconsulting.erpnext.mprp.transform_items.model.MultiCheckSerialNoList;
import in.epochconsulting.erpnext.mprp.transform_items.model.SerialNoModel;
import in.epochconsulting.erpnext.mprp.transform_items.pojo.TransformItemsDetailsList;
import in.epochconsulting.erpnext.mprp.transform_items.pojo.TransformedItemServerData;
import in.epochconsulting.erpnext.mprp.utils.AlertDialogHandler;
import in.epochconsulting.erpnext.mprp.utils.Constants;
import in.epochconsulting.erpnext.mprp.utils.CustomUrl;
import in.epochconsulting.erpnext.mprp.utils.Utility;

import static in.epochconsulting.erpnext.mprp.issue_materials.model.IssueMaterialFromStockDataFactory.makeIssueMaterialFromStockDataList;
import static in.epochconsulting.erpnext.mprp.transform_items.model.TransformedItemDataFactory.makeTransformedList;

public class IssueMaterialInStockActivity extends BasicActivity implements IssueMaterialFromStockListener {
    
    Button addRowButton, saveButton;
    private IssueMaterialFromStockList missueMaterialFromStockList; //connected to the adapter
    private IssueMaterialFromStockList issueMaterialFromStockListForDropDown;
    private IssueMaterialFromStockAdapter mAdapter;
    private RecyclerView mRecyclerView;
    Spinner itemToBeIssuedFromStockSpinnerList;
    int iCurrentSelection;
    String rowTitle;
    private static IssueMaterialFromStockDetailsModel selectedItemToBeIssued;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_material_in_stock);
        
        addRowButton = (Button)findViewById(R.id.issue_items_addRow_button);
        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIssueMaterialRow();
            }
        });
        saveButton = (Button)findViewById(R.id.issueitems_save_button) ;
        saveButton.setEnabled(false);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkIfQuantitiesAreSufficient();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_issuefromstock_items);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchIssueMaterialFromStockdetailsFromServer(SingletonData.getInstance().getMloggedInUserName());
    }



    private void fetchIssueMaterialFromStockdetailsFromServer(String username) {

        Map<String, String> param = new HashMap<>() ;
        param.put("loggedInUser",username);
        String url = Utility.getInstance().buildUrl(CustomUrl.API_METHOD,null,CustomUrl.GET_ISSUE_MATERIAL_FROM_STOCK_ITEM_DETAILS);
        if(this.getApplicationContext()!= null){
            ServiceLocatorImpl.getInstance().executeGetVolleyRequest(getApplicationContext(), url, IssueMaterialFromStockItemsDetailsList.class, param, getHeaders(), new Response.Listener<IssueMaterialFromStockItemsDetailsList>() {
                @Override
                public void onResponse(IssueMaterialFromStockItemsDetailsList response) {
                    if(response!= null)
                    {
                        //populate the model for the adapter and then set the adapter here
                        SingletonData.getInstance().setIssueMaterialFromStockItemsDetailsList(response);
                        List<IssueMaterialFromStockServerData> issueMaterialFromStockServerDataList = SingletonData.getInstance().getIssueMaterialFromStockItemsDetailsList().getIssueMaterialFromStockServerDatas();
                        if(issueMaterialFromStockServerDataList!=null) {

                            missueMaterialFromStockList = new IssueMaterialFromStockList();
                            setupModelForAdapter();
                            //throw a dialog box with a spinner to select a new item to add

                            setIssueMaterialFromStockListForDropDown( makeIssueMaterialFromStockDataList(SingletonData.getInstance().getIssueMaterialFromStockItemsDetailsList().getIssueMaterialFromStockServerDatas()));
                            addRowButton.setEnabled(true);
                        }



                        else
                        {
                            Toast.makeText(getApplicationContext(),getString(R.string.issue_materials_not_already_in_stock_str),Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),getString(R.string.issue_materials_not_already_in_stock_str),Toast.LENGTH_SHORT).show();
                    }

                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    String errormsg = null;
                    try{
                        errormsg = new String(error.networkResponse.data,"UTF-8");
                    }catch(UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Server denied request with error" + errormsg, Toast.LENGTH_LONG).show();


                }
            });

        }


    }



    private Map<String,String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        SharedPreferences prefs = this.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        String userId = prefs.getString(Constants.USER_ID, null);
        String sid = prefs.getString(Constants.SESSION_ID, null);
        headers.put("user_id", userId);
        headers.put("sid", sid);

        return headers;
    }

    private void setIssueMaterialFromStockListForDropDown(IssueMaterialFromStockList issueMaterialFromStockList) {
        this.issueMaterialFromStockListForDropDown = issueMaterialFromStockList;
    }
    public IssueMaterialFromStockList getIssueMaterialFromStockListForDropDown()
    {
        return this.issueMaterialFromStockListForDropDown;
    }

    private void setupModelForAdapter() {
        mAdapter = new IssueMaterialFromStockAdapter(missueMaterialFromStockList, this);
        mRecyclerView.setAdapter(mAdapter);

    }



    private void addIssueMaterialRow() {

        LayoutInflater inflater = this.getLayoutInflater() ;
        View dialogiew = inflater.inflate(R.layout.dialog_info_content,null);
        TextView title = (TextView) dialogiew.findViewById(R.id.dialog_info_title);
        title.setText(getResources().getString(R.string.choose_item_to_be_issued_str));
        TextView message = (TextView) dialogiew.findViewById(R.id.dialog_info_message) ;
        message.setText(getResources().getString(R.string.choose_item_to_be_issued_from_drop_down_str));
        LinearLayout layout = (LinearLayout) dialogiew.findViewById(R.id.dialog_layout);
        itemToBeIssuedFromStockSpinnerList = new Spinner(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        itemToBeIssuedFromStockSpinnerList.setLayoutParams(lp);
        layout.addView(itemToBeIssuedFromStockSpinnerList);

        iCurrentSelection =-1;
        populateTheItemCodeListDropDown(getIssueMaterialFromStockListForDropDown());

        showAlertDialog(null,null , false, getResources().getString(R.string.ok_str), getString(R.string.cancel_str),null, dialogiew, new AlertDialogHandler() {
            @Override
            public void onPositiveButtonClicked() {

                if (getSelectedItemToBeIssuedFromList()!=null && rowTitle!=null) {
                    //do something here

                    mAdapter.addRow(getSelectedItemToBeIssuedFromList(),missueMaterialFromStockList.getIssueMaterialFromStockDetailsModelList().size());



                } else {

                    showErrorDialog  (getString(R.string.no_more__transformed_items_to_be_selected ));
                }


            }

            @Override
            public void onNegativeButtonClicked() {

                //do nothing on cancel
                dialogbox.dismiss();



            }
        });


    }
    private void populateTheItemCodeListDropDown(final IssueMaterialFromStockList itemList) {
        List<String> array = new ArrayList<>();
        rowTitle = null;

        for (IssueMaterialFromStockDetailsModel iteminList : itemList.getIssueMaterialFromStockDetailsModelList()) {
            if(iteminList!=null)
            {
                String name =  iteminList.getItemCode();

                array.add(name);
            }



        }
        //now set the adapter

        ArrayAdapter<String> spinnersadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array);
        spinnersadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemToBeIssuedFromStockSpinnerList.setAdapter(spinnersadapter);

        itemToBeIssuedFromStockSpinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (iCurrentSelection != position && selectedItem!=null) {
                    //set the item_code here
                    rowTitle = selectedItem;
                    IssueMaterialFromStockDetailsModel itemSelected = itemList.getIssueMaterialFromStockDetailsModelList().get(position);

                    //get the itemMade object at selected position
                    setSelectedItemToBeIssuedFromList(itemSelected);

                }
                iCurrentSelection = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing here because we are just staying here in the same activity if nothing is selected

                rowTitle = null;
                setSelectedItemToBeIssuedFromList(null);


            }
        });

    }

    private static void setSelectedItemToBeIssuedFromList(IssueMaterialFromStockDetailsModel itemSelected) {
        selectedItemToBeIssued = itemSelected;
    }
    private static IssueMaterialFromStockDetailsModel getSelectedItemToBeIssuedFromList(){
        return selectedItemToBeIssued;
    }

    //All dialog boxes that are shown
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

                loadAppropriateMenuItem(R.id.nav_select_task);

            }

            @Override
            public void onNegativeButtonClicked() {
                //do nothing


            }

        });

    }

    //All abstract methods inherited to be implemented here

    public void loadAppropriateMenuItem(int menuId)
    {
        Intent homeActivity = new Intent(this, Home.class);
        homeActivity.putExtra("NavigateToSelectTask",menuId);
        startActivity(homeActivity);
        finish();

    }

    @Override
    protected void autoLogout() {

    }



    @Override
    public void DownstreamWHSelected(String downstreamWH, int itemPos) {
        missueMaterialFromStockList.getIssueMaterialFromStockDetailsModelList().get(itemPos).setSelectedDownstreamWH(downstreamWH);

    }

    @Override
    public void issueQntyEntered(String qty, int itemSelectedPos) {
        missueMaterialFromStockList.getIssueMaterialFromStockDetailsModelList().get(itemSelectedPos).setIssuedQty(Double.valueOf(qty));

    }

    @Override
    public void dsWHListIsEmpty(int dsWHListIsNullStr) {
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.dswhlistisnull_str),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void deleteRow(int adapterPosition) {
        mAdapter.deleteRow(adapterPosition);
    }

    @Override
    public void onSelectBatchAndSLN(int itemPosition) {
        Intent intent = new Intent(this, BatchAndSLNSelectionForIssueFromStockActivity.class);
        intent.putExtra("ISSUED_ITEM_FROM_STOCK_CODE", missueMaterialFromStockList.getIssueMaterialFromStockDetailsModelList().get(itemPosition).getItemCode());
        intent.putExtra("ISSUED_ITEM_FROM_STOCK_POSITION", itemPosition);

        SingletonData.getInstance().setIssueMaterialFromStockModelListForStatePersistance(missueMaterialFromStockList);
        startActivityForResult(intent, 1);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK  && SingletonData.getInstance().getSelectedBatchAndSerialNoListForIssuedItemFromStock()!=null) {
                List<MultiCheckList> selectedBatchAndSerialnos = SingletonData.getInstance().getSelectedBatchAndSerialNoListForIssuedItemFromStock();

                int positionOfItemInAdapter = data.getIntExtra("ISSUED_ITEM_FROM_STOCK_ADAPTER_POSITION",-1);


                if(positionOfItemInAdapter!=-1 ) {
                    IssueMaterialFromStockDetailsModel itemWhoseBatchSelected = missueMaterialFromStockList.getIssueMaterialFromStockDetailsModelList().get(positionOfItemInAdapter);
                    getSelectedBatchAndSlnList(itemWhoseBatchSelected, selectedBatchAndSerialnos);
                    if(!saveButton.isEnabled()) {
                        saveButton.setEnabled(true);
                    }


                }

            }
        }
    }

    private void getSelectedBatchAndSlnList(IssueMaterialFromStockDetailsModel itemWhoseBatchSelected, List<MultiCheckList> selectedBatchAndSerialnos) {
        if(itemWhoseBatchSelected.isHasSerialNos() || itemWhoseBatchSelected.isHasBatchNos()){
            fillDetailsOfBatchAndSerialNo(itemWhoseBatchSelected, selectedBatchAndSerialnos);
        }
    }

    private void fillDetailsOfBatchAndSerialNo(IssueMaterialFromStockDetailsModel itemWhoseBatchSelected, List<MultiCheckList> selectedBatchAndSerialnos) {
        for(MultiCheckList selectedBatchAndSlNoList : selectedBatchAndSerialnos)
        {
            if(itemWhoseBatchSelected.getItemCode().equals(selectedBatchAndSlNoList.getItemCode())) {
                if (selectedBatchAndSlNoList instanceof MultiCheckBatchList) {
                    setUpBatchListForItemIssuedFromStock(itemWhoseBatchSelected, selectedBatchAndSlNoList);
                }

                if (selectedBatchAndSlNoList instanceof MultiCheckSerialNoList) {
                    setUpSerialnoListForItemIssuedFromStock(itemWhoseBatchSelected, selectedBatchAndSlNoList);


                }
            }
        }
    }

    private void setUpSerialnoListForItemIssuedFromStock(IssueMaterialFromStockDetailsModel itemWhoseBatchSelected, MultiCheckList selectedBatchAndSlNoList) {
        if (!((MultiCheckSerialNoList) selectedBatchAndSlNoList).getSerialNoList().isEmpty() && itemWhoseBatchSelected.isHasSerialNos()) {
            itemWhoseBatchSelected.setSerialNoModelList(((MultiCheckSerialNoList) selectedBatchAndSlNoList).getSerialNoList());
        } else {
            itemWhoseBatchSelected.setSerialNoModelList(Collections.<SerialNoModel>emptyList());
        }

    }

    private void setUpBatchListForItemIssuedFromStock(IssueMaterialFromStockDetailsModel itemWhoseBatchSelected, MultiCheckList selectedBatchAndSlNoList) {
        if (!selectedBatchAndSlNoList.getItems().isEmpty() && itemWhoseBatchSelected.isHasBatchNos()) {


            itemWhoseBatchSelected.setBatchNoModelList(((MultiCheckBatchList) selectedBatchAndSlNoList).getBatchList());
        } else {
            itemWhoseBatchSelected.setBatchNoModelList(Collections.<BatchNoModel>emptyList());
        }

    }

    //Saving and Submitting to the Server for further processing
    private void checkIfQuantitiesAreSufficient() {
        //check if all the requested quantities are available and batches and serial nos are selected

        new Thread() {
            boolean returnVal = true;

            StringBuilder errorMsg = new StringBuilder("The following errors were thrown when trying to save details for items that need to be transformed\n");

            @Override
            public void run() {
                for (IssueMaterialFromStockDetailsModel itemIssued : missueMaterialFromStockList.getIssueMaterialFromStockDetailsModelList()) {
                    returnVal = checkBatchAndSerialNoQuantitiesForItemsIssued(itemIssued, errorMsg);

                }
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!returnVal) {
                                showErrorDialog(errorMsg.toString());
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.all_correct_str), Toast.LENGTH_SHORT).show();
                                //throw another dialog to select batch and serial nos for the items made
                                constructMessageForDialogBox();

                            }

                        }

                    });

                } catch (final Exception ex) {
                    Log.i("---", "Exception in thread");

                }
            }
        }.start();


    }

    private void constructMessageForDialogBox() {
        if (missueMaterialFromStockList.getIssueMaterialFromStockDetailsModelList().isEmpty()) {
            //no contents at all, throw the errorDialog
            String errorMsg = "You are not issuing any item. Please select at least one item and provide a non zero quantity in the required quantity list";
            showErrorDialog(errorMsg);
        } else {
            StringBuilder messageStr = new StringBuilder(" You are issuing the following items" + "\n");

            for (int i = 0; i < missueMaterialFromStockList.getIssueMaterialFromStockDetailsModelList().size(); i++) {

                messageStr.append(missueMaterialFromStockList.getIssueMaterialFromStockDetailsModelList().get(i).getItemCode() + " : " + missueMaterialFromStockList.getIssueMaterialFromStockDetailsModelList().get(i).getIssuedQty());
                messageStr.append("\n");
            }
            showIssuedItemsDialog(messageStr.toString());

        }

    }

    private void showIssuedItemsDialog(String data) {
        LayoutInflater dialogViewinflator = this.getLayoutInflater();
        View dialogView = dialogViewinflator.inflate(R.layout.dialog_info_content,null);
        TextView title =  dialogView.findViewById(R.id.dialog_info_title) ;
        title.setText(R.string.issue_items);
        TextView message = dialogView.findViewById(R.id.dialog_info_message) ;
        message.setText(data);


        this.showAlertDialog(null,null , true, getResources().getString(R.string.submit_str), getString(R.string.no_str),getResources().getString(R.string.cancel_str), dialogView, new AlertDialogHandler() {
            @Override
            public void onPositiveButtonClicked() {


                constructJsonArrayList();


            }

            @Override
            public void onNegativeButtonClicked() {
                //do nothing
                dialogbox.dismiss();
                //should take me back to home screeen

                loadAppropriateMenuItem(R.id.nav_select_task);


            }

        });

    }

    private void constructJsonArrayList() {

        ObjectMapper mapper = new ObjectMapper();
        try {



            // Convert object to JSON string and pretty print
            String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(missueMaterialFromStockList);
            sendReqdItemsToApi(jsonInString);
            System.out.println(jsonInString);
            // writeJsonToFile(jsonInString);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void sendReqdItemsToApi(String jsonInString) {
        Map<String, String> param = new HashMap<>();
        param.put("loggedInUser",SingletonData.getInstance().getMloggedInUserName());
        param.put("issuedItemsFromStockList",jsonInString);

        String urlStr = Utility.getInstance().buildUrl(CustomUrl.API_METHOD, param, CustomUrl.SEND_ISSUE_ITEMS_FROM_STOCK_LIST);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlStr, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                String returnmsg = "";

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    returnmsg = jsonObject.getString("message");
                    if(returnmsg.contains("Success")) {
                        showSuccessDialog(returnmsg);
                        //Toast.makeText(getApplicationContext(),returnmsg, Toast.LENGTH_LONG).show();
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

               /* String errormsg = null;
                try{
                    errormsg = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));

                }catch(UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                if(errormsg!=null)
                    showErrorDialog(errormsg);
                else{
                    showErrorDialog(error.toString());
                }*/
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

    private boolean checkBatchAndSerialNoQuantitiesForItemsIssued(IssueMaterialFromStockDetailsModel itemIssued, StringBuilder errorMsg) {
        boolean success = true;

        if(itemIssued.isHasBatchNos() && checkIfItemIssuedHasSufficientBatcheNumbers(itemIssued,errorMsg) == 0)
        {
            success = false;
        }
        if(itemIssued.isHasSerialNos() && checkIfItemIssuedHasSufficientSerialNos(itemIssued,errorMsg)==0)
        {
            success = false;

        }
        if(itemIssued.getIssuedQty()> itemIssued.getAvailablQty()){
            success = false;
            errorMsg.append("You are trying to issue more than what is available in your warehouse for the item ");
            errorMsg.append(itemIssued.getItemCode());
            errorMsg.append(". Please issue appropriately.");
        }
        return success;

    }
    private int checkIfItemIssuedHasSufficientSerialNos(IssueMaterialFromStockDetailsModel itemIssued, StringBuilder errorMsg) {
        if(itemIssued.getSerialNoModelList().isEmpty())
        {
            errorMsg.append("Please select serial nos for the item ");
            errorMsg.append(itemIssued.getItemCode());
            errorMsg.append("\n");
            return 0;
        }
        if(itemIssued.getSerialNoModelList().size() < itemIssued.getIssuedQty()){
            errorMsg.append("Insufficient serial nos for the issued item ");
            errorMsg.append(itemIssued.getItemCode());
            errorMsg.append("  on ERPNext. Please readjust quantity to be issued. \n");
            return 0;

        }
        else{

            if(findNumberOfSerialNosSelected(itemIssued.getSerialNoModelList())< itemIssued.getIssuedQty()){
                errorMsg.append("Not enough serial nos have been selected for the item ");
                errorMsg.append(itemIssued.getItemCode());
                errorMsg.append(". Please choose more serial nos\n");
                return 0;
            }
        }
        return 1;

    }

    private int findNumberOfSerialNosSelected(List<SerialNoModel> serialNoModelList) {
        int selectedCount = 0;

        for(SerialNoModel sln: serialNoModelList)
        {
            if(sln.isSelected()){
                selectedCount++;
            }
        }
        return selectedCount;
    }

    private int checkIfItemIssuedHasSufficientBatcheNumbers(IssueMaterialFromStockDetailsModel itemIssued, StringBuilder errorMsg) {
        if(itemIssued.getBatchNoModelList().isEmpty())
        {
            errorMsg.append("Batches not available for the item ");
            errorMsg.append(itemIssued.getItemCode());
            errorMsg.append(" Please create batches on ERPNext\n");
            return 0;
        }
        double totalQntyRequested = 0.0;
        double totalQntyAvaialble = 0.0;
        for(BatchNoModel batch: itemIssued.getBatchNoModelList()){

            totalQntyAvaialble+= batch.getBatchQtyAtWarehouse();
            totalQntyRequested+= batch.getRequestedBatchQty();
        }
        if(totalQntyAvaialble >= itemIssued.getIssuedQty())
        {
            if(totalQntyRequested < itemIssued.getIssuedQty()){
                errorMsg.append("Insufficient quanity of item ");
                errorMsg.append(itemIssued.getItemCode());
                errorMsg.append(" selected from batches. Sufficient quantity available in stock, please select appropriate batches and quantities\n");
                return 0;
            }
        }
        else
        {
            errorMsg.append("Insufficient quantites available for the item ");
            errorMsg.append(itemIssued.getItemCode());
            errorMsg.append(". Please readjust quantity to be issued.\n");
            return 0;
        }
        return 1;

    }


}
