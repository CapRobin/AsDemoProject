package com.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.R;
import com.demo.adapter.MenuListAdapter;
import com.demo.entity.ChildInfo;
import com.demo.entity.ListMenu;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright © CapRobin
 *
 * Name：XutilDbActivity
 * Describe：xUtils3数据库详解
 * Date：2018-09-25 17:19:41
 * Author: CapRobin@yeah.net
 *
 */
@ContentView(R.layout.activity_xutils_db)
public class XutilDbActivity extends BaseActivity {
    @ViewInject(R.id.title_back_igv)
    private ImageView title_back_igv;
    @ViewInject(R.id.title_tv)
    private TextView title_tv;
    @ViewInject(R.id.title_right_igv)
    private ImageView title_right_igv;
    @ViewInject(R.id.dbMenuList)
    private ListView dbMenuList;
    @ViewInject(R.id.scrollView)
    private ScrollView scrollView;
    @ViewInject(R.id.showText)
    private TextView showText;
    //    //列表Item图标
//    private int[] icons = null;
    //列表Item标题
    private String[] titles = null;
    //列表Item介绍
    private String[] intros = null;

    private List<ListMenu> menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        //初始化配置
        initView();
    }

    /**
     * Describe：初始化配置
     * Params:
     * Return:
     * Date：2018-09-25 15:50:22
     */
    private void initView() {
        title_back_igv.setVisibility(View.VISIBLE);
        title_tv.setText("XUtils3数据库使用");
        title_back_igv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        //列表Item图标
//        icons = new int[]{R.mipmap.ic_launcher};
        //列表Item标题
        titles = new String[]{
                "创建数据库",
                "删除数据库",
                "删除数据表",
                "新增表数据",
                "删除表中全部数据",
                "根据条件删除表数据",
                "修改表数据",
                "查询表数据"
        };
        //列表Item介绍
        intros = new String[]{"案例说明"};
        menuList = new ArrayList<ListMenu>();

        //循环加载列表项目
        for (int i = 0; i < titles.length; i++) {
            ListMenu menu = new ListMenu();
//            menu.setIcon(icons[0]);
            menu.setTitle(titles[i]);
            menu.setIntro(intros[0]);
            menuList.add(menu);
        }
        MenuListAdapter menuListAdapter = new MenuListAdapter(this, menuList, dbMenuList);
        dbMenuList.setAdapter(menuListAdapter);
        dbMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(db != null){
                    try {
                        switch (position) {
                            case 0:
                                //创建数据库
                                addData();
                                break;
                            case 1:
                                //删除数据库
                                db.dropDb();
                                break;
                            case 2:
                                //删除表
                                db.dropTable(ChildInfo.class);
                                break;
                            case 3:
                                //新增表中数据
                                insertData();
                                break;
                            case 4:
                                //删除表中全部数据
                                deleteDataInfo(1);
                                break;
                            case 5:
                                //根据条件删除表中数据
                                deleteDataInfo(2);
                                break;
                            case 6:
                                //修改表中的数据
                                updateDataInfo();
                                break;
                            case 7:
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
        });

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
            ChildInfo childInfo = new ChildInfo("XinZeng", "11", "111");
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
        loading.setDialogLable("数据查询中...");
        loading.show();
        try {
            //查询数据库表中第一条数据
            ChildInfo first2 = null;
            first2 = db.findFirst(ChildInfo.class);
//            Log.i("JAVA", first2.toString());
            //添加查询条件进行查询
            //List<ChildInfo> all = db.selector(ChildInfo.class).where("id", ">", 2).and("id", "<", 4).findAll();
            List<ChildInfo> queryDataList = db.selector(ChildInfo.class).findAll();
            if(queryDataList.size() > 0){
                scrollView.setVisibility(View.VISIBLE);
                String showTextStr = "";
                for (int i = 0; i < queryDataList.size(); i++) {
                    showTextStr = showTextStr + "ID:" + queryDataList.get(i).getId() + "\n";
                    showTextStr = showTextStr + "Name：" + queryDataList.get(i).getcName() + "\n";
                    showTextStr = showTextStr + "Age：" + queryDataList.get(i).getcAge() + "\n";
                    showTextStr = showTextStr + "Number：" + queryDataList.get(i).getcNum() + "\n";
                    showTextStr = showTextStr + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n";
                }
                showText.setText(showTextStr);
                System.out.println("查询数据结果为：" + showTextStr);
            }else {
                showText.setText("");
                scrollView.setVisibility(View.GONE);
                setToast("数据表中没有数据");
            }
        } catch (DbException e) {
            e.printStackTrace();
            setToast(e.getMessage());
        }finally {
            loading.dismiss();
        }
    }
}

