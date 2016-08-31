package com.androidtalk.entity;

import android.database.Cursor;

import java.io.Serializable;

/**
 * 设置命令行时的命令实体
 */
public class Command implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String cmdName;
    private String cmdCategory;
    private String relation;

    public Command() {
    }

    /**
     * 构造函数
     * @param cur
     */
    public Command(Cursor cur) {
        this.id = cur.getString(0);
        this.cmdName = cur.getString(1);
        this.cmdCategory = cur.getString(2);
        this.relation = cur.getString(3);
    }

    public Command(String id, String cmdName, String cmdCategory, String relation) {
        this.id = id;
        this.cmdName = cmdName;
        this.cmdCategory = cmdCategory;
        this.relation = relation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public String getCmdCategory() {
        return cmdCategory;
    }

    public void setCmdCategory(String cmdCategory) {
        this.cmdCategory = cmdCategory;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return id + " " + cmdName + " " + cmdCategory + " " + relation;
    }
}
