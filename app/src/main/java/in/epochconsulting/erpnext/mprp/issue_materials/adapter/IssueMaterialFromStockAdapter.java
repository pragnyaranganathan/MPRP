package in.epochconsulting.erpnext.mprp.issue_materials.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.issue_materials.implementation.IssueMaterialFromStockListener;
import in.epochconsulting.erpnext.mprp.issue_materials.model.IssueMaterialFromStockDetailsModel;
import in.epochconsulting.erpnext.mprp.issue_materials.model.IssueMaterialFromStockList;
import in.epochconsulting.erpnext.mprp.issue_materials.view.CustomIssueMaterialFromStockVH;

/**
 * Created by pragnya on 10/9/18.
 */

public class IssueMaterialFromStockAdapter extends RecyclerView.Adapter<CustomIssueMaterialFromStockVH> {

    private IssueMaterialFromStockList items;

    private Activity activity;

    public IssueMaterialFromStockAdapter(IssueMaterialFromStockList list, Activity activity){
        this.items = list;
        this.activity = activity;
    }

    @Override
    public CustomIssueMaterialFromStockVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.issue_from_stock_table_contents, parent,false);
        return new CustomIssueMaterialFromStockVH(view, (IssueMaterialFromStockListener) activity);
    }

    @Override
    public void onBindViewHolder(CustomIssueMaterialFromStockVH holder, int position) {
        holder.setItem(items.getIssueMaterialFromStockDetailsModelList().get(position));

    }

    @Override
    public int getItemCount() {
        return items.getIssueMaterialFromStockDetailsModelList().size();
    }

    //all methods related to the adding and deletion of rows

    public void addRow(IssueMaterialFromStockDetailsModel selectedItemToBeIssuedFromList, int position) {
        items.getIssueMaterialFromStockDetailsModelList().add(selectedItemToBeIssuedFromList);
        notifyItemInserted(position);
    }
    public void deleteRow(int adapterPos){
        items.getIssueMaterialFromStockDetailsModelList().remove(adapterPos);
        notifyItemRemoved(adapterPos);
    }
}
