package com.c11partner.desktop.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 每日一言API工具类
 * 提供从网络获取每日一言功能
 */
public class QuoteApiUtils {
    private static final String API_URL = "https://v.api.aa1.cn/api/yiyan/index.php";
    private QuoteListener listener;

    /**
     * 每日一言监听器接口
     * 用于接收每日一言获取结果的回调
     */
    public interface QuoteListener {
        /**
         * 当每日一言获取成功时调用
         *
         * @param quote 每日一言内容
         */
        void onQuoteReceived(String quote);
    }

    /**
     * 获取每日一言
     * 异步获取每日一言内容
     *
     * @param listener 每日一言监听器
     */
    public void getDailyQuote(QuoteListener listener) {
        this.listener = listener;
        new FetchQuoteTask().execute();
    }

    /**
     * 获取每日一言的异步任务类
     * 在后台线程中执行网络请求，获取每日一言内容
     */
    private class FetchQuoteTask extends AsyncTask<Void, Void, String> {
        /**
         * 在后台线程中执行网络请求
         *
         * @param voids 无参数
         * @return 每日一言内容
         */
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setRequestProperty("Accept", "application/json");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } catch (Exception e) {
                Log.e("QuoteApiClient", "Error fetching quote: " + e.getMessage(), e);
                if (e instanceof java.net.UnknownHostException) {
                    Log.e("QuoteApiClient", "No internet connection");
                }
                return null;
            }
        }

        /**
         * 在主线程中处理网络请求结果
         *
         * @param result 网络请求结果
         */
        @Override
        protected void onPostExecute(String result) {
            if (listener != null && result != null) {
                String quoteText = result.replaceAll("<script[^>]*>[\\s\\S]*?<\\/script>", "");
                quoteText = quoteText.replaceAll("<[^>]+>", "");
                listener.onQuoteReceived(quoteText.trim());
            }
        }
    }
}