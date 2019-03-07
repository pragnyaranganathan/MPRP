package in.epochconsulting.erpnext.mprp.request_items.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.epochconsulting.erpnext.mprp.request_items.pojo.RequestedItemServerData;

/**
 * Created by pragnya on 22/3/18.
 */

public class RequestItemsDetailsList {
    @SerializedName("message")
    @Expose
    private List<RequestedItemServerData> itemsDataList = null;

    public List<RequestedItemServerData> getItemsDataList() {
        return itemsDataList;
    }

    public void setItemsDataList(List<RequestedItemServerData> itemsDataList) {
        this.itemsDataList = itemsDataList;
    }
}
