package com.bidchat.nik.floatingheart;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // public final String TAG = getClass().getCanonicalName();
    ImageView imageAnimateHeart;
    Button buttonAnimateHeart;
    int minAngle, maxAngle;
    ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = (ViewGroup) findViewById(android.R.id.content);

        imageAnimateHeart = (ImageView) findViewById(R.id.image_animate_heart);
        imageAnimateHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFloatingHeart(view, getApplicationContext());
            }
        });
        buttonAnimateHeart = (Button) findViewById(R.id.button_animate_heart);
        buttonAnimateHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFloatingHeart(view, getApplicationContext());
            }
        });
    }

    public void createFloatingHeart(View view, Context context) {
        final int scaleUpDuration = 150;
        final int scaleDownDuration = 50;

        AnimationSet animationSet = new AnimationSet(true);

        Animation animationScaleUp = new ScaleAnimation(0f, 1.2f, 0f, 1.2f, view.getPivotX(), view.getPivotY());//  fromX,  toX,  fromY,  toY,  pivotX,  pivotY
        animationScaleUp.setFillAfter(false);
        animationScaleUp.setDuration(scaleUpDuration);
        animationScaleUp.setInterpolator(new BounceInterpolator());
        animationSet.addAnimation(animationScaleUp);

        Animation animationScaleDown = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f, view.getPivotX(), view.getPivotY());
        animationScaleDown.setStartOffset(scaleUpDuration);
        animationScaleDown.setFillAfter(false);
        animationScaleDown.setDuration(scaleDownDuration);
        animationScaleDown.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(animationScaleDown);

        Random random = new Random();
        /*
         * To generate a random dispersing value between 0 to width of screen
         */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final int NUMBER_OF_CYCLES = 4;
        final int DIP_COVERED_PER_CYCLE = (size.y / 2) / NUMBER_OF_CYCLES;
        final int ANIMATION_TIME = (((size.y / 2)) / DIP_COVERED_PER_CYCLE) * 500;

        int minXDispersePoint = -(int) view.getX();
        int maxXDispersePoint = (int) (size.x - (view.getX() + view.getWidth()));
        int randomXDispersePoint = random.nextInt(maxXDispersePoint - minXDispersePoint) + minXDispersePoint;

        Animation animationTranslate = new TranslateAnimation(view.getX(), view.getX() + randomXDispersePoint, view.getY(), view.getY() - (rootView.getHeight() / 2f));// fromXDelta, toXDelta, fromYDelta, toYDelta
        animationTranslate.setFillAfter(true); // Needed to keep the result of the animation
        animationTranslate.setDuration(ANIMATION_TIME);
        animationTranslate.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(animationTranslate);

        /*
         * To generate a random angle for each floating heart between -8 to 8
         */
        determineAngle(minXDispersePoint, maxXDispersePoint);
        int randomStartAngle = random.nextInt(maxAngle - minAngle) + minAngle;

        Animation animationRotate = new RotateAnimation(0, randomStartAngle, Animation.ABSOLUTE, view.getPivotX(),
                Animation.ABSOLUTE, view.getPivotY());
        animationRotate.setFillAfter(true); // Needed to keep the result of the animation
        animationRotate.setDuration(ANIMATION_TIME / NUMBER_OF_CYCLES);
        animationRotate.setRepeatCount(Animation.INFINITE);
        animationRotate.setRepeatMode(Animation.REVERSE);
        animationRotate.setInterpolator(new CycleInterpolator(Animation.INFINITE));
        animationSet.addAnimation(animationRotate);

        final Animation animationAlpha = new AlphaAnimation(1, 0);// fromAlpha, toAlpha
        animationAlpha.setFillAfter(true); // Needed to keep the result of the animation
        animationAlpha.setDuration(ANIMATION_TIME / (int) (NUMBER_OF_CYCLES - 0.5));
        animationAlpha.setInterpolator(new LinearInterpolator());
        animationAlpha.setStartOffset((ANIMATION_TIME - (ANIMATION_TIME / NUMBER_OF_CYCLES)));

        ImageView imageHeart = new ImageView(this);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
        LinearLayout.LayoutParams layoutParamsLeftLetter = new LinearLayout.LayoutParams(px, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageHeart.setLayoutParams(layoutParamsLeftLetter);
        imageHeart.setAdjustViewBounds(true);
        imageHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart));
        imageHeart.setAlpha(0.8f);
        rootView.addView(imageHeart);

        CustomAnimationListener customAnimationListener = new CustomAnimationListener();
        customAnimationListener.setImage(animationSet, imageHeart);
        animationAlpha.setAnimationListener(customAnimationListener);
        animationSet.addAnimation(animationAlpha);
        imageHeart.startAnimation(animationSet);
    }

    /**
     * @param x1 - left end disperse point
     * @param x2 - right disperse point
     */
    public void determineAngle(int x1, int x2) {
        if (x1 == 0 || x1 > (-20)) {
            minAngle = -8;
            maxAngle = -1;
        } else if (x2 == 0 || x2 < (20)) {
            minAngle = 1;
            maxAngle = 8;
        } else if (-(x1) < x2) {
            minAngle = -4;
            maxAngle = 8;
        } else {
            minAngle = -8;
            maxAngle = 4;
        }
    }

    private class CustomAnimationListener implements Animation.AnimationListener {
        ImageView view;
        AnimationSet animationSet;

        void setImage(AnimationSet animationSet, ImageView view) {
            this.animationSet = animationSet;
            this.view = view;
        }

        public void onAnimationEnd(Animation animation) {
            rootView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.clearAnimation();
                    animationSet = null;
                    rootView.removeView(view);
                    view = null;
                    System.gc();
                }
            }, 0);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }
}