package in.epochconsulting.erpnext.mprp.transform_items.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pragnya on 16/4/18.
 */

public class ItemMadeModel implements Parcelable{


    private String itemMadeCode;
    private List<ItemConsumedModel> itemConsumedModelList = null;
    private String passEntryBasedOn;
    private Double qtyReqd;
    private Double stockQnty;
    private String stockUOM;

   private  boolean mustBeAWholeNumber;
    private boolean hasBatchNos;
    private boolean hasSerialNos;
    private boolean isBoMValuesUsed;
    private List<BatchNoModel> batchNoModelArrayList ;
    private List<SerialNoModel> serialNoModelArrayList;
    private List<String> selectedSerialNos ;


    public ItemMadeModel() {
        itemConsumedModelList = new ArrayList<>();
        batchNoModelArrayList = new ArrayList<>();
        serialNoModelArrayList = new ArrayList<>();
        selectedSerialNos = new ArrayList<>();
    }


    private ItemMadeModel(Parcel in) {
        itemMadeCode = in.readString();
        passEntryBasedOn = in.readString();
        stockUOM = in.readString();
        mustBeAWholeNumber = in.readByte() != 0;
        hasBatchNos = in.readByte() != 0;
        hasSerialNos = in.readByte() != 0;
        qtyReqd = in.readDouble();
        stockQnty = in.readDouble();
        isBoMValuesUsed = in.readByte()!=0;

         itemConsumedModelList = new ArrayList<>();
        batchNoModelArrayList = new ArrayList<>();
        serialNoModelArrayList = new ArrayList<>();

        in.readList(itemConsumedModelList, List.class.getClassLoader());
        in.readList(batchNoModelArrayList, List.class.getClassLoader());
        in.readList(serialNoModelArrayList, List.class.getClassLoader());
    }

    public static final Creator<ItemMadeModel> CREATOR = new Creator<ItemMadeModel>() {
        @Override
        public ItemMadeModel createFromParcel(Parcel in) {

            return new ItemMadeModel(in);
        }

        @Override
        public ItemMadeModel[] newArray(int size) {
            return new ItemMadeModel[size];
        }
    };

    public String getItemMadeCode() {
        return itemMadeCode;
    }

    public void setItemMadeCode(String itemMadeCode) {
        this.itemMadeCode = itemMadeCode;
    }

    public List<ItemConsumedModel> getItemConsumedModelList() {
        return itemConsumedModelList;
    }

    public void setItemConsumedModelList(List<ItemConsumedModel> itemConsumedModelList) {
        this.itemConsumedModelList = itemConsumedModelList;
    }

    public String getPassEntryBasedOn() {
        return passEntryBasedOn;
    }

    public void setPassEntryBasedOn(String passEntryBasedOn) {
        this.passEntryBasedOn = passEntryBasedOn;
    }

    public boolean isMustBeAWholeNumber() {
        return mustBeAWholeNumber;
    }

    public void setMustBeAWholeNumber(boolean mustBeAWholeNumber) {
        this.mustBeAWholeNumber = mustBeAWholeNumber;
    }


    public Double getQtyReqd() {
        return qtyReqd;
    }

    public void setQtyReqd(Double qtyReqd) {
        this.qtyReqd = qtyReqd;
    }

    public Double getStockQnty() {
        return stockQnty;
    }

    public void setStockQnty(Double stockQnty) {
        this.stockQnty = stockQnty;
    }

    public String getStockUOM() {
        return stockUOM;
    }

    public void setStockUOM(String stockUOM) {
        this.stockUOM = stockUOM;
    }

    public boolean isHasBatchNos() {
        return hasBatchNos;
    }

    public void setHasBatchNos(boolean hasBatchNos) {
        this.hasBatchNos = hasBatchNos;
    }

    public boolean isHasSerialNos() {
        return hasSerialNos;
    }

    public void setHasSerialNos(boolean hasSerialNos) {
        this.hasSerialNos = hasSerialNos;
    }

    public List<BatchNoModel> getBatchNoModelArrayList() {
        return batchNoModelArrayList;
    }

    public void setBatchNoModelArrayList(List<BatchNoModel> batchNoModelArrayList) {
        this.batchNoModelArrayList = batchNoModelArrayList;
    }

    public List<SerialNoModel> getSerialNoModelArrayList() {
        return serialNoModelArrayList;
    }

    public void setSerialNoModelArrayList(List<SerialNoModel> serialNoModelArrayList) {
        this.serialNoModelArrayList = serialNoModelArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemMadeCode);
        parcel.writeString(passEntryBasedOn);
        parcel.writeString(stockUOM);
        parcel.writeByte((byte) (mustBeAWholeNumber ? 1 : 0));
        parcel.writeByte((byte) (hasBatchNos ? 1 : 0));
        parcel.writeByte((byte) (hasSerialNos ? 1 : 0));
        parcel.writeByte((byte) (isBoMValuesUsed?1:0));
        parcel.writeDouble(qtyReqd);
        parcel.writeDouble(stockQnty);
        parcel.writeList(itemConsumedModelList);
        parcel.writeList(batchNoModelArrayList);
        parcel.writeList(serialNoModelArrayList);
    }

    public List<String> getSelectedSerialNos() {
        return selectedSerialNos;
    }

    public void setSelectedSerialNos(List<String> selectedSerialNos) {
        this.selectedSerialNos = selectedSerialNos;
    }

    public boolean isBoMValuesUsed() {
        return isBoMValuesUsed;
    }

    public void setBoMValuesUsed(boolean boMValuesUsed) {
        isBoMValuesUsed = boMValuesUsed;
    }
}
