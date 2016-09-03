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
	System.out.println("政务初始化啦...");

	// 要给帧布局填充布局对象
	TextView view = new TextView(mActivity);
	view.setText("政务");
	view.setTextColor(Color.RED);
	view.setTextSize(22);
	view.setGravity(Gravity.CENTER);

	flContent.addView(view);

	// 修改页面标题
	tvTitle.setText("人口管理");

	// 显示菜单按钮
	btnMenu.setVisibility(View.VISIBLE);
}
}
