package in.epochconsulting.erpnext.mprp.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.epochconsulting.erpnext.mprp.model.pojo.ControlDocumentData;

/**
 * Created by pragnya on 16/3/18.
 */

public class ControlDocumentTableEntries {
    @SerializedName("data")
    @Expose
    private List<ControlDocumentData> cdList = null;

    public List<ControlDocumentData> getCdList() {
        return cdList;
    }

    public void setCdList(List<ControlDocumentData> cdList) {
        this.cdList = cdList;
    }
}
