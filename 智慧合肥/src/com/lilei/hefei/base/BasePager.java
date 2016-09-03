package com.lilei.hefei.base;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lilei.hefei.MainActivity;
import com.lilei.hefei.R;


public class BasePager {
	
	public Activity mActivity;
	public View rootView;  //当前布局文件对象
	public TextView tvTitle;
	public ImageButton btnMenu,btnPhoto;
	public FrameLayout flContent;

	public BasePager(Activity activity){
		mActivity = activity;
		rootView = initView();
	}

	public View initView() {
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
		btnPhoto = (ImageButton) view.findViewById(R.id.btn_photo);
		flContent = (FrameLayout) view.findViewById(R.id.fl_content);

		btnMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle();         //动态打开关闭侧边栏
			}
		});
		return view;
	}
	protected void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然
	}

	public void initData(){
		
	}
}
