package com.example.download;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lq on 2018/9/5.
 */

public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_CANCELED = 2;

    private DownloadListener listener;
    private boolean isCanceled = false;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    private int lastProgress;

    @Override
    protected Integer doInBackground(String... strings) {
        InputStream is = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            long downloadLength = 0;
            String downloadUrl = strings[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            long contentLength = getContentLength(downloadUrl);//75172728
            Log.d("msg", "contentLength:" + contentLength);
            if (contentLength == 0) {
                return TYPE_FAILED;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(downloadUrl).build();
            Response response = client.newCall(request).execute();
            if (response != null) {

                fos = new FileOutputStream(file);
                is = response.body().byteStream();
                byte[] b = new byte[1024];
                long total = 0;
                int len = -1;
                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    }
                    total += len;
                    fos.write(b, 0, len);
                    int progress = (int) (total * 100 / contentLength);
                    publishProgress(progress);


                }
                Log.d("msg", "total:" + total);
            }
            response.body().close();
            return TYPE_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return TYPE_FAILED;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        //这里做了堵塞
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            return contentLength;
        }
        return 0;
    }
}
