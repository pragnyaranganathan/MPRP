package in.epochconsulting.erpnext.mprp.transform_items.implementation;

import android.view.View;

/**
 * Created by pragnya on 12/4/18.
 */

public interface TransformItemsListener {
     void itemConsumedClicked(String itemConsumedCode, int position);
    void itemMadeClicked(String itemMadeCode, int position);
    void onQntyEntered(String qnty, int position, boolean checked);
    void onRawMaterialQntyEntered(String qty, int adapterPosition, int position, boolean bomUsed);
    void onSetBatchAndSLNButtonPressedForRawMaterial(int position);
    void onSetBatchAndSLNButtonPressedForFinishedGoods(int position);
    void onfinishedGoodsItemConsumedQtyEntered(String qty, int adapterPosition, int parentPos);

    void onRawMaterialItemTransformedQtyEntered(String s, int adapterPosition, boolean useBOMValue);

    void onUseBOMSelectedForRawMaterialTransformedItem(boolean checked, int adapterPosition);

    void onSetBatchAndSLNForItemConsumed(int adapterPosition, int parentItemMadePos);

    void onUseBOMSelectedFinishedGoodsTransformedItem(boolean checked, int adapterPosition);

    void deleteRow(int adapterPosition);
}
