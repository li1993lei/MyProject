package com.lilei.hefei;

import com.lilei.hefei.utils.SpTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;


public class SplashActivity extends Activity {
	private RelativeLayout rlRoot;
	private AnimationSet set;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initAnimation();
		initEvent();

	}

	private void initEvent() {
		rlRoot.startAnimation(set);
		//给动画设置一个监听事件  
		set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//判断是否是第一次进入  要是第一次进入 就让用户进入新手指导界面   否则 动画结束后进入主页面
				boolean isFirstEnter = SpTools.getBoolean(getApplicationContext(),"is_first_enter", true);
				Intent intent;
				if (isFirstEnter) { //进入新手引导界面
					intent = new Intent(SplashActivity.this, GuideActivity.class);
				}else{ //进入主页面
					intent = new Intent(SplashActivity.this, MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		});
		
	}

	private void initAnimation() {
		//旋转动画
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF,0.5f);
		ra.setFillAfter(true);  //设定动画结束时的状态 
		ra.setDuration(1000);  //动画时长
		//缩放动画
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF,0.5f);
		sa.setFillAfter(true);
		sa.setDuration(1000);
		//透明度动画
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		aa.setFillAfter(true);
		aa.setDuration(2000);
		//动画集合
		set = new AnimationSet(true);
		set.addAnimation(ra);
		set.addAnimation(sa);
		set.addAnimation(aa);
	}

	private void initView() {
		setContentView(R.layout.activity_splash);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		
	}

   
}
