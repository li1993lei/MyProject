package com.lilei.hefei;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class NewsDetailActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.ll_control)
	private LinearLayout llControl;
	@ViewInject(R.id.btn_back)
	private ImageButton btnBack;
	@ViewInject(R.id.btn_textsize)
	private ImageButton btnTextSize;
	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;
	@ViewInject(R.id.btn_menu)
	private ImageButton btnMenu;
	@ViewInject(R.id.wv_news_detail)
	private WebView mWebView;
	@ViewInject(R.id.pb_loading)
	private ProgressBar pbLoading;
	private String mUrl2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		ViewUtils.inject(this);
		
		llControl.setVisibility(View.VISIBLE);
		btnBack.setVisibility(View.VISIBLE);
		btnMenu.setVisibility(View.GONE);
		
		btnBack.setOnClickListener(this);
		btnTextSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		
		mUrl2 = getIntent().getStringExtra("detailHtmlUrl");
		mWebView.loadUrl(mUrl2);
		
		WebSettings settings = mWebView.getSettings();
		settings.setBuiltInZoomControls(true);// ��ʾ���Ű�ť(wap��ҳ��֧��)
		settings.setUseWideViewPort(true);// ֧��˫������(wap��ҳ��֧��)
		settings.setJavaScriptEnabled(true);// ֧��js��
		
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				pbLoading.setVisibility(View.VISIBLE);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pbLoading.setVisibility(View.INVISIBLE);
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(mUrl2);
				return true;
			}
		});
		mWebView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) { //���ȸı�
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
			}
			@Override
			public void onReceivedTitle(WebView view, String title) {  //�յ�����
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_textsize:
			showChoseDialog();
			
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_share:
			Share();
			break;

		default:
			break;
		}
		
	}
	private void Share() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		
		oks.setTheme(OnekeyShareTheme.SKYBLUE);//�޸�������ʽ
		
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(getString(R.string.share));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl("http://sharesdk.cn");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("���Ƿ����ı�");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		//oks.setImagePath("/sdcard/test.jpg");// ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://sharesdk.cn");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("���ǲ��������ı�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
		oks.show(this);
		
	}
	private int tempWhich;
	private int currentWhich;
	private void showChoseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��������");

		String[] items = new String[] { "���������", "�������", "��������", "С������",
				"��С������" };
		builder.setSingleChoiceItems(items, currentWhich, new DialogInterface.OnClickListener() {
			
			

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tempWhich = which;
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WebSettings settings = mWebView.getSettings();
				switch (tempWhich) {
				case 0:
					// ��������
					settings.setTextSize(TextSize.LARGEST);
					// settings.setTextZoom(22);
					break;
				case 1:
					// ������
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					// ��������
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					// С����
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					// ��С����
					settings.setTextSize(TextSize.SMALLEST);
					break;

				default:
					break;
				}
				currentWhich = tempWhich;
				
			}
		});
		builder.create().show();
	}


}
