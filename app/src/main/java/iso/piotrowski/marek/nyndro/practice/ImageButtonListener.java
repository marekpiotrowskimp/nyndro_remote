package iso.piotrowski.marek.nyndro.practice;

import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.Utility;

/**
 * Created by marek.piotrowski on 12/08/2017.
 */

public class ImageButtonListener implements PracticeAdapter.IImageCardViewListener {

    private PracticeContract.IPresenter presenter;

    ImageButtonListener(PracticeContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onClick(View view, PracticeModel practice, int multiple) {
        presenter.addHistoryForPractice(practice, multiple);
        presenter.addProgressToPractice(practice, multiple);
        presenter.requestBackup();
        startEffect(view);
    }

    public void startEffect(final View view) {
        Utility.startSoundEffect(R.raw.tweet);
        Animation animation = AnimationUtils.loadAnimation(NyndroApp.getContext(), R.anim.add_button_animation);
        view.clearAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                presenter.refreshData();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ((View) view.getParent().getParent().getParent()).startAnimation(AnimationUtils.loadAnimation(NyndroApp.getContext(), R.anim.color_blinking_cardview));
        view.startAnimation(animation);

    }
}