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
 * Created by pragnya on 16/4/18.
 */

public class CustomItemConsumedForFinishedGoodsViewHolder extends RecyclerView.ViewHolder {
    private TextView itemConsumedName;
    private EditText itemConsumedQnty;
    private TextView itemConsumedQntyPerUnit;
    private TextView itemUOM;
    TransformItemsListener mListener;
    int parentItemMadePos;
    ImageView setBatchAndSln;

    public CustomItemConsumedForFinishedGoodsViewHolder(View itemView, TransformItemsListener listener, int parentItemMadePosition) {
        super(itemView);

        itemConsumedName = itemView.findViewById(R.id.consumed_item_name_finished_goods);
        itemConsumedQnty = itemView.findViewById(R.id.item_consumed_finished_goods_qnty);
        //itemConsumedQntyPerUnit = itemView.findViewById(R.id.consumed_item_finished_goods_qty_used_per_unit);
        itemUOM = itemView.findViewById(R.id.item_consumed_finished_goods_stock_uom);
        mListener = listener;
        parentItemMadePos = parentItemMadePosition;
        setBatchAndSln = itemView.findViewById(R.id.item_consumed_finished_goods_set_batch_and_sln);
    }

    public TextView getItemConsumedName() {
        return itemConsumedName;
    }

    public void setItemConsumedName(TextView itemConsumedName) {
        this.itemConsumedName = itemConsumedName;
    }
    public void setItemConsumed(String itemConsumed, String qtyPerunit, String qnty, boolean mustBeAWholeNumber, String stockUOM, boolean isBOMUsed)
    {
        itemConsumedName.setText(itemConsumed);
        itemConsumedQnty.setText(qnty);
        if(isBOMUsed)
        {
            itemConsumedQnty.setEnabled(false);
        }
        else{
            itemConsumedQnty.setEnabled(true);
        }
        //itemConsumedQntyPerUnit.setText(qtyPerunit);
        itemUOM.setText(stockUOM);
        if(mustBeAWholeNumber){
            itemConsumedQnty.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else{
            itemConsumedQnty.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        itemConsumedQnty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    mListener.onfinishedGoodsItemConsumedQtyEntered(textView.getText().toString(),getAdapterPosition(), parentItemMadePos);

                }
                return false;
            }
        });
        setBatchAndSln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSetBatchAndSLNForItemConsumed(getAdapterPosition(), parentItemMadePos);
            }
        });

    }
}
