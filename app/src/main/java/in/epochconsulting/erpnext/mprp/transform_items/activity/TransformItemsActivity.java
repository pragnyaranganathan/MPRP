package in.epochconsulting.erpnext.mprp.transform_items.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.activity.Home;
import in.epochconsulting.erpnext.mprp.common.BasicActivity;
import in.epochconsulting.erpnext.mprp.fragments.DatePickerFragment;
import in.epochconsulting.erpnext.mprp.implementation.ServiceLocatorImpl;
import in.epochconsulting.erpnext.mprp.transform_items.adapter.TransformedItemsParentAdapter;
import in.epochconsulting.erpnext.mprp.transform_items.implementation.TransformItemsListener;
import in.epochconsulting.erpnext.mprp.model.pojo.SingletonData;
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



import static in.epochconsulting.erpnext.mprp.transform_items.model.TransformedItemDataFactory.makeTransformedList;

public class TransformItemsActivity extends BasicActivity implements TransformItemsListener,  DatePickerFragment.OnChildFragmentInteractionListener{


    private TransformedItemsParentAdapter adapter;


    RecyclerView recyclerView;

    ItemMadeList transformedItemlist;

    int iCurrentSelection;
    Spinner transformedItemCodeList;
    String rowTitle ;
    Button addrowButton;
    Button saveButton;

    private static ItemMadeModel item;
    private ItemMadeList transformedItemListForDropDown;
    private int previousItemMadeClickedPosition = -1;

