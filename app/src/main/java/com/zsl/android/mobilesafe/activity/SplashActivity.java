package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.application.BaseApplication;
import com.zsl.android.mobilesafe.utils.PermissionUtils;
import com.zsl.android.mobilesafe.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

    // Handler 消息
    private final int CODE_UPDATE_DIALOG = 1;
    private final int CODE_NET_ERROR = 2;
    private final int CODE_ENTER_HOME = 3;

    // 服务器信息
    private int mVersionCode;
    private String mVersionName;
    private String mDesc;
    private String mDownloadUrl;

    // 组件
    private TextView tvVersion;

    // 其他
    private final String mDownloadPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "MobileSafe" + File.separator;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                default:
                    break;
            }
        }
    };
    private SharedPreferences mPerf;
    private View rlRoot;

    /*
    * 动态请求权限的回调
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQUEST_WRITE_EXTERNAL_STORAGE:
                break;
            case PermissionUtils.REQUEST_ACCESS__LOCATION:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发现新版本：" + mVersionName);
        builder.setMessage(mDesc);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void download() {
        if (PermissionUtils.checkSdcardPermission(this)) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("下载中...");
            progressDialog.show();
            RequestParams params = new RequestParams(mDownloadUrl);
            params.setSaveFilePath(mDownloadPath + "mobilesafe" + mVersionName + ".apk");
            x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onWaiting() {
                }

                @Override
                public void onStarted() {
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    if (isDownloading) {
                        progressDialog.setMax((int) total);
                        progressDialog.setProgress((int) current);
                    }
                }

                @Override
                public void onSuccess(File result) {
                    Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(SplashActivity.this, "com.zsl.android.mobilesafe.fileProvider", result);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        uri = Uri.fromFile(result);
                    }
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    startActivityForResult(intent, 0);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(SplashActivity.this, "xUtils onError" + ex.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    enterHome();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Toast.makeText(SplashActivity.this, "xUtils cancelled", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    enterHome();
                }

                @Override
                public void onFinished() {
                }
            });
        } else {
            Toast.makeText(this, "Sdcard is not Granted!", Toast.LENGTH_LONG).show();
            enterHome();
        }
    }

    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 申请 Sdcard 权限
        PermissionUtils.requestAppPermissions(this);

        // 把数据库文件拷贝到 app 目录
        copyDB("address.db");

        // 界面初始化
        tvVersion = (TextView) findViewById(R.id.tv_version);
        rlRoot = findViewById(R.id.rl_root);
        tvVersion.setText("当前版本：" + getVersionName());
        AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
        anim.setDuration(2000);
        rlRoot.setAnimation(anim);

        // 检查版本更新
        mPerf = getSharedPreferences("config", MODE_PRIVATE);
        boolean autoUpdate = mPerf.getBoolean(BaseApplication.PREF_KEY_AUTO_UPDATE, true);
        if (autoUpdate)
            checkVersion();
        else mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
    }

    private void copyDB(String dbName) {
        // getFilesDir() 得到 app 的私有文件目录，即 data/data/com.zsl.myapp/files/
        File destFile = new File(getFilesDir(), dbName);// 要拷贝的目标地址

        if (destFile.exists()) {
            return;
        }

        FileOutputStream out = null;
        InputStream in = null;

        try {
            //  getAssets().open() 打开 assets 里的文件，返回一个 InputStream
            in = getAssets().open(dbName);
            out = new FileOutputStream(destFile);

            int len = 0;
            byte[] buffer = new byte[1024];

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void checkVersion() {
        final long startTime = System.currentTimeMillis();
        new Thread() {
            Message message = Message.obtain();

            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.zsllsz.com/mobilesafe/update.json");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(2000);
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String response = StreamUtils.readFromStream(inputStream);
                        JSONObject jo = new JSONObject(response);
                        mVersionName = jo.getString("versionName");
                        mVersionCode = jo.getInt("versionCode");
                        mDesc = jo.getString("description");
                        mDownloadUrl = jo.getString("downloadUrl");
                        if (mVersionCode > getVersionCode()) {
                            message.what = CODE_UPDATE_DIALOG;
                        } else {
                            message.what = CODE_ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    message.what = CODE_NET_ERROR;
                } catch (IOException e) {
                    message.what = CODE_NET_ERROR;
                } catch (JSONException e) {
                    message.what = CODE_NET_ERROR;
                } finally {
                    long endTIme = System.currentTimeMillis();
                    long timeUsed = endTIme - startTime;
                    if (timeUsed < 2000) {
                        try {
                            Thread.sleep(2000 - timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }
}
