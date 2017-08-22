package iso.piotrowski.marek.nyndro.tools;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by marek.piotrowski on 22/08/2017.
 */

public class UITool {
    public enum TypeOfButtonAnimation {
        Emerge, Disappear
    }

    public static TypeOfButtonAnimation lastTypeOfAnimation = TypeOfButtonAnimation.Emerge;

    public static void animateButton(final View mFloatingView, final TypeOfButtonAnimation typeOfAnimation) {
        Animation animation = null;
        switch (typeOfAnimation) {
            case Disappear:
                animation = AnimationUtils.loadAnimation(NyndroApp.getContect(), R.anim.floating_button_disappear);
                break;
            case Emerge:
                animation = AnimationUtils.loadAnimation(NyndroApp.getContect(), R.anim.floating_button_emerge);
                break;
        }
        lastTypeOfAnimation = typeOfAnimation;
        if (animation != null) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    mFloatingView.setVisibility(typeOfAnimation == TypeOfButtonAnimation.Emerge ? View.VISIBLE : View.INVISIBLE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mFloatingView.startAnimation(animation);
        }
    }

}
