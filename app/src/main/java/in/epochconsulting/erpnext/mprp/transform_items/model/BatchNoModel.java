package in.epochconsulting.erpnext.mprp.transform_items.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pragnya on 8/5/18.
 */

public class BatchNoModel implements Parcelable {
    private String batchNo;
    private Double batchQtyAtWarehouse;
    private Double requestedBatchQty;
    //added on 29th May 2018
    private String batchExpDate;
    private boolean isSelected;

    public BatchNoModel(String batchNo, Double batchQtyAtWarehouse) {
        this.batchNo = batchNo;
        this.batchQtyAtWarehouse = batchQtyAtWarehouse;
        this.requestedBatchQty = 0.0;
        this.isSelected = false;
    }

    protected BatchNoModel(Parcel in) {
        batchNo = in.readString();
        batchQtyAtWarehouse = in.readParcelable(Parcelable.class.getClassLoader());
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(batchNo);
        dest.writeDouble(batchQtyAtWarehouse);
        dest.writeString(batchExpDate);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BatchNoModel> CREATOR = new Creator<BatchNoModel>() {
        @Override
        public BatchNoModel createFromParcel(Parcel in) {
            return new BatchNoModel(in);
        }

        @Override
        public BatchNoModel[] newArray(int size) {
            return new BatchNoModel[size];
        }
    };

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Double getBatchQtyAtWarehouse() {
        return batchQtyAtWarehouse;
    }

    public void setBatchQtyAtWarehouse(Double batchQtyAtWarehouse) {
        this.batchQtyAtWarehouse = batchQtyAtWarehouse;
    }

    public String getBatchExpDate() {
        return batchExpDate;
    }

    public void setBatchExpDate(String batchExpDate) {
        this.batchExpDate = batchExpDate;
    }

    public Double getRequestedBatchQty() {
        return requestedBatchQty;
    }

    public void setRequestedBatchQty(Double requestedBatchQty) {
        this.requestedBatchQty = requestedBatchQty;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
