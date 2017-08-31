package iso.piotrowski.marek.nyndro.plans.AddNewPlans;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 12.08.2016.
 */
public class AddRemainderAdapter extends RecyclerView.Adapter<AddRemainderAdapter.AddRemainderHolder> {

    private int selectedPosition = 0;
    private List<PracticeModel> practiceList;

    public AddRemainderAdapter (List<PracticeModel> practiceList){
        this.practiceList = practiceList;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public class AddRemainderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.add_remainder_image_practice) ImageView practiceImage;
        @BindView(R.id.add_remainder_name_practice) TextView practiceName;
        CardView cardView;
        AddRemainderHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
            ButterKnife.bind(this, this.cardView);
        }
    }

    @Override
    public AddRemainderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv;

        cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.add_remainder_cardview, parent, false);

        return new AddRemainderHolder(cv);
    }

    @Override
    public void onBindViewHolder(AddRemainderHolder holder, int position) {
        PracticeModel practice = practiceList.get(position);
        holder.practiceImage.setImageDrawable(NyndroApp.getContext().getResources()
                .getDrawable(practice.getPracticeImageId()));
        holder.practiceName.setText(practice.getName());

        if ((position == getSelectedPosition())) {
            addAnimation(holder.practiceImage, position);
//            addAnimation(holder.practiceName, position);
        }

        holder.cardView.clearAnimation();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addAnimation(view.findViewById(R.id.add_remainder_image_practice), position);
                //addAnimation(view.findViewById(R.id.add_remainder_name_practice), position);
            }
        });
    }

    private void addAnimation(final View view, final int position) {
        setSelectedPosition(position);
        Animation animation;
        animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.cardview_selected_animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                if ((getSelectedPosition() == position)) {
                    view.startAnimation(animation);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.clearAnimation();
        view.startAnimation(animation);
    }

    public PracticeModel getPractice() {
        if ((getSelectedPosition() < 0) || (getSelectedPosition() >= practiceList.size())) return null;
        return practiceList.get(getSelectedPosition());
    }

    @Override
    public int getItemCount() {
        return practiceList.size();
    }
}
