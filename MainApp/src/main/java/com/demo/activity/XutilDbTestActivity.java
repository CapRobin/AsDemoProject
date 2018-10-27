package com.demo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.R;
import com.demo.entity.ChildInfo;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ContentView(R.layout.activity_xutil_sql)
public class XutilDbTestActivity extends BaseActivity {
    @ViewInject(R.id.title_back_igv)
    private ImageView title_back_igv;
    @ViewInject(R.id.title_tv)
    private TextView title_tv;
    @ViewInject(R.id.title_right_igv)
    private ImageView title_right_igv;

    @ViewInject(R.id.test_btn_01)
    private Button test_btn_01;
    @ViewInject(R.id.test_btn_02)
    private Button test_btn_02;
    @ViewInject(R.id.test_btn_03)
    private Button test_btn_03;
    @ViewInject(R.id.test_btn_04)
    private Button test_btn_04;
    @ViewInject(R.id.test_btn_0501)
    private Button test_btn_0501;
    @ViewInject(R.id.test_btn_0502)
    private Button test_btn_0502;
    @ViewInject(R.id.test_btn_06)
    private Button test_btn_06;
    @ViewInject(R.id.test_btn_07)
    private Button test_btn_07;
    @ViewInject(R.id.showText)
    private TextView showText;
    @ViewInject(R.id.scrollView)
    private ScrollView scrollView;

//    private DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
//            .setDbName("test.db")
//            // 不设置dbDir时, 默认存储在app的私有目录.
//            .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
//            .setDbVersion(2)
//            .setDbOpenListener(new DbManager.DbOpenListener() {
//                @Override
//                public void onDbOpened(DbManager db) {
//                    // 开启WAL, 对写入加速提升巨大
//                    db.getDatabase().enableWriteAheadLogging();
//                }
//            })
//            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
//                @Override
//                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
//                    // TODO: ...
//                    // db.addColumn(...);
//                    // db.dropTable(...);
//                    // ...
//                    // or
//                    // db.dropDb();
//                }
//            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        x.view().inject(this);
        initView();
    }

    /**
     * Describe：初始化配置
     * Params:
     * Return:
     * Date：2018-09-22 09:40:28
     */

    private void initView() {
        title_back_igv.setVisibility(View.VISIBLE);
        title_tv.setText("Xutil3数据库操作");

        //DbUtils db = DbUtils.create(this.getActivity(), "/sdcard/", "test.db");
//        DbUtils db = DbUtils.create(this.getActivity());
//        db.configAllowTransaction(true);
//        db.configDebug(true);
    }

    @Event(value = {R.id.title_back_igv, R.id.test_btn_01, R.id.test_btn_02, R.id.test_btn_03, R.id.test_btn_04, R.id.test_btn_0501, R.id.test_btn_0502, R.id.test_btn_06, R.id.test_btn_07})
    private void Click(View view) {
        if (db != null) {
            try {
                switch (view.getId()) {
                    case R.id.title_back_igv:
                        finish();
                        break;
                    case R.id.test_btn_01:
                        //创建数据库
                        addData();
                        break;
                    case R.id.test_btn_02:
                        //删除数据库
                        db.dropDb();
                        break;
                    case R.id.test_btn_03:
                        //删除表
                        db.dropTable(ChildInfo.class);
                        break;
                    case R.id.test_btn_04:
                        //新增表中数据
                        insertData();
                        break;
                    case R.id.test_btn_0501:
                        //删除表中全部数据
                        deleteDataInfo(1);
                        break;
                    case R.id.test_btn_0502:
                        //根据条件删除表中数据
                        deleteDataInfo(2);
                        break;
                    case R.id.test_btn_06:
                        //修改表中的数据
                        updateDataInfo();
                        break;
                    case R.id.test_btn_07:
                        //查询表中的数据
                        queryDataInfo();
                        break;
                    default:
                        break;
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Describe：
     * Params:
     * Return:
     * Date：2018-09-22 09:44:04
     */

    private void addData() {
        //用集合向child_info表中插入多条数据
        ArrayList<ChildInfo> childInfolist = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            ChildInfo childInfo = new ChildInfo();
            childInfo.setcName("name_" + (1 + i));
            childInfo.setcAge("age_" + (1 + i));
            childInfo.setcNum("number_" + (1 + i));
            childInfolist.add(childInfo);
        }
        for (ChildInfo childInfo : childInfolist) {
            try {
                //db.save()方法不仅可以插入单个对象，还能插入集合
                db.save(childInfo);
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Describe：新增表中数据
     * Params:
     * Return:
     * Date：2018-09-25 11:46:46
     */
    private void insertData() {
        try {
            ChildInfo childInfo = new ChildInfo("XinZeng", "11", "111", "1111");
            db.save(childInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * Describe：删除表中数据
     * Params:
     * Return:
     * Date：2018-09-25 11:49:18
     */
    private void deleteDataInfo(int type) {
        try {
            switch (type){
                case 1:
                    //第一种写法(child_info表中数据将被全部删除)：
                    db.delete(ChildInfo.class);
                    setToast("数据表全部数据已清空");
                    break;
                case 2:
                    //第二种写法，添加删除条件：
                    WhereBuilder wb = WhereBuilder.b();
                    //构造修改的条件
                    wb.and("rownum", "<", 10);
                    wb.and("rownum", "=", 10);
                    db.delete(ChildInfo.class, wb);
                    setToast("删除前10条数据成功！");
                    break;
                default:
                    break;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * Describe：修改表中的数据
     * Params:
     * Return:
     * Date：2018-09-25 11:51:06
     */
    private void updateDataInfo() {
        try {
            //第一种写法：
            ChildInfo first1 = db.findFirst(ChildInfo.class);
            first1.setcName("zhansan2");
            db.update(first1, "c_name"); //c_name：表中的字段名
            //第二种写法：
            WhereBuilder b2 = WhereBuilder.b();
            b2.and("id", "=", first1.getId()); //构造修改的条件
            KeyValue name = new KeyValue("c_name", "zhansan3");
            db.update(ChildInfo.class, b2, name);
            //第三种写法：
            first1.setcName("zhansan4");
            db.saveOrUpdate(first1);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * Describe：查询表中的数据
     * Params:
     * Return:
     * Date：2018-09-25 11:52:30
     */
    private void queryDataInfo() {
        try {
            //查询数据库表中第一条数据
            ChildInfo first2 = null;
            first2 = db.findFirst(ChildInfo.class);
//            Log.i("JAVA", first2.toString());
            //添加查询条件进行查询
            //List<ChildInfo> all = db.selector(ChildInfo.class).where("id", ">", 2).and("id", "<", 4).findAll();
            List<ChildInfo> all = db.selector(ChildInfo.class).findAll();
            if(all.size() > 0){
                scrollView.setVisibility(View.VISIBLE);
            String showTextStr = "";
            for (int i = 0; i < all.size(); i++) {
                showTextStr = showTextStr + "ID:" + all.get(i).getId() + "\n";
                showTextStr = showTextStr + "Name：" + all.get(i).getcName() + "\n";
                showTextStr = showTextStr + "Age：" + all.get(i).getcAge() + "\n";
                showTextStr = showTextStr + "Number：" + all.get(i).getcNum() + "\n";
                showTextStr = showTextStr + "~~~~~~~~~~~~~~\n";
            }
                System.out.println("查询数据结果为：" + showTextStr);
            showText.setText(showTextStr);
            }
//                        for (ChildInfo childInfo2 : all) {
//                            Log.i("JAVA", childInfo2.toString());
//                            System.out.println("获取数据为："+childInfo2.toString()+"\n");
//                        }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
