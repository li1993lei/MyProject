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
		//����������һ�������¼�  
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
				//�ж��Ƿ��ǵ�һ�ν���  Ҫ�ǵ�һ�ν��� �����û���������ָ������   ���� ���������������ҳ��
				boolean isFirstEnter = SpTools.getBoolean(getApplicationContext(),"is_first_enter", true);
				Intent intent;
				if (isFirstEnter) { //����������������
					intent = new Intent(SplashActivity.this, GuideActivity.class);
				}else{ //������ҳ��
					intent = new Intent(SplashActivity.this, MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		});
		
	}

	private void initAnimation() {
		//��ת����
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF,0.5f);
		ra.setFillAfter(true);  //�趨��������ʱ��״̬ 
		ra.setDuration(1000);  //����ʱ��
		//���Ŷ���
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF,0.5f);
		sa.setFillAfter(true);
		sa.setDuration(1000);
		//͸���ȶ���
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		aa.setFillAfter(true);
		aa.setDuration(2000);
		//��������
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
