package com.leo.applock;

import android.widget.ImageView;

public class GesturePoint {
	public static final int POINT_STATE_NORMAL = 0; // 正常状态
	public static final int POINT_STATE_SELECTED = 1; // 按下状态
	public static final int POINT_STATE_WRONG = 2; // 错误状态
	/**
	 * 左边x的值
	 */
	private int mLeftX;
	/**
	 * 右边x的值
	 */
	private int mRightX;
	/**
	 * 上边y的值
	 */
	private int mTopY;
	/**
	 * 下边y的值
	 */
	private int mBottomY;
	/**
	 * 这个点对应的ImageView控件
	 */
	private ImageView mImage;

	/**
	 * 中心x值
	 */
	private int mCenterX;

	/**
	 * 中心y值
	 */
	private int mCenterY;

	/**
	 * 状态值
	 */
	private int mPointState;

	/**
	 * 代表这个Point对象代表的数字，从1开始
	 */
	private int mNum;

	public GesturePoint(int leftX, int rightX, int topY, int bottomY, ImageView image, int num) {
		super();
		this.mLeftX = leftX;
		this.mRightX = rightX;
		this.mTopY = topY;
		this.mBottomY = bottomY;
		this.mImage = image;

		this.mCenterX = (leftX + rightX) / 2;
		this.mCenterY = (topY + bottomY) / 2;

		this.mNum = num;
	}

	public int getLeftX() {
		return mLeftX;
	}

	public void setLeftX(int leftX) {
		this.mLeftX = leftX;
	}

	public int getRightX() {
		return mRightX;
	}

	public void setRightX(int rightX) {
		this.mRightX = rightX;
	}

	public int getTopY() {
		return mTopY;
	}

	public void setTopY(int topY) {
		this.mTopY = topY;
	}

	public int getBottomY() {
		return mBottomY;
	}

	public void setBottomY(int bottomY) {
		this.mBottomY = bottomY;
	}

	public ImageView getImage() {
		return mImage;
	}

	public void setImage(ImageView image) {
		this.mImage = image;
	}

	public int getCenterX() {
		return mCenterX;
	}

	public void setCenterX(int centerX) {
		this.mCenterX = centerX;
	}

	public int getCenterY() {
		return mCenterY;
	}

	public void setCenterY(int centerY) {
		this.mCenterY = centerY;
	}

	public int getPointState() {
		return mPointState;
	}

	public void setPointState(int state) {
		mPointState = state;
		switch (state) {
		case POINT_STATE_NORMAL:
			this.mImage.setBackgroundResource(R.drawable.gesture_node_normal);
			break;
		case POINT_STATE_SELECTED:
			this.mImage.setBackgroundResource(R.drawable.gesture_node_pressed);
			break;
		case POINT_STATE_WRONG:
			this.mImage.setBackgroundResource(R.drawable.gesture_node_wrong);
			break;
		default:
			break;
		}
	}

	public int getNum() {
		return mNum;
	}

	public void setNum(int num) {
		this.mNum = num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mBottomY;
		result = prime * result + ((mImage == null) ? 0 : mImage.hashCode());
		result = prime * result + mLeftX;
		result = prime * result + mRightX;
		result = prime * result + mTopY;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GesturePoint other = (GesturePoint) obj;
		if (mBottomY != other.mBottomY)
			return false;
		if (mImage == null) {
			if (other.mImage != null)
				return false;
		} else if (!mImage.equals(other.mImage))
			return false;
		if (mLeftX != other.mLeftX)
			return false;
		if (mRightX != other.mRightX)
			return false;
		if (mTopY != other.mTopY)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [leftX=" + mLeftX + ", rightX=" + mRightX + ", topY=" + mTopY + ", bottomY=" + mBottomY + "]";
	}
}
