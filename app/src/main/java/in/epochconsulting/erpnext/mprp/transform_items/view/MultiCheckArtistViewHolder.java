package in.epochconsulting.erpnext.mprp.transform_items.view;

import android.app.Activity;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;

import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder;

import in.epochconsulting.erpnext.mprp.R;

/**
 * Created by pragnya on 8/5/18.
 */

public class MultiCheckArtistViewHolder extends CheckableChildViewHolder {

    private CheckedTextView childCheckedTextView;


    public MultiCheckArtistViewHolder(View itemView) {
        super(itemView);

        childCheckedTextView =
                (CheckedTextView) itemView.findViewById(R.id.list_item_multicheck_batch_name);
    }

    @Override
    public Checkable getCheckable() {
        return childCheckedTextView;
    }

    public void setArtistName(String artistName) {
        childCheckedTextView.setText(artistName);
    }
    //Start: Added the below lines of Code 23rd May 2018

    public void setCheckedForBatch(boolean checkedStatus)
    {
        childCheckedTextView.setChecked(checkedStatus);
        childCheckedTextView.setEnabled(false);//this is done so that, user is unable to uncheck this
    }
    //End: Added the below lines of Code 23rd May 2018


}