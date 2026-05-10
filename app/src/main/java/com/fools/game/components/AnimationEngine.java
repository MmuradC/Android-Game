package com.fools.game.components;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

public class AnimationEngine {
    public static void shakeView(View view) {
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
                view,
                PropertyValuesHolder.ofFloat("translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
        );
        animator.setDuration(400);
        animator.start();
    }

    public static void playCardAnimation(View cardView, Runnable onEnd) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(cardView, "translationY", 0, -300f);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(cardView, "alpha", 1f, 0f);
        
        animator.setDuration(300);
        alphaAnim.setDuration(300);
        
        animator.start();
        alphaAnim.start();
        
        // Wait for animation to finish before applying effect
        cardView.postDelayed(onEnd, 300);
    }
}
