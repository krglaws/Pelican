package pelican.pelican;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Sebastian on 3/25/2018.
 */

public class TabsView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ImageView recordButton;
    private ImageView homeButton;
    private ImageView cameraButton;
    private ImageView line;
    private ArgbEvaluator mArgbEval;
    private int mainColor;
    private int fadeColor;
    private int viewTranslationX;

    public TabsView(@NonNull Context context) {
        this(context, null);
    }

    public TabsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setViewPager(final ViewPager viewPager){
        viewPager.addOnPageChangeListener(this);

        homeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() != 0) viewPager.setCurrentItem(0);
            }
        });

        cameraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() != 1) viewPager.setCurrentItem(1);
            }
        });
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.tabs, this, true);
        recordButton = findViewById(R.id.recordButton);
        homeButton = findViewById(R.id.homeButton);
        cameraButton = findViewById(R.id.cameraButton);

        line = findViewById(R.id.line);
        mArgbEval = new ArgbEvaluator();

        //int translationValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());

        line.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewTranslationX = (int) (homeButton.getX() - cameraButton.getX());
                line.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mainColor = ContextCompat.getColor(getContext(), R.color.white);
        fadeColor = ContextCompat.getColor(getContext(), R.color.gray);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0){
            int color = (int) mArgbEval.evaluate(1-positionOffset, mainColor, fadeColor);
            int newColor = (int) mArgbEval.evaluate(positionOffset, mainColor, fadeColor);
            homeButton.setColorFilter(newColor);
            cameraButton.setColorFilter(color);
            homeButton.setAlpha(0.75F + (1 - positionOffset));
            cameraButton.setAlpha(0.75F + positionOffset);

            //scale
            homeButton.setScaleX(1 + (0.3F * (1 - positionOffset)));
            homeButton.setScaleY(1 + (0.3F * (1 - positionOffset)));
            cameraButton.setScaleX(1 + (0.3F * (positionOffset)));
            cameraButton.setScaleY(1 + (0.3F * (positionOffset)));

            line.setTranslationX(-(viewTranslationX * (positionOffset)));
        }
        else {
            int color = (int) mArgbEval.evaluate(1-positionOffset, mainColor, fadeColor);
            int newColor = (int) mArgbEval.evaluate(positionOffset, mainColor, fadeColor);
            homeButton.setColorFilter(color);
            cameraButton.setColorFilter(newColor);
            homeButton.setAlpha(0.75F + positionOffset);
            cameraButton.setAlpha(0.75F + (1 - positionOffset));

            line.setTranslationX(-(viewTranslationX * (1-positionOffset)));

        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
