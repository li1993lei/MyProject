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
	//  ���»���ʱ ����Ҫ��Ӧ�ӿؼ��Ļ����¼�  ��ʱ��Ҫ����
	//���һ�����һҳ ���󻬵�ÿ��ҳ������һҳ ��ʱҲҪ������һ����ǩ ������Ҫ��������Ӧ�����¼�
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//�����಻Ҫ�����ҵ��¼�
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
				//���һ���
				if (dx > 0) {
					//���һ�
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
				//���»���
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
