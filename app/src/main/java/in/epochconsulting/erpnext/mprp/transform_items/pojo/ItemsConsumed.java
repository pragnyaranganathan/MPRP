package in.epochconsulting.erpnext.mprp.transform_items.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pragnya on 11/4/18.
 */

public class ItemsConsumed {

    @SerializedName("qnty_consumed_per_unit")
    @Expose
    private Double qntyConsumedPerUnit;
    @SerializedName("item_code")
    @Expose
    private String itemCode;
    @SerializedName("must_be_whole_no")
    @Expose
    private Integer mustBeWholeNo;
    @SerializedName("stock_uom")
    @Expose
    private String stockUom;
    @SerializedName("has_batch_no")
    @Expose
    private Integer hasBatchNo;
    @SerializedName("has_serial_no")
    @Expose
    private Integer hasSerialNo;
    @SerializedName("stock_qnty")
    @Expose
    private Double stockQnty;

    @SerializedName("batch_nos")
    @Expose
    private List<BatchNo> batchNos = null;

    @SerializedName("serial_nos")
    @Expose
    private List<SerialNo> serialNos = null;

    public Double getQntyConsumedPerUnit() {
        return qntyConsumedPerUnit;
    }

    public void setQntyConsumedPerUnit(Double qntyConsumedPerUnit) {
        this.qntyConsumedPerUnit = qntyConsumedPerUnit;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public Integer getMustBeWholeNo() {
        return mustBeWholeNo;
    }

    public void setMustBeWholeNo(Integer mustBeWholeNo) {
        this.mustBeWholeNo = mustBeWholeNo;
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

    public Double getStockQnty() {
        return stockQnty;
    }

    public void setStockQnty(Double stockQnty) {
        this.stockQnty = stockQnty;
    }

    public List<BatchNo> getBatchNos() {
        return batchNos;
    }

    public void setBatchNos(List<BatchNo> batchNos) {
        this.batchNos = batchNos;
    }

    public List<SerialNo> getSerialNos() {
        return serialNos;
    }

    public void setSerialNos(List<SerialNo> serialNos) {
        this.serialNos = serialNos;
    }
}
