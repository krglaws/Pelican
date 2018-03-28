package pelican.pelican;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int position = 0;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            position = extras.getInt("pos");
        }
        ViewPager mViewPager = findViewById(R.id.viewPager);
        FragmentPagerAdapter adapterViewPager = new CustomPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(position);
        mViewPager.setPageTransformer(true, new CustomPageTransformer());

    }
    //custom pager adapter
    private static class CustomPagerAdapter extends FragmentPagerAdapter {
        private CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return HomeFragment.newInstance();
            } else {
                return CameraFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
    //custom page transformer
    private static class CustomPageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {
            //view overlap
            view.setTranslationX(view.getWidth() * -position);

            //change alpha based on position
            if(position <= -1.0F || position >= 1.0F) {
                view.setAlpha(0.0F);
            } else if( position == 0.0F ) {
                view.setAlpha(1.0F);
            } else { //position is between -1.0F & 0.0F OR 0.0F & 1.0F
                view.setAlpha(1.0F - Math.abs(position));
            }
        }
    }

}
