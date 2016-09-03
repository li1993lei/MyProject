package com.lilei.hefei.veiw;

import android.R.integer;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyTopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;
	public MyTopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyTopNewsViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	//  上下滑动时 我们要相应子控件的滑动事件  此时需要拦截
	//向右滑到第一页 向左滑到每个页面的最后一页 此时也要跳到下一个标签 都是需要父类来响应滑动事件
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//请求父类不要拦截我的事件
		getParent().requestDisallowInterceptTouchEvent(true);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			
			int dx = moveX - startX;
			int dy = moveY - startY;
			
			
			if (Math.abs(dx) > Math.abs(dy)) {
				int currentItem = getCurrentItem();
				//左右滑动
				if (dx > 0) {
					//向右划
					if (currentItem == 0) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}else{
					int count = getAdapter().getCount();
					if (currentItem == count -1) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
				
			}else{
				//上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