    EditText batchExpirydate; //the item made batch ans SLN selection dialog box's batch expiry date edittext

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transform_items);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.transform_items));
        addrowButton = (Button)findViewById(R.id.add_row_button);

        saveButton = (Button) findViewById(R.id.txfm_items_save_button) ;

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfQuantitiesAreSufficient();


            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.txfm_items_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRow();
            }
        });

        if(savedInstanceState==null)
        {
            saveButton.setEnabled(false);
            addrowButton.setEnabled(false);
            fetchTransformedItemsDetailsFromBackEnd(SingletonData.getInstance().getMloggedInUserName());
        }


    }


    private void checkIfQuantitiesAreSufficient() {
        //check if all the requested quantities are available and batches and serial nos are selected

        new Thread() {
            boolean returnVal = true;

            StringBuilder errorMsg = new StringBuilder("The following errors were thrown when trying to save details for items that need to be transformed\n");

            @Override
            public void run() {
                for (ItemMadeModel itemMade : transformedItemlist.getItemMadeModelList()) {
                    returnVal = checkBatchAndSerialNoQuantitiesForItemsConsumed(itemMade, errorMsg);

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



    private boolean checkBatchAndSerialNoQuantitiesForItemsConsumed(ItemMadeModel itemMade, StringBuilder errorMsg) {
        boolean success = true;
        if(itemMade.isHasBatchNos() && itemMade.getBatchNoModelArrayList().isEmpty())
        {
            success = false;
            errorMsg.append("Item Made requires batch number. Please set the batch number for the item");
            errorMsg.append(itemMade.getItemMadeCode());
        }
        if(itemMade.isHasSerialNos() && itemMade.getSelectedSerialNos().isEmpty())
        {
            success = false;
            errorMsg.append("Please choose the serial nos for the made item ");
            errorMsg.append(itemMade.getItemMadeCode());
            errorMsg.append(". ");
            errorMsg.append(itemMade.getQtyReqd());
            errorMsg.append(" serial nos required.\n");
        }
        for(ItemConsumedModel itemConsumed: itemMade.getItemConsumedModelList())
        {
            if(itemConsumed.isHasSerialNos() && checkIfItemConsumedHasSufficientSerialNos(itemConsumed, errorMsg) == 0)
            {
                   success = false;

            }

            if(itemConsumed.isHasBatchNos() && checkIfItemConsumedHasSufficientBatcheNumbers(itemConsumed, errorMsg) == 0){

                    success = false;

            }

        }
        return success;

    }

    private int checkIfItemConsumedHasSufficientSerialNos(ItemConsumedModel itemConsumed, StringBuilder errorMsg) {
        if(itemConsumed.getSerialNoModelList().isEmpty())
        {
            errorMsg.append("Please select serial nos for the item ");
            errorMsg.append(itemConsumed.getItemConsumedCode());
            errorMsg.append("\n");
            return 0;
        }
        if(itemConsumed.getSerialNoModelList().size() < itemConsumed.getItemConsumedQnty()){
            errorMsg.append("Insufficient serial nos for the consumed item ");
            errorMsg.append(itemConsumed.getItemConsumedCode());
            errorMsg.append(" Please readjust values\n");
            return 0;

        }
        else{

            if(findNumberOfSerialNosSelected(itemConsumed.getSerialNoModelList())< itemConsumed.getItemConsumedQnty()){
                errorMsg.append("Not enough serial nos have been selected for the item ");
                errorMsg.append(itemConsumed.getItemConsumedCode());
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

    private int checkIfItemConsumedHasSufficientBatcheNumbers(ItemConsumedModel itemConsumed, StringBuilder errorMsg) {
        if(itemConsumed.getBatchNoModelList().isEmpty())
        {
            errorMsg.append("Batches not available for the item ");
            errorMsg.append(itemConsumed.getItemConsumedCode());
            errorMsg.append(" Please create batches on ERPNext\n");
            return 0;
        }
        double totalQntyRequested = 0.0;
        double totalQntyAvaialble = 0.0;
        for(BatchNoModel batch: itemConsumed.getBatchNoModelList()){

            totalQntyAvaialble+= batch.getBatchQtyAtWarehouse();
            totalQntyRequested+= batch.getRequestedBatchQty();
        }
        if(totalQntyAvaialble >= itemConsumed.getItemConsumedQnty())
        {
            if(totalQntyRequested < itemConsumed.getItemConsumedQnty()){
                errorMsg.append("Insufficient quanity of item ");
                errorMsg.append(itemConsumed.getItemConsumedCode());
                errorMsg.append(" selected from batches. Sufficient quantity available in stock, please select appropriate batches and quantities\n");
                return 0;
            }
        }
        else
        {
            errorMsg.append("Insufficient quantites available for the item ");
            errorMsg.append(itemConsumed.getItemConsumedCode());
            errorMsg.append(". Please readjust requested quantity\n");
            return 0;
        }
        return 1;

    }



    @Override
    protected void autoLogout() {
        //done nothing here for now
    }

    private void addRow() {

       showAlertDialogToAddRow();



    }

    private void fetchTransformedItemsDetailsFromBackEnd(String username) {

        Map<String, String> param = new HashMap<>() ;
        param.put("loggedInUser",username);
        String url = Utility.getInstance().buildUrl(CustomUrl.API_METHOD,null,CustomUrl.GET_TRANSFORMED_ITEM_DETAILS);
        if(this.getApplicationContext()!= null){
            ServiceLocatorImpl.getInstance().executeGetVolleyRequest(getApplicationContext(), url, TransformItemsDetailsList.class, param, getHeaders(), new Response.Listener<TransformItemsDetailsList>() {
                @Override
                public void onResponse(TransformItemsDetailsList response) {
                    if(response!= null)
                    {
                        //populate the model for the adapter and then set the adapter here
                        SingletonData.getInstance().setTransformedItemsDetailsList(response);
                        List<TransformedItemServerData> transformedItemServerDataList = SingletonData.getInstance().getTransformedItemsDetailsList().getItemsDataList();
                        if(transformedItemServerDataList!=null) {
                            transformedItemlist = new ItemMadeList();
                            //adapter.setItemMadeList(transformedItemlist);
                           setupModelForAdapter();
                            //throw a dialog box with a spinner to select a new item to add

                            setTransformedItemListForDropDown( makeTransformedList(SingletonData.getInstance().getTransformedItemsDetailsList().getItemsDataList()));
                            addrowButton.setEnabled(true);


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



    private void setupModelForAdapter() {

        adapter = new TransformedItemsParentAdapter(transformedItemlist,this);
        recyclerView.setAdapter(adapter);

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




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("transformed_item_list",transformedItemlist);
        outState.putParcelable("drop_down_list", transformedItemListForDropDown);
        outState.putBoolean("addrow_button_status",addrowButton.isEnabled());
        outState.putBoolean("save_button_status",saveButton.isEnabled());
        outState.putInt("previous_item_made_clicked_position",previousItemMadeClickedPosition);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if(savedInstanceState == null)
        {

            return;
        }
        transformedItemlist = savedInstanceState.getParcelable("transformed_item_list");
        setupModelForAdapter();
        transformedItemListForDropDown = savedInstanceState.getParcelable("drop_down_list");
        addrowButton.setEnabled(savedInstanceState.getBoolean("addrow_button_status"));
        saveButton.setEnabled(savedInstanceState.getBoolean("save_button_status"));
        previousItemMadeClickedPosition = savedInstanceState.getInt("previous_item_made_clicked_position");

        super.onRestoreInstanceState(savedInstanceState);

    }




    @Override
    public void itemConsumedClicked(String itemConsumedCode, int position) {
        //throw a dialog box open to set the details which are prefilled with content from the backend or saved in this instance. Also have to auto calculate te other values
        //Toast.makeText(this,"the consumed item clicked is "+itemConsumedCode+" and the clicked position is: "+position,Toast.LENGTH_SHORT).show();
        //i dont know what to do
    }

    @Override
    public void itemMadeClicked(String itemMadeCode, int position) {
        //Toast.makeText(this,"the made/transformed item clicked is "+itemMadeCode+" and the clicked position is "+position,Toast.LENGTH_SHORT).show();
        //I dont know what this is there for..dunno what to do
    }

    private void showAlertDialogToAddRow() {

        LayoutInflater inflater = this.getLayoutInflater() ;
        View dialogiew = inflater.inflate(R.layout.dialog_info_content,null);
        TextView title = (TextView) dialogiew.findViewById(R.id.dialog_info_title);
        title.setText(getResources().getString(R.string.choose_item_to_be_transformed_str));
        TextView message = (TextView) dialogiew.findViewById(R.id.dialog_info_message) ;
        message.setText(getResources().getString(R.string.add_transformed_item_row));
        LinearLayout layout = (LinearLayout) dialogiew.findViewById(R.id.dialog_layout);
         transformedItemCodeList = new Spinner(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        transformedItemCodeList.setLayoutParams(lp);
        layout.addView(transformedItemCodeList);

         iCurrentSelection =-1;
         populateTheItemCodeListDropDown(getTransformedItemListForDropDown());

        showAlertDialog(null,null , false, getResources().getString(R.string.ok_str), getString(R.string.cancel_str),null, dialogiew, new AlertDialogHandler() {
            @Override
            public void onPositiveButtonClicked() {

                if (getSelectedTransformedItemFromList()!=null && rowTitle!=null) {
                    //do something here
                    adapter.addRow(getSelectedTransformedItemFromList(),transformedItemlist.getItemMadeModelList().size());

                    //TODO remove this item from the spinner so that user doesnt select this again
                    getTransformedItemListForDropDown().getItemMadeModelList().remove(getSelectedTransformedItemFromList());




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
    private void populateTheItemCodeListDropDown(final ItemMadeList itemList) {
        List<String> array = new ArrayList<>();
        rowTitle = null;

        for (ItemMadeModel iteminList : itemList.getItemMadeModelList()) {
            if(iteminList!=null)
            {
                String name =  iteminList.getItemMadeCode();

                array.add(name);
            }



        }
        //now set the adapter

        ArrayAdapter<String> spinnersadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array);
        spinnersadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transformedItemCodeList.setAdapter(spinnersadapter);

        transformedItemCodeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (iCurrentSelection != position && selectedItem!=null) {
                    //set the item_code here
                    rowTitle = selectedItem;
                    ItemMadeModel itemMadeSelected = itemList.getItemMadeModelList().get(position);

                    //get the itemMade object at selected position
                    setSelectedTrnsformedItemFromList(itemMadeSelected);

                }
                iCurrentSelection = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing here because we are just staying here in the same activity if nothing is selected

                rowTitle = null;
                setSelectedTrnsformedItemFromList(null);


            }
        });

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
    public static void setSelectedTrnsformedItemFromList(ItemMadeModel sitemitem)
    {
        item = sitemitem;
    }
    public static ItemMadeModel getSelectedTransformedItemFromList()
    {
        return item;
    }

    public ItemMadeList getTransformedItemListForDropDown() {
        return transformedItemListForDropDown;
    }

    public void setTransformedItemListForDropDown(ItemMadeList transformedItemListForDropDown) {
        this.transformedItemListForDropDown = transformedItemListForDropDown;
    }

    public void onQntyEntered(String qty, int position, boolean checked){
        //get the model at this position, caqtlculate the quantities of the items consumed and update the textviews accordingly
        boolean returnStatus = adapter.qntyEnteredForItemMade(qty,position, checked);
        if(returnStatus){
            Toast.makeText(getApplicationContext(),"Please choose the batch and serial nos for the items made and consumed by clicking on the down arrow button", Toast.LENGTH_SHORT).show();
            /*
            if (position!=previousItemMadeClickedPosition)
            {
                previousItemMadeClickedPosition = position;
                SingletonData.getInstance().setSelectedBatchAndSerialNoList(null); //to handle the retention of previously selected batch and serial nos for the same item made position
            }

            showBatchAndSerialNoSelectionDialog(position);*/
        }

    }

    public void onRawMaterialQntyEntered(String qty, int adapterPosition, int position, boolean bomUsed)
    {
        //position is the parent adapter's position, ie.the item made position in the list
        //adapterPosition is the position of the item consumed in the list
        boolean returnStatus  = adapter.qntyEnteredForRawMaterialItemConsumed(qty,adapterPosition, position, bomUsed);
        //that is all calculations are ok then throw the dialog
        if(returnStatus){
           /* //Start: added thhis on 15th June 2018
            if (position!=previousItemMadeClickedPosition)
            {
                previousItemMadeClickedPosition = position;
                SingletonData.getInstance().setSelectedBatchAndSerialNoList(null); //to handle the retention of previously selected batch and serial nos for the same item made position
            }
            //End: addded on 15th June 2018
            showBatchAndSerialNoSelectionDialog(position);*/
        }


    }
    private void showBatchAndSerialNoSelectionDialog(int itemMadePosition,int itemConsumedPosition) {
        //start the batch selection activity only if the given item
        //check to see
        Intent intent = new Intent(this, BatchNoSelectionActivity.class);
        intent.putExtra("ITEM_CONSUMED", transformedItemlist.getItemMadeModelList().get(itemMadePosition).getItemConsumedModelList().get(itemConsumedPosition).getItemConsumedCode());
        intent.putExtra("ITEM_CONSUMED_POSITION", itemConsumedPosition);
        intent.putExtra("ITEM_MADE_POSITION",itemMadePosition);
        SingletonData.getInstance().setTransformedItemModelListForStatePersistance(transformedItemlist);
        startActivityForResult(intent, 1);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK  && SingletonData.getInstance().getSelectedBatchAndSerialNoList()!=null) {
                List<MultiCheckList> selectedBatchAndSerialnos = SingletonData.getInstance().getSelectedBatchAndSerialNoList();

                    int positionOfItemMadeInAdapter = data.getIntExtra("ITEM_MADE_ADAPTER_POSITION",-1);
                    int positionOfItemConsumed = data.getIntExtra("ITEM_CONSUMED_ADAPTER_POSITION",-1);

                    if(positionOfItemMadeInAdapter!=-1 && positionOfItemConsumed!=-1) {
                        ItemConsumedModel itemConsumedBatchSelected = transformedItemlist.getItemMadeModelList().get(positionOfItemMadeInAdapter).getItemConsumedModelList().get(positionOfItemConsumed);
                        getSelectedBatchAndSlnList(itemConsumedBatchSelected, selectedBatchAndSerialnos);
                        if(!saveButton.isEnabled()) {
                            saveButton.setEnabled(true);
                        }


                }

            }
        }
    }

    private void getSelectedBatchAndSlnList(ItemConsumedModel itemConsumed, List<MultiCheckList> selectedBatchAndSerialnos) {

        if (itemConsumed.isHasBatchNos() || itemConsumed.isHasSerialNos()) {
            fillDetailsOfBatchAndSerialNo(itemConsumed, selectedBatchAndSerialnos);
        }


    }

    private void fillDetailsOfBatchAndSerialNo(ItemConsumedModel itemConsumed, List<MultiCheckList> selectedBatchAndSerialnos) {
        for(MultiCheckList selectedBatchAndSlNoList : selectedBatchAndSerialnos)
        {
            if(itemConsumed.getItemConsumedCode().equals(selectedBatchAndSlNoList.getItemCode())) {
                if (selectedBatchAndSlNoList instanceof MultiCheckBatchList) {
                    setUpBatchListForItemConsumed(itemConsumed, selectedBatchAndSlNoList);
                 }

                if (selectedBatchAndSlNoList instanceof MultiCheckSerialNoList) {
                    setUpSerialnoListForItemConsumed(itemConsumed, selectedBatchAndSlNoList);


                }
            }
        }
    }

    private void setUpSerialnoListForItemConsumed(ItemConsumedModel itemConsumed, MultiCheckList selectedBatchAndSlNoList) {
        if (!((MultiCheckSerialNoList) selectedBatchAndSlNoList).getSerialNoList().isEmpty() && itemConsumed.isHasSerialNos()) {
            itemConsumed.setSerialNoModelList(((MultiCheckSerialNoList) selectedBatchAndSlNoList).getSerialNoList());
        } else {
            itemConsumed.setSerialNoModelList(Collections.<SerialNoModel>emptyList());
        }

    }

    private void setUpBatchListForItemConsumed(ItemConsumedModel itemConsumed, MultiCheckList selectedBatchAndSlNoList) {
        if (!selectedBatchAndSlNoList.getItems().isEmpty() && itemConsumed.isHasBatchNos()) {


            itemConsumed.setBatchNoModelList(((MultiCheckBatchList) selectedBatchAndSlNoList).getBatchList());
        } else {
            itemConsumed.setBatchNoModelList(Collections.<BatchNoModel>emptyList());
        }
    }

    public void onSetBatchAndSLNButtonPressedForRawMaterial(int position){
        showDialogToSpecifyBatchAndSerialNosForItemMade(transformedItemlist.getItemMadeModelList().get(position));

    }
    public void onSetBatchAndSLNButtonPressedForFinishedGoods(int position){
        showDialogToSpecifyBatchAndSerialNosForItemMade(transformedItemlist.getItemMadeModelList().get(position));

    }

    @Override
    public void onfinishedGoodsItemConsumedQtyEntered(String qty, int adapterPosition, int parentPos) {
        adapter.finshedGoodsItemConsumedQtyEntered(qty,adapterPosition,parentPos);
    }

    @Override
    public void onRawMaterialItemTransformedQtyEntered(String s, int adapterPosition, boolean useBOMValue) {
        adapter.rawMaterialItemTransformedQtyentered(s,adapterPosition,useBOMValue);
    }

    @Override
    public void onUseBOMSelectedForRawMaterialTransformedItem(boolean checked, int adapterPosition) {
        adapter.onUseBOMSelectedForRawMaterialTransformedItem(checked,adapterPosition);
    }
    @Override
    public void onUseBOMSelectedFinishedGoodsTransformedItem(boolean checked, int adapterPosition){
        adapter.onUseBOMSelectedForFinishedGoodsTransformedItem(checked,adapterPosition);
    }

    @Override
    public void deleteRow(int adapterPosition) {
        adapter.deleteRow(adapterPosition);
    }

    @Override
    public void onSetBatchAndSLNForItemConsumed(int itemConsumedPosition, int parentItemMadePos) {

        showBatchAndSerialNoSelectionDialog(parentItemMadePos,itemConsumedPosition);


    }

    private void showDialogToSpecifyBatchAndSerialNosForItemMade(final ItemMadeModel itemMadeModel) {
        LayoutInflater inflater = this.getLayoutInflater() ;
        View dialogiew = inflater.inflate(R.layout.dialog_set_batch_nos,null);
        TextView title = (TextView) dialogiew.findViewById(R.id.dialog_set_batch_title);
        title.setText(itemMadeModel.getItemMadeCode());

         final EditText batchNumber = dialogiew.findViewById(R.id.dialog_set_batch_edittext);
        final Button datePickerButton = dialogiew.findViewById(R.id.batch_expiry_date_picker_button);
         batchExpirydate = dialogiew.findViewById(R.id.batch_expiry_edittext);
        final EditText slnList = dialogiew.findViewById(R.id.sln_list);
        TextView slnInstructionText = dialogiew.findViewById(R.id.sln_instructions);
        datePickerButton.setEnabled(false);
        batchNumber.setEnabled(false);
        batchExpirydate.setEnabled(false);
        slnList.setEnabled(false);
        slnInstructionText.setEnabled(false);
        if(itemMadeModel.isHasBatchNos())
        {
            batchNumber.setEnabled(true);

        }
        if(itemMadeModel.isHasSerialNos()) {
            slnList.setEnabled(true);

            slnInstructionText.setEnabled(true);
            StringBuilder slnInitialList = new StringBuilder();
            slnInstructionText.setText("Please enter one serial no per line. You need to enter/choose " + itemMadeModel.getQtyReqd() + " serial nos or choose from the ones listed below.");
            //Start: Added on 18th June 2018 for state persistnacce
            if (itemMadeModel.getSelectedSerialNos()!=null && !itemMadeModel.getSelectedSerialNos().isEmpty()) {
                for (String serialno:itemMadeModel.getSelectedSerialNos()) {
                    slnInitialList.append(serialno);
                    slnInitialList.append("\n");

                }
                slnList.setText(slnInitialList.toString());



            } else {//end: Added on 18h June 2018 for state persistance
                if (itemMadeModel.getSerialNoModelArrayList() != null || !itemMadeModel.getSerialNoModelArrayList().isEmpty()) {
                    for (int i = 0; i < itemMadeModel.getSerialNoModelArrayList().size(); i++) {
                        slnInitialList.append(itemMadeModel.getSerialNoModelArrayList().get(i).getSerialNo());
                        slnInitialList.append("\n");
                    }
                    slnList.setText(slnInitialList.toString());
                }
            }
        }
        //all additionn of batch numbers with experiry date
        batchNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE && itemMadeModel.getBatchNoModelArrayList()!=null){

                    Toast.makeText(getApplicationContext(),"Please set the expiry date for this batch,",Toast.LENGTH_LONG).show();
                    datePickerButton.setEnabled(true);
                    batchExpirydate.setEnabled(true);


                }
                return false;
            }
        });
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();


            }
        });
        showAlertDialog(null, null, false, getString(R.string.ok_str), getString(R.string.no_str), getString(R.string.cancel_str), dialogiew, new AlertDialogHandler() {
            @Override
            public void onPositiveButtonClicked() {

                String defaultExpDate = "2020/05/30";
                if(!batchExpirydate.getText().toString().isEmpty())
                {
                    defaultExpDate = batchExpirydate.getText().toString();
                }
                saveBatchAndSlns(itemMadeModel, batchNumber.getText().toString(), defaultExpDate,slnList.getText().toString());
                dialogbox.dismiss();
            }

            @Override
            public void onNegativeButtonClicked() {
                dialogbox.dismiss();

            }
        });

    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    private void saveBatchAndSlns(ItemMadeModel itemMadeModel, String batch, String expDate, String slns) {
        if(itemMadeModel.isHasBatchNos() && itemMadeModel.getBatchNoModelArrayList()!=null)
        {
            BatchNoModel batchModel= new BatchNoModel(batch, itemMadeModel.getQtyReqd());
            batchModel.setSelected(true);
            if(!expDate.isEmpty() )
                batchModel.setBatchExpDate(expDate);
            else
                batchModel.setBatchExpDate("2018/06/30");
            itemMadeModel.getBatchNoModelArrayList().add(batchModel);

        }
        if(itemMadeModel.isHasSerialNos())
        {
            String [] data = slns.split("\n");
            itemMadeModel.setSelectedSerialNos(Arrays.asList(data));
            //System.out.println("The SLN selected are : "+Arrays.toString(data));
        }

    }

    private void constructMessageForDialogBox() {
        if (transformedItemlist.getItemMadeModelList().isEmpty()) {
            //no contents at all, throw the errorDialog
            String errorMsg = "You are not requesting any items. Please select at least one item and provide a non zero quantity in the required quantity list";
            showErrorDialog(errorMsg);
        } else {
            StringBuilder messageStr = new StringBuilder(" You are requesting for these items" + "\n");

            for (int i = 0; i < transformedItemlist.getItemMadeModelList().size(); i++) {

                messageStr.append(transformedItemlist.getItemMadeModelList().get(i).getItemMadeCode() + " : " + transformedItemlist.getItemMadeModelList().get(i).getQtyReqd());
                messageStr.append("\n");
            }
            showTransformedItemsDialog(messageStr.toString());

        }
    }
    public void constructJsonArrayList() {
        ObjectMapper mapper = new ObjectMapper();
        try {


            // Convert object to JSON string
            //String jsonInString = mapper.writeValueAsString(transformedItemlist);
            //System.out.println(jsonInString);

            // Convert object to JSON string and pretty print
            String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(transformedItemlist);
            sendReqdItemsToApi(jsonInString);
            //System.out.println(jsonInString);
           // writeJsonToFile(jsonInString);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void writeJsonToFile(String jsonInString) {
        ///*final String FILENAME = "file:///home/pragnya/Desktop/MPRP_ProjectResearch/JsonInput.txt";
        BufferedWriter bw = null;
        FileWriter fw = null;

        File file =  null; //new File("/home/pragnya/Desktop/MPRP_ProjectResearch/JsonInput.txt");


        try {


           file = new File("/home/pragnya/Desktop/file.txt");
            boolean ret = file.createNewFile();
             fw = new FileWriter(file.getAbsoluteFile());
             bw = new BufferedWriter(fw);
            bw.write(jsonInString);

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

    }



    private void sendReqdItemsToApi(String inString) {
        Map<String, String> param = new HashMap<>();
        param.put("loggedInUser",SingletonData.getInstance().getMloggedInUserName());
        param.put("transformedItemsList",inString);

        String urlStr = Utility.getInstance().buildUrl(CustomUrl.API_METHOD, param, CustomUrl.SEND_TRANSFORMED_ITEMS_LIST);


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

                String errormsg = null;
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
                }



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

    public void showTransformedItemsDialog(String data) {
        LayoutInflater dialogViewinflator = this.getLayoutInflater();
        View dialogView = dialogViewinflator.inflate(R.layout.dialog_info_content,null);
        TextView title =  dialogView.findViewById(R.id.dialog_info_title) ;
        title.setText(R.string.requested_items_title_str);
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
    public void loadAppropriateMenuItem(int menuId)
    {
        Intent homeActivity = new Intent(this, Home.class);
        homeActivity.putExtra("NavigateToSelectTask",menuId);
        startActivity(homeActivity);
        finish();

    }


    @Override
    public void messageFromChildToParent(String myString) {

       batchExpirydate.setText( myString);

    }
}
