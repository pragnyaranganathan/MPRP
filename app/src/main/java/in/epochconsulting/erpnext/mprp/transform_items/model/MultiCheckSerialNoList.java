package in.epochconsulting.erpnext.mprp.transform_items.model;

import java.util.List;

/**
 * Created by pragnya on 9/5/18.
 */

public class MultiCheckSerialNoList extends MultiCheckList {

    private List<SerialNoModel> serialNoList;
    public MultiCheckSerialNoList(String title, List<SerialNoModel> items) {
        super(title, items);
        serialNoList = items;
    }

    public List<SerialNoModel> getSerialNoList() {
        return serialNoList;
    }
}
