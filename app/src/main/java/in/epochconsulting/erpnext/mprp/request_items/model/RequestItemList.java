package in.epochconsulting.erpnext.mprp.request_items.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pragnya on 3/5/18.
 */

public class RequestItemList {
    private List<RequestedItemDetailsModel> requestedItemDetailsModelList = new ArrayList<>();

    public List<RequestedItemDetailsModel> getRequestedItemDetailsModelList() {
        return requestedItemDetailsModelList;
    }

    public void setRequestedItemDetailsModelList(List<RequestedItemDetailsModel> requestedItemDetailsModelList) {
        this.requestedItemDetailsModelList = requestedItemDetailsModelList;
    }
}
