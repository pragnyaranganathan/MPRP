package in.epochconsulting.erpnext.mprp.transform_items.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pragnya on 11/4/18.
 */

public class TransformItemsDetailsList {
    @SerializedName("message")
    @Expose
    private List<TransformedItemServerData> itemsDataList = null;

    public List<TransformedItemServerData> getItemsDataList() {
        return itemsDataList;
    }

    public void setItemsDataList(List<TransformedItemServerData> itemsDataList) {
        this.itemsDataList = itemsDataList;
    }
}
