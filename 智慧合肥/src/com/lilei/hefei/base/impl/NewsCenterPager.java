package com.lilei.hefei.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lilei.hefei.MainActivity;
import com.lilei.hefei.base.BaseMenuDetailPager;
import com.lilei.hefei.base.BasePager;
import com.lilei.hefei.base.impl.menu.InteractMenuDetailPager;
import com.lilei.hefei.base.impl.menu.NewsMenuDetailPager;
import com.lilei.hefei.base.impl.menu.PhotoMenuDetailPager;
import com.lilei.hefei.base.impl.menu.TopicMenuDetailPager;
import com.lilei.hefei.contact.GlobalConstants;
import com.lilei.hefei.domain.NewsMenu;
import com.lilei.hefei.fragment.LeftMenuFragment;
import com.lilei.hefei.utils.CacheUtils;

public class NewsCenterPager extends BasePager {
	private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;// 菜单详情页集合
	private NewsMenu menuData;

	public NewsCenterPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initData() {
		System.out.println("新闻中心初始化了...");

		// // 要锟斤拷帧锟斤拷锟斤拷锟斤拷洳硷拷侄锟斤拷锟�
		// TextView view = new TextView(mActivity);
		// view.setText("锟斤拷锟斤拷");
		// view.setTextColor(Color.RED);
		// view.setTextSize(22);
		// view.setGravity(Gravity.CENTER);

		// flContent.addView(view);

		//
		tvTitle.setText("新闻中心");

		//
		btnMenu.setVisibility(View.VISIBLE);
		// 判断是否有缓存
		String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL,
				mActivity);
		if (!TextUtils.isEmpty(cache)) {
			System.out.println("发现缓存了");
			processData(cache);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstants.CATEGORY_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						// System.out.println(result);
						CacheUtils.setCache(GlobalConstants.CATEGORY_URL,
								result, mActivity);
						processData(result); //
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						error.printStackTrace();
						Toast.makeText(mActivity, msg, 0).show();

					}
				});
	}

	protected void processData(String result) {
		Gson gson = new Gson();
		menuData = gson.fromJson(result, NewsMenu.class);
		//把数据传给侧边栏
		MainActivity mainUI = (MainActivity) mActivity;
		LeftMenuFragment fragment = mainUI.getLeftMenuFragment();
		fragment.setMenuData(menuData.data);
		
		//加载布局
		mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
		mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,menuData.data.get(0).children));
		mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
		mMenuDetailPagers.add(new PhotoMenuDetailPager(mActivity,btnPhoto));
		mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));
		setCurrentDetailPager(0);
	}

	public void setCurrentDetailPager(int position) {
		BaseMenuDetailPager pager = mMenuDetailPagers.get(position);
		View view = pager.mRootView;
		flContent.removeAllViews();
		
		flContent.addView(view);
		//让子类联网去拿数据
		pager.initData();
		//更新标题
		tvTitle.setText(menuData.data.get(position).title);
		
		if (pager instanceof PhotoMenuDetailPager) {
			btnPhoto.setVisibility(View.VISIBLE);
		}else {
			btnPhoto.setVisibility(View.GONE);
		}
	}
}
