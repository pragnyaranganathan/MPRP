package in.epochconsulting.erpnext.mprp.transform_items.model;

import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;

import java.util.List;

/**
 * Created by pragnya on 9/5/18.
 */

public class MultiCheckList extends MultiCheckExpandableGroup {
    public MultiCheckList(String title, List items) {
        super(title, items);

    }
    private String itemCode;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}
