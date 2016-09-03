package com.lilei.hefei.domain;

import java.util.ArrayList;

/**
 * 分类信息封装
 * 
 * 使用Gson解析�?,对象书写�?�?: 1. 逢{}创建对象, 逢[]创建集合(ArrayList) 2. �?有字段名称要和json返回字段高度�?�?
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class NewsMenu {

	public int retcode;
	public ArrayList<Integer> extend;
	public ArrayList<NewsMenuData> data;

	// 侧边栏菜单对�?
	public class NewsMenuData {
		public int id;
		public String title;
		public int type;

		public ArrayList<NewsTabData> children;

		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + ", children=" + children
					+ "]";
		}
	}

	// 页签的对�?
	public class NewsTabData {
		public int id;
		public String title;
		public int type;
		public String url;

		@Override
		public String toString() {
			return "NewsTabData [title=" + title + "]";
		}

	}

	@Override
	public String toString() {
		return "NewsMenu [data=" + data + "]";
	}

}
