package in.epochconsulting.erpnext.mprp.transform_items.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.transform_items.implementation.TransformItemsListener;
import in.epochconsulting.erpnext.mprp.transform_items.model.ItemConsumedModel;
import in.epochconsulting.erpnext.mprp.transform_items.view.CustomItemConsumedForFinishedGoodsViewHolder;
import in.epochconsulting.erpnext.mprp.transform_items.view.CustomItemConsumedForRawMaterialViewHolder;
import in.epochconsulting.erpnext.mprp.transform_items.view.CustomTransformedItemsForFinishedGoodsViewHolder;
import in.epochconsulting.erpnext.mprp.transform_items.view.CustomTransformedItemsForRawMaterialViewHolder;

import static in.epochconsulting.erpnext.mprp.transform_items.adapter.TransformedItemsParentAdapter.ITEM_PASSED_ON_FINISHED_GOODS;
import static in.epochconsulting.erpnext.mprp.transform_items.adapter.TransformedItemsParentAdapter.ITEM_PASSED_ON_RAW_MATERIAL;

/**
 * Created by pragnya on 16/4/18.
 */

public class TransformedItemsChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemConsumedModel> itemsCosumedList;
    Activity activity ;
    int parentItemMadePosition;
    boolean isBOMUsed;

    public TransformedItemsChildAdapter(int parentItemMadePosition, Activity activity, List<ItemConsumedModel> itemConsumedModelList, boolean boMValuesUsed) {
        this.itemsCosumedList = itemConsumedModelList;
        this.activity = activity;
        this.parentItemMadePosition = parentItemMadePosition;
        isBOMUsed = boMValuesUsed;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case ITEM_PASSED_ON_RAW_MATERIAL:
                View rawMaterialItemView = inflater.inflate(R.layout.transform_items_consumed_child_recyclerview_raw_material,parent,false);
                viewHolder = new CustomItemConsumedForRawMaterialViewHolder(rawMaterialItemView, (TransformItemsListener)activity, parentItemMadePosition);
                break;
            case ITEM_PASSED_ON_FINISHED_GOODS:
                View finishedGoodsItemView = inflater.inflate(R.layout.transform_items_consumed_child_recyclerview_finished_goods,parent,false);
                viewHolder =  new CustomItemConsumedForFinishedGoodsViewHolder(finishedGoodsItemView, (TransformItemsListener)activity, parentItemMadePosition);
                break;
            default:
                throw new IllegalArgumentException("Invalid viewType");
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemConsumedModel itemConsumed = itemsCosumedList.get(position);
        switch (holder.getItemViewType()){
            case ITEM_PASSED_ON_FINISHED_GOODS:
                CustomItemConsumedForFinishedGoodsViewHolder viewHolder = (CustomItemConsumedForFinishedGoodsViewHolder)holder;

                viewHolder.setItemConsumed(itemConsumed.getItemConsumedCode(), String.valueOf(itemConsumed.getStockQnty()), String.valueOf(itemConsumed.getItemConsumedQnty()),itemConsumed.isMustBeAWholeNumber(), itemConsumed.getStockUOM(), isBOMUsed);

                break;
            case ITEM_PASSED_ON_RAW_MATERIAL:
                CustomItemConsumedForRawMaterialViewHolder vh2 = (CustomItemConsumedForRawMaterialViewHolder) holder;
                vh2.setItemConsumedRawMaterial(itemConsumed.getItemConsumedCode(), String.valueOf(itemConsumed.getStockQnty()),String.valueOf(itemConsumed.getItemConsumedQnty()), itemConsumed.isMustBeAWholeNumber(), itemConsumed.getStockUOM(), isBOMUsed);
                break;
            default:break;
        }

    }



    @Override
    public int getItemCount() {
        return itemsCosumedList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if(!itemsCosumedList.get(position).isItemRawMaterial())
        {
            return ITEM_PASSED_ON_FINISHED_GOODS;
        }
        else if(itemsCosumedList.get(position).isItemRawMaterial())
        {
            return ITEM_PASSED_ON_RAW_MATERIAL;
        }
        else {
            return -1;
        }
    }
}
