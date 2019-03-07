package in.epochconsulting.erpnext.mprp.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import in.epochconsulting.erpnext.mprp.request_items.model.RequestItemList;
import in.epochconsulting.erpnext.mprp.request_items.model.RequestedItemDetailsModel;

/**
 * Created by pragnya on 19/3/18.
 */

public class CustomRequestedItemsUtil extends DiffUtil.Callback {
    private RequestItemList mOldList;
    private RequestItemList mNewList;



    public CustomRequestedItemsUtil(RequestItemList oldList, RequestItemList newList){
        this.mNewList = newList;
        this.mOldList = oldList;

    }
    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.getRequestedItemDetailsModelList().size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.getRequestedItemDetailsModelList().size() : 0;
    }
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

       // return mNewList.get(newItemPosition).equals( mOldList.get(oldItemPosition));
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        System.out.println("Hello!!!!!!!!!!!!! New Year old itemPos is "+ oldItemPosition+" new item pos is "+newItemPosition);
        if(mNewList.getRequestedItemDetailsModelList().get(newItemPosition).equals(mOldList.getRequestedItemDetailsModelList().get(oldItemPosition))) { //I have to override It
            System.out.println("I am in true, the objects are the same");
            return true;
        }
        else {
            System.out.println("I am in not true, objects are not the same");
            return false;
        }
        //return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
    }
   @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
       System.out.println("WASSSSSSSSSAP");
        RequestedItemDetailsModel newData = mNewList.getRequestedItemDetailsModelList().get(newItemPosition);
       RequestedItemDetailsModel oldData = mOldList.getRequestedItemDetailsModelList().get(oldItemPosition);
        Bundle diffBundle = new Bundle();
        if (!newData.getmUOM().equalsIgnoreCase(oldData.getmUOM()) ) {
            diffBundle.putString("KEY_UOM", newData.getmUOM());
        }
        if (!newData.getmAvailableQnty().equalsIgnoreCase(oldData.getmAvailableQnty())) {
            diffBundle.putString("KEY_AVAIL_QNTY", newData.getmAvailableQnty());
        }
        if (!newData.getmRequiredQnty().equalsIgnoreCase(oldData.getmRequiredQnty())) {
            diffBundle.putString("KEY_REQD_QNTY", newData.getmRequiredQnty());
        }
        if (diffBundle.size() == 0) return null;
        return diffBundle;
    }


}
