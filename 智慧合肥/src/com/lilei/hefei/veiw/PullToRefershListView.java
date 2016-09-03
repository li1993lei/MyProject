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
	public static final int PULL_TO_REFRESH = 0;// ����ˢ��
	public static final int RELEASE_REFRESH = 1;// �ͷ�ˢ��
	public static final int REFRESHING = 2; // ˢ��
	private int mCurrentState = PULL_TO_REFRESH;// ��ǰˢ��״̬

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
			downY = ev.getY(); // ���µ�Y����
			// System.out.println(downY);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mCurrentState == REFRESHING) {
				return super.onTouchEvent(ev);
			}
			moveY = ev.getY(); // �ƶ����Y����
			// System.out.println(moveY);
			float offset = moveY - downY;

			if (offset > 0 && getFirstVisiblePosition() == 0) {
				int paddingTop = (int) (-measuredHeight + offset);
				headerView.setPadding(0, paddingTop, 0, 0);
				if (paddingTop >= 0 && mCurrentState != RELEASE_REFRESH) {
					// �л����ͷ�ˢ��״̬
					mCurrentState = RELEASE_REFRESH;
					updateHeader();
					System.out.println("�л����ͷ�ˢ��ģʽ: " + paddingTop);
				} else if (paddingTop < 0 && mCurrentState != PULL_TO_REFRESH) {
					// �л�������ˢ��״̬
					mCurrentState = PULL_TO_REFRESH;
					updateHeader();
					System.out.println("�л�������ˢ��״̬: " + paddingTop);
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
			tvTitle.setText("����ˢ��");
			break;
		case RELEASE_REFRESH:
			ivArraw.startAnimation(rotateUpAnim);
			tvTitle.setText("�ͷ�ˢ��");
			break;
		case REFRESHING:
			ivArraw.clearAnimation();
			ivArraw.setVisibility(View.INVISIBLE);
			pbLoading.setVisibility(View.VISIBLE);
			tvTitle.setText("����ˢ��");
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
			tvTitle.setText("����ˢ��");
			headerView.setPadding(0, -measuredHeight, 0, 0);
			mCurrentState = PULL_TO_REFRESH;
			if (isFreshTime) { // ���ݸ��³ɹ� �Ÿ���ʱ��
				String time = setRefreshTime();
				tvDesc.setText("������ʱ��:" + time);
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
		// ����ת, Χ�����Լ�������, ��ʱ����ת0 -> -180.

		rotateUpAnim = new RotateAnimation(0f, -180f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateUpAnim.setDuration(300);
		rotateUpAnim.setFillAfter(true); // ����ͣ���ڽ���λ��

		// ����ת, Χ�����Լ�������, ��ʱ����ת -180 -> -360

		rotateDownAnim = new RotateAnimation(-180f, -360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateDownAnim.setDuration(300);
		rotateDownAnim.setFillAfter(true); // ����ͣ���ڽ���λ��

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

		headerView.measure(0, 0); // �趨������ʽ

		measuredHeight = headerView.getMeasuredHeight(); // ��ȡ�����߶�

		headerView.setPadding(0, -measuredHeight, 0, 0);
		addHeaderView(headerView);
	}

	private boolean isLoadMore;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// ����
		if (scrollState == SCROLL_STATE_IDLE) {

			int lastVisiblePosition = getLastVisiblePosition();
			if (lastVisiblePosition == getCount() - 1 && !isLoadMore) {
				// ˢ��
				isLoadMore = true;
				System.out.println("�������ظ���");
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
