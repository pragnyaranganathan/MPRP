package in.epochconsulting.erpnext.mprp.transform_items.view;

/**
 * Created by pragnya on 11/4/18.
 */

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.transform_items.implementation.TransformItemsListener;

public class ItemConsumedViewHolder extends ChildViewHolder {

    private TextView itemConsumedTextView;
    private TransformItemsListener mListener = null;

    public ItemConsumedViewHolder(View itemView, TransformItemsListener listener) {
        super(itemView);
        itemConsumedTextView = (TextView) itemView.findViewById(R.id.list_item_item_consumed_name);
        mListener = listener;

        itemConsumedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null) {
                    mListener.itemConsumedClicked(itemConsumedTextView.getText().toString(), getAdapterPosition());
                }
            }
        });
    }

    public void setItemConsumedName(String name) {
        itemConsumedTextView.setText(name);


    }


}
