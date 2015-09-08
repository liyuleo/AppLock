package com.leo.applock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

/**
 * 手势密码路径绘制
 * 
 */
public class GestureDrawline extends View {
	private int mPositionX;// 声明起点坐标
	private int mPositionY;
	private Paint mPaint;// 声明画笔
	private Canvas mCanvas;// 画布
	private Bitmap mBitmap;// 位图
	private List<GesturePoint> mList;// 装有各个view坐标的集合
	private List<Pair<GesturePoint, GesturePoint>> mLineList;// 记录画过的线
	private Map<String, GesturePoint> mAutoCheckPointMap;// 自动选中的情况点
	private boolean isDrawEnable = true; // 是否允许绘制

	/**
	 * 屏幕的宽度和高度
	 */
	private int[] mScreenDispaly;

	/**
	 * 手指当前在哪个Point内
	 */
	private GesturePoint mCurrentPoint;
	/**
	 * 用户绘图的回调
	 */
	private GestureCallBack mCallBack;

	/**
	 * 用户当前绘制的图形密码
	 */
	private StringBuilder mPassWordSb;

	/**
	 * 是否为校验
	 */
	private boolean isVerify;

	/**
	 * 用户传入的passWord
	 */
	private String mPassWord;

	public GestureDrawline(Context context, List<GesturePoint> list, boolean isVerify, String passWord,
			GestureCallBack callBack) {
		super(context);
		mScreenDispaly = AppUtils.getScreenDispaly(context);
		mPaint = new Paint(Paint.DITHER_FLAG);// 创建一个画笔
		mBitmap = Bitmap.createBitmap(mScreenDispaly[0], mScreenDispaly[0], Bitmap.Config.ARGB_8888); // 设置位图的宽高
		mCanvas = new Canvas();
		mCanvas.setBitmap(mBitmap);
		mPaint.setStyle(Style.STROKE);// 设置非填充
		mPaint.setStrokeWidth(10);// 笔宽5像素
		mPaint.setColor(Color.rgb(245, 142, 33));// 设置默认连线颜色
		mPaint.setAntiAlias(true);// 不显示锯齿

		this.mList = list;
		this.mLineList = new ArrayList<Pair<GesturePoint, GesturePoint>>();

		initAutoCheckPointMap();
		this.mCallBack = callBack;

		// 初始化密码缓存
		this.isVerify = isVerify;
		this.mPassWordSb = new StringBuilder();
		this.mPassWord = passWord;
	}

	private void initAutoCheckPointMap() {
		mAutoCheckPointMap = new HashMap<String, GesturePoint>();
		mAutoCheckPointMap.put("1,3", getGesturePointByNum(2));
		mAutoCheckPointMap.put("1,7", getGesturePointByNum(4));
		mAutoCheckPointMap.put("1,9", getGesturePointByNum(5));
		mAutoCheckPointMap.put("2,8", getGesturePointByNum(5));
		mAutoCheckPointMap.put("3,7", getGesturePointByNum(5));
		mAutoCheckPointMap.put("3,9", getGesturePointByNum(6));
		mAutoCheckPointMap.put("4,6", getGesturePointByNum(5));
		mAutoCheckPointMap.put("7,9", getGesturePointByNum(8));
	}

	private GesturePoint getGesturePointByNum(int num) {
		for (GesturePoint point : mList) {
			if (point.getNum() == num) {
				return point;
			}
		}
		return null;
	}

