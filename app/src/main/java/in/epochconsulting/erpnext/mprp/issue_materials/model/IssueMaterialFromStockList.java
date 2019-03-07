package in.epochconsulting.erpnext.mprp.issue_materials.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pragnya on 29/8/18.
 */

public class IssueMaterialFromStockList {
    private List<IssueMaterialFromStockDetailsModel> issueMaterialFromStockDetailsModelList = new ArrayList<>();


    public List<IssueMaterialFromStockDetailsModel> getIssueMaterialFromStockDetailsModelList() {
        return issueMaterialFromStockDetailsModelList;
    }

    public void setIssueMaterialFromStockDetailsModelList(List<IssueMaterialFromStockDetailsModel> issueMaterialFromStockDetailsModelList) {
        this.issueMaterialFromStockDetailsModelList = issueMaterialFromStockDetailsModelList;
    }
}
