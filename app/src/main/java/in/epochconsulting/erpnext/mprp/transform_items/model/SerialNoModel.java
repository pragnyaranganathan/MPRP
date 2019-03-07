package in.epochconsulting.erpnext.mprp.transform_items.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pragnya on 8/5/18.
 */

public class SerialNoModel implements Parcelable {

    private String serialNo;
    private String warehouse;
    private boolean isSelected;

    public SerialNoModel(String serialNo, String warehouse) {
        this.serialNo = serialNo;
        this.warehouse = warehouse;
        this.isSelected = false; //by default
    }

    private SerialNoModel(Parcel in) {
        serialNo = in.readString();
        warehouse = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<SerialNoModel> CREATOR = new Creator<SerialNoModel>() {
        @Override
        public SerialNoModel createFromParcel(Parcel in) {
            return new SerialNoModel(in);
        }

        @Override
        public SerialNoModel[] newArray(int size) {
            return new SerialNoModel[size];
        }
    };

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



    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(serialNo);
        parcel.writeString(warehouse);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}
