package com.andbase.demo.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andbase.demo.R;
import com.andbase.demo.db.sdcard.DBSDHelper;
import com.andbase.demo.model.LocalUser;
import com.andbase.demo.model.Phone;
import com.andbase.demo.model.Stock;
import com.andbase.library.app.base.AbBaseActivity;
import com.andbase.library.db.orm.dao.AbDBDao;
import com.andbase.library.db.orm.dao.AbDBDaoImpl;
import com.andbase.library.util.AbJsonUtil;

import java.util.ArrayList;
import java.util.List;

public class DBActivity extends AbBaseActivity {

    //定义数据库操作实现类
    private AbDBDao dao = null;
    private TextView resultData = null;
    private List<Stock> stocks = new ArrayList<Stock>();

    private long id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_db);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化数据库操作实现类
        dao = new AbDBDaoImpl(DBSDHelper.getInstance(this),LocalUser.class);


        Button insertBtn  = (Button)this.findViewById(R.id.insertBtn);
        Button queryBtn  = (Button)this.findViewById(R.id.queryBtn);
        Button deleteBtn  = (Button)this.findViewById(R.id.deleteBtn);
        Button clearBtn  = (Button)this.findViewById(R.id.clearBtn);

        Button clearScreenBtn  = (Button)this.findViewById(R.id.clearScreenBtn);

        resultData  = (TextView)this.findViewById(R.id.resultData);

        //测试数据
        Phone phone = new Phone();
        phone.setName("华为C199");
        phone.setDesc("Android 手机");

        final LocalUser mLocalUser  = new LocalUser();
        mLocalUser.setUserId("100");
        mLocalUser.setName("我是100");
        mLocalUser.setPhone(phone);

        final Stock stock1 = new Stock();
        stock1.setText1("100的桌子");

        final Stock stock2 = new Stock();
        stock2.setText1("100的衣服");

        stocks.add(stock1);
        stocks.add(stock2);

        mLocalUser.setStocks(stocks);

        insertBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //显示插入的数据
                resultData.append("插入数据：表local_user");
                resultData.append(AbJsonUtil.toJson(mLocalUser)+"\n");
                resultData.append("---------------------------------------\n");

                //保存
                //(1)获取数据库
                dao.startWritableDatabase(false);
                //(2)执行查询
                id = dao.insert(mLocalUser);
                resultData.append("保存的数据的ID为："+id+"\n");
                //(3)关闭数据库
                dao.closeDatabase();

            }
        });

        queryBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                queryData();
            }
        });


        clearBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //保存
                //(1)获取数据库
                dao.startWritableDatabase(false);
                //(2)执行查询
                dao.deleteAll();
                //(3)关闭数据库
                dao.closeDatabase();

                resultData.setText("");
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //保存
                //(1)获取数据库
                dao.startWritableDatabase(false);
                //(2)执行查询
                dao.delete((int)id);
                //(3)关闭数据库
                dao.closeDatabase();

                resultData.setText("");
            }
        });

        clearScreenBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultData.setText("");
            }
        });

    }

    public void queryData(){
        //查询出结果检查是否成功了
        dao.startReadableDatabase();
        List<LocalUser>  mLocalUserList = dao.queryList();
        dao.closeDatabase();

        if(mLocalUserList==null || mLocalUserList.size()==0){
            resultData.append("查询数据：无数据");
        }else{
            resultData.append("查询数据：\n");
            resultData.append(AbJsonUtil.toJson(mLocalUserList)+"\n");
            resultData.append("---------------------------------------\n");
        }
    }

}

