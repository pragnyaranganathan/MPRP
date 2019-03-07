package in.epochconsulting.erpnext.mprp.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pragnya on 10/3/18.
 */

public class ControlDocumentItemTableEntries {
    @SerializedName("data")
    @Expose
    private List<ItemData> itemDataList = null;

    public List<ItemData> getItemDataList() {
        return itemDataList;
    }

    public void setItemDataList(List<ItemData> itemDataList) {
        this.itemDataList = itemDataList;
    }
}
