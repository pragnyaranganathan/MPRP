package in.epochconsulting.erpnext.mprp.transform_items.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pragnya on 16/4/18.
 */

public class ItemMadeList implements Parcelable{
    private List<ItemMadeModel> itemMadeModelList;


    public ItemMadeList()
    {
        itemMadeModelList = new ArrayList<>();
    }


    protected ItemMadeList(Parcel in) {
        itemMadeModelList = in.createTypedArrayList(ItemMadeModel.CREATOR);
    }

    public static final Creator<ItemMadeList> CREATOR = new Creator<ItemMadeList>() {
        @Override
        public ItemMadeList createFromParcel(Parcel in) {
            return new ItemMadeList(in);
        }

        @Override
        public ItemMadeList[] newArray(int size) {
            return new ItemMadeList[size];
        }
    };

    public List<ItemMadeModel> getItemMadeModelList() {
        return itemMadeModelList;
    }

    public void setItemMadeModelList(List<ItemMadeModel> itemMadeModelList) {
        this.itemMadeModelList = itemMadeModelList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(itemMadeModelList);
    }
}
