package pelican.pelican;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Sebastian on 3/25/2018.
 */

public class TabsView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ImageView recordButton;
    private ImageView homeButton;
    private ImageView cameraButton;

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

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.tabs, this, true);
        recordButton = findViewById(R.id.recordButton);
        homeButton = findViewById(R.id.homeButton);
        cameraButton = findViewById(R.id.cameraButton);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
