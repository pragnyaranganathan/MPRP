package in.epochconsulting.erpnext.mprp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.activity.Home;
import in.epochconsulting.erpnext.mprp.issue_materials.activity.IssueMaterialInStockActivity;
import in.epochconsulting.erpnext.mprp.request_items.activity.RequestItemsActivity;
import in.epochconsulting.erpnext.mprp.transform_items.activity.TransformItemsActivity;
import in.epochconsulting.erpnext.mprp.common.BaseFragment;
import in.epochconsulting.erpnext.mprp.implementation.CustomEventListener;
import in.epochconsulting.erpnext.mprp.utils.AlertDialogHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectTaskFragment extends BaseFragment {

    ImageButton mRequestItemsButton;
    ImageButton mTransformItemsButton;
    ImageButton mIssueItemsButton;
    View selectTaskFragment;
    private CustomEventListener selectModeFragmentListener;
    Home activity;


    public SelectTaskFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        selectTaskFragment = inflater.inflate(R.layout.fragment_select_task, container, false);

        // Inflate the layout for this fragment
        mIssueItemsButton = selectTaskFragment.findViewById(R.id.select_task_issue_items_button);
        mRequestItemsButton = selectTaskFragment.findViewById(R.id.select_task_request_items_button);
        mTransformItemsButton = selectTaskFragment.findViewById(R.id.select_task_transform_items_button);
        activity = (Home) this.getActivity();

        mRequestItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRequestItemsActivity();

            }
        });
        mTransformItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTransformActivity();

            }
        });
        mIssueItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialogToChooseIssueType();
                showPopuptoChooseIssueType();
            }
        });

        return selectTaskFragment;
    }

    private void showPopuptoChooseIssueType() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(activity, mIssueItemsButton);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.issue_items_options_popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(activity,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                loadSelectedIssueActivity(item.getItemId());
                return true;
            }
        });

        popup.show();//showing popup menu


}

    private void loadSelectedIssueActivity(int itemId) {
        switch (itemId){
            case R.id.issue_item_from_stock_popup:
                startIssueMaterialAlreadyInStockMaterial();
                break;
            case R.id.make_and_issue_item_popup:
                //do nothig for now
                break;
        }
    }

    private void showDialogToChooseIssueType() {
        LayoutInflater dialogViewinflator = this.activity.getLayoutInflater();
        View dialogView = dialogViewinflator.inflate(R.layout.dialog_info_content,null);
        TextView title =  dialogView.findViewById(R.id.dialog_info_title) ;
        title.setText(R.string.issue_items);
        TextView message = dialogView.findViewById(R.id.dialog_info_message) ;
        message.setText(R.string.choose_issue_type_str);


        activity.showAlertDialog(null,null , true, getResources().getString(R.string.issue_materials_already_in_stock_str), getString(R.string.make_and_issue_materials_str),null, dialogView, new AlertDialogHandler() {
            @Override
            public void onPositiveButtonClicked() {

                startIssueMaterialAlreadyInStockMaterial();


            }

            @Override
            public void onNegativeButtonClicked() {

                //do nothing for now..todo implement this later


            }

        });

    }

    private void startIssueMaterialAlreadyInStockMaterial() {
        Intent issueMaterialAlreadyInStockActivity = new Intent(SelectTaskFragment.super.getContext(), IssueMaterialInStockActivity.class);
        startActivity(issueMaterialAlreadyInStockActivity);
    }

    private void startRequestItemsActivity() {
        Intent requestItemsActivity = new Intent(SelectTaskFragment.super.getContext(), RequestItemsActivity.class);
        startActivity(requestItemsActivity);
    }

    private void startTransformActivity() {
        Intent transformItemsActivity = new Intent(SelectTaskFragment.super.getContext(), TransformItemsActivity.class);
        startActivity(transformItemsActivity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getString(R.string.select_task));
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if(activity instanceof CustomEventListener) {
            selectModeFragmentListener = (CustomEventListener) activity;
        } else {
            // Throw an error!
            throw new ClassCastException(activity.toString()
                    + " must implement CustomEventListener");
        }
    }

}
