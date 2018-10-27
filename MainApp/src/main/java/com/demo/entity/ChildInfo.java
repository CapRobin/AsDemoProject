package com.demo.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * onCreated = "sql"：当第一次创建表需要插入数据时候在此写sql语句
 */
@Table(name = "child_info", onCreated = "")
public class ChildInfo {
    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;
    @Column(name = "c_name")
    private String cName;
    @Column(name = "c_age")
    private String cAge;
    @Column(name = "c_number")
    private String cNumber;
    @Column(name = "c_nickname")
    private String cNickname;

    public String getcAge() {
        return cAge;
    }

    public void setcAge(String cAge) {
        this.cAge = cAge;
    }

    public String getcNum() {
        return cNumber;
    }

    public void setcNum(String cNum) {
        this.cNumber = cNum;
    }

    public String getcNickname() {
        return cNickname;
    }

    public void setcNickname(String cNname) {
        this.cNickname = cNname;
    }


    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public ChildInfo() {
    }

    public ChildInfo(String cName,String cAge,String cNumber,String cNname) {
        this.cName = cName;
        this.cAge = cAge;
        this.cNumber = cNumber;
        this.cNickname = cNname;
    }
//    public ChildInfo(String cName) {
//        this.cName = cName;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Override
    public String toString() {
        return "ChildInfo{" + "id=" + id + ",cName='" + cName + '\'' + '}';
    }
}