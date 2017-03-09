package com.example.administrator.chenyipengxutil3work.application;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/3/8.
 */

//初始化Xutil
public class Mapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Xutil 外部初始化
        x.Ext.init(this);
        //为了更好的体验,不输出日志
        x.Ext.setDebug(false);
    }
}
