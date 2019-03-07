package in.epochconsulting.erpnext.mprp.transform_items.view;

/**
 * Created by pragnya on 11/4/18.
 */

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;



import in.epochconsulting.erpnext.mprp.R;

public class GenreViewHolder extends GroupViewHolder {

    private TextView genreName;
    private ImageView arrow;


    public GenreViewHolder(View itemView) {
        super(itemView);
        genreName =  itemView.findViewById(R.id.list_item_item_consumed_name);
        arrow =  itemView.findViewById(R.id.list_item_genre_arrow);

    }

    public void setGenreTitle(ExpandableGroup genre) {

            genreName.setText(genre.getTitle());





    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}
