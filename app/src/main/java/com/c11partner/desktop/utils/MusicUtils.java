package com.c11partner.desktop.utils;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.SystemClock;
import android.view.KeyEvent;

/**
 * 音乐工具类
 * 提供音乐播放控制功能，包括播放、暂停、上一首、下一首等操作
 */
public class MusicUtils {

    private AudioManager audioManager;
    private Context mContext;

    /**
     * 构造函数
     * 初始化音频管理器
     *
     * @param context 上下文
     */
    public MusicUtils(Context context) {
        mContext = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 播放音乐
     * 发送播放媒体按键事件
     */
    public void play() {
        // 首先尝试使用通用的播放/暂停键
        if (!sendMediaButtonEvent(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)) {
            // 如果失败，则分别尝试播放和暂停键
            sendMediaButtonEvent(KeyEvent.KEYCODE_MEDIA_PLAY);
        }
    }

    /**
     * 暂停音乐
     * 发送暂停媒体按键事件
     */
    public void pause() {
        // 首先尝试使用通用的播放/暂停键
        if (!sendMediaButtonEvent(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)) {
            // 如果失败，则分别尝试播放和暂停键
            sendMediaButtonEvent(KeyEvent.KEYCODE_MEDIA_PAUSE);
        }
    }

    /**
     * 播放上一首
     * 发送上一首媒体按键事件
     */
    public void previous() {
        sendMediaButtonEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
    }

    /**
     * 播放下一首
     * 发送下一首媒体按键事件
     */
    public void next() {
        sendMediaButtonEvent(KeyEvent.KEYCODE_MEDIA_NEXT);
    }

    /**
     * 发送媒体按键事件
     * 向系统发送指定的媒体按键事件
     *
     * @param keyCode 按键代码
     * @return 事件是否成功发送
     */
    private boolean sendMediaButtonEvent(int keyCode) {
        long eventTime = SystemClock.uptimeMillis();
        KeyEvent keyEventDown = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, keyCode, 0);
        KeyEvent keyEventUp = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_UP, keyCode, 0);
        
        try {
            // 尝试调用返回boolean值的新版本方法
            java.lang.reflect.Method method = AudioManager.class.getMethod("dispatchMediaKeyEvent", KeyEvent.class);
            if (method.getReturnType() == Boolean.TYPE) {
                Boolean downSuccess = (Boolean) method.invoke(audioManager, keyEventDown);
                Boolean upSuccess = (Boolean) method.invoke(audioManager, keyEventUp);
                return downSuccess && upSuccess;
            } else {
                // 老版本方法返回void
                method.invoke(audioManager, keyEventDown);
                method.invoke(audioManager, keyEventUp);
                return true; // 假设成功
            }
        } catch (Exception e) {
            // 反射调用失败时使用传统方式
            try {
                audioManager.dispatchMediaKeyEvent(keyEventDown);
                audioManager.dispatchMediaKeyEvent(keyEventUp);
                return true; // 假设成功
            } catch (Exception ex) {
                return false; // 发送失败
            }
        }
    }

    /**
     * 检查音乐是否正在播放
     * 通过多种方式检查音乐播放状态，提高准确性
     *
     * @return 如果音乐正在播放返回true，否则返回false
     */
    public boolean isMusicPlaying() {
        // 首先使用标准方法检查
        boolean isMusicActive = audioManager.isMusicActive();
        
        // 在某些设备上，isMusicActive可能不够准确，添加额外的检查
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 获取当前音频焦点信息
            int streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            
            // 如果音乐流音量为0，可能没有音乐在播放
            if (streamVolume == 0) {
                return false;
            }
            
            // 如果音乐活跃且音量不为0，很可能是正在播放
            if (isMusicActive && streamVolume > 0) {
                return true;
            }
        }
        
        // 返回标准检查结果
        return isMusicActive;
    }
}