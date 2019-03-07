package in.epochconsulting.erpnext.mprp.issue_materials.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.epochconsulting.erpnext.mprp.transform_items.pojo.BatchNo;
import in.epochconsulting.erpnext.mprp.transform_items.pojo.SerialNo;

/**
 * Created by pragnya on 3/8/18.
 */

public class IssueMaterialFromStockServerData {
    @SerializedName("batch_nos")
    @Expose
    private List<BatchNo> batchNos = null;
    @SerializedName("downstream_wh_list")
    @Expose
    private List<String> downstreamWhList = null;
    @SerializedName("item_code")
    @Expose
    private String itemCode;
    @SerializedName("stock_uom")
    @Expose
    private String stockUom;
    @SerializedName("has_batch_no")
    @Expose
    private Integer hasBatchNo;
    @SerializedName("has_serial_no")
    @Expose
    private Integer hasSerialNo;
    @SerializedName("available_quantity")
    @Expose
    private Double availableQuantity;
    @SerializedName("serial_nos")
    @Expose
    private List<SerialNo> serialNos = null;

    public List<BatchNo> getBatchNos() {
        return batchNos;
    }

    public void setBatchNos(List<BatchNo> batchNos) {
        this.batchNos = batchNos;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getStockUom() {
        return stockUom;
    }

    public void setStockUom(String stockUom) {
        this.stockUom = stockUom;
    }

    public Integer getHasBatchNo() {
        return hasBatchNo;
    }

    public void setHasBatchNo(Integer hasBatchNo) {
        this.hasBatchNo = hasBatchNo;
    }

    public Integer getHasSerialNo() {
        return hasSerialNo;
    }

    public void setHasSerialNo(Integer hasSerialNo) {
        this.hasSerialNo = hasSerialNo;
    }

    public Double getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Double availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public List<SerialNo> getSerialNos() {
        return serialNos;
    }

    public void setSerialNos(List<SerialNo> serialNos) {
        this.serialNos = serialNos;
    }

    public List<String> getDownstreamWhList() {
        return downstreamWhList;
    }

    public void setDownstreamWhList(List<String> downstreamWhList) {
        this.downstreamWhList = downstreamWhList;
    }
}

