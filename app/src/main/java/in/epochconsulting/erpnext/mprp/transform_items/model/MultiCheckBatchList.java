package in.epochconsulting.erpnext.mprp.transform_items.model;

import java.util.List;

/**
 * Created by pragnya on 8/5/18.
 */

public class MultiCheckBatchList extends MultiCheckList {


    private List<BatchNoModel> itemList;
    public MultiCheckBatchList(String title, List<BatchNoModel> items){
        super(title, items);
        itemList = items;

    }


    public List<BatchNoModel> getBatchList() {
        return itemList;
    }


}
