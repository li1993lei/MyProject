package com.lilei.hefei.fragment;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lilei.hefei.MainActivity;
import com.lilei.hefei.R;
import com.lilei.hefei.base.impl.NewsCenterPager;
import com.lilei.hefei.domain.NewsMenu.NewsMenuData;

public class LeftMenuFragment extends BaseFragment {
	@ViewInject(R.id.lv_list)
	private ListView lvList;

	private ArrayList<NewsMenuData> mNewsMenuData;// ������������ݶ���

	private int mCurrentPos;// ��ǰ��ѡ�е�item��λ��

	private LeftMenuAdapter mAdapter;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		// lvList = (ListView) view.findViewById(R.id.lv_list);
		ViewUtils.inject(this, view);// ע��view���¼�
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}
	/**
	 * ���õ�ǰ�˵�����ҳ
	 * 
	 * @param position
	 */
	protected void setCurrentDetailPager(int position) {
		// ��ȡ�������ĵĶ���
		MainActivity mainUI = (MainActivity) mActivity;
		// ��ȡContentFragment
		ContentFragment fragment = mainUI.getContentFragment();
		// ��ȡNewsCenterPager
		NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
		// �޸��������ĵ�FrameLayout�Ĳ���
		newsCenterPager.setCurrentDetailPager(position);
	}

	/**
	 * �򿪻��߹رղ����
	 */
	protected void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();// �����ǰ״̬�ǿ�, ���ú�͹�; ��֮��Ȼ
	}

	class LeftMenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mNewsMenuData.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			return mNewsMenuData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.list_item_left_menu,
					null);
			TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);

			NewsMenuData item = getItem(position);
			tvMenu.setText(item.title);

			if (position == mCurrentPos) {
				// ��ѡ��
				tvMenu.setEnabled(true);// ���ֱ�Ϊ��ɫ
			} else {
				// δѡ��
				tvMenu.setEnabled(false);// ���ֱ�Ϊ��ɫ
			}

			return view;
		}

	}

	public void setMenuData(ArrayList<NewsMenuData> data) {
		mNewsMenuData = data;
		mAdapter = new LeftMenuAdapter();
		lvList.setAdapter(mAdapter);
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentPos = position;// ���µ�ǰ��ѡ�е�λ��
				mAdapter.notifyDataSetChanged();// ˢ��listview

				// ��������
				toggle();

				// ��������֮��, Ҫ�޸��������ĵ�FrameLayout�е�����
				setCurrentDetailPager(position);
			}
		});
	}

}
