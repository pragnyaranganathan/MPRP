package in.epochconsulting.erpnext.mprp.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pragnya on 10/3/18.
 */

public class ItemData {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("item_code")
    @Expose
    private String item_code;

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
