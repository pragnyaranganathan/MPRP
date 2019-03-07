package in.epochconsulting.erpnext.mprp.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.epochconsulting.erpnext.mprp.model.pojo.UserData;

/**
 * Created by pragnya on 14/3/18.
 */

public class UserTableEntries {
    @SerializedName("data")
    @Expose
    private List<UserData> userDataList = null;

    public List<UserData> getUserData() {
        return userDataList;
    }

    public void setUserData(List<UserData> userData) {
        this.userDataList = userData;
    }
}
