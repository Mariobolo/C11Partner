package com.c11partner.desktop;

import android.app.Application;
import android.util.Log;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * C11Partner 应用入口
 * 负责初始化腾讯X5内核
 */
public class App extends Application {
    private static final String TAG = "C11Partner";

    @Override
    public void onCreate() {
        super.onCreate();
        initX5();
    }

    /**
     * 初始化腾讯X5内核
     * 异步加载，首次启动时若本地无内核会自动下载
     */
    private void initX5() {
        // 设置TBS下载监听
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int code) {
                Log.d(TAG, "X5内核下载完成, code=" + code);
            }

            @Override
            public void onInstallFinish(int code) {
                Log.d(TAG, "X5内核安装完成, code=" + code);
            }

            @Override
            public void onDownloadProgress(int progress) {
                Log.d(TAG, "X5内核下载进度: " + progress + "%");
            }
        });

        // 初始化X5环境
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.d(TAG, "X5核心初始化完成");
            }

            @Override
            public void onViewInitFinished(boolean isX5) {
                Log.d(TAG, "X5视图初始化完成, 是否使用X5内核: " + isX5);
            }
        });
    }
}
