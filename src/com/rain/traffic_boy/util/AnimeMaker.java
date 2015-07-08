package com.rain.traffic_boy.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

public class AnimeMaker {

	public static AnimatorSet scaleXY(Object... objects) {

		ObjectAnimator anim1 = ObjectAnimator.ofFloat(objects[0], "scaleX", 1f,
				1.5f, 1f);
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(objects[1], "scaleX",
				1.5f, 1f, 1.5f);
		ObjectAnimator anim3 = ObjectAnimator.ofFloat(objects[0], "scaleY", 1f,
				1.5f, 1f);
		ObjectAnimator anim4 = ObjectAnimator.ofFloat(objects[1], "scaleY",
				1.5f, 1f, 1.5f);
		anim1.setRepeatMode(ObjectAnimator.INFINITE);
		anim2.setRepeatMode(ObjectAnimator.INFINITE);
		anim3.setRepeatMode(ObjectAnimator.INFINITE);
		anim4.setRepeatMode(ObjectAnimator.INFINITE);
		anim1.setRepeatCount(100);
		anim2.setRepeatCount(100);
		anim3.setRepeatCount(100);
		anim4.setRepeatCount(100);

		final AnimatorSet set = new AnimatorSet();
		set.play(anim1).with(anim2).with(anim3).with(anim4);
		set.setDuration(8000);
		return set;

	}

}
