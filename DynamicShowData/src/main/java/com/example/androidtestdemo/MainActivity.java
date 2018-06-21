package com.example.androidtestdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.AnimatorListenerAdapter;
import com.actionbarsherlock.internal.nineoldandroids.animation.ValueAnimator;

public class MainActivity extends Activity implements OnClickListener {
	private View mHolder1, mHolder2, mHolder3;
	private EditText mEdit01 = null;
	private EditText mEdit02 = null;
	private Button testBtn = null;
	private NoScrollGridView gridView1, gridview2 = null;
	private List<String> myGridViewList1, myGridViewList2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	/**
	 *
	 * @Describe：初始化View
	 * @Throws:
	 * @Date：2014年8月19日 上午9:55:22
	 * @Version v1.0
	 */
	private void initView() {
		setContentView(R.layout.activity_main);
		mHolder1 = findViewById(R.id.holder1);
		mHolder2 = findViewById(R.id.holder2);
		mHolder3 = findViewById(R.id.holder3);
		mEdit01 = (EditText) findViewById(R.id.mEdit01);
		mEdit02 = (EditText) findViewById(R.id.mEdit02);
		testBtn = (Button) findViewById(R.id.testBtn);
		gridView1 = (NoScrollGridView) findViewById(R.id.gridview1);
		gridview2 = (NoScrollGridView) findViewById(R.id.gridview2);
		setData();

		mEdit01.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case KeyEvent.ACTION_DOWN:
						closeInputMethod();
						popViewisShow(1);
						break;
				}
				return false;
			}
		});

		mEdit02.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case KeyEvent.ACTION_DOWN:
						closeInputMethod();
						popViewisShow(2);
						break;
				}
				return false;
			}
		});

		// testButton.setOnClickListener(this);
		// ggButton.setOnClickListener(this);
		testBtn.setOnClickListener(this);
		mHolder2.setOnClickListener(this);
	}

	/**
	 *
	 * @Describe：构造View的数据
	 * @Throws:
	 * @Date：2014年8月19日 上午10:09:09
	 * @Version v1.0
	 */
	private void setData() {
		// 创建第一个GridView数据视图
		myGridViewList1 = new ArrayList<String>();
		// 创建第二个GridView数据视图
		myGridViewList2 = new ArrayList<String>();

		// 构造数据
		for (int i = 0; i < 20; i++) {
			myGridViewList1.add("Item_01_" + (i + 1));
		}
		for (int i = 0; i < 20; i++) {
			myGridViewList2.add("Item_02_" + (i + 1));
		}

		showView1(myGridViewList1, 1);
		showView1(myGridViewList2, 2);

	}

	/**
	 *
	 * @Describe：装载数据
	 * @param list
	 * @Throws:
	 * @Date：2014年8月20日 上午10:31:19
	 * @Version v1.0
	 */
	public void showView1(List<String> list, int viewId) {

		MyLocationAdapter locationAdapter = new MyLocationAdapter(this, list);
		switch (viewId) {
			case 1:
				gridView1.setSelector(new ColorDrawable(Color.TRANSPARENT));
				gridView1.setAdapter(locationAdapter);
				gridView1.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						String string = myGridViewList1.get(position);
						mEdit01.setText(string);
						popViewisShow(1);
						popViewisShow(2);
						mEdit02.setFocusable(true);
						mEdit02.requestFocus();
						mEdit02.performClick();
					}
				});
				break;
			case 2:
				gridview2.setSelector(new ColorDrawable(Color.TRANSPARENT));
				gridview2.setAdapter(locationAdapter);
				gridview2.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						String string = myGridViewList2.get(position);
						mEdit02.setText(string);
						popViewisShow(2);
						// ggButton.performClick();
					}
				});
				break;
			case 3:

				break;
		}
	}

	public static ValueAnimator createHeightAnimator(final View view, int start, int end) {
		ValueAnimator animator = ValueAnimator.ofInt(start, end);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int value = (Integer) valueAnimator.getAnimatedValue();

				ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
				layoutParams.height = value;
				view.setLayoutParams(layoutParams);
			}
		});
		// animator.setDuration(DURATION);
		return animator;
	}

	public static void animateExpanding(final View view) {
		view.setVisibility(View.VISIBLE);

		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(widthSpec, heightSpec);

		ValueAnimator animator = createHeightAnimator(view, 0, view.getMeasuredHeight());
		animator.start();
	}

	public static void animateCollapsing(final View view) {
		int origHeight = view.getHeight();

		ValueAnimator animator = createHeightAnimator(view, origHeight, 0);
		animator.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator animation) {
				view.setVisibility(View.GONE);
			};
		});
		animator.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.mEdit01:
				popViewisShow(1);
				break;
			case R.id.mEdit02:
				popViewisShow(2);
				break;
			case R.id.testBtn:
				Toast.makeText(MainActivity.this, "这是第二个测试Button", Toast.LENGTH_SHORT).show();
				break;
			case R.id.holder2:
				if (View.GONE == mHolder2.findViewById(R.id.hiddenview2).getVisibility()) {
					animateExpanding(mHolder2.findViewById(R.id.hiddenview2));
				} else {
					animateCollapsing(mHolder2.findViewById(R.id.hiddenview2));
				}
				break;
		}
	}

	/**
	 *
	 * @Describe：控制视图是否显示
	 * @Throws:
	 * @Date：2014年8月18日 上午10:39:33
	 * @Version v1.0
	 */
	private void popViewisShow(int id) {

		switch (id) {
			case 1:
				if (View.GONE == mHolder1.findViewById(R.id.hiddenview1).getVisibility()) {
					animateExpanding(mHolder1.findViewById(R.id.hiddenview1));
				} else {
					animateCollapsing(mHolder1.findViewById(R.id.hiddenview1));
				}
				break;
			case 2:
				if (View.GONE == mHolder3.findViewById(R.id.hiddenview3).getVisibility()) {
					animateExpanding(mHolder3.findViewById(R.id.hiddenview3));
				} else {
					animateCollapsing(mHolder3.findViewById(R.id.hiddenview3));
				}
				break;
		}
	}

	/**
	 *
	 * @Describe：关闭输入法
	 * @Throws:
	 * @Date：2014年8月20日 上午11:58:30
	 * @Version v1.0
	 */
	private void closeInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();

		// isOpen若返回true，则表示输入法打开
		if (isOpen) {
			imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}
}
