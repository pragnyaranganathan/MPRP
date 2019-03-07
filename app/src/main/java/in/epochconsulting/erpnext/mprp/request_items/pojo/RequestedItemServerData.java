package in.epochconsulting.erpnext.mprp.request_items.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pragnya on 22/3/18.
 */

public class RequestedItemServerData {
    @SerializedName("item_code")
    @Expose
    private String item_code;

    @SerializedName("warehouse")
    @Expose
    private String warehouse;

    @SerializedName("uom")
    @Expose
    private String uom;

    @SerializedName("available_qnty")
    @Expose
    private String available_qnty;

    @SerializedName("reqd_qnty")
    @Expose
    private String reqd_qnty;


    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getAvailable_qnty() {
        return available_qnty;
    }

    public void setAvailable_qnty(String available_qnty) {
        this.available_qnty = available_qnty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }


    public String getReqd_qnty() {
        return reqd_qnty;
    }

    public void setReqd_qnty(String reqd_qnty) {
        this.reqd_qnty = reqd_qnty;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
}
