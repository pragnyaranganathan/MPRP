package in.epochconsulting.erpnext.mprp.transform_items.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.List;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.transform_items.implementation.TransformItemsListener;
import in.epochconsulting.erpnext.mprp.transform_items.model.BatchNoModel;
import in.epochconsulting.erpnext.mprp.transform_items.model.ItemConsumedModel;
import in.epochconsulting.erpnext.mprp.transform_items.model.ItemMadeList;
import in.epochconsulting.erpnext.mprp.transform_items.model.ItemMadeModel;

import in.epochconsulting.erpnext.mprp.transform_items.model.SerialNoModel;
import in.epochconsulting.erpnext.mprp.transform_items.view.CustomTransformedItemsForFinishedGoodsViewHolder;
import in.epochconsulting.erpnext.mprp.transform_items.view.CustomTransformedItemsForRawMaterialViewHolder;



/**
 * Created by pragnya on 16/4/18.
 */

public class TransformedItemsParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ItemMadeList itemMadeList;
    private Activity activity;

    public static final int ITEM_PASSED_ON_RAW_MATERIAL = 0;
    public static final int ITEM_PASSED_ON_FINISHED_GOODS = 1;

    public TransformedItemsParentAdapter( ItemMadeList list, Activity activity)
    {
        itemMadeList = list;
        this.activity = activity;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case ITEM_PASSED_ON_RAW_MATERIAL:
                View rawMaterialItemView = inflater.inflate(R.layout.list_item_transformed,parent,false);
                viewHolder = new CustomTransformedItemsForRawMaterialViewHolder(rawMaterialItemView, (TransformItemsListener) activity);
                break;
            case ITEM_PASSED_ON_FINISHED_GOODS:
                View finishedGoodsItemView = inflater.inflate(R.layout.list_item_transformed_finished_goods,parent,false);
                viewHolder =  new CustomTransformedItemsForFinishedGoodsViewHolder(finishedGoodsItemView, (TransformItemsListener) this.activity);
                break;
            default:
                throw new IllegalArgumentException("Invalid viewType");
        }
        return viewHolder;



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case ITEM_PASSED_ON_FINISHED_GOODS:
                CustomTransformedItemsForFinishedGoodsViewHolder viewHolder = (CustomTransformedItemsForFinishedGoodsViewHolder)holder;
                configureTransformedItemForFinishedGoodsViewHolder(viewHolder, position);
                break;
            case ITEM_PASSED_ON_RAW_MATERIAL:
                CustomTransformedItemsForRawMaterialViewHolder vh2 = (CustomTransformedItemsForRawMaterialViewHolder) holder;
                configureTransformedItemForRawMaterialViewHolder(vh2, position);
                break;
            default:break;
        }

    }


    private void configureTransformedItemForRawMaterialViewHolder(CustomTransformedItemsForRawMaterialViewHolder holder, int position) {
        ItemMadeModel itemMade = itemMadeList.getItemMadeModelList().get(position);
        boolean isAWholeNumber;
        if(itemMade.isMustBeAWholeNumber())
            isAWholeNumber = true;
        else
        {
            isAWholeNumber = false;
        }
        holder.setItem(itemMade.getItemMadeCode(), String.valueOf(itemMade.getStockQnty()),String.valueOf(itemMade.getQtyReqd()),isAWholeNumber, itemMade.getStockUOM(), itemMade.isBoMValuesUsed());
        LinearLayoutManager childlinearLayout = new LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false);
        holder.recyclerView_consumedItemsList.setLayoutManager(childlinearLayout);
        holder.recyclerView_consumedItemsList.setHasFixedSize(true);
        TransformedItemsChildAdapter transformedItemsChildAdapter = new TransformedItemsChildAdapter(position, this.activity,itemMadeList.getItemMadeModelList().get(position).getItemConsumedModelList(), itemMadeList.getItemMadeModelList().get(position).isBoMValuesUsed());
        holder.recyclerView_consumedItemsList.setAdapter(transformedItemsChildAdapter);

    }

    private void configureTransformedItemForFinishedGoodsViewHolder(CustomTransformedItemsForFinishedGoodsViewHolder holder, int position) {
        ItemMadeModel itemMade = itemMadeList.getItemMadeModelList().get(position);
        boolean isAWholeNumber;

        if(itemMade.isMustBeAWholeNumber()) {
            isAWholeNumber = true;


        }
        else
        {
            isAWholeNumber = false;
        }

        holder.setItem(itemMade.getItemMadeCode(),String.valueOf(itemMade.getStockQnty()), String.valueOf(itemMade.getQtyReqd()), isAWholeNumber, itemMade.getStockUOM(), itemMade.isBoMValuesUsed()); //sending empty string for now
        LinearLayoutManager childlinearLayout = new LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false);
        holder.recyclerView_consumedItemsList.setLayoutManager(childlinearLayout);
        holder.recyclerView_consumedItemsList.setHasFixedSize(true);
        TransformedItemsChildAdapter transformedItemsChildAdapter = new TransformedItemsChildAdapter(position, this.activity,itemMadeList.getItemMadeModelList().get(position).getItemConsumedModelList(), itemMadeList.getItemMadeModelList().get(position).isBoMValuesUsed());
        holder.recyclerView_consumedItemsList.setAdapter(transformedItemsChildAdapter);
    }

    @Override
    public int getItemCount() {
        return itemMadeList.getItemMadeModelList().size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if(itemMadeList.getItemMadeModelList().get(position).getPassEntryBasedOn().equals(activity.getString(R.string.finished_goods_str)   ))
        {
            return ITEM_PASSED_ON_FINISHED_GOODS;
        }
        else if(itemMadeList.getItemMadeModelList().get(position).getPassEntryBasedOn().equals(activity.getResources().getString(R.string.raw_material_string)))
        {
            return ITEM_PASSED_ON_RAW_MATERIAL;
        }
        else {
            return -1;
        }
    }

    public void addRow(ItemMadeModel itemtobeMade, int position)
    {
        itemMadeList.getItemMadeModelList().add(itemtobeMade);
        notifyItemInserted(position);
    }
    public boolean qntyEnteredForItemMade(String qty, int position, boolean useBOMVal) {
        ItemMadeModel itemMade = itemMadeList.getItemMadeModelList().get(position);
        //CheckBox bomVal = (CheckBox) useBOMVal;
        if(useBOMVal) {
            itemMade.setBoMValuesUsed(true);
            if (itemMade.isMustBeAWholeNumber()) {
                return (calculateWholeValuesForFinishedGoods(itemMade, position, qty));
            } else {
                return (calculateFractionValuesForFinishedGoods(itemMade, position, qty));
            }
        }
        else{

            //should ideally allow any value to be entered in the edit texts
            //allow editing of values of item consumed as well, dont auto calculate it

            if(!itemMade.isMustBeAWholeNumber())
            {

                itemMade.setBoMValuesUsed(false);
            }
            else if (Double.valueOf(qty)%1 !=0 && itemMade.isMustBeAWholeNumber())
            {
                Toast.makeText(activity.getApplicationContext(), " You are attempting to use values that allow fractions to be used for the item "+itemMade.getItemMadeCode()+". Only whole numbers are allowed for this item. ", Toast.LENGTH_LONG).show();
                return false;
            }
            else{

                itemMade.setQtyReqd(Double.valueOf(qty));
            }
            itemMade.setBoMValuesUsed(false);
            notifyItemChanged(position);

            return true;

        }


    }

    private boolean calculateFractionValuesForFinishedGoods(ItemMadeModel itemMade, int position, String qty) {


        Double finishedGoodsMultiple = Double.valueOf(qty)/(itemMade.getStockQnty());
        List<ItemConsumedModel> itemConsumedModelList = itemMade.getItemConsumedModelList();
        for (ItemConsumedModel itemConsumed : itemConsumedModelList) {
            if(itemConsumed.isMustBeAWholeNumber() && finishedGoodsMultiple%1 !=0)
            {
                Toast.makeText(activity.getApplicationContext(), " You are attempting to use values that allow fractions to be used for the item "+itemConsumed.getItemConsumedCode()+". Fractions are not allowed. ", Toast.LENGTH_LONG).show();
                return false;
            }
            else if(itemConsumed.isMustBeAWholeNumber() && finishedGoodsMultiple%1 == 0)
            {
               // itemConsumed.setItemConsumedQnty(String.valueOf((int)finishedGoodsMultiple * parseString(itemConsumed.getStockQnty(),Integer.class)));
                itemConsumed.setItemConsumedQnty((double)(Math.round(finishedGoodsMultiple) * Math.round((itemConsumed.getStockQnty())))); //,Integer.class)));
            }
            else
            {
               // itemConsumed.setItemConsumedQnty(String.valueOf(finishedGoodsMultiple * parseString(itemConsumed.getStockQnty(),Float.class)));
                itemConsumed.setItemConsumedQnty((finishedGoodsMultiple * (itemConsumed.getStockQnty()))); //,Float.class)));

            }

            itemMade.setQtyReqd(Double.valueOf(qty));
            notifyItemChanged(position);


        }
        return true;

    }

    private boolean calculateWholeValuesForFinishedGoods(ItemMadeModel itemMade, int position, String qty) {

        Double finishedGoodsMultiple = Double.valueOf(qty)/(itemMade.getStockQnty());
        if(finishedGoodsMultiple%1 !=0)
        {
            Toast.makeText(activity.getApplicationContext(),"You are attempting to use values that result in fractions for the Item Made. You cannot use fractions. Please try another value ", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            itemMade.setQtyReqd(Double.valueOf(qty));

            List<ItemConsumedModel> itemConsumedModelList = itemMade.getItemConsumedModelList();
            for (ItemConsumedModel itemConsumed : itemConsumedModelList) {
                //calculate the appropriate number
                if(itemConsumed.isMustBeAWholeNumber()) {

                    itemConsumed.setItemConsumedQnty((double)(Math.round(finishedGoodsMultiple) * Math.round((itemConsumed.getStockQnty()))));
                }
                else{
                    itemConsumed.setItemConsumedQnty((finishedGoodsMultiple * (itemConsumed.getStockQnty())));

                }
            }
            notifyItemChanged(position);
            return true;

        }
    }

   /* static <T extends Number> T parseString(String str, Class<T> cls) {
        if (cls == Float.class) {
            return (T) Float.valueOf(str);
        } else if (cls == Integer.class) {
            return (T) Integer.valueOf(str);
        }
        throw new IllegalArgumentException();

    }*/

    public boolean qntyEnteredForRawMaterialItemConsumed(String qty, int adapterPosition, int parentPosition, boolean bomUsed)
    {

       if(bomUsed) {
           return(calculateValuesForRawMaterialItemConsumedBasedOnBOM(qty, adapterPosition,parentPosition));
       }
       else
       {
           if(!itemMadeList.getItemMadeModelList().get(parentPosition).getItemConsumedModelList().get(adapterPosition).isMustBeAWholeNumber()){
               itemMadeList.getItemMadeModelList().get(parentPosition).getItemConsumedModelList().get(adapterPosition).setItemConsumedQnty(Double.valueOf(qty));

           }
           else if(itemMadeList.getItemMadeModelList().get(parentPosition).getItemConsumedModelList().get(adapterPosition).isMustBeAWholeNumber() && Double.valueOf(qty)%1==0) {
               itemMadeList.getItemMadeModelList().get(parentPosition).getItemConsumedModelList().get(adapterPosition).setItemConsumedQnty(Double.valueOf(qty));
           }
           else{
               Toast.makeText(activity.getApplicationContext(), "You cannot use fractions for "+itemMadeList.getItemMadeModelList().get(parentPosition).getItemConsumedModelList().get(adapterPosition).getItemConsumedCode()+". Please use whole numbers only for this item.  ", Toast.LENGTH_LONG).show();
               return false;

           }
           itemMadeList.getItemMadeModelList().get(parentPosition).setBoMValuesUsed(false);
           notifyItemChanged(parentPosition);
           return true;
       }





    }

    private boolean calculateValuesForRawMaterialItemConsumedBasedOnBOM(String qty, int adapterPosition, int parentPosition) {
        // /get the inner item consumed model based on the position and then do the calculation
        ItemMadeModel itemMade = itemMadeList.getItemMadeModelList().get(parentPosition);
        List<ItemConsumedModel> itemConsumedList = itemMade.getItemConsumedModelList();
        ItemConsumedModel itemConsumedQntyEntered = itemMade.getItemConsumedModelList().get(adapterPosition);

        Double rawMaterialMultipleWholeNumber;
        rawMaterialMultipleWholeNumber = Double.valueOf(qty)/(itemConsumedQntyEntered.getStockQnty());


        //get the itemMade isAWholeNumber, if so allow only whole number multiples for the qnty entered. If the qty is not a whole number, then throw an error to user
        if(itemMade.isMustBeAWholeNumber())

        {
            if (rawMaterialMultipleWholeNumber %1 !=0) {
                //throw error to user saying the qty cannot be fractions since
                Toast.makeText(activity.getApplicationContext(), "The quanity you have entered allows the item made to have fractions, the Item Made cannot be a fraction. Please enter appropriate values", Toast.LENGTH_LONG).show();
                return false;

            } else if (rawMaterialMultipleWholeNumber%1 ==0) {
                //allow the processing to continue
                long iMultiple = Math.round( rawMaterialMultipleWholeNumber);

                itemMade.setQtyReqd((double)iMultiple * Math.round((itemMade.getStockQnty())));
                for(ItemConsumedModel itemConsumed : itemConsumedList)
                {
                    if(itemConsumed.isMustBeAWholeNumber())
                    {


                        itemConsumed.setItemConsumedQnty((double)(iMultiple*Math.round((itemConsumed.getStockQnty()))));
                    }
                    else
                    {

                        itemConsumed.setItemConsumedQnty((iMultiple*(itemConsumed.getStockQnty())));
                    }
                }


            }

        }
        else {

            for(ItemConsumedModel itemConsumed: itemConsumedList)
            {
                if(itemConsumed.isMustBeAWholeNumber()&& rawMaterialMultipleWholeNumber %1!=0)
                {
                    //dont know what is supposed to happen here
                    Toast.makeText(activity.getApplicationContext(), "You are attempting to use fractions for "+itemConsumed.getItemConsumedCode()+". You cannot use fractions for this item. Please choose another appropriate number. ", Toast.LENGTH_LONG).show();
                    return false;
                }
                else if(itemConsumed.isMustBeAWholeNumber() && rawMaterialMultipleWholeNumber%1 ==0)
                {

                    itemConsumed.setItemConsumedQnty((double)((Math.round(rawMaterialMultipleWholeNumber) * Math.round(Double.valueOf(itemConsumed.getStockQnty())))));
                }
                else
                {

                    itemConsumed.setItemConsumedQnty(((rawMaterialMultipleWholeNumber * Double.valueOf(itemConsumed.getStockQnty()))));
                }
            }
            itemMade.setQtyReqd(( rawMaterialMultipleWholeNumber* itemMade.getStockQnty()));

        }

        notifyItemChanged(parentPosition);
        return true;
    }

    public void setupBatchAndSerialNos() {
        List<ItemConsumedModel> itConsumedList =  itemMadeList.getItemMadeModelList().get(0).getItemConsumedModelList();
        for(int i = 0;i<itConsumedList.size();i++) {
            List<BatchNoModel> batchNoModelList = itConsumedList.get(i).getBatchNoModelList();

            List<SerialNoModel> serialNoModelList = itConsumedList.get(i).getSerialNoModelList();
            System.out.println("The serial nos selected for "+ itConsumedList.get(i).getItemConsumedCode()+" are:");
            for(SerialNoModel serialNo : serialNoModelList)
            {
                if(serialNo.isSelected())
                {
                    System.out.println(serialNo.getSerialNo());
                }
            }


        }

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState == null)
        {
            System.out.println("savedInstanceState is null");
        }
        else
        {
            itemMadeList = savedInstanceState.getParcelable("transformed_item_list");
            notifyDataSetChanged();
        }
    }

    public void finshedGoodsItemConsumedQtyEntered(String qty, int adapterPosition, int parentPos) {

        if(itemMadeList.getItemMadeModelList().get(parentPos).getItemConsumedModelList().get(adapterPosition).isMustBeAWholeNumber() && Double.valueOf(qty)%1!=0){
            Toast.makeText(activity.getApplicationContext(),"Fractions cannot be used for the item "+itemMadeList.getItemMadeModelList().get(parentPos).getItemConsumedModelList().get(adapterPosition).getItemConsumedCode()+". Only whole numbers are allowed.",Toast.LENGTH_LONG).show();
        }
        else {
            itemMadeList.getItemMadeModelList().get(parentPos).getItemConsumedModelList().get(adapterPosition).setItemConsumedQnty(Double.valueOf(qty));
        }

    }

    public void rawMaterialItemTransformedQtyentered(String s, int adapterPosition, boolean useBOMValue) {
        itemMadeList.getItemMadeModelList().get(adapterPosition).setQtyReqd(Double.valueOf(s));
    }

    public void onUseBOMSelectedForRawMaterialTransformedItem(boolean checked, int adapterPosition) {
        itemMadeList.getItemMadeModelList().get(adapterPosition).setBoMValuesUsed(checked);
        notifyItemChanged(adapterPosition);
    }

    public void onUseBOMSelectedForFinishedGoodsTransformedItem(boolean checked, int adapterPosition) {
        itemMadeList.getItemMadeModelList().get(adapterPosition).setBoMValuesUsed(checked);
        notifyItemChanged(adapterPosition);
    }

    public void deleteRow(int adapterPosition) {
        itemMadeList.getItemMadeModelList().remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }
}
