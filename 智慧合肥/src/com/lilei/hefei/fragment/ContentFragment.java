package com.lilei.hefei.fragment;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lilei.hefei.MainActivity;
import com.lilei.hefei.R;
import com.lilei.hefei.base.BasePager;
import com.lilei.hefei.base.impl.GovAffiaPager;
import com.lilei.hefei.base.impl.HomePager;
import com.lilei.hefei.base.impl.NewsCenterPager;
import com.lilei.hefei.base.impl.SettingPager;
import com.lilei.hefei.base.impl.SmartServicePager;
import com.lilei.hefei.veiw.NoScrollViewPager;

public class ContentFragment extends BaseFragment {
	private NoScrollViewPager mViewPager;
	private RadioGroup rgGroup;
	private ArrayList<BasePager> mPagers;
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
		rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
		return view;
	}
	class MyPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagers.get(position);
			View view = pager.rootView;
			
			container.addView(view);
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}
	@Override
	public void initData() {
		mPagers = new ArrayList<BasePager>();
		mPagers.add(new HomePager(mActivity));
		mPagers.add(new NewsCenterPager(mActivity));
		mPagers.add(new SmartServicePager(mActivity));
		mPagers.add(new GovAffiaPager(mActivity));
		mPagers.add(new SettingPager(mActivity));
		
		//ҳ��������
		mViewPager.setAdapter(new MyPagerAdapter());
		// ������ǩ�л�����
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					// ��ҳ
					// mViewPager.setCurrentItem(0);
					mViewPager.setCurrentItem(0, false);// ��2:��ʾ�Ƿ���л�������
					break;
				case R.id.rb_news:
					// ��������
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.rb_smart:
					// �ǻ۷���
					mViewPager.setCurrentItem(2, false);
					break;
				case R.id.rb_gov:
					// ����
					mViewPager.setCurrentItem(3, false);
					break;
				case R.id.rb_setting:
					// ����
					mViewPager.setCurrentItem(4, false);
					break;

				default:
					break;
				}
			}
		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				BasePager pager = mPagers.get(position);
				pager.initData();

				if (position == 0 || position == mPagers.size() - 1) {
					// ��ҳ������ҳҪ���ò����
					setSlidingMenuEnable(false);
				} else {
					// ����ҳ�濪�������
					setSlidingMenuEnable(true);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
			
			
		});
		//�ֶ����ص�һ������
		mPagers.get(0).initData();
		setSlidingMenuEnable(false);
	}
	/**
	 * ��������ò����
	 * 
	 * @param enable
	 */
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
	public NewsCenterPager getNewsCenterPager() {
		NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
		return pager;
	}
}
