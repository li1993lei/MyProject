package com.lilei.hefei.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lilei.hefei.base.BasePager;

public class GovAffiaPager extends BasePager {

	public GovAffiaPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
@Override
public void initData() {
	System.out.println("�����ʼ����...");

	// Ҫ��֡������䲼�ֶ���
	TextView view = new TextView(mActivity);
	view.setText("����");
	view.setTextColor(Color.RED);
	view.setTextSize(22);
	view.setGravity(Gravity.CENTER);

	flContent.addView(view);

	// �޸�ҳ�����
	tvTitle.setText("�˿ڹ���");

	// ��ʾ�˵���ť
	btnMenu.setVisibility(View.VISIBLE);
}
}
