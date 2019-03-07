package in.epochconsulting.erpnext.mprp.issue_materials.model;

import java.util.ArrayList;
import java.util.List;

import in.epochconsulting.erpnext.mprp.issue_materials.pojo.IssueMaterialFromStockServerData;
import in.epochconsulting.erpnext.mprp.transform_items.model.BatchNoModel;
import in.epochconsulting.erpnext.mprp.transform_items.model.MultiCheckBatchList;
import in.epochconsulting.erpnext.mprp.transform_items.model.MultiCheckList;
import in.epochconsulting.erpnext.mprp.transform_items.model.MultiCheckSerialNoList;
import in.epochconsulting.erpnext.mprp.transform_items.model.SerialNoModel;
import in.epochconsulting.erpnext.mprp.transform_items.model.TransformedItemDataFactory;
import in.epochconsulting.erpnext.mprp.transform_items.pojo.BatchNo;
import in.epochconsulting.erpnext.mprp.transform_items.pojo.SerialNo;

/**
 * Created by pragnya on 29/8/18.
 */

public class IssueMaterialFromStockDataFactory {

    IssueMaterialFromStockDataFactory(){

    }
    public static IssueMaterialFromStockList makeIssueMaterialFromStockDataList(List<IssueMaterialFromStockServerData> serverDataList){
        IssueMaterialFromStockList list = new IssueMaterialFromStockList();
        ArrayList<IssueMaterialFromStockDetailsModel> issueMaterialFromStockDetailsModelArrayList = new ArrayList<>();
        for(IssueMaterialFromStockServerData materialFromStockServerData: serverDataList){
            IssueMaterialFromStockDetailsModel detailsModel = new IssueMaterialFromStockDetailsModel();
            detailsModel.setItemCode(materialFromStockServerData.getItemCode());
            detailsModel.setAvailablQty(materialFromStockServerData.getAvailableQuantity());
            detailsModel.setIssuedQty(0.0);
            detailsModel.setStockUOM(materialFromStockServerData.getStockUom());
            if(materialFromStockServerData.getHasBatchNo() == 1){
                detailsModel.setHasBatchNos(true);
                detailsModel.setBatchNoModelList(makeBatchNoListForIssueItemFromStock(materialFromStockServerData.getBatchNos()));
            }
            else{
                detailsModel.setHasBatchNos(false);
                detailsModel.setBatchNoModelList(null);
            }
            if(materialFromStockServerData.getHasSerialNo() == 1){
                detailsModel.setHasSerialNos(true);
                detailsModel.setSerialNoModelList(makeSerialNoListForIssueItemFromStock(materialFromStockServerData.getSerialNos()));
            }
            else
            {
                detailsModel.setHasSerialNos(false);
                detailsModel.setSerialNoModelList(null);
            }
            detailsModel.setDownStreamWHList(materialFromStockServerData.getDownstreamWhList());


            issueMaterialFromStockDetailsModelArrayList.add(detailsModel);
        }
        list.setIssueMaterialFromStockDetailsModelList(issueMaterialFromStockDetailsModelArrayList);
        return list;

    }

    private static List<BatchNoModel> makeBatchNoListForIssueItemFromStock(List<BatchNo> batchNoList) {
        List<BatchNoModel> batchNoModelList = new ArrayList<>();
        if(batchNoList!=null && !batchNoList.isEmpty()) {
            for (BatchNo batch : batchNoList) {
                BatchNoModel batchModel = new BatchNoModel(batch.getBatchNo(), batch.getBatchQtyAtWarehouse());
                //Start - Added on 23rd May 2018, to auto select a single batch for an item
                if(batchNoList.size() == 1)
                {
                    batchModel.setRequestedBatchQty(batchModel.getBatchQtyAtWarehouse());
                }
                //End: Added on 23rd May 2018
                batchNoModelList.add(batchModel);

            }
            return batchNoModelList;
        }
        else
            return new ArrayList<BatchNoModel>();
    }

    private static List<SerialNoModel> makeSerialNoListForIssueItemFromStock(List<SerialNo> serialNoList) {
        List<SerialNoModel> serialNoModelList = new ArrayList<>();
        if(serialNoList!=null && !serialNoList.isEmpty()) {
            for (SerialNo sln : serialNoList) {
                SerialNoModel serialNoModel = new SerialNoModel(sln.getSerialNo(), sln.getWarehouse());
                serialNoModelList.add(serialNoModel);
            }
            return serialNoModelList;
        }
        else
            return new ArrayList<SerialNoModel>();

    }

    public static List<MultiCheckList> makeMultiCheckGenresForItemToBeIssuedFromStock(IssueMaterialFromStockDetailsModel itemModel){
        List<MultiCheckList> multiCheckList = new ArrayList<>();

        if(itemModel.isHasBatchNos() && itemModel.getBatchNoModelList()!=null && !itemModel.getBatchNoModelList().isEmpty()){

            MultiCheckBatchList batchDetails = new MultiCheckBatchList(itemModel.getItemCode()+" -"+ " Batch Nos", itemModel.getBatchNoModelList());
            batchDetails.setItemCode(itemModel.getItemCode());
            multiCheckList.add(batchDetails);
        }
        if(itemModel.isHasSerialNos()  &&itemModel.getSerialNoModelList()!=null && !itemModel.getSerialNoModelList().isEmpty() )
        {

            MultiCheckSerialNoList serialNoDetails = new MultiCheckSerialNoList(itemModel.getItemCode()+" -"+" Serial Nos", itemModel.getSerialNoModelList());
            serialNoDetails.setItemCode(itemModel.getItemCode());
            multiCheckList.add(serialNoDetails);
        }

        return multiCheckList;
    }
}
