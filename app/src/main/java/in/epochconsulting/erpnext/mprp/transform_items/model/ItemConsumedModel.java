package in.epochconsulting.erpnext.mprp.transform_items.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pragnya on 16/4/18.
 */

public class ItemConsumedModel implements Parcelable {
    private String itemConsumedCode;
    private Double itemConsumedQnty;
    private boolean isItemRawMaterial;
    private boolean mustBeAWholeNumber;
    private Double itemStockQnty;
    private String stockUOM;
    private boolean hasBatchNos;
    private boolean hasSerialNos;
    private List<BatchNoModel> batchNoModelList;
    private List<SerialNoModel> serialNoModelList;

    public ItemConsumedModel(String itemConsumedCode, Double itemConsumedQnty, boolean isItemRawMaterial, boolean mustBeAWholeNumber, Double itemStockQnty, String stockUOM, boolean hasBatchNos, boolean hasSerialNos) {
        this.itemConsumedCode = itemConsumedCode;
        this.itemConsumedQnty = itemConsumedQnty;
        this.isItemRawMaterial = isItemRawMaterial;
        this.mustBeAWholeNumber = mustBeAWholeNumber;
        this.itemStockQnty = itemStockQnty;
        this.stockUOM = stockUOM;
        this.hasBatchNos = hasBatchNos;
        this.hasSerialNos = hasSerialNos;
        this.batchNoModelList = new ArrayList<>();
        this.serialNoModelList = new ArrayList<>();
    }

    private ItemConsumedModel(Parcel in) {
        itemConsumedCode = in.readString();
        isItemRawMaterial = in.readByte() != 0;
        mustBeAWholeNumber = in.readByte() != 0;
        stockUOM = in.readString();
        hasBatchNos = in.readByte() != 0;
        hasSerialNos = in.readByte() != 0;
        itemConsumedQnty = in.readDouble();
        itemStockQnty = in.readDouble();
        batchNoModelList = new ArrayList<>();
        serialNoModelList = new ArrayList<>();
        in.readList(batchNoModelList, List.class.getClassLoader());
        in.readList(serialNoModelList, List.class.getClassLoader());
    }

    public static final Creator<ItemConsumedModel> CREATOR = new Creator<ItemConsumedModel>() {
        @Override
        public ItemConsumedModel createFromParcel(Parcel in) {
            return new ItemConsumedModel(in);
        }

        @Override
        public ItemConsumedModel[] newArray(int size) {
            return new ItemConsumedModel[size];
        }
    };

    public String getItemConsumedCode() {
        return itemConsumedCode;
    }

    public void setItemConsumedCode(String itemConsumedCode) {
        this.itemConsumedCode = itemConsumedCode;
    }

    public Double getItemConsumedQnty() {
        return itemConsumedQnty;
    }

    public void setItemConsumedQnty(Double itemConsumedQnty) {
        this.itemConsumedQnty = itemConsumedQnty;
    }

    public boolean isItemRawMaterial() {
        return isItemRawMaterial;
    }

    public void setItemRawMaterial(boolean itemRawMaterial) {
        isItemRawMaterial = itemRawMaterial;
    }

    public boolean isMustBeAWholeNumber() {
        return mustBeAWholeNumber;
    }

    public void setMustBeAWholeNumber(boolean mustBeAWholeNumber) {
        this.mustBeAWholeNumber = mustBeAWholeNumber;
    }

    public Double getStockQnty() {
        return itemStockQnty;
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

    public List<BatchNoModel> getBatchNoModelList() {
        return batchNoModelList;
    }

    public void setBatchNoModelList(List<BatchNoModel> batchNoModelList) {
        this.batchNoModelList = batchNoModelList;
    }

    public List<SerialNoModel> getSerialNoModelList() {
        return serialNoModelList;
    }

    public void setSerialNoModelList(List<SerialNoModel> serialNoModelList) {
        this.serialNoModelList = serialNoModelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemConsumedCode);
        parcel.writeByte((byte) (isItemRawMaterial ? 1 : 0));
        parcel.writeByte((byte) (mustBeAWholeNumber ? 1 : 0));
        parcel.writeString(stockUOM);
        parcel.writeByte((byte) (hasBatchNos ? 1 : 0));
        parcel.writeByte((byte) (hasSerialNos ? 1 : 0));
        parcel.writeDouble(itemConsumedQnty);
        parcel.writeDouble(itemStockQnty);
        parcel.writeList(batchNoModelList);
        parcel.writeList(serialNoModelList);
    }
}
