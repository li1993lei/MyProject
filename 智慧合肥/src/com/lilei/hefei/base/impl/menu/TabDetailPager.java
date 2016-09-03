package com.lilei.hefei.base.impl.menu;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lilei.hefei.NewsDetailActivity;
import com.lilei.hefei.R;
import com.lilei.hefei.base.BaseMenuDetailPager;
import com.lilei.hefei.contact.GlobalConstants;
import com.lilei.hefei.domain.NewsMenu.NewsTabData;
import com.lilei.hefei.domain.NewsTabBean;
import com.lilei.hefei.domain.NewsTabBean.NewsData;
import com.lilei.hefei.domain.NewsTabBean.TopNews;
import com.lilei.hefei.utils.CacheUtils;
import com.lilei.hefei.utils.SpTools;
import com.lilei.hefei.veiw.MyTopNewsViewPager;
import com.lilei.hefei.veiw.PullToRefershListView;
import com.lilei.hefei.veiw.PullToRefershListView.onFreshListener;
import com.viewpagerindicator.CirclePageIndicator;

public class TabDetailPager extends BaseMenuDetailPager {

//	private TextView view;
	@ViewInject(R.id.tv_title)
	private TextView tvtitle;
	
	@ViewInject(R.id.indicator)
	private CirclePageIndicator indicator;
	
	@ViewInject(R.id.lv_news)
	private PullToRefershListView lvNews;
	private NewsTabData mnewsTabData;
	@ViewInject(R.id.vp_top_news)
	private MyTopNewsViewPager vpTopNews;  //当修改自定义控件时  应当引入自己的名称
	private String mUrlString;
	private ArrayList<TopNews> mTopNewsDatas;

	private ArrayList<NewsData> newsData;

