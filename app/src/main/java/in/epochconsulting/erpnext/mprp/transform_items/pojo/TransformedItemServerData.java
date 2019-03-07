package in.epochconsulting.erpnext.mprp.transform_items.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pragnya on 11/4/18.
 */

public class TransformedItemServerData {

    @SerializedName("items_consumed")
    @Expose
    private List<ItemsConsumed> itemsConsumed = null;
    @SerializedName("item_code")
    @Expose
    private String itemCode;
    @SerializedName("whole_number")
    @Expose
    private Integer wholeNumber;
    @SerializedName("has_batch_no")
    @Expose
    private Integer hasBatchNo;
    @SerializedName("has_serial_no")
    @Expose
    private Integer hasSerialNo;
    @SerializedName("pass_based_on")
    @Expose
    private String passBasedOn;
    @SerializedName("stock_uom")
    @Expose
    private String stockUom;
    @SerializedName("quantity")
    @Expose
    private Double quantity;

    @SerializedName("batch_nos")
    @Expose
    private List<BatchNo> batchNos = null;

    @SerializedName("serial_nos")
    @Expose
    private List<SerialNo> serialNos = null;

    public List<ItemsConsumed> getItemsConsumed() {
        return itemsConsumed;
    }

    public void setItemsConsumed(List<ItemsConsumed> itemsConsumed) {
        this.itemsConsumed = itemsConsumed;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getWholeNumber() {
        return wholeNumber;
    }

    public void setWholeNumber(Integer wholeNumber) {
        this.wholeNumber = wholeNumber;
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

    public String getPassBasedOn() {
        return passBasedOn;
    }

    public void setPassBasedOn(String passBasedOn) {
        this.passBasedOn = passBasedOn;
    }

    public String getStockUom() {
        return stockUom;
    }

    public void setStockUom(String stockUom) {
        this.stockUom = stockUom;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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

