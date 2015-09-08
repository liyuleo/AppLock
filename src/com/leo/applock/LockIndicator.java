package com.leo.applock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * 手势密码图案提示
 * 
 */
public class LockIndicator extends View {
	private int mNumRow = 3; // 行
	private int mNumColum = 3; // 列
	private int mPatternWidth = 40;
	private int mPatternHeight = 40;
	private int mF = 5;
	private int mG = 5;
	private int mStrokeWidth = 3;
	private Paint mPaint = null;
	private Drawable mPatternNoraml = null;
	private Drawable mPatternPressed = null;
	private String mLockPassStr; // 手势密码

	public LockIndicator(Context paramContext) {
		super(paramContext);
	}

	@SuppressWarnings("deprecation")
	public LockIndicator(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet, 0);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(mStrokeWidth);
		mPaint.setStyle(Paint.Style.STROKE);
		mPatternNoraml = getResources().getDrawable(R.drawable.lock_pattern_node_normal);
		mPatternPressed = getResources().getDrawable(R.drawable.lock_pattern_node_pressed);
		if (mPatternPressed != null) {
			mPatternWidth = mPatternPressed.getIntrinsicWidth();
			mPatternHeight = mPatternPressed.getIntrinsicHeight();
			this.mF = (mPatternWidth / 4);
			this.mG = (mPatternHeight / 4);
			mPatternPressed.setBounds(0, 0, mPatternWidth, mPatternHeight);
			mPatternNoraml.setBounds(0, 0, mPatternWidth, mPatternHeight);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if ((mPatternPressed == null) || (mPatternNoraml == null)) {
			return;
		}
		// 绘制3*3的图标
		for (int i = 0; i < mNumRow; i++) {
			for (int j = 0; j < mNumColum; j++) {
				mPaint.setColor(-16777216);
				int i1 = j * mPatternHeight + j * this.mG;
				int i2 = i * mPatternWidth + i * this.mF;
				canvas.save();
				canvas.translate(i1, i2);
				String curNum = String.valueOf(mNumColum * i + (j + 1));
				if (!TextUtils.isEmpty(mLockPassStr)) {
					if (mLockPassStr.indexOf(curNum) == -1) {
						// 未选中
						mPatternNoraml.draw(canvas);
					} else {
						// 被选中
						mPatternPressed.draw(canvas);
					}
				} else {
					// 重置状态
					mPatternNoraml.draw(canvas);
				}
				canvas.restore();
			}
		}
	}

	@Override
	protected void onMeasure(int paramInt1, int paramInt2) {
		if (mPatternPressed != null)
			setMeasuredDimension(mNumColum * mPatternHeight + this.mG * (-1 + mNumColum),
					mNumRow * mPatternWidth + this.mF * (-1 + mNumRow));
	}

	/**
	 * 请求重新绘制
	 * 
	 * @param paramString
	 *            手势密码字符序列
	 */
	public void setPath(String paramString) {
		mLockPassStr = paramString;
		invalidate();
	}
}