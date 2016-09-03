package com.lilei.hefei.base.impl.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lilei.hefei.MainActivity;
import com.lilei.hefei.R;
import com.lilei.hefei.base.BaseMenuDetailPager;
import com.lilei.hefei.domain.NewsMenu.NewsTabData;
import com.viewpagerindicator.TabPageIndicator;

public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {
	
	@ViewInject(R.id.vp_news_menu_detail)
	private ViewPager mViewPager;

	@ViewInject(R.id.indicator)
	private TabPageIndicator mIndicator;
	private ArrayList<NewsTabData> mTabData;// ҳǩ��������
	private ArrayList<TabDetailPager> mPagers;// ҳǩҳ�漯��
	public NewsMenuDetailPager(Activity activity, ArrayList<NewsTabData> children) {
		super(activity);
		mTabData = children;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_news_menu_detail,
				null);
		ViewUtils.inject(this, view);
		return view;
	}
	@Override
	public void initData() {
		mPagers  = new ArrayList<TabDetailPager>();
		for (int i = 0; i < mTabData.size(); i++) {
			TabDetailPager pager = new TabDetailPager(mActivity,mTabData.get(i));
			mPagers.add(pager);
		}
		mViewPager.setAdapter(new NewsMenuDetailAdapter());
		mIndicator.setViewPager(mViewPager);
		mIndicator.setOnPageChangeListener(this);
	}
	class NewsMenuDetailAdapter extends PagerAdapter {
		
		
		// ָ��ָʾ���ı���
		@Override
		public CharSequence getPageTitle(int position) {
			NewsTabData data = mTabData.get(position);
			return data.title;
		}

		@Override
		public int getCount() {
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mPagers.get(position);

			View view = pager.mRootView;
			container.addView(view);

			pager.initData();

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	//	System.out.println("��ǰλ��:" + position);
		

		
	}
	protected void setSlidingMenuEnable(boolean enable) {
		// ��ȡ���������
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	@Override
	public void onPageSelected(int arg0) {
		if (arg0 == 0) {
			// ���������
			setSlidingMenuEnable(true);
		} else {
			// ���ò����
			setSlidingMenuEnable(false);
		}
	}
	@OnClick(R.id.btn_next)
	public void nextPage(View view) {
		// �����¸�ҳ��
		int currentItem = mViewPager.getCurrentItem();
		currentItem++;
		mViewPager.setCurrentItem(currentItem);
	}
}
