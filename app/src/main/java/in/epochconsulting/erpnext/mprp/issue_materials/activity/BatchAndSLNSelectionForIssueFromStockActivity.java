package in.epochconsulting.erpnext.mprp.issue_materials.activity;

import android.app.Dialog;
import android.content.Intent;
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
import in.epochconsulting.erpnext.mprp.issue_materials.model.IssueMaterialFromStockDetailsModel;
import in.epochconsulting.erpnext.mprp.model.pojo.SingletonData;
import in.epochconsulting.erpnext.mprp.transform_items.adapter.MultiCheckGenreAdapter;
import in.epochconsulting.erpnext.mprp.transform_items.model.BatchNoModel;
import in.epochconsulting.erpnext.mprp.transform_items.model.MultiCheckList;
import in.epochconsulting.erpnext.mprp.transform_items.model.SerialNoModel;

import static in.epochconsulting.erpnext.mprp.issue_materials.model.IssueMaterialFromStockDataFactory.makeMultiCheckGenresForItemToBeIssuedFromStock;

public class BatchAndSLNSelectionForIssueFromStockActivity extends AppCompatActivity {

    int mItemPosition;
    String mItemCode;
    MultiCheckGenreAdapter mAdapter;
    List<MultiCheckList> mBatchAndSlnList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_and_slnselection_for_issue_from_stock);
        setTitle(getClass().getSimpleName());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mItemCode = extras.getString("ISSUED_ITEM_FROM_STOCK_CODE");
            mItemPosition = extras.getInt("ISSUED_ITEM_FROM_STOCK_POSITION");
            if(SingletonData.getInstance().getIssueMaterialFromStockModelListForStatePersistance()!=null){

                setAdapterForBatchSelection(SingletonData.getInstance().getIssueMaterialFromStockModelListForStatePersistance().getIssueMaterialFromStockDetailsModelList().get(mItemPosition));

            } else {


                Toast.makeText(getApplicationContext(), "Oops something went wrong in finding the batches and serial nos for the item" + mItemCode + ". Please go back to try again.", Toast.LENGTH_SHORT).show();

            }

        }

        Button check = (Button) findViewById(R.id.issue_material_from_stock_batch_selection_save_button);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("ISSUED_ITEM_FROM_STOCK_ADAPTER_POSITION", mItemPosition);
                setResult(RESULT_OK, intent);
                SingletonData.getInstance().setSelectedBatchAndSerialNoListForIssuedItemFromStock(mBatchAndSlnList);
                finish();


            }
        });
    }

    private void setAdapterForBatchSelection(IssueMaterialFromStockDetailsModel issueMaterialFromStockDetailsModel) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.issue_material_from_stock_batch_selection_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBatchAndSlnList = makeMultiCheckGenresForItemToBeIssuedFromStock(issueMaterialFromStockDetailsModel);

        mAdapter = new MultiCheckGenreAdapter(mBatchAndSlnList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setChildClickListener(new OnCheckChildClickListener() {
            @Override
            public void onCheckChildCLick(View v, boolean checked, CheckedExpandableGroup group, int childIndex) {
                //Added the if condition because the view is set to null if I manually check the children. Dunno why?
                if(v!=null) {

                    setUpBatchAndSLNSelectionDetails(v, group, childIndex);
                }
            }


        });
        //Start: Added on 15th June to take care of state persistance
        if(issueMaterialFromStockDetailsModel != null)
        {
            mAdapter.checkAllPreviouslySelectedBatchesAndSerialNos();
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
        mAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter.onRestoreInstanceState(savedInstanceState);
    }

}
