package iso.piotrowski.marek.nyndro.practice;

import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.R;

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

    public void startSoundEffect() {
        MediaPlayer mediaPlayer = MediaPlayer.create(NyndroApp.getContect(), R.raw.tweet);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });
        mediaPlayer.start();
    }

    public void startEffect(final View view) {
        startSoundEffect();
        Animation animation = AnimationUtils.loadAnimation(NyndroApp.getContect(), R.anim.add_button_animation);
        view.clearAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                presenter.dataWereChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ((View) view.getParent().getParent().getParent()).startAnimation(AnimationUtils.loadAnimation(NyndroApp.getContect(), R.anim.color_blinking_cardview));
        view.startAnimation(animation);

    }
}