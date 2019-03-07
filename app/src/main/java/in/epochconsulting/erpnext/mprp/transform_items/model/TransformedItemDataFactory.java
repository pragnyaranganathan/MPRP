package in.epochconsulting.erpnext.mprp.transform_items.model;

/**
 * Created by pragnya on 11/4/18.
 */

import java.util.ArrayList;

import java.util.List;


import in.epochconsulting.erpnext.mprp.transform_items.pojo.BatchNo;
import in.epochconsulting.erpnext.mprp.transform_items.pojo.ItemsConsumed;
import in.epochconsulting.erpnext.mprp.transform_items.pojo.SerialNo;
import in.epochconsulting.erpnext.mprp.transform_items.pojo.TransformedItemServerData;
import in.epochconsulting.erpnext.mprp.utils.Constants;

public class TransformedItemDataFactory {

    TransformedItemDataFactory(){

    }



    public static ItemMadeList  makeTransformedList(List<TransformedItemServerData> transformedItemServerDataList)
    {
        ItemMadeList list = new ItemMadeList();
        boolean isItemRawMaterial ;
        ArrayList<ItemMadeModel> itemMadeModelArrayList = new ArrayList<>();
        for(TransformedItemServerData transformedItemServerData : transformedItemServerDataList) {
            ItemMadeModel rvitemMade = new ItemMadeModel();
            rvitemMade.setItemMadeCode(transformedItemServerData.getItemCode());
            rvitemMade.setPassEntryBasedOn(transformedItemServerData.getPassBasedOn());
            rvitemMade.setStockUOM(transformedItemServerData.getStockUom());
            rvitemMade.setStockQnty((transformedItemServerData.getQuantity()));
            rvitemMade.setQtyReqd(transformedItemServerData.getQuantity());
            rvitemMade.setBoMValuesUsed(true); //initially set it to true

            if(transformedItemServerData.getWholeNumber() == 1)
            {
                rvitemMade.setMustBeAWholeNumber(true);
                //rvitemMade.setQtyReqd(0.0);
                //rvitemMade.setQtyReqd(transformedItemServerData.getQuantity());

            }
            else {
                rvitemMade.setMustBeAWholeNumber(false);
                //rvitemMade.setQtyReqd(0.0);

            }
            if(rvitemMade.getPassEntryBasedOn().equals(Constants.PASS_ENTRY_BASED_ON_RAW_MATERIAL))
            {
                isItemRawMaterial = true;
            }
            else
            {
                isItemRawMaterial = false;
            }
            if(transformedItemServerData.getHasSerialNo() == 1)
            {
                rvitemMade.setHasSerialNos(true);
                rvitemMade.setSerialNoModelArrayList(makeSerialNoList(transformedItemServerData.getSerialNos()));
            }else{
                rvitemMade.setSerialNoModelArrayList(new ArrayList<SerialNoModel>());
            }
            if(transformedItemServerData.getHasBatchNo() == 1) {
                rvitemMade.setHasBatchNos(true);
            }
                //rvitemMade.setBatchNoModelArrayList(makeBatchNoList(transformedItemServerData.getBatchNos()));
            rvitemMade.setBatchNoModelArrayList(new ArrayList<BatchNoModel>());



            List<ItemConsumedModel> itemConsumedModelArrayList = makeItemConsumedModelList(isItemRawMaterial, transformedItemServerData.getItemsConsumed());
            rvitemMade.setItemConsumedModelList(itemConsumedModelArrayList);
            itemMadeModelArrayList.add(rvitemMade);

        }
        list.setItemMadeModelList(itemMadeModelArrayList);
        return list;
    }

    private static List<BatchNoModel> makeBatchNoList(List<BatchNo> batchNoList) {
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



    private static List<SerialNoModel> makeSerialNoList(List<SerialNo> serialNoList) {
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

    private static List<ItemConsumedModel> makeItemConsumedModelList(boolean isItemRawMaterial, List<ItemsConsumed> itemsConsumedList)
    {
        List<ItemConsumedModel> itemConsumedModelArrayList = new ArrayList<>();


        //add item Consumed for each item made
        for(ItemsConsumed itemConsumed: itemsConsumedList)
        {
            boolean mustBeAWholeNumber ;
            boolean hasBatchNos;
            boolean hasSerialNos;

            if(itemConsumed.getMustBeWholeNo() == 1 )
            {
                mustBeAWholeNumber = true;

            } else {
                mustBeAWholeNumber = false;

            }
            if(itemConsumed.getHasBatchNo() == 1)
            {
                hasBatchNos = true;
            } else{
                hasBatchNos = false;
            }
            if (itemConsumed.getHasSerialNo() == 1)
            {
                hasSerialNos = true;

            } else {
                hasSerialNos = false;
            }
            ItemConsumedModel rvItemConsumed= new ItemConsumedModel(itemConsumed.getItemCode(), itemConsumed.getQntyConsumedPerUnit(), isItemRawMaterial, mustBeAWholeNumber, itemConsumed.getStockQnty(), itemConsumed.getStockUom(), hasBatchNos, hasSerialNos);
            if(hasBatchNos){
            rvItemConsumed.setBatchNoModelList(makeBatchNoList(itemConsumed.getBatchNos()));
                }
            else {
                rvItemConsumed.setBatchNoModelList(null);
            }
            if(hasSerialNos){
            rvItemConsumed.setSerialNoModelList(makeSerialNoList(itemConsumed.getSerialNos()));
            }else{
                rvItemConsumed.setSerialNoModelList(null);
            }
            itemConsumedModelArrayList.add(rvItemConsumed);
        }
        return  itemConsumedModelArrayList;
    }
    public static List<MultiCheckList> makeMultiCheckGenres(ItemConsumedModel itemsConsumed) {
        List<MultiCheckList> multiCheckList = new ArrayList<>();

             if(itemsConsumed.isHasBatchNos() && itemsConsumed.getBatchNoModelList()!=null && !itemsConsumed.getBatchNoModelList().isEmpty()){
                  // MultiCheckBatchList batchDetails = new MultiCheckBatchList(itemsConsumed.getItemConsumedCode()+" -"+ " Batch Nos", makeBatchNoList(itemsConsumed.getBatchNos()));
                 MultiCheckBatchList batchDetails = new MultiCheckBatchList(itemsConsumed.getItemConsumedCode()+" -"+ " Batch Nos", itemsConsumed.getBatchNoModelList());
                   batchDetails.setItemCode(itemsConsumed.getItemConsumedCode());
                   multiCheckList.add(batchDetails);
               }
               if(itemsConsumed.isHasSerialNos()  &&itemsConsumed.getSerialNoModelList()!=null && !itemsConsumed.getSerialNoModelList().isEmpty() )
               {
                   //MultiCheckSerialNoList serialNoDetails = new MultiCheckSerialNoList(itemsConsumed.getItemConsumedCode()+" -"+" Serial Nos", makeSerialNoList(itemsConsumed.getSerialNos()));
                   MultiCheckSerialNoList serialNoDetails = new MultiCheckSerialNoList(itemsConsumed.getItemConsumedCode()+" -"+" Serial Nos", itemsConsumed.getSerialNoModelList());
                   serialNoDetails.setItemCode(itemsConsumed.getItemConsumedCode());
                   multiCheckList.add(serialNoDetails);
               }




        return multiCheckList;
    }



}

