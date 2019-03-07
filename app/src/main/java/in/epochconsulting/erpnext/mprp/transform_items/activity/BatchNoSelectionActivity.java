package in.epochconsulting.erpnext.mprp.transform_items.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablecheckrecyclerview.listeners.OnCheckChildClickListener;

import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

import java.util.List;

import in.epochconsulting.erpnext.mprp.R;

import in.epochconsulting.erpnext.mprp.fragments.DatePickerFragment;
import in.epochconsulting.erpnext.mprp.model.pojo.SingletonData;
import in.epochconsulting.erpnext.mprp.transform_items.adapter.MultiCheckGenreAdapter;
import in.epochconsulting.erpnext.mprp.transform_items.model.BatchNoModel;

import in.epochconsulting.erpnext.mprp.transform_items.model.ItemConsumedModel;
import in.epochconsulting.erpnext.mprp.transform_items.model.ItemMadeList;
import in.epochconsulting.erpnext.mprp.transform_items.model.MultiCheckList;

import in.epochconsulting.erpnext.mprp.transform_items.model.SerialNoModel;

import in.epochconsulting.erpnext.mprp.transform_items.pojo.ItemsConsumed;
import in.epochconsulting.erpnext.mprp.transform_items.pojo.TransformedItemServerData;

import static in.epochconsulting.erpnext.mprp.transform_items.model.TransformedItemDataFactory.makeMultiCheckGenres;

public class BatchNoSelectionActivity extends AppCompatActivity  {
    private MultiCheckGenreAdapter adapter;
    List<MultiCheckList> batchAndSlnList;
    String itemConsumedCode = null;

    int itemConsumedPosition;
    int itemMadePositionIntheAdapter = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_no_selection);
        setTitle(getClass().getSimpleName());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemConsumedCode = extras.getString("ITEM_CONSUMED");
            itemConsumedPosition = extras.getInt("ITEM_CONSUMED_POSITION");
            itemMadePositionIntheAdapter = extras.getInt("ITEM_MADE_POSITION");
            if (SingletonData.getInstance().getTransformedItemModelListForStatePersistance() != null) {
                setAdapterForBatchSelection(SingletonData.getInstance().getTransformedItemModelListForStatePersistance().getItemMadeModelList().get(itemMadePositionIntheAdapter).getItemConsumedModelList().get(itemConsumedPosition));

            } else {


                Toast.makeText(getApplicationContext(), "Oops something went wrong in finding the batchs and serial nos for the item" + itemConsumedCode + ". Please go back to try again.", Toast.LENGTH_SHORT).show();

            }
        }






        Button check = (Button) findViewById(R.id.check_first_child);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();

                intent.putExtra("ITEM_MADE_ADAPTER_POSITION", itemMadePositionIntheAdapter);
                intent.putExtra("ITEM_CONSUMED_ADAPTER_POSITION", itemConsumedPosition);
                setResult(RESULT_OK, intent);
                SingletonData.getInstance().setSelectedBatchAndSerialNoList(batchAndSlnList);
                finish();


            }
        });
    }




    private void setAdapterForBatchSelection(ItemConsumedModel itemConsumed) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        batchAndSlnList = makeMultiCheckGenres(itemConsumed);

        adapter = new MultiCheckGenreAdapter(batchAndSlnList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setChildClickListener(new OnCheckChildClickListener() {
            @Override
            public void onCheckChildCLick(View v, boolean checked, CheckedExpandableGroup group, int childIndex) {
                //Added the if condition because the view is set to null if I manually check the children. Dunno why?
                if(v!=null) {

                    setUpBatchAndSLNSelectionDetails(v, group, childIndex);
                }
            }


        });
        //Start: Added on 15th June to take care of state persistance
        if(itemConsumed != null)
        {
            adapter.checkAllPreviouslySelectedBatchesAndSerialNos();// - I dont need this because the multicheck group takes care of this
            //Added on 24th may to auto check the entries having only one batch/serial nos
            //adapter.autoCheckSingleBatchAndSerialEntries();
            //End: added on 24th May
        }

        //End: Added on 15ht June to take care of state persistance


    }

    private void setUpBatchAndSLNSelectionDetails(View v,  CheckedExpandableGroup group, int childIndex) {
        if (group.getItems().get(childIndex) instanceof BatchNoModel)

        {
            BatchNoModel batchNoModel = (BatchNoModel) group.getItems().get(childIndex);
            if (group.isChildChecked(childIndex)) {
                if (group.getItems().size() > 1) {
                    setUpBatchSelectionDialog(v, batchNoModel);
                } else if (group.getItems().size() == 1) {
                    //set the auto qnty for those items that have a single batch
                    batchNoModel.setRequestedBatchQty(batchNoModel.getBatchQtyAtWarehouse());
                    batchNoModel.setSelected(true);
                }

            } else {
                //if the batch is unselected, then set the requested qnty from that batch back to 0
                batchNoModel.setRequestedBatchQty(0.0);
                batchNoModel.setSelected(false);
            }
        } else if (group.getItems().get(childIndex) instanceof SerialNoModel) {
            if (group.isChildChecked(childIndex)) {
                ((SerialNoModel) group.getItems().get(childIndex)).setSelected(true);
            } else {
                ((SerialNoModel) group.getItems().get(childIndex)).setSelected(false);
            }

        }


    }

    private void setUpBatchSelectionDialog(final View v, final BatchNoModel batchNoModel) {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle(batchNoModel.getBatchNo());

        TextView text = (TextView) dialog.findViewById(R.id.custom_dialog_message);
        text.setText(getResources().getString(R.string.request_batch_qty_str));

        final Button dialogButton = (Button) dialog.findViewById(R.id.custom_dialog_OKButton);
        dialogButton.setEnabled(false);



        EditText batchQty = (EditText) dialog.findViewById(R.id.custom_dialog_editText);
        batchQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (textView.getText().toString().equals("") || textView.getText().toString().isEmpty()) {
                        textView.setText("0.0");

                    }
                    Double requestedQty = Double.valueOf(textView.getText().toString());
                    if (batchNoModel.getBatchQtyAtWarehouse() < requestedQty) {
                        requestedQty = batchNoModel.getBatchQtyAtWarehouse();
                        Toast.makeText(v.getContext(), " You are requesting more than what is available in this batch...re- adjusting values. Please select another batch for the rest", Toast.LENGTH_LONG).show();
                        textView.setText(String.valueOf(requestedQty));
                    }
                    batchNoModel.setRequestedBatchQty(Double.valueOf(textView.getText().toString()));
                    batchNoModel.setSelected(true);
                    dialogButton.setEnabled(true);
                }

                return false;
            }
        });



        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }



}

