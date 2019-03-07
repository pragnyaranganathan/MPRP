package in.epochconsulting.erpnext.mprp.issue_materials.implementation;

/**
 * Created by pragnya on 10/9/18.
 */

public interface IssueMaterialFromStockListener {
    void DownstreamWHSelected(String downstreamWH, int itemPos);
    void issueQntyEntered(String qty, int itemSelectedPos);

    void dsWHListIsEmpty(int dswhlistisnull_str);

    void onSelectBatchAndSLN(int adapterPosition);

    void deleteRow(int adapterPosition);
}
