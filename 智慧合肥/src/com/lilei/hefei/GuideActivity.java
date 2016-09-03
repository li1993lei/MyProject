package com.lilei.hefei;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lilei.hefei.utils.SpTools;

public class GuideActivity extends Activity {

	private ViewPager mViewPager;
	private LinearLayout llContainer;
	private ImageView ivRedPoint;// С���
	private Button btnStart;

	private ArrayList<ImageView> mImageViewList; // imageView����

	// ����ҳͼƬid����
	private int[] mImageIds = new int[] { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3 };

	// С����ƶ�����
	private int mPointDis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ������,
														// ������setContentView֮ǰ����
		setContentView(R.layout.activity_guide);

		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		llContainer = (LinearLayout) findViewById(R.id.ll_container);
		ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
		btnStart = (Button) findViewById(R.id.btn_start);

		initData();// �ȳ�ʼ������
		mViewPager.setAdapter(new GuideAdapter());// ��������

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// ĳ��ҳ�汻ѡ��
				if (position == mImageViewList.size() - 1) {// ���һ��ҳ����ʾ��ʼ����İ�ť
					btnStart.setVisibility(View.VISIBLE);
				} else {
					btnStart.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// ��ҳ�滬�������еĻص�
				System.out.println("��ǰλ��:" + position + ";�ƶ�ƫ�ưٷֱ�:"
						+ positionOffset);
				// ����С������
				int leftMargin = (int) (mPointDis * positionOffset) + position
						* mPointDis;// ����С��㵱ǰ����߾�
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint
						.getLayoutParams();
				params.leftMargin = leftMargin;// �޸���߾�

				// �������ò��ֲ���
				ivRedPoint.setLayoutParams(params);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// ҳ��״̬�����仯�Ļص�
			}
		});

		// ��������Բ��ľ���
		// �ƶ�����=�ڶ���Բ��leftֵ - ��һ��Բ��leftֵ
		// measure->layout(ȷ��λ��)->draw(activity��onCreate����ִ�н���֮��Ż��ߴ�����)
		// mPointDis = llContainer.getChildAt(1).getLeft()
		// - llContainer.getChildAt(0).getLeft();
		// System.out.println("Բ�����:" + mPointDis);

		// ����layout�����������¼�,λ��ȷ����֮���ٻ�ȡԲ����
		// ��ͼ��
		ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// �Ƴ�����,�����ظ��ص�
						ivRedPoint.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						// ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						// layout����ִ�н����Ļص�
						mPointDis = llContainer.getChildAt(1).getLeft()
								- llContainer.getChildAt(0).getLeft();
						System.out.println("Բ�����:" + mPointDis);
					}
				});
		
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//����sp, �Ѿ����ǵ�һ�ν�����
				SpTools.setBoolean(getApplicationContext(), "is_first_enter", false);
				
				//������ҳ��
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		});
	}

	// ��ʼ������
	private void initData() {
		mImageViewList = new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView view = new ImageView(this);
			view.setBackgroundResource(mImageIds[i]);// ͨ�����ñ���,�����ÿ����䲼��
			// view.setImageResource(resId)
			mImageViewList.add(view);

			// ��ʼ��СԲ��
			ImageView point = new ImageView(this);
			point.setImageResource(R.drawable.shape_point_gray);// ����ͼƬ(shape��״)

			// ��ʼ�����ֲ���, ��߰�������,���ؼ���˭,����˭�����Ĳ��ֲ���
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			if (i > 0) {
				// �ӵڶ����㿪ʼ������߾�
				params.leftMargin = 10;
			}

			point.setLayoutParams(params);// ���ò��ֲ���

			llContainer.addView(point);// ���������Բ��
		}
	}

	class GuideAdapter extends PagerAdapter {

		// item�ĸ���
		@Override
		public int getCount() {
			return mImageViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		// ��ʼ��item����
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = mImageViewList.get(position);
			container.addView(view);
			return view;
		}

		// ����item
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
