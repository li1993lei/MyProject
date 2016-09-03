package com.lilei.hefei.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lilei.hefei.base.BasePager;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void initData() {
		System.out.println("�ǻ۷����ʼ����...");

		// Ҫ��֡������䲼�ֶ���
		TextView view = new TextView(mActivity);
		view.setText("�ǻ۷���");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);

		// �޸�ҳ�����
		tvTitle.setText("����");

		// ��ʾ�˵���ť
		btnMenu.setVisibility(View.VISIBLE);
	}
}