	private String moreUrl;
	private Handler mHandler;
	private MyAdapter adapter;
	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		mnewsTabData = newsTabData;
		mUrlString = GlobalConstants.SERVER_URL + mnewsTabData.url;
	//	System.out.println(mUrlString);
	}

	@Override
	public View initView() {
		/*view = new TextView(mActivity);
				 
				 
				  view.setTextColor(Color.RED);
				  view.setTextSize(22);
				  view.setGravity(Gravity.CENTER);
 		return view;*/
		View view = View.inflate(mActivity, R.layout.top_news_paper, null);
		
		ViewUtils.inject(this, view);
		
		View headerView = View.inflate(mActivity, R.layout.list_header_top_news, null);
		
		ViewUtils.inject(this, headerView);

		lvNews.addHeaderView(headerView);
		lvNews.setOnFreshListener(new onFreshListener() {
			
			@Override
			public void onFresh() {
				getTabDataFromServer(); //重新加载数据
			}

			@Override
			public void onLoadMore() {
				if (moreUrl != null) {
					
					getMoreDataFromServer(); //加在更多数据
				}else {
					//没有下一页数据了   此处出现 bUG无数据时无法关闭加载空间波
					lvNews.onFreshComplete(true);  
					Toast.makeText(mActivity, "没有数据了", 0).show();
					//adapter.notifyDataSetChanged();  
				}
			}
		});
		lvNews.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position = position - lvNews.getHeaderViewsCount();
				NewsData  news = newsData.get(position);
				String readIds = SpTools.getString(mActivity, "read_ids", "");
				if (!readIds.contains(news.id + "")) { //防止重复添加
					readIds = readIds + news.id +",";
					SpTools.setString(mActivity, "read_ids", readIds);
				}
				
				//改变颜色
			TextView textView = (TextView) view.findViewById(R.id.tv_title);
			textView.setTextColor(Color.GRAY);
			
			//跳转到详情页
			Intent intent = new Intent(mActivity,NewsDetailActivity.class);
			intent.putExtra("detailHtmlUrl", news.url);
			mActivity.startActivity(intent);
			}
		});
		return view;
	}
	class MyPagerAdapter extends PagerAdapter{
		private BitmapUtils bitmapUtils;
		public MyPagerAdapter(){
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default); //设置加载时的默认图片
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTopNewsDatas.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mActivity);
	//		imageView.setBackgroundResource(R.drawable.topnews_item_default);
			imageView.setScaleType(ScaleType.FIT_XY);  //填充父空间
			String imageUrl = mTopNewsDatas.get(position).topimage;
			bitmapUtils.display(imageView, imageUrl);
			container.addView(imageView);
			return imageView;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((View) object);
		}
		
	}
	//联网加载数据
	@Override
	public void initData() {
		//缓存的处理
		String cache = CacheUtils.getCache(mUrlString, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache,false);
		}
		getTabDataFromServer();
		//getMoreDataFromServer();
	}

	private void getTabDataFromServer() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrlString, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result,false);  //解析数据
				//做缓存
				CacheUtils.setCache(mUrlString, result, mActivity);
		//		System.out.println(result);
				
				//加载完成
				lvNews.onFreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 1).show();
				
				lvNews.onFreshComplete(false);
			}
		});
	}

	protected void processData(String result,boolean isLoadMore) {
		Gson gson = new Gson();
		NewsTabBean tabBean = gson.fromJson(result, NewsTabBean.class);//拿到数据
		mTopNewsDatas = tabBean.data.topnews;
		String mUrl = tabBean.data.more;
		if (!TextUtils.isEmpty(mUrl)) {
			
			moreUrl = GlobalConstants.SERVER_URL + tabBean.data.more;
		}else{
			moreUrl = null;
		}
		if (!isLoadMore) {
			
			//数据加载完成后  启动适配器
			if (mTopNewsDatas != null) {
				vpTopNews.setAdapter(new MyPagerAdapter());
				//设置指示器小点
				indicator.setViewPager(vpTopNews);
				indicator.setSnap(true);  //快照方式
				indicator.setOnPageChangeListener(new OnPageChangeListener() {
					
					@Override
					public void onPageSelected(int arg0) {
						tvtitle.setText(mTopNewsDatas.get(arg0).title);
					}
					
					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				tvtitle.setText(mTopNewsDatas.get(0).title);
				indicator.onPageSelected(0);  //当页面销毁时  消除indicator保留位置 导致的 图片原点不一致的BUG
			}
			
			newsData = tabBean.data.news;
			if (newsData != null) {
				adapter = new MyAdapter();
				lvNews.setAdapter(adapter);
			}
			if (mHandler == null) {
				mHandler = new Handler(){
					public void handleMessage(android.os.Message msg) {
						int currentItem = vpTopNews.getCurrentItem();
						currentItem ++;
						if (currentItem > mTopNewsDatas.size() -1) {
							currentItem = 0;
						}
						vpTopNews.setCurrentItem(currentItem);
						mHandler.sendEmptyMessageDelayed(0, 3000);
					};
				};
				mHandler.sendEmptyMessageDelayed(0, 3000);
				vpTopNews.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							System.out.println("ACTION_DOWN");
							// 停止广告自动轮播
							// 删除handler的所有消息
							mHandler.removeCallbacksAndMessages(null);
							// mHandler.post(new Runnable() {
							//
							// @Override
							// public void run() {
							// //在主线程运行
							// }
							// });
							break;
						case MotionEvent.ACTION_CANCEL:// 取消事件,
														// 当按下viewpager后,直接滑动listview,导致抬起事件无法响应,但会走此事件
							System.out.println("ACTION_CANCEL");
							// 启动广告
							mHandler.sendEmptyMessageDelayed(0, 3000);
							break;
						case MotionEvent.ACTION_UP:
							System.out.println("ACTION_UP");
							// 启动广告
							mHandler.sendEmptyMessageDelayed(0, 3000);
							break;

						default:
							break;
						}
						return false;
					}
				});
			}
		}else {
			ArrayList<NewsData> news = tabBean.data.news;
			newsData.addAll(news);
			adapter.notifyDataSetChanged();
		}
	}
	protected void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, moreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result,true);  //解析数据
				//做缓存
			
		//		System.out.println(result);
				
				//加载完成
				lvNews.onFreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 1).show();
				
				lvNews.onFreshComplete(false);
			}
		});
	}
	private class MyAdapter extends BaseAdapter{
		BitmapUtils mbitmapUtils;
		public MyAdapter(){
			mbitmapUtils = new BitmapUtils(mActivity);
			mbitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newsData.size();
		}

		@Override
		public NewsData getItem(int position) {
			// TODO Auto-generated method stub
			return newsData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder  = new ViewHolder();
				convertView = View.inflate(mActivity, R.layout.list_item_news, null);
				holder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			NewsData data = getItem(position);
			String readIds = SpTools.getString(mActivity,"read_ids" , "");
			if (readIds.contains(data.id+"")) {
				holder.tvTitle.setTextColor(Color.GRAY);
			}else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}
			holder.tvTitle.setText(data.title);
			holder.tvDate.setText(data.pubdate);
			
			mbitmapUtils.display(holder.ivIcon, data.listimage);
			return convertView;
		}
		
	}

	static class ViewHolder {
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvDate;
	}
}
