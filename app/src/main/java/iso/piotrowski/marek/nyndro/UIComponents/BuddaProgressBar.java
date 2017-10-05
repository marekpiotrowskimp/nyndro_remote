package iso.piotrowski.marek.nyndro.UIComponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 01.08.2016.
 */
public class BuddaProgressBar extends View {

    private int desiredWidth = 500;
    private int desiredHeight = 150;

    private Bitmap buddhaBitmap;
    private int maxProgress;
    private int progress;
    private int colorBuddha;
    private Paint textPaint;
    private Paint drawPaint;
    private Context context;
    private int dx,dy,di,dj, progressGradient;
    private LightingColorFilter colorFilter;
    private LightingColorFilter colorFilterEmpty;
    private Rect srcRectTemp;
    private Rect desRectTemp;

    public BuddaProgressBar (Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
        this.context=context;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.BuddaProgressBar,
                0, 0);

        try {
            setMaxProgress(a.getInteger(R.styleable.BuddaProgressBar_max_progress, 100));
            setProgress(a.getInteger(R.styleable.BuddaProgressBar_progress, 0));
            setColorBuddha(a.getInteger(R.styleable.BuddaProgressBar_color_buddha,0xFF2020));
        } finally {
            a.recycle();
        }
        init();
    }

    private void init (){
        textPaint = new Paint();
        textPaint.setARGB(255,255,30,30);
        drawPaint = new Paint();
        drawPaint.setARGB(155,30,30,30);
        drawPaint.setShadowLayer(1,10,10, Color.rgb(200,200,200));
        drawPaint.setAntiAlias(true);
        drawPaint.setFilterBitmap(true);
        drawPaint.setMaskFilter(new BlurMaskFilter(8,BlurMaskFilter.Blur.NORMAL));
        buddhaBitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_buddha_32);
        colorFilter = new LightingColorFilter(0, colorBuddha);
        colorFilterEmpty = new LightingColorFilter(0, 0);
        srcRectTemp = new Rect();
        desRectTemp = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //desire dimensions
        desiredWidth = widthSize;
        di = desiredWidth / buddhaBitmap.getWidth();
        dx = desiredWidth / di;
        dj = (maxProgress/1000) / di;
        if ((dj*di)<(maxProgress/1000)) dj++;
        dy = (buddhaBitmap.getHeight()+5);
        desiredHeight = dy*dj;
        progressGradient = (maxProgress/1000);

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int j=0;j<dj;j++) {
            for (int i=0;i<di;i++) {
                int suma =j*di+i;
                if (suma<progressGradient) {
                    int left = i * dx;
                    int top = j * dy;
                    if (suma<progress/1000) {
                        drawPaint.setColorFilter(colorFilter);
                        canvas.drawBitmap(buddhaBitmap, left, top, drawPaint);
                    } else
                    {
                        if (suma==progress/1000)
                        {
                            int devHeight = buddhaBitmap.getHeight();
                            devHeight = (int)(devHeight * (float)(1-((float)(progress-suma*1000)/1000)));

                            srcRectTemp.set(0,0,buddhaBitmap.getWidth(),devHeight);
                            desRectTemp.set(left, top, left + buddhaBitmap.getWidth(), top + devHeight);
                            drawPaint.setColorFilter(colorFilterEmpty);
                            canvas.drawBitmap(buddhaBitmap, srcRectTemp, desRectTemp, drawPaint);

                            srcRectTemp.set(0,devHeight,buddhaBitmap.getWidth(),buddhaBitmap.getHeight());
                            desRectTemp.set(left, top + devHeight, left + buddhaBitmap.getWidth(), top +buddhaBitmap.getHeight());
                            drawPaint.setColorFilter(colorFilter);
                            canvas.drawBitmap(buddhaBitmap, srcRectTemp, desRectTemp, drawPaint);
                        }else {
                            drawPaint.setColorFilter(colorFilterEmpty);
                            canvas.drawBitmap(buddhaBitmap, left, top, drawPaint);
                        }
                    }
                }
            }
        }

    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        invalidate();
        requestLayout();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
        requestLayout();
    }

    public int getColorBuddha() {
        return colorBuddha;
    }

    public void setColorBuddha(int colorBuddha) {
        this.colorBuddha = colorBuddha;
        invalidate();
        requestLayout();
    }
}
