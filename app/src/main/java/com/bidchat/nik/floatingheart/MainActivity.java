package com.bidchat.nik.floatingheart;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public final String TAG = getClass().getCanonicalName();
    private static int ANIMATION_TIME = 4000;
    private static int ONE_CYCLE_TIME = 1000;
    private static int NUMBER_OF_CYCLES = ANIMATION_TIME / ONE_CYCLE_TIME;
    ImageView imageAnimateHeart;
    Button buttonAnimateHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageAnimateHeart = (ImageView) findViewById(R.id.image_animate_heart);
        imageAnimateHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFloatingHeart(view);
            }
        });
        buttonAnimateHeart = (Button) findViewById(R.id.button_animate_heart);
        buttonAnimateHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFloatingHeart(view);
            }
        });
    }

    public void createFloatingHeart(View view) {
        // int[] arrayColors = getApplicationContext().getResources().getIntArray(R.array.color_array);
        final int scaleUpDuration = 150;
        final int scaleDownDuration = 50;

        final ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);

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

        Animation animationTranslate = new TranslateAnimation(view.getX(), view.getX(), view.getY(), view.getY() - (rootView.getHeight() / 1.2f));// fromXDelta, toXDelta, fromYDelta, toYDelta
        // animationTranslate.setStartOffset(scaleUpDuration + scaleDownDuration);
        animationTranslate.setFillAfter(true); // Needed to keep the result of the animation
        animationTranslate.setDuration(ANIMATION_TIME);
        animationTranslate.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(animationTranslate);

        /**
         * To generate a random angle for each floating heart
         */
        Random random = new Random();
        int minAngle = -10;
        int maxAngle = 10;
        int randomStartAngle = random.nextInt(maxAngle - minAngle) + minAngle;
        // int randomEndAngle = random.nextInt(maxAngle - minAngle) + minAngle;

        Animation animationRotate = new RotateAnimation(0, randomStartAngle, Animation.ABSOLUTE, view.getPivotX(),
                Animation.ABSOLUTE, view.getPivotY());
        // animationRotate.setStartOffset(scaleUpDuration + scaleDownDuration);
        animationRotate.setFillAfter(true); // Needed to keep the result of the animation
        animationRotate.setDuration(ANIMATION_TIME / NUMBER_OF_CYCLES);
        animationRotate.setRepeatCount(Animation.INFINITE);
        animationRotate.setRepeatMode(Animation.REVERSE);
        animationRotate.setInterpolator(new FastOutSlowInInterpolator());
        animationSet.addAnimation(animationRotate);

        Animation animationAlpha = new AlphaAnimation(1, 0);// fromAlpha, toAlpha
        // animationAlpha.setStartOffset(scaleUpDuration + scaleDownDuration);
        animationAlpha.setFillAfter(true); // Needed to keep the result of the animation
        animationAlpha.setDuration(ANIMATION_TIME / NUMBER_OF_CYCLES);
        animationAlpha.setInterpolator(new LinearInterpolator());
        animationAlpha.setStartOffset((ANIMATION_TIME - (ANIMATION_TIME / NUMBER_OF_CYCLES)));
        animationSet.addAnimation(animationAlpha);

        /**
         * Randow color picker
         */

        /**
         * int minArrayIndex = 0;
         * int maxArrayIndex = arrayColors.length - 1;
         * int randomColorIndex = random.nextInt(maxArrayIndex - minArrayIndex) + minArrayIndex;
         */

        ImageView imageHeart = new ImageView(this);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
        LinearLayout.LayoutParams layoutParamsLeftLetter = new LinearLayout.LayoutParams(px, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageHeart.setLayoutParams(layoutParamsLeftLetter);
        imageHeart.setAdjustViewBounds(true);
        imageHeart.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart));
        // imageHeart.setColorFilter(arrayColors[randomColorIndex]);
        rootView.addView(imageHeart);

        imageHeart.startAnimation(animationSet);
    }
}
