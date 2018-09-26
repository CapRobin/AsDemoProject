package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.andbase.demo.R;
import com.andbase.demo.global.MyApplication;
import com.andbase.library.app.base.AbBaseActivity;
import com.andbase.library.util.AbDateUtil;
import com.andbase.library.util.AbDialogUtil;
import com.andbase.library.util.AbStrUtil;
import com.andbase.library.view.wheel.AbNumericWheelAdapter;
import com.andbase.library.view.wheel.AbStringWheelAdapter;
import com.andbase.library.view.wheel.AbWheelUtil;
import com.andbase.library.view.wheel.AbWheelView;

public class WheelActivity extends AbBaseActivity {
	private MyApplication application;
	private View ymdView = null;
	private View mdhmView = null;
	private View hmView = null;
	private View numberView = null;
    private View stringView = null;

	private TextView ymdTextView = null;
	private TextView mdhmTextView = null;
	private TextView hmTextView = null;
	private TextView numberTextView = null;
    private TextView stringTextView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		toolbar.setTitle(R.string.title_wheel);
		toolbar.setContentInsetsRelative(0, 0);
		toolbar.setNavigationIcon(R.drawable.ic_back);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		application = (MyApplication) this.getApplication();

        ymdTextView = (TextView)findViewById(R.id.text_value_1);
        mdhmTextView = (TextView)findViewById(R.id.text_value_2);
        hmTextView = (TextView)findViewById(R.id.text_value_3);
        numberTextView = (TextView)findViewById(R.id.text_value_4);
        stringTextView = (TextView)findViewById(R.id.text_value_5);

