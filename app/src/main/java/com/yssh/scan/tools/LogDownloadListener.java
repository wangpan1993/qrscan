package com.yssh.scan.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.yssh.scan.R;
import com.yssh.scan.WebActivity;

import java.io.File;

public class LogDownloadListener extends DownloadListener {

    private final AlertDialog dialog;
    private final TextView tv_progress;
    private Context mContext;

    public LogDownloadListener(Context mContext) {
        super("okDownload");
        this.mContext = mContext;
        dialog = new AlertDialog.Builder(mContext).create();
        View view = View.inflate(mContext, R.layout.dialog_progress, null);
        dialog.setView(view);
        ImageView imageView = view.findViewById(R.id.iv_loading);
        tv_progress = view.findViewById(R.id.tv_progress);
        Glide.with(mContext).asGif().load(R.drawable.updata_loading).into(imageView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                OkDownload.getInstance().pauseAll();
            }
        });
//                dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    @Override
    public void onStart(Progress progress) {
        dialog.show();
    }

    @Override
    public void onProgress(Progress progress) {
        tv_progress.setText(String.format("%.2f", progress.fraction * 100) + "%");

    }

    @Override
    public void onError(Progress progress) {
        if (dialog != null && mContext != null & !((Activity) mContext).isFinishing()) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onFinish(final File file, Progress progress) {
        if (dialog != null && mContext != null & !((Activity) mContext).isFinishing()) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        AlertUtils.showConfirmDialog((Activity) mContext, "下载完成，是否安装", true, new AlertUtils.IDialogClickListener() {
            @Override
            public void onConfirm() {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    // Android高版本安装器不允许直接访问File，需要借助FileProvider(或使用取巧方法：调低targetSdkVersion)
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    mContext.startActivity(intent);
                } catch (Throwable e) {
                    Log.e("TAG", "install: " + e.getMessage());
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onRemove(Progress progress) {
        if (dialog != null && mContext != null & !((Activity) mContext).isFinishing()) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
