package in.epochconsulting.erpnext.mprp.transform_items.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SerialNo {

    @SerializedName("warehouse")
    @Expose
    private String warehouse;
    @SerializedName("serial_no")
    @Expose
    private String serialNo;

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

}