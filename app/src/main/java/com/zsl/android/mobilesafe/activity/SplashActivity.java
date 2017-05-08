package com.zsl.android.mobilesafe.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.zsl.android.mobilesafe.R;
import com.zsl.android.mobilesafe.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

    // Handler 消息
    private final int CODE_UPDATE_DIALOG = 1;
    private final int CODE_NET_ERROR = 2;

    // 服务器信息
    private int mVersionCode;
    private String mVersionName;
    private String mDesc;
    private String mDownloadUrl;

    // 组件
    private TextView tvVersion;

    // 其他
    private final String mSavePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "MobileSafe" + File.separator;
    private boolean mSavePathReady = false;
    private static final int MY_WRITE_EXTERNAL_STORAGE = 0;

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
                default:
                    break;
            }
        }
    };

    /*
    * sdcard 创建目录
    * */
    private void initEnv() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File path = new File(mSavePath);
            if (!path.exists()) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_EXTERNAL_STORAGE);
                    // Activity 的 onRequestPermissionsResult() 将被回调
                }
            } else {
                mSavePathReady = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    File path = new File(mSavePath);
                    if (path.mkdirs()) {
                        mSavePathReady = true;
                    } else {
                        mSavePathReady = false;
                        Log.d("zslzsl", "mkdir failed: " + mSavePath);
                    }
                } else {
                    mSavePathReady = false;
                }
                return;
            }
        }
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
        if (mSavePathReady) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("亲，努力下载中。。。");
            RequestParams params = new RequestParams(mDownloadUrl);
            params.setSaveFilePath(mSavePath);
            x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onWaiting() {
                }

                @Override
                public void onStarted() {
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    if(isDownloading) {
                        Log.d("zslzsl", "onLoading");
                        progressDialog.setMax((int) total);
                        progressDialog.setProgress((int) current);
                        progressDialog.show();
                    }
                }

                @Override
                public void onSuccess(File result) {
                    Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    enterHome();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(SplashActivity.this, "xUtils onError", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    enterHome();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Toast.makeText(SplashActivity.this, "cancelled", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    enterHome();
                }

                @Override
                public void onFinished() {
                    Toast.makeText(SplashActivity.this, "onFinished", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    enterHome();
                }
            });
        } else {
            Toast.makeText(this, "Sdcard is not ready!", Toast.LENGTH_LONG).show();
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
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("当前版本：" + getVersionName());
        initEnv();
        checkVersion();
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
        new Thread() {
            Message message = Message.obtain();

            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.zsllsz.com/mobilesafe/update.json");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(3000);
                    connection.setReadTimeout(5000);
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
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    message.what = CODE_NET_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }
}
