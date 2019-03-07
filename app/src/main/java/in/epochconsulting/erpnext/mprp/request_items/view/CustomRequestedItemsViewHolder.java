package in.epochconsulting.erpnext.mprp.request_items.view;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.request_items.model.RequestedItemDetailsModel;

/**
 * Created by pragnya on 16/3/18.
 */

public class CustomRequestedItemsViewHolder  extends RecyclerView.ViewHolder{
     TextView mItemCode;
     TextView mUOM;
     TextView mAvailableQnty;
     EditText mRequestedQnty;
     TextView mSlNo;


    RequestQntyEnteredListener reqdQtyEnteredListener;
    int mCurrentSelection;
    int rowSelected;



    public interface RequestQntyEnteredListener{
        void onRequiredQntyEntered(String reqdQnty,  int itemSelectedPosition);
    }

    public CustomRequestedItemsViewHolder(final View itemView,   final int currentSelection, final RequestQntyEnteredListener reqdQtyEnteredListener) {
        super(itemView);


        this.reqdQtyEnteredListener = reqdQtyEnteredListener;
        mItemCode = itemView.findViewById(R.id.requested_item_code);
        mUOM = itemView.findViewById(R.id.requested_item_uom);
        mAvailableQnty = itemView.findViewById(R.id.requested_item_available_qnty);
        mRequestedQnty = itemView.findViewById(R.id.requested_item_requested_qnty);
        mSlNo = itemView.findViewById(R.id.requested_item_slno);
        mCurrentSelection = currentSelection;



        mRequestedQnty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                reqdQtyEnteredListener.onRequiredQntyEntered(textView.getText().toString(),  getAdapterPosition());

                }
                return false;
            }
        });
    }



    public void setItem(RequestedItemDetailsModel item) {


       mItemCode.setText(item.getmItemCode());
        mUOM.setText(item.getmUOM());
        mAvailableQnty.setText(item.getmAvailableQnty());
        mRequestedQnty.setText(item.getmRequiredQnty());
        this.mSlNo.setText(String.valueOf(getAdapterPosition()+1));
        this.rowSelected = getAdapterPosition(); //have to do it here because the binding happens here
    }
}
