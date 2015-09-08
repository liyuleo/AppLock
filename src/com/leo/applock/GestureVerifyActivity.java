package com.leo.applock;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 
 * 手势绘制/校验界面
 * 
 */
public class GestureVerifyActivity extends Activity implements android.view.View.OnClickListener {
	// 验证成功
	public static final int VERIFY_SUCCESS = 0x100;
	// 验证失败
	public static final int VERIFY_FAIL = 0x101;
	// 其他方式
	public static final int VERIFY_OTHER = 0x102;

	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextForget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_verify);
		setUpViews();
		setUpListeners();
	}

	private void setUpViews() {
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
		// 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(this, true,
				PreferenceUtils.getPrefString(getApplicationContext(), PreferenceUtils.PARMA_GESTURE_PASSWORD, ""),
				new com.leo.applock.GestureDrawline.GestureCallBack() {

					@Override
					public void onGestureCodeInput(String inputCode) {

					}

					@Override
					public void checkedSuccess() {
						mGestureContentView.clearDrawlineState(0L);
						setResult(VERIFY_SUCCESS);
						GestureVerifyActivity.this.finish();
					}

					@Override
					public void checkedFail() {
						mGestureContentView.clearDrawlineState(1300L);
						mTextTip.setVisibility(View.VISIBLE);
						mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>密码错误</font>"));
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this,
								R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
	}

	private void setUpListeners() {
		mTextForget.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_cancel:
			setResult(VERIFY_FAIL);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(VERIFY_FAIL);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
