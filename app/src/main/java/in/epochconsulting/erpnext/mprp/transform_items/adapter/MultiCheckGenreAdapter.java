package in.epochconsulting.erpnext.mprp.transform_items.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.transform_items.model.BatchNoModel;
import in.epochconsulting.erpnext.mprp.transform_items.model.MultiCheckList;
import in.epochconsulting.erpnext.mprp.transform_items.model.SerialNoModel;
import in.epochconsulting.erpnext.mprp.transform_items.view.GenreViewHolder;
import in.epochconsulting.erpnext.mprp.transform_items.view.MultiCheckArtistViewHolder;

/**
 * Created by pragnya on 8/5/18.
 */

public class MultiCheckGenreAdapter extends
        CheckableChildRecyclerViewAdapter<GenreViewHolder, MultiCheckArtistViewHolder> {



    public MultiCheckGenreAdapter(List<MultiCheckList> groups) {
        super(groups);


    }

    @Override
    public MultiCheckArtistViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_batch_no_details, parent, false);
        return new MultiCheckArtistViewHolder(view);
    }

    @Override
    public void onBindCheckChildViewHolder(MultiCheckArtistViewHolder holder, int position,
                                           CheckedExpandableGroup group, int childIndex) {
        if(group.getItems().get(childIndex) instanceof BatchNoModel) {
            final BatchNoModel batchNoModel = (BatchNoModel)group.getItems().get(childIndex);
            holder.setArtistName(batchNoModel.getBatchNo());


        }
        else if(group.getItems().get(childIndex) instanceof SerialNoModel)
        {
            final SerialNoModel serialNoModel = (SerialNoModel)group.getItems().get(childIndex);
            holder.setArtistName(serialNoModel.getSerialNo());
        }

    }

    @Override
    public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_consumed, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {
        holder.setGenreTitle(group);
    }

    public void autoCheckSingleBatchAndSerialEntries() {
        for(int i = 0; i<this.getGroups().size();i++)
        {
            if(this.getGroups().get(i).getItems().size() == 1)
            {
                checkChild(true,i,0); //only one child so check it
            }
        }
    }
    public void checkAllPreviouslySelectedBatchesAndSerialNos()
    {
        for(int i=0;i<this.getGroups().size();i++)
        {
            for(int j=0;j<this.getGroups().get(i).getItems().size();j++)
            {
                if(this.getGroups().get(i).getItems().get(j) instanceof BatchNoModel)
                {
                    BatchNoModel batchNoModel =(BatchNoModel) this.getGroups().get(i).getItems().get(j);
                    if (batchNoModel.isSelected())
                    {
                        checkChild(true,i,j);
                    }
                }
                else if(this.getGroups().get(i).getItems().get(j) instanceof SerialNoModel){
                    SerialNoModel serialNoModel = (SerialNoModel) this.getGroups().get(i).getItems().get(j);
                    if(serialNoModel.isSelected())
                    {
                        checkChild(true,i,j);
                    }
                }
            }
        }
    }
}