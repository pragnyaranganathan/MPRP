package in.epochconsulting.erpnext.mprp.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pragnya on 14/3/18.
 */

public class UserData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("full_name")
    @Expose
    private String fullname;
    @SerializedName("role_profile_name")
    @Expose
    private String userRoleProfile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullName) {
        this.fullname = fullName;
    }
    public String getUserRoleProfile() {
        return userRoleProfile;
    }

    public void setUserRoleProfile(String user_role_profile) {
        this.userRoleProfile = user_role_profile;
    }
}
