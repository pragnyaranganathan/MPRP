package in.epochconsulting.erpnext.mprp.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pragnya on 16/3/18.
 */

public class ControlDocumentData {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("warehouse")
    @Expose
    private String warehouse;

    @SerializedName("upstream_warehouse")
    @Expose
    private String upstreamWH;

    @SerializedName("downstream_warehouse")
    @Expose
    private String downstreamWH;

    //methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getUpstreamWH() {
        return upstreamWH;
    }

    public void setUpstreamWH(String upstreamWH) {
        this.upstreamWH = upstreamWH;
    }

    public String getDownstreamWH() {
        return downstreamWH;
    }

    public void setDownstreamWH(String downstreamWH) {
        this.downstreamWH = downstreamWH;
    }
}
