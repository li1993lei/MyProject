package com.lilei.hefei.veiw;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lilei.hefei.R;

public class PullToRefershListView extends ListView implements OnScrollListener {

	private View headerView;
	private ImageView ivArraw;
	private ProgressBar pbLoading;
	private TextView tvTitle;
	private TextView tvDesc;
	private int measuredHeight;
	private RotateAnimation rotateUpAnim;
	private RotateAnimation rotateDownAnim;
	private float downY;
	private float moveY;
	public static final int PULL_TO_REFRESH = 0;// 下拉刷新
	public static final int RELEASE_REFRESH = 1;// 释放刷新
	public static final int REFRESHING = 2; // 刷新
	private int mCurrentState = PULL_TO_REFRESH;// 当前刷新状态

	public PullToRefershListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
		initAnimation();
	}

	public PullToRefershListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
		initAnimation();
	}

	public PullToRefershListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
		initAnimation();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY(); // 按下的Y坐标
			// System.out.println(downY);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mCurrentState == REFRESHING) {
				return super.onTouchEvent(ev);
			}
			moveY = ev.getY(); // 移动后的Y坐标
			// System.out.println(moveY);
			float offset = moveY - downY;

			if (offset > 0 && getFirstVisiblePosition() == 0) {
				int paddingTop = (int) (-measuredHeight + offset);
				headerView.setPadding(0, paddingTop, 0, 0);
				if (paddingTop >= 0 && mCurrentState != RELEASE_REFRESH) {
					// 切换成释放刷新状态
					mCurrentState = RELEASE_REFRESH;
					updateHeader();
					System.out.println("切换成释放刷新模式: " + paddingTop);
				} else if (paddingTop < 0 && mCurrentState != PULL_TO_REFRESH) {
					// 切换成下拉刷新状态
					mCurrentState = PULL_TO_REFRESH;
					updateHeader();
					System.out.println("切换成下拉刷新状态: " + paddingTop);
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mCurrentState == RELEASE_REFRESH) {
				headerView.setPadding(0, 0, 0, 0);
				mCurrentState = REFRESHING;
				updateHeader();
			} else if (mCurrentState == PULL_TO_REFRESH) {
				headerView.setPadding(0, -measuredHeight, 0, 0);
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void updateHeader() {
		switch (mCurrentState) {
		case PULL_TO_REFRESH:
			ivArraw.startAnimation(rotateDownAnim);
			tvTitle.setText("下拉刷新");
			break;
		case RELEASE_REFRESH:
			ivArraw.startAnimation(rotateUpAnim);
			tvTitle.setText("释放刷新");
			break;
		case REFRESHING:
			ivArraw.clearAnimation();
			ivArraw.setVisibility(View.INVISIBLE);
			pbLoading.setVisibility(View.VISIBLE);
			tvTitle.setText("正在刷新");
			if (mListener != null) {
				mListener.onFresh();
			}

			break;

		default:
			break;
		}
	}

	public void onFreshComplete(boolean isFreshTime) {
		if (!isLoadMore) {
			
			ivArraw.setVisibility(View.VISIBLE);
			pbLoading.setVisibility(View.INVISIBLE);
			tvTitle.setText("下拉刷新");
			headerView.setPadding(0, -measuredHeight, 0, 0);
			mCurrentState = PULL_TO_REFRESH;
			if (isFreshTime) { // 数据更新成功 才更新时间
				String time = setRefreshTime();
				tvDesc.setText("最后更新时间:" + time);
			}
		}else {
			footerView.setPadding(0, -footerHeiget, 0, 0);
		}

	}

	private String setRefreshTime() {
		long millis = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(millis);
	}

	private onFreshListener mListener;
	private View footerView;
	private int footerHeiget;

	public void setOnFreshListener(onFreshListener listener) {
		mListener = listener;
	}

	public interface onFreshListener {
		public void onFresh();

		public void onLoadMore();
	}

	private void initAnimation() {
		// 向上转, 围绕着自己的中心, 逆时针旋转0 -> -180.

		rotateUpAnim = new RotateAnimation(0f, -180f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateUpAnim.setDuration(300);
		rotateUpAnim.setFillAfter(true); // 动画停留在结束位置

		// 向下转, 围绕着自己的中心, 逆时针旋转 -180 -> -360

		rotateDownAnim = new RotateAnimation(-180f, -360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateDownAnim.setDuration(300);
		rotateDownAnim.setFillAfter(true); // 动画停留在结束位置

	}

	private void initFooterView() {
		footerView = View.inflate(getContext(),
				R.layout.pull_to_refresh_footer, null);

		footerView.measure(0, 0);

		footerHeiget = footerView.getMeasuredHeight();
		footerView.setPadding(0, -footerHeiget, 0, 0);
		setOnScrollListener(this);
		addFooterView(footerView);

	}

	private void initHeaderView() {
		headerView = View.inflate(getContext(),
				R.layout.pull_to_refersh_header_view, null);
		ivArraw = (ImageView) headerView.findViewById(R.id.iv_arrow);
		pbLoading = (ProgressBar) headerView.findViewById(R.id.pb);
		tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
		tvDesc = (TextView) headerView.findViewById(R.id.tv_desc_last_refresh);

		headerView.measure(0, 0); // 设定测量方式

		measuredHeight = headerView.getMeasuredHeight(); // 获取测量高度

		headerView.setPadding(0, -measuredHeight, 0, 0);
		addHeaderView(headerView);
	}

	private boolean isLoadMore;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 空闲
		if (scrollState == SCROLL_STATE_IDLE) {

			int lastVisiblePosition = getLastVisiblePosition();
			if (lastVisiblePosition == getCount() - 1 && !isLoadMore) {
				// 刷新
				isLoadMore = true;
				System.out.println("上拉加载更多");
				mListener.onLoadMore();
				setSelection(getCount() - 1);
				footerView.setVisibility(View.VISIBLE);
				footerView.setPadding(0, 0, 0, 0);
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	

}
