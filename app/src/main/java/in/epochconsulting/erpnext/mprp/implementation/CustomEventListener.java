package in.epochconsulting.erpnext.mprp.implementation;

import java.util.List;

import in.epochconsulting.erpnext.mprp.request_items.model.RequestItemList;

/**
 * Created by pragnya on 30/3/18.
 */

public interface CustomEventListener {
    void showDialog(String data, RequestItemList list);
    void showErrorDialog(String message);

    void showSuccessDialog(String message);

    void loadAppropriateFragment(int fragmentId);
}