        ymdTextView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
                ymdView = View.inflate(WheelActivity.this,R.layout.view_choose_three, null);
                initYMDWheel(ymdView,ymdTextView);
				AbDialogUtil.showDialog(ymdView,Gravity.BOTTOM);
			}

		});

        mdhmTextView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mdhmView = View.inflate(WheelActivity.this,R.layout.view_choose_three, null);
                initMDHMWheel(mdhmView,mdhmTextView);
				AbDialogUtil.showDialog(mdhmView,Gravity.BOTTOM);
			}

		});

        hmTextView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				hmView = View.inflate(WheelActivity.this,R.layout.view_choose_two, null);
				initHMWheel(hmView,hmTextView);
				AbDialogUtil.showDialog(hmView,Gravity.BOTTOM);
			}

		});

        numberTextView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
                numberView = View.inflate(WheelActivity.this,R.layout.view_choose_one, null);
                initWheelNumber(numberView);
				AbDialogUtil.showDialog(numberView,Gravity.BOTTOM);
			}

		});

        stringTextView.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                stringView = View.inflate(WheelActivity.this,R.layout.view_choose_one, null);
                initWheelText(stringView);
                AbDialogUtil.showDialog(stringView,Gravity.BOTTOM);
            }

        });

    }

    public void initWheelText(final View view){
        List<String> list = new ArrayList<String>();
        list.add("绿色");
        list.add("红色");
        list.add("蓝色");
        list.add("白色");
        list.add("黑色");


        final AbWheelView mWheelView1 = (AbWheelView)view.findViewById(R.id.wheelView1);
        mWheelView1.setAdapter(new AbStringWheelAdapter(list));
        // 可循环滚动
        mWheelView1.setCyclic(true);
        // 添加文字
        mWheelView1.setLabel("");
        // 初始化时显示的数据
        mWheelView1.setCurrentItem(2);
        mWheelView1.setValueTextSize(35);
        mWheelView1.setLabelTextSize(35);
        mWheelView1.setLabelTextColor(0x80000000);
        //mWheelView1.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

        Button okBtn = (Button)view.findViewById(R.id.okBtn);
        Button cancelBtn = (Button)view.findViewById(R.id.cancelBtn);
        okBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                AbDialogUtil.removeDialog(view.getContext());
                int index = mWheelView1.getCurrentItem();
                String val = mWheelView1.getAdapter().getItem(index);
                stringTextView.setText(val);
            }

        });

        cancelBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                AbDialogUtil.removeDialog(view.getContext());
            }

        });
    }

    public void initWheelNumber(final View view){
    	final AbWheelView mWheelView1 = (AbWheelView)view.findViewById(R.id.wheelView1);
		mWheelView1.setAdapter(new AbNumericWheelAdapter(40, 190));
		// 可循环滚动
		mWheelView1.setCyclic(true);
		// 添加文字
		mWheelView1.setLabel("单位");
		// 初始化时显示的数据
		mWheelView1.setCurrentItem(40);
		mWheelView1.setValueTextSize(35);
		mWheelView1.setLabelTextSize(35);
		mWheelView1.setLabelTextColor(0x80000000);
		//mWheelView1.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		Button okBtn = (Button)view.findViewById(R.id.okBtn);
		Button cancelBtn = (Button)view.findViewById(R.id.cancelBtn);
		okBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
                AbDialogUtil.removeDialog(view.getContext());
				int index = mWheelView1.getCurrentItem();
				String val = mWheelView1.getAdapter().getItem(index);
                numberTextView.setText(val);
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
                AbDialogUtil.removeDialog(view.getContext());
			}

		});
    }

    /**
     * 年月日
     * @param view
     * @param textView
     */
    public void initYMDWheel(final View view,final TextView textView){

        //年月日时间选择器
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DATE);
        String date =  textView.getText().toString().trim();
        if(!AbStrUtil.isEmpty(date)){
            Date dateNew = AbDateUtil.getDateByFormat(date, AbDateUtil.dateFormatYMD);
            if(dateNew!=null){
                year = 1900+dateNew.getYear();
                month = dateNew.getMonth()+1;
                day = dateNew.getDate();
            }
        }

        final AbWheelView mWheelViewY = (AbWheelView)view.findViewById(R.id.wheelView1);
        final AbWheelView mWheelViewM = (AbWheelView)view.findViewById(R.id.wheelView2);
        final AbWheelView mWheelViewD = (AbWheelView)view.findViewById(R.id.wheelView3);

        //mWheelViewY.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
        //mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
        //mWheelViewD.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
        AbWheelUtil.initWheelPickerYMD(mWheelViewY, mWheelViewM, mWheelViewD, year,month,day, year-10, year+10);

        Button okBtn = (Button)view.findViewById(R.id.okBtn);
        Button cancelBtn = (Button)view.findViewById(R.id.cancelBtn);
        okBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                AbDialogUtil.removeDialog(view.getContext());

                int indexYear = mWheelViewY.getCurrentItem();
                String year = mWheelViewY.getAdapter().getItem(indexYear);

                int indexMonth = mWheelViewM.getCurrentItem();
                String month = mWheelViewM.getAdapter().getItem(indexMonth);

                int indexDay = mWheelViewD.getCurrentItem();
                String day = mWheelViewD.getAdapter().getItem(indexDay);
                textView.setText(AbStrUtil.dateTimeFormat(year+"-"+month+"-"+day));
            }

        });

        cancelBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                AbDialogUtil.removeDialog(view.getContext());
            }

        });


    }

	/**
	 * 月日时分
	 * @param view
	 * @param textView
     */
    public void initMDHMWheel(final View view,final TextView textView){
    	final AbWheelView mWheelViewMD = (AbWheelView)view.findViewById(R.id.wheelView1);
        final AbWheelView mWheelViewHH = (AbWheelView)view.findViewById(R.id.wheelView2);
		final AbWheelView mWheelViewMM = (AbWheelView)view.findViewById(R.id.wheelView3);

		Button okBtn = (Button)view.findViewById(R.id.okBtn);
		Button cancelBtn = (Button)view.findViewById(R.id.cancelBtn);
		//mWheelViewMD.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		//mWheelViewMM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		//mWheelViewHH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		AbWheelUtil.initWheelPickerMDHM(mWheelViewMD, mWheelViewMM, mWheelViewHH,2016,8,29,17,24);

        okBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                AbDialogUtil.removeDialog(view.getContext());
                int index1 = mWheelViewMD.getCurrentItem();
                int index2 = mWheelViewHH.getCurrentItem();
                int index3 = mWheelViewMM.getCurrentItem();

                String dmStr =  AbWheelUtil.MDHMList.get(index1);
                Calendar calendar = Calendar.getInstance();
                int second = calendar.get(Calendar.SECOND);
                String val = AbStrUtil.dateTimeFormat(dmStr+" "+index2+":"+index3+":"+second) ;
                textView.setText(val);
            }

        });

        cancelBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                AbDialogUtil.removeDialog(view.getContext());
            }

        });


    }

	/**
	 * 时分
	 * @param view
	 * @param textView
     */
    public void initHMWheel(final View view,final TextView textView){
		final AbWheelView mWheelViewHH = (AbWheelView)view.findViewById(R.id.wheelView1);
		final AbWheelView mWheelViewMM = (AbWheelView)view.findViewById(R.id.wheelView2);
		Button okBtn = (Button)view.findViewById(R.id.okBtn);
		Button cancelBtn = (Button)view.findViewById(R.id.cancelBtn);
		//mWheelViewMM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		//mWheelViewHH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
        //
        AbWheelUtil.initWheelPickerHM(mWheelViewHH,mWheelViewMM,17,25);

        okBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                AbDialogUtil.removeDialog(view.getContext());
                int index1 = mWheelViewHH.getCurrentItem();
                int index2 = mWheelViewMM.getCurrentItem();
                String val = AbStrUtil.dateTimeFormat(index1+":"+index2) ;
                textView.setText(val);
            }

        });

        cancelBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                AbDialogUtil.removeDialog(view.getContext());
            }

        });
    }




}


