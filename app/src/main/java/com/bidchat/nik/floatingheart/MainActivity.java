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
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // public final String TAG = getClass().getCanonicalName();
    ImageView imageAnimateHeart;
    Button buttonAnimateHeart;
    int minAngle, maxAngle;
    ViewGroup rootView;

    int minXDispersePoint;
    int maxXDispersePoint;

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

        FrameLayout activityMain = (FrameLayout) findViewById(R.id.activity_main);
        activityMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFloatingHeart(imageAnimateHeart, getApplicationContext());
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

        Random random = new Random();

        int minXPivot = 0;
        int maxXPivot = view.getWidth();
        int randomPivot = random.nextInt(maxXPivot - minXPivot) + minXPivot;

        Animation animationScaleUp = new ScaleAnimation(0f, 1.2f, 0f, 1.2f, randomPivot, randomPivot);//  fromX,  toX,  fromY,  toY,  pivotX,  pivotY
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

        /*
         * To generate a random dispersing value between 0 to width of screen
         */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final int NUMBER_OF_CYCLES = 4;
        final int DIP_COVERED_PER_CYCLE = (size.y / 2) / NUMBER_OF_CYCLES;
        final int ANIMATION_TIME = (((size.y / 2)) / DIP_COVERED_PER_CYCLE) * 350;

        determineXDispersePoint((int) view.getX() + (view.getWidth() / 2), size.x - view.getWidth());
        int randomXDispersePoint = random.nextInt(maxXDispersePoint - minXDispersePoint) + minXDispersePoint;

        Animation animationTranslate = new TranslateAnimation(view.getX(), randomXDispersePoint, view.getY(), view.getY() - (rootView.getHeight() / 2f));// fromXDelta, toXDelta, fromYDelta, toYDelta
        animationTranslate.setFillAfter(true); // Needed to keep the result of the animation
        animationTranslate.setDuration(ANIMATION_TIME);
        animationTranslate.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(animationTranslate);

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
     * @param x     - location of the image
     * @param width - width of screen
     */
    public void determineXDispersePoint(int x, int width) {
        if (width - x < x) {
            minXDispersePoint = x - 100;
            if (width - x < 100) {
                maxXDispersePoint = width;
            } else {
                Log.d("Location", "You are here");
                maxXDispersePoint = x + 100;
            }
        } else {
            maxXDispersePoint = x + 100;
            if (x - 100 < 0) {
                minXDispersePoint = 0;
            } else {
                minXDispersePoint = x - 100;
            }
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