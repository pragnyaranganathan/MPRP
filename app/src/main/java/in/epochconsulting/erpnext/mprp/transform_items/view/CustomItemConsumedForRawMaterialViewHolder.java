package in.epochconsulting.erpnext.mprp.transform_items.view;

import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.transform_items.implementation.TransformItemsListener;

/**
 * Created by pragnya on 24/4/18.
 */

public class CustomItemConsumedForRawMaterialViewHolder extends RecyclerView.ViewHolder {
    private TextView itemConsumedNameRawMaterial;
    private EditText itemConsumedQntyRawMaterial;
    private TextView itemConsumedQntyPerUnitRawMaterial;
    private TextView itemConsumedRawMaterialUOM;
    TransformItemsListener mListener;
    private int parentItemMadePos;
    private boolean bomUsed;
    ImageView selectBatchAndSln;

    public CustomItemConsumedForRawMaterialViewHolder(View itemView, TransformItemsListener listener, int parentItemMadePosition) {
        super(itemView);
        itemConsumedNameRawMaterial = itemView.findViewById(R.id.consumed_item_name_raw_material);
        itemConsumedQntyRawMaterial = itemView.findViewById(R.id.item_consumed_raw_material_qnty);
        mListener = listener;
        parentItemMadePos = parentItemMadePosition;
        //itemConsumedQntyPerUnitRawMaterial = itemView.findViewById(R.id.consumed_item_raw_material_qty_used_per_unit);
        itemConsumedRawMaterialUOM = itemView.findViewById(R.id.item_consumed_raw_material_stock_uom);
        selectBatchAndSln = itemView.findViewById(R.id.item_consumed_raw_material_set_batch_and_sln);
    }



    public TextView getItemConsumedNameRawMaterial() {
        return itemConsumedNameRawMaterial;
    }

    public void setItemConsumedNameRawMaterial(TextView itemConsumedNameRawMaterial) {
        this.itemConsumedNameRawMaterial = itemConsumedNameRawMaterial;
    }
    public void setItemConsumedRawMaterial(String itemConsumed, final String qtyConsumedPerUnit, final String qnty, boolean mustBeAWholeNumber, String stockUOM, final boolean isBOMUsed)
    {
        itemConsumedNameRawMaterial.setText(itemConsumed);
        itemConsumedQntyRawMaterial.setText(qnty);
        //itemConsumedQntyPerUnitRawMaterial.setText(qtyConsumedPerUnit);
        itemConsumedRawMaterialUOM.setText(stockUOM);

        if(mustBeAWholeNumber)
        {
            itemConsumedQntyRawMaterial.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        else {
            itemConsumedQntyRawMaterial.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        itemConsumedQntyRawMaterial.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if ((Double.valueOf(textView.getText().toString())/Double.valueOf(qtyConsumedPerUnit)%1!=0) && !isBOMUsed) {

                        bomUsed = false;
                    }
                    else{

                        bomUsed= true;
                    }
                    mListener.onRawMaterialQntyEntered(textView.getText().toString(),getAdapterPosition(), parentItemMadePos, bomUsed);

                }
                return false;
            }
        });
        selectBatchAndSln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSetBatchAndSLNForItemConsumed(getAdapterPosition(), parentItemMadePos);
            }
        });

    }
}
