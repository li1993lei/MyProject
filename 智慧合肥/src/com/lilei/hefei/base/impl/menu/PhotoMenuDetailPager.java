package com.lilei.hefei.base.impl.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.lilei.hefei.R;
import com.lilei.hefei.base.BaseMenuDetailPager;
import com.lilei.hefei.base.impl.menu.TabDetailPager.ViewHolder;
import com.lilei.hefei.contact.GlobalConstants;
import com.lilei.hefei.domain.PhotosBean;
import com.lilei.hefei.domain.PhotosBean.PhotoNews;
import com.lilei.hefei.utils.SpTools;

public class PhotoMenuDetailPager extends BaseMenuDetailPager implements OnClickListener {
	@ViewInject(R.id.lv_photo)
	private ListView lvPhoto;
	@ViewInject(R.id.gv_photo)
	private GridView gvPhoto;
	private ImageButton btnPhoto;
	private ArrayList<PhotoNews> newsPhotosList;
	private MyAdapter adapter;
	public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
		super(activity);
		this.btnPhoto = btnPhoto;
		btnPhoto.setOnClickListener(this);
	}

	@Override
	public View initView() {
//		TextView view = new TextView(mActivity);
//		view.setText("≤Àµ•œÍ«È“≥-◊ÈÕº");
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
		View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
		ViewUtils.inject(this, view);
		return view;
	}
	@Override
	public void initData() {
		//ºÏ≤È «∑Ò”–ª∫¥Ê
		String cache = SpTools.getString(mActivity, GlobalConstants.PHOTOS_URL, "");
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}
		getDataFromServer();
	}
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstants.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				//◊ˆª∫¥Ê
				SpTools.setString(mActivity, GlobalConstants.PHOTOS_URL, result);
				//Ω‚Œˆ
				processData(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 1).show();
				
			}
		});
	}

	protected void processData(String result) {
		Gson gson = new Gson();
		PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);
		newsPhotosList = photosBean.data.news;
		adapter = new MyAdapter();
		lvPhoto.setAdapter(adapter);
		gvPhoto.setAdapter(adapter);
	}
	class MyAdapter extends BaseAdapter{
		BitmapUtils bitmapUtils;
		public MyAdapter(){
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils
			.configDefaultLoadingImage(R.drawable.pic_item_list_default);//≈‰÷√ƒ¨»œÕº∆¨
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newsPhotosList.size();
		}

		@Override
		public PhotoNews getItem(int position) {
			// TODO Auto-generated method stub
			return newsPhotosList.get(position);
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
				holder = new ViewHolder();
				convertView = View.inflate(mActivity, R.layout.list_item_photos, null);
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			PhotoNews photosBean = getItem(position);
			bitmapUtils.display(holder.ivPic, photosBean.listimage);
			holder.tvTitle.setText(photosBean.title);
			return convertView;
		}
		
	}
	static class ViewHolder{
		public ImageView ivPic;
		public TextView tvTitle;
	} 
	private boolean isListView = true;
	@Override
	public void onClick(View v) {
		if (isListView) {
			lvPhoto.setVisibility(View.VISIBLE);
			gvPhoto.setVisibility(View.GONE);
			btnPhoto.setImageResource(R.drawable.icon_pic_list_type);
		}else {
			lvPhoto.setVisibility(View.GONE);
			gvPhoto.setVisibility(View.VISIBLE);
			btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
		}
		isListView = !isListView;
	}

}
