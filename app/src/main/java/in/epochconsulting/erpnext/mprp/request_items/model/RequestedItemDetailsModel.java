package in.epochconsulting.erpnext.mprp.request_items.model;

/**
 * Created by pragnya on 3/5/18.
 */

public class RequestedItemDetailsModel {
    private String mItemCode;
    private String mUOM;
    private String mAvailableQnty;
    private String mRequiredQnty;
    private String mSrcWarehouse;

    public RequestedItemDetailsModel(String mItemCode, String mUOM, String mAvailableQnty, String mRequiredQnty, String srcWarehouse) {
        this.mItemCode = mItemCode;
        this.mUOM = mUOM;
        this.mAvailableQnty = mAvailableQnty;
        this.mRequiredQnty = mRequiredQnty;
        this.mSrcWarehouse = srcWarehouse; //This is act
    }
    public String getmItemCode() {
        return mItemCode;
    }

    public void setmItemCode(String mItemCode) {
        this.mItemCode = mItemCode;
    }

    public String getmUOM() {
        return mUOM;
    }

    public void setmUOM(String mUOM) {
        this.mUOM = mUOM;
    }

    public String getmAvailableQnty() {
        return mAvailableQnty;
    }

    public void setmAvailableQnty(String mAvailableQnty) {
        this.mAvailableQnty = mAvailableQnty;
    }

    public String getmRequiredQnty() {
        return mRequiredQnty;
    }

    public void setmRequiredQnty(String mRequiredQnty) {
        this.mRequiredQnty = mRequiredQnty;
    }

    public String getmSrcWarehouse() {
        return mSrcWarehouse;
    }

    public void setmSrcWarehouse(String mSrcWarehouse1) {
        this.mSrcWarehouse = mSrcWarehouse1;
    }
}
