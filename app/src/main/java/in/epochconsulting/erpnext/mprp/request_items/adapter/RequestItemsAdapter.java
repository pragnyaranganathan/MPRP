package in.epochconsulting.erpnext.mprp.request_items.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.request_items.model.RequestItemList;
import in.epochconsulting.erpnext.mprp.request_items.model.RequestedItemDetailsModel;
import in.epochconsulting.erpnext.mprp.request_items.view.CustomRequestedItemsViewHolder;

/**
 * Created by pragnya on 8/3/18.
 */

public class RequestItemsAdapter extends RecyclerView.Adapter<CustomRequestedItemsViewHolder> {



    //private List<RequestedItemsDisplayData> items;
    RequestItemList items;
    private LayoutInflater inflater;


    private CustomRequestedItemsViewHolder.RequestQntyEnteredListener requestQntyEnteredListener;
    private int itemSelectedPos;


    // allows clicks events to be caught


    // parent activity will implement this method to respond to click events


    // Provide a suitable constructor (depends on the kind of dataset)
    public RequestItemsAdapter(RequestItemList requestedData, LayoutInflater inflater, CustomRequestedItemsViewHolder.RequestQntyEnteredListener requestQntyEnteredListener) {
        this.inflater = inflater;


        this.requestQntyEnteredListener = requestQntyEnteredListener;

        this.items = requestedData;

    }
    public void updateItems(final RequestItemList newItems) {
        //final List<RequestedItemsDisplayData> oldItems = new ArrayList<>(this.items);
       /* this.items.clear();
        if (newItems != null) {
            System.out.println("New Updates are not null");
            this.items.addAll(newItems);
        }

        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldItems.size();
            }

            @Override
            public int getNewListSize() {
                return items.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {


                System.out.println("I am in are Items the same");
                return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {


                System.out.println("are the contents between the 2 lists same");
                return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
            }
        }).dispatchUpdatesTo(this);*/
      /*  DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CustomRequestedItemsUtil(oldItems, newItems));
        diffResult.dispatchUpdatesTo(this);
        this.items.clear();
        if(newItems!=null) {
            this.items.addAll(newItems);
        }*/

    }
    // inflates the cell layout from xml when needed

    // Create new views (invoked by the layout manager)
    @Override
    public CustomRequestedItemsViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = inflater.inflate(R.layout.cell_content,parent,false);
        return new CustomRequestedItemsViewHolder(v,0,requestQntyEnteredListener); //make the initial selection at 0
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(CustomRequestedItemsViewHolder holder, int position) {
        // - get element from your dataset at this position    // - replace the contents of the view with that element

        holder.setItem(items.getRequestedItemDetailsModelList().get(position));

    }

   /*@Override
    public void onBindViewHolder(CustomRequestedItemsViewHolder holder, int position, List<Object> payloads) {
        //some fields at the item positions have changed, so update changes
        CustomRequestedItemsViewHolder vh = (CustomRequestedItemsViewHolder) holder;
        if (payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads);
            //do nothing
        }
        else{
            //update the changes to the viewholder
            Bundle bundle = (Bundle)payloads.get(0);
            for(String key: bundle.keySet()){
                if(key.equals(RequestedItemsDisplayData.KEY_UOM)){
                    //TODO update the uom field
                    items.get(position).getItemData().setUom(bundle.getString(key)); //  ();
                }
                else if(key.equals(RequestedItemsDisplayData.KEY_AVAIL_QNTY)){
                    //TODO updte the available qnty field
                    items.get(position).getItemData().setAvailable_qnty(bundle.getString(key));
                }
                else if (key.equals(RequestedItemsDisplayData.KEY_REQD_QNTY)){
                    //TODO Update the reqd qnty
                    items.get(position).getItemData().setReqd_qnty(bundle.getString(key));
                }
            }
            vh.setItem(items.get(position));
        }

    }*/

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.getRequestedItemDetailsModelList().size();
    }

    public void addRow(RequestedItemDetailsModel selectedRequestedItemFromList, int position) {
        items.getRequestedItemDetailsModelList().add(selectedRequestedItemFromList);
        notifyItemInserted(position);
    }
}
