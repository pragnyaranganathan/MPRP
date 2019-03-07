package in.epochconsulting.erpnext.mprp.transform_items.view;

import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.transform_items.implementation.TransformItemsListener;

/**
 * Created by pragnya on 18/4/18.
 */

public class CustomTransformedItemsForRawMaterialViewHolder extends RecyclerView.ViewHolder {
    TextView itemName;
    TextView itemQnty;
    TextView itemQntyPerunit;
    TextView itemMadeRawMaterialUOM;
    public RecyclerView recyclerView_consumedItemsList;
    ImageView setBatchAndSLNButton;
    TransformItemsListener listener;
    CheckBox useBOMValues;
    boolean useBOMValue;
    Button deleteRow ;

    public CustomTransformedItemsForRawMaterialViewHolder(View itemView, TransformItemsListener listener) {
        super(itemView);
        itemName = itemView.findViewById(R.id.list_item_transformed_name_raw_material);
        recyclerView_consumedItemsList = itemView.findViewById(R.id.items_consumed_rv);
        itemQnty = itemView.findViewById(R.id.list_item_transformed_name_raw_material_qnty);
        //itemQntyPerunit = itemView.findViewById(R.id.list_item_raw_material_qty_used_per_unit);
        itemMadeRawMaterialUOM = itemView.findViewById(R.id.item_made_raw_material_stock_uom);
        setBatchAndSLNButton = itemView.findViewById(R.id.item_made_raw_material_set_batch_button);
        this.listener = listener;
        useBOMValues = itemView.findViewById(R.id.usebomval_rm);
        deleteRow = itemView.findViewById(R.id.delete_row_rm);
    }

    public void setItem(String name, String qty, final String reqdQnty, boolean isAWholeNumber, String stockUOM, boolean isBOMUsed) {
        itemName.setText(name);
        //itemQntyPerunit.setText(qty);
        itemQnty.setText(reqdQnty);
        itemMadeRawMaterialUOM.setText(stockUOM);
        useBOMValue = isBOMUsed;
        useBOMValues.setChecked(useBOMValue);
        itemQnty.setEnabled(!useBOMValue); //to allow edits if BOM is not used


        if(isAWholeNumber) {

            itemQnty.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else{

            itemQnty.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        setBatchAndSLNButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSetBatchAndSLNButtonPressedForRawMaterial(getAdapterPosition());

            }
        });
        itemQnty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE){

                    listener.onRawMaterialItemTransformedQtyEntered(textView.getText().toString(),getAdapterPosition(),useBOMValue);
                    //useBOMValues.setChecked(false);

                }
                return false;
            }
        });
        useBOMValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUseBOMSelectedForRawMaterialTransformedItem(useBOMValues.isChecked(),getAdapterPosition());
            }
        });

        deleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.deleteRow(getAdapterPosition());
            }
        });
    }
}