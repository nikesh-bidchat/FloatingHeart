package com.bidchat.nik.floatingheart;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public final String TAG = getClass().getCanonicalName();
    private static int ANIMATION_TIME = 4000;
    Button buttonAnimateHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAnimateHeart = (Button) findViewById(R.id.button_animate_heart);
        buttonAnimateHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFloatingHeart(view);
            }
        });
    }

    public void createFloatingHeart(View view) {
        AnimationSet animationSet = new AnimationSet(true);

        Animation animationTranslate = new TranslateAnimation(view.getX(), view.getX(), view.getY(), view.getY() - 800);// fromXDelta, toXDelta, fromYDelta, toYDelta
        animationTranslate.setFillAfter(true); // Needed to keep the result of the animation
        animationTranslate.setDuration(ANIMATION_TIME);
        animationTranslate.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(animationTranslate);

        /**
         * To generate a random angle for each floating heart
         */
        Random random = new Random();
        int minAngle = -8;
        int maxAngle = 8;
        int randomStartAngle = random.nextInt(maxAngle - minAngle) + minAngle;
        // int randomEndAngle = random.nextInt(maxAngle - minAngle) + minAngle;

        Animation animationRotate = new RotateAnimation(0, randomStartAngle, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1);
        animationRotate.setFillAfter(true); // Needed to keep the result of the animation
        animationRotate.setDuration(ANIMATION_TIME / 4);
        animationRotate.setRepeatCount(Animation.INFINITE);
        animationRotate.setRepeatMode(Animation.REVERSE);
        animationRotate.setInterpolator(new FastOutSlowInInterpolator());
        animationSet.addAnimation(animationRotate);

        Animation animationAlpha = new AlphaAnimation(1, 0);// fromAlpha, toAlpha
        animationAlpha.setFillAfter(true); // Needed to keep the result of the animation
        animationAlpha.setDuration(ANIMATION_TIME / 4);
        animationAlpha.setRepeatCount(0);
        animationAlpha.setInterpolator(new LinearInterpolator());
        animationAlpha.setStartOffset(ANIMATION_TIME - (ANIMATION_TIME / 4));
        animationSet.addAnimation(animationAlpha);

        final ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        ImageView imageHeart = new ImageView(this);
        LinearLayout.LayoutParams layoutParamsLeftLetter = new LinearLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageHeart.setLayoutParams(layoutParamsLeftLetter);
        imageHeart.setAdjustViewBounds(true);
        imageHeart.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart));
        rootView.addView(imageHeart);

        imageHeart.startAnimation(animationSet);
    }
}
