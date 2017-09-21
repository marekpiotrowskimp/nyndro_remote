package iso.piotrowski.marek.nyndro.tools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
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
                animation = AnimationUtils.loadAnimation(NyndroApp.getContext(), R.anim.floating_button_disappear);
                break;
            case Emerge:
                animation = AnimationUtils.loadAnimation(NyndroApp.getContext(), R.anim.floating_button_emerge);
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

    public static Bitmap makeRoundCorners(int resId, int radius) {
        Bitmap bitmap = BitmapFactory.decodeResource(NyndroApp.getContext().getResources() , resId);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = convertDpToPixel(radius, NyndroApp.getContext());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