	// 画位图
	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	// 触摸事件
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isDrawEnable == false) {
			// 当期不允许绘制
			return true;
		}
		mPaint.setColor(Color.rgb(245, 142, 33));// 设置默认连线颜色
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mPositionX = (int) event.getX();
			mPositionY = (int) event.getY();
			// 判断当前点击的位置是处于哪个点之内
			mCurrentPoint = getPointAt(mPositionX, mPositionY);
			if (mCurrentPoint != null) {
				mCurrentPoint.setPointState(GesturePoint.POINT_STATE_SELECTED);
				mPassWordSb.append(mCurrentPoint.getNum());
			}
			// canvas.drawPoint(mov_x, mov_y, paint);// 画点
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			clearScreenAndDrawList();

			// 得到当前移动位置是处于哪个点内
			GesturePoint pointAt = getPointAt((int) event.getX(), (int) event.getY());
			// 代表当前用户手指处于点与点之前
			if (mCurrentPoint == null && pointAt == null) {
				return true;
			} else {// 代表用户的手指移动到了点上
				if (mCurrentPoint == null) {// 先判断当前的point是不是为null
					// 如果为空，那么把手指移动到的点赋值给currentPoint
					mCurrentPoint = pointAt;
					// 把currentPoint这个点设置选中为true;
					mCurrentPoint.setPointState(GesturePoint.POINT_STATE_SELECTED);
					mPassWordSb.append(mCurrentPoint.getNum());
				}
			}
			if (pointAt == null || mCurrentPoint.equals(pointAt)
					|| GesturePoint.POINT_STATE_SELECTED == pointAt.getPointState()) {
				// 点击移动区域不在圆的区域，或者当前点击的点与当前移动到的点的位置相同，或者当前点击的点处于选中状态
				// 那么以当前的点中心为起点，以手指移动位置为终点画线
				mCanvas.drawLine(mCurrentPoint.getCenterX(), mCurrentPoint.getCenterY(), event.getX(), event.getY(),
						mPaint);// 画线
			} else {
				// 如果当前点击的点与当前移动到的点的位置不同
				// 那么以前前点的中心为起点，以手移动到的点的位置画线
				mCanvas.drawLine(mCurrentPoint.getCenterX(), mCurrentPoint.getCenterY(), pointAt.getCenterX(),
						pointAt.getCenterY(), mPaint);// 画线
				pointAt.setPointState(GesturePoint.POINT_STATE_SELECTED);

				// 判断是否中间点需要选中
				GesturePoint betweenPoint = getBetweenCheckPoint(mCurrentPoint, pointAt);
				if (betweenPoint != null && GesturePoint.POINT_STATE_SELECTED != betweenPoint.getPointState()) {
					// 存在中间点并且没有被选中
					Pair<GesturePoint, GesturePoint> pair1 = new Pair<GesturePoint, GesturePoint>(mCurrentPoint,
							betweenPoint);
					mLineList.add(pair1);
					mPassWordSb.append(betweenPoint.getNum());
					Pair<GesturePoint, GesturePoint> pair2 = new Pair<GesturePoint, GesturePoint>(betweenPoint,
							pointAt);
					mLineList.add(pair2);
					mPassWordSb.append(pointAt.getNum());
					// 设置中间点选中
					betweenPoint.setPointState(GesturePoint.POINT_STATE_SELECTED);
					// 赋值当前的point;
					mCurrentPoint = pointAt;
				} else {
					Pair<GesturePoint, GesturePoint> pair = new Pair<GesturePoint, GesturePoint>(mCurrentPoint,
							pointAt);
					mLineList.add(pair);
					mPassWordSb.append(pointAt.getNum());
					// 赋值当前的point;
					mCurrentPoint = pointAt;
				}
			}
			invalidate();
			break;
		case MotionEvent.ACTION_UP:// 当手指抬起的时候
			if (isVerify) {
				// 手势密码校验
				// 清掉屏幕上所有的线，只画上集合里面保存的线
				if (mPassWord.equals(mPassWordSb.toString())) {
					// 代表用户绘制的密码手势与传入的密码相同
					mCallBack.checkedSuccess();
				} else {
					// 用户绘制的密码与传入的密码不同。
					mCallBack.checkedFail();
				}
			} else {
				mCallBack.onGestureCodeInput(mPassWordSb.toString());
			}
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 指定时间去清除绘制的状态
	 * 
	 * @param delayTime
	 *            延迟执行时间
	 */
	public void clearDrawlineState(long delayTime) {
		if (delayTime > 0) {
			// 绘制红色提示路线
			isDrawEnable = false;
			drawErrorPathTip();
		}
		new Handler().postDelayed(new clearStateRunnable(), delayTime);
	}

	/**
	 * 清除绘制状态的线程
	 */
	final class clearStateRunnable implements Runnable {
		public void run() {
			// 重置passWordSb
			mPassWordSb = new StringBuilder();
			// 清空保存点的集合
			mLineList.clear();
			// 重新绘制界面
			clearScreenAndDrawList();
			for (GesturePoint p : mList) {
				p.setPointState(GesturePoint.POINT_STATE_NORMAL);
			}
			invalidate();
			isDrawEnable = true;
		}
	}

	/**
	 * 通过点的位置去集合里面查找这个点是包含在哪个Point里面的
	 * 
	 * @param x
	 * @param y
	 * @return 如果没有找到，则返回null，代表用户当前移动的地方属于点与点之间
	 */
	private GesturePoint getPointAt(int x, int y) {

		for (GesturePoint point : mList) {
			// 先判断x
			int leftX = point.getLeftX();
			int rightX = point.getRightX();
			if (!(x >= leftX && x < rightX)) {
				// 如果为假，则跳到下一个对比
				continue;
			}

			int topY = point.getTopY();
			int bottomY = point.getBottomY();
			if (!(y >= topY && y < bottomY)) {
				// 如果为假，则跳到下一个对比
				continue;
			}

			// 如果执行到这，那么说明当前点击的点的位置在遍历到点的位置这个地方
			return point;
		}

		return null;
	}

	private GesturePoint getBetweenCheckPoint(GesturePoint pointStart, GesturePoint pointEnd) {
		int startNum = pointStart.getNum();
		int endNum = pointEnd.getNum();
		String key = null;
		if (startNum < endNum) {
			key = startNum + "," + endNum;
		} else {
			key = endNum + "," + startNum;
		}
		return mAutoCheckPointMap.get(key);
	}

	/**
	 * 清掉屏幕上所有的线，然后画出集合里面的线
	 */
	private void clearScreenAndDrawList() {
		mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		for (Pair<GesturePoint, GesturePoint> pair : mLineList) {
			mCanvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(), pair.second.getCenterX(),
					pair.second.getCenterY(), mPaint);// 画线
		}
	}

	/**
	 * 校验错误/两次绘制不一致提示
	 */
	private void drawErrorPathTip() {
		mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		mPaint.setColor(Color.rgb(154, 7, 21));// 设置默认线路颜色
		for (Pair<GesturePoint, GesturePoint> pair : mLineList) {
			pair.first.setPointState(GesturePoint.POINT_STATE_WRONG);
			pair.second.setPointState(GesturePoint.POINT_STATE_WRONG);
			mCanvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(), pair.second.getCenterX(),
					pair.second.getCenterY(), mPaint);// 画线
		}
		invalidate();
	}

	public interface GestureCallBack {

		/**
		 * 用户设置/输入了手势密码
		 */
		public abstract void onGestureCodeInput(String inputCode);

		/**
		 * 代表用户绘制的密码与传入的密码相同
		 */
		public abstract void checkedSuccess();

		/**
		 * 代表用户绘制的密码与传入的密码不相同
		 */
		public abstract void checkedFail();
	}

}
