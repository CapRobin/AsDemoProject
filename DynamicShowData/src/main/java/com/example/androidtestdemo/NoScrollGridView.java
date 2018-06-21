package com.example.androidtestdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;


/**
* Created by IntelliJ IDEA.
* User: zhUser
* Date: 13-1-24
* Time: 下午6:53
*/
public class NoScrollGridView extends GridView{

     public NoScrollGridView(Context context, AttributeSet attrs){
          super(context, attrs);
     }

     public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
          int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
          super.onMeasure(widthMeasureSpec, mExpandSpec);
     }
}
/**
* Created by IntelliJ IDEA.
* User: zhUser
* Date: 13-1-24
* Time: 下午6:53
*/
class NoScrollListView extends ListView{

     public NoScrollListView(Context context, AttributeSet attrs){
          super(context, attrs);
     }

     public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
          int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
          super.onMeasure(widthMeasureSpec, mExpandSpec);
     }
}