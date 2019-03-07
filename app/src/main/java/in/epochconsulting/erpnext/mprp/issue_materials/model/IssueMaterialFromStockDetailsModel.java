package in.epochconsulting.erpnext.mprp.issue_materials.model;

import java.util.List;

import in.epochconsulting.erpnext.mprp.transform_items.model.BatchNoModel;
import in.epochconsulting.erpnext.mprp.transform_items.model.SerialNoModel;

/**
 * Created by pragnya on 29/8/18.
 */

public class IssueMaterialFromStockDetailsModel {

    private String itemCode;
    private String stockUOM;
    private Double issuedQty;
    private Double availablQty;
    private List<String> downStreamWHList;
    private String selectedDownstreamWH;
    private boolean hasBatchNos;
    private boolean hasSerialNos;
    private List<BatchNoModel> batchNoModelList;
    private List<SerialNoModel> serialNoModelList;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getStockUOM() {
        return stockUOM;
    }

    public void setStockUOM(String stockUOM) {
        this.stockUOM = stockUOM;
    }

    public Double getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Double issuedQty) {
        this.issuedQty = issuedQty;
    }

    public List<String> getDownStreamWHList() {
        return downStreamWHList;
    }

    public void setDownStreamWHList(List<String> downStreamWHList) {
        this.downStreamWHList = downStreamWHList;
    }

    public String getSelectedDownstreamWH() {
        return selectedDownstreamWH;
    }

    public void setSelectedDownstreamWH(String selectedDownstreamWH) {
        this.selectedDownstreamWH = selectedDownstreamWH;
    }

    public boolean isHasBatchNos() {
        return hasBatchNos;
    }

    public void setHasBatchNos(boolean hasBatchNos) {
        this.hasBatchNos = hasBatchNos;
    }

    public boolean isHasSerialNos() {
        return hasSerialNos;
    }

    public void setHasSerialNos(boolean hasSerialNos) {
        this.hasSerialNos = hasSerialNos;
    }

    public List<BatchNoModel> getBatchNoModelList() {
        return batchNoModelList;
    }

    public void setBatchNoModelList(List<BatchNoModel> batchNoModelList) {
        this.batchNoModelList = batchNoModelList;
    }

    public List<SerialNoModel> getSerialNoModelList() {
        return serialNoModelList;
    }

    public void setSerialNoModelList(List<SerialNoModel> serialNoModelList) {
        this.serialNoModelList = serialNoModelList;
    }

    public Double getAvailablQty() {
        return availablQty;
    }

    public void setAvailablQty(Double availablQty) {
        this.availablQty = availablQty;
    }
}
