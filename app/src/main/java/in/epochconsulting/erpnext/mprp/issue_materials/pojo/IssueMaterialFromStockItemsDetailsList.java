package in.epochconsulting.erpnext.mprp.issue_materials.pojo;

/**
 * Created by pragnya on 1/8/18.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class IssueMaterialFromStockItemsDetailsList {
    @SerializedName("message")
    @Expose
    private List<IssueMaterialFromStockServerData> issueMaterialFromStockServerDatas = null;

    public List<IssueMaterialFromStockServerData> getIssueMaterialFromStockServerDatas() {
        return issueMaterialFromStockServerDatas;
    }

    public void setIssueMaterialFromStockServerDatas(List<IssueMaterialFromStockServerData> message) {
        this.issueMaterialFromStockServerDatas = message;
    }
}



