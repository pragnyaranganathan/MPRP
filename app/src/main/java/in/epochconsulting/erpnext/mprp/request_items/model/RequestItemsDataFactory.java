package in.epochconsulting.erpnext.mprp.request_items.model;

import java.util.ArrayList;
import java.util.List;

import in.epochconsulting.erpnext.mprp.request_items.pojo.RequestItemsDetailsList;
import in.epochconsulting.erpnext.mprp.request_items.pojo.RequestedItemServerData;

/**
 * Created by pragnya on 3/5/18.
 */

public class RequestItemsDataFactory {
    public static RequestItemList  makeRequestItemsList(List<RequestedItemServerData> requestedItemServerDataList){
        RequestItemList list = new RequestItemList();
        ArrayList<RequestedItemDetailsModel> requestItemsModelList = new ArrayList<>();
        for(RequestedItemServerData requestItemFromServer : requestedItemServerDataList)
        {
            RequestedItemDetailsModel itemDetailsModel = new RequestedItemDetailsModel(requestItemFromServer.getItem_code(), requestItemFromServer.getUom(), requestItemFromServer.getAvailable_qnty(), requestItemFromServer.getReqd_qnty(), requestItemFromServer.getWarehouse());
            requestItemsModelList.add(itemDetailsModel);
        }
        list.setRequestedItemDetailsModelList(requestItemsModelList);
        return list;
    }
}
