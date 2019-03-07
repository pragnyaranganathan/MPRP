package in.epochconsulting.erpnext.mprp.issue_materials.view;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.issue_materials.implementation.IssueMaterialFromStockListener;
import in.epochconsulting.erpnext.mprp.issue_materials.model.IssueMaterialFromStockDetailsModel;

/**
 * Created by pragnya on 10/9/18.
 */

public class CustomIssueMaterialFromStockVH extends RecyclerView.ViewHolder {
    private TextView mItemCode;
    private TextView mUOM;
    private TextView mAvailableQty;
    private EditText mQtyIssued;

    private Spinner mSelectDSWHDropDown;
    private ImageView mSelectBatchSLN;
    private IssueMaterialFromStockListener mListener;
    int mCurrentSelection;
    private TextView mSlNo;

    Button mDeleteRow;

    public CustomIssueMaterialFromStockVH(View itemView, IssueMaterialFromStockListener activity) {
        super(itemView);
        this.mItemCode = itemView.findViewById(R.id.issue_item_from_stock_item_code);
        this.mUOM = itemView.findViewById(R.id.issue_item_from_stock_uom);
        this.mQtyIssued = itemView.findViewById(R.id.isse_item_from_stock_qty);
        this.mSelectDSWHDropDown = itemView.findViewById(R.id.issue_item_from_stock_select_dswh);
        this.mSelectBatchSLN = itemView.findViewById(R.id.issue_item_from_stock_select_batchorsln);
        this.mSlNo = itemView.findViewById(R.id.issue_material_from_stock_slno);
        this.mAvailableQty = itemView.findViewById(R.id.issue_item_from_stock_avaliable_qty);
        this.mDeleteRow = itemView.findViewById(R.id.delete_row_issue_mat_from_stock);
        this.mListener = activity;
        mCurrentSelection = -1;

        mQtyIssued.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    mListener.issueQntyEntered (textView.getText().toString(),  getAdapterPosition());

                }
                return false;
            }
        });
        mSelectDSWHDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(mCurrentSelection!=position && adapterView.getItemAtPosition(position)!=null)
                {
                    mListener.DownstreamWHSelected(adapterView.getItemAtPosition(position).toString(),getAdapterPosition());


                }
                mCurrentSelection = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing

            }
        });


    }

    public void setItem(IssueMaterialFromStockDetailsModel issueMaterialFromStockDetailsModel) {
        List<String> array = new ArrayList<>();
        array.add("Select downstream warehouse");
        for(String dsWH: issueMaterialFromStockDetailsModel.getDownStreamWHList()){
            if(dsWH!=null) {
                array.add(dsWH);
            }

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),android.R.layout.simple_spinner_item,array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mSelectDSWHDropDown.setAdapter(adapter);
        mSelectDSWHDropDown.setSelection(mCurrentSelection);
        if(issueMaterialFromStockDetailsModel.getDownStreamWHList() == null)
        {
            mListener.dsWHListIsEmpty(R.string.dswhlistisnull_str);
        }

        this.mItemCode.setText( issueMaterialFromStockDetailsModel.getItemCode());
        this.mUOM.setText(issueMaterialFromStockDetailsModel.getStockUOM());
        this.mAvailableQty.setText(String.format(Locale.ENGLISH,issueMaterialFromStockDetailsModel.getAvailablQty().toString(),double.class));
        this.mQtyIssued.setText( String.format(Locale.ENGLISH,issueMaterialFromStockDetailsModel.getIssuedQty().toString(),double.class));
        this.mSlNo.setText(String.valueOf(getAdapterPosition()+1));
        mSelectBatchSLN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSelectBatchAndSLN(getAdapterPosition());
            }
        });
        mDeleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.deleteRow(getAdapterPosition());
            }
        });

    }
}
