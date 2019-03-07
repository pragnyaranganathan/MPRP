package in.epochconsulting.erpnext.mprp.transform_items.view;

/**
 * Created by pragnya on 11/4/18.
 */

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.transform_items.implementation.TransformItemsListener;

public class ItemMadeViewHolder extends ChildViewHolder  {

    private TextView itemMadeName;
    private TransformItemsListener mListener= null;

    public ItemMadeViewHolder(View itemView, TransformItemsListener listener) {
        super(itemView);
        itemMadeName = (TextView) itemView.findViewById(R.id.list_item_item_made_name);
        mListener = listener ;
        itemMadeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null)
                {
                    mListener.itemMadeClicked(itemMadeName.getText().toString(),getAdapterPosition());
                }

            }
        });
    }

    public void setItemMadeName(String name) {
        itemMadeName.setText(name);


    }


}