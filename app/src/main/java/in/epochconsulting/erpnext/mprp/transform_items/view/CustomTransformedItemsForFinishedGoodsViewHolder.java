package in.epochconsulting.erpnext.mprp.transform_items.view;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.transform_items.implementation.TransformItemsListener;

/**
 * Created by pragnya on 13/4/18.
 */

public class CustomTransformedItemsForFinishedGoodsViewHolder extends RecyclerView.ViewHolder{
    TextView itemName;
    EditText qntyReqd;
    TextView qntyPerUnit;
    public RecyclerView recyclerView_consumedItemsList;
    private TextView itemMadeUOM;
    TransformItemsListener listener;
    ImageView setBatchAndSLNButton;
    CheckBox useBOMValues;
    boolean useBOMValue;
    Button deleteRow;

    public CustomTransformedItemsForFinishedGoodsViewHolder(View itemView, final TransformItemsListener listener) {
        super(itemView);
        itemName = itemView.findViewById(R.id.list_item_transformed_finished_goods_name);
        recyclerView_consumedItemsList = itemView.findViewById(R.id.items_consumed_rv_finished_goods);
        qntyReqd = itemView.findViewById(R.id.list_item_transformed_finished_goods_qnty);
        this.listener =  listener;
        //qntyPerUnit = itemView.findViewById(R.id.list_item_finished_goods_qty_used_per_unit);
        itemMadeUOM = itemView.findViewById(R.id.txfm_item_made_finished_goods_stock_uom);
        setBatchAndSLNButton = itemView.findViewById(R.id.item_made_finished_goods_set_batch_number_button);
        useBOMValues = itemView.findViewById(R.id.usebomval_fg);
        deleteRow = itemView.findViewById(R.id.delete_row_fg);



    }
    public void setItem(String name, final String qntyPerUnit, String qnty, boolean isAWholeNumber, String stockUOM,boolean isBOMUsed){
        itemName.setText(name);
        qntyReqd.setText(qnty);
       // this.qntyPerUnit.setText(qntyPerUnit);
        itemMadeUOM.setText(stockUOM);
        useBOMValue = isBOMUsed;
        useBOMValues.setChecked(isBOMUsed);

        if (isAWholeNumber)
        {
            qntyReqd.setInputType(InputType.TYPE_CLASS_NUMBER);

        }
        else
        {
            qntyReqd.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        qntyReqd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if ((Double.valueOf(qntyReqd.getText().toString())/Double.valueOf(qntyPerUnit)%1!=0) || !useBOMValues.isChecked()) {

                        useBOMValue = false;
                    }
                    else{

                        useBOMValue= true;
                    }
                    listener.onQntyEntered(textView.getText().toString(),getAdapterPosition(),useBOMValue);
                    //useBOMValues.setChecked(false);

                }
                return false;
            }
        });
        setBatchAndSLNButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSetBatchAndSLNButtonPressedForFinishedGoods(getAdapterPosition());
            }
        });
        useBOMValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUseBOMSelectedFinishedGoodsTransformedItem(useBOMValues.isChecked(),getAdapterPosition());
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
