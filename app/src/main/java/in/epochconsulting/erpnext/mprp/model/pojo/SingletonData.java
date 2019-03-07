package in.epochconsulting.erpnext.mprp.model.pojo;

import java.util.ArrayList;
import java.util.List;

import in.epochconsulting.erpnext.mprp.issue_materials.model.IssueMaterialFromStockList;
import in.epochconsulting.erpnext.mprp.issue_materials.pojo.IssueMaterialFromStockItemsDetailsList;
import in.epochconsulting.erpnext.mprp.request_items.pojo.RequestItemsDetailsList;
import in.epochconsulting.erpnext.mprp.transform_items.model.ItemMadeList;
import in.epochconsulting.erpnext.mprp.transform_items.model.MultiCheckList;
import in.epochconsulting.erpnext.mprp.transform_items.pojo.TransformItemsDetailsList;

/**
 * Created by pragnya on 10/3/18.
 */

public class SingletonData {

    private static SingletonData singleton;

    private ControlDocumentItemTableEntries itemTableEntries;
    private UserTableEntries loggedInUser = null;
    private ControlDocumentTableEntries controlDocumentTableEntries;
    private RequestItemsDetailsList requestedItemsDetailsList;
    private String mloggedInUserName ;

    private TransformItemsDetailsList transformedItemsDetailsList;
    private List<MultiCheckList> selectedBatchAndSerialNoList = null;
    private ItemMadeList transformedItemModelListForStatePersistance;

    private IssueMaterialFromStockItemsDetailsList issueMaterialFromStockItemsDetailsList;
    private IssueMaterialFromStockList issueMaterialFromStockModelListForStatePersistance;

    private List<MultiCheckList> selectedBatchAndSerialNoListForIssuedItemFromStock = null;

    private SingletonData(){
    }
    public static SingletonData getInstance(){
        if(singleton==null){
            singleton= new SingletonData();

        }
        return singleton;
    }

    public ControlDocumentItemTableEntries getItemTableEntries() {
        return itemTableEntries;
    }

    public void setItemTableEntries(ControlDocumentItemTableEntries itemTableEntries) {
        this.itemTableEntries = itemTableEntries;
    }

    public UserTableEntries getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(UserTableEntries loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public ControlDocumentTableEntries getControlDocumentTableEntries() {
        return controlDocumentTableEntries;
    }

    public void setControlDocumentTableEntries(ControlDocumentTableEntries controlDocumentTableEntries) {
        this.controlDocumentTableEntries = controlDocumentTableEntries;
    }

    public RequestItemsDetailsList getRequestedItemsDetailsList() {
        return requestedItemsDetailsList;
    }

    public void setRequestedItemsDetailsList(RequestItemsDetailsList requestedItemsDetailsList) {
        this.requestedItemsDetailsList = requestedItemsDetailsList;
    }

    public void setLoggedInUserName(String loggedInUser) {
        this.mloggedInUserName = loggedInUser;
    }

    public String getMloggedInUserName() {
        return mloggedInUserName;
    }

    public TransformItemsDetailsList getTransformedItemsDetailsList() {
        return transformedItemsDetailsList;
    }

    public void setTransformedItemsDetailsList(TransformItemsDetailsList transformedItemsDetailsList) {
        this.transformedItemsDetailsList = transformedItemsDetailsList;
    }

    public void setSelectedBatchAndSerialNoList(List<MultiCheckList> batchAndSlnList) {
        this.selectedBatchAndSerialNoList = batchAndSlnList;
    }

    public List<MultiCheckList> getSelectedBatchAndSerialNoList() {
        return selectedBatchAndSerialNoList;
    }


    public ItemMadeList getTransformedItemModelListForStatePersistance() {
        return transformedItemModelListForStatePersistance;
    }

    public void setTransformedItemModelListForStatePersistance(ItemMadeList transformedItemModelListForStatePersistance) {
        this.transformedItemModelListForStatePersistance = transformedItemModelListForStatePersistance;
    }

    public void setIssueMaterialFromStockItemsDetailsList(IssueMaterialFromStockItemsDetailsList response) {
        issueMaterialFromStockItemsDetailsList = response;
    }

    public IssueMaterialFromStockItemsDetailsList getIssueMaterialFromStockItemsDetailsList() {
        return issueMaterialFromStockItemsDetailsList;
    }

    public void setIssueMaterialFromStockModelListForStatePersistance(IssueMaterialFromStockList issueMaterialModelList){
        this.issueMaterialFromStockModelListForStatePersistance = issueMaterialModelList;
    }
    public IssueMaterialFromStockList getIssueMaterialFromStockModelListForStatePersistance() {
        return issueMaterialFromStockModelListForStatePersistance;
    }

    public void setSelectedBatchAndSerialNoListForIssuedItemFromStock(List<MultiCheckList> mBatchAndSlnList) {
        this.selectedBatchAndSerialNoListForIssuedItemFromStock = mBatchAndSlnList;
    }

    public List<MultiCheckList> getSelectedBatchAndSerialNoListForIssuedItemFromStock() {
        return selectedBatchAndSerialNoListForIssuedItemFromStock;
    }
}
