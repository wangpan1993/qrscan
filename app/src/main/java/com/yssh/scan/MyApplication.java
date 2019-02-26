package com.yssh.scan;

import android.app.Application;
import android.os.Environment;

import com.lzy.okgo.OkGo;
import com.lzy.okserver.OkDownload;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化OkGo
        OkGo.getInstance().init(this);

        String path = Environment.getExternalStorageDirectory().getPath() + "/download";
        OkDownload.getInstance().setFolder(path);
    }
}
