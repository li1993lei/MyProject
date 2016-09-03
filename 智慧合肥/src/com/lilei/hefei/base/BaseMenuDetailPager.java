package com.lilei.hefei.base;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {

	public Activity mActivity;
	public View mRootView;// �˵�����ҳ������

	public BaseMenuDetailPager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}

	// ��ʼ������,��������ʵ��
	public abstract View initView();

	// ��ʼ������
	public void initData() {

	}

}

