package in.epochconsulting.erpnext.mprp.transform_items.pojo;

/**
 * Created by pragnya on 7/5/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BatchNo {

    @SerializedName("batch_qty_at_warehouse")
    @Expose
    private Double batchQtyAtWarehouse;
    @SerializedName("batch_no")
    @Expose
    private String batchNo;

    public Double getBatchQtyAtWarehouse() {
        return batchQtyAtWarehouse;
    }

    public void setBatchQtyAtWarehouse(Double batchQtyAtWarehouse) {
        this.batchQtyAtWarehouse = batchQtyAtWarehouse;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

}