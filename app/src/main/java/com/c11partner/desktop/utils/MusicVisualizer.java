package com.c11partner.desktop.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.util.Log;

/**
 * 音乐可视化工具类
 * 提供基于音频频谱的音乐律动效果
 */
public class MusicVisualizer {
    private static final String TAG = "MusicVisualizer";
    
    private Visualizer visualizer;
    private AudioManager audioManager;
    private OnVisualizerUpdateListener listener;
    private boolean isActive = false;
    
    public interface OnVisualizerUpdateListener {
        /**
         * 当音频频谱数据更新时调用
         * @param waveform 频谱波形数据
         * @param frequency 频率数据
         */
        void onVisualizerUpdate(byte[] waveform, byte[] frequency);
    }
    
    public MusicVisualizer(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }
    
    /**
     * 设置可视化更新监听器
     * @param listener 监听器
     */
    public void setOnVisualizerUpdateListener(OnVisualizerUpdateListener listener) {
        this.listener = listener;
    }
    
    /**
     * 开始音乐可视化
     */
    public void startVisualizer() {
        try {
            if (visualizer != null) {
                stopVisualizer();
            }
            
            // 创建Visualizer对象
            visualizer = new Visualizer(0); // 0表示使用全局音频会话
            visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]); // 设置最大捕获大小
            visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                @Override
                public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                    // 波形数据更新
                    if (listener != null) {
                        listener.onVisualizerUpdate(waveform, null);
                    }
                }

                @Override
                public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                    // FFT频谱数据更新
                    if (listener != null) {
                        listener.onVisualizerUpdate(null, fft);
                    }
                }
            }, Visualizer.getMaxCaptureRate() / 2, true, true); // 同时捕获波形和FFT数据
            
            visualizer.setEnabled(true);
            isActive = true;
            Log.d(TAG, "音乐可视化已启动");
        } catch (Exception e) {
            Log.e(TAG, "启动音乐可视化失败", e);
        }
    }
    
    /**
     * 停止音乐可视化
     */
    public void stopVisualizer() {
        try {
            if (visualizer != null) {
                visualizer.setEnabled(false);
                visualizer.release();
                visualizer = null;
            }
            isActive = false;
            Log.d(TAG, "音乐可视化已停止");
        } catch (Exception e) {
            Log.e(TAG, "停止音乐可视化失败", e);
        }
    }
    
    /**
     * 检查音乐可视化是否处于活动状态
     * @return 如果处于活动状态返回true，否则返回false
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * 获取当前音乐音量
     * @return 音量值（0-100）
     */
    public int getCurrentVolume() {
        try {
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            return (int) ((currentVolume / (float) maxVolume) * 100);
        } catch (Exception e) {
            Log.e(TAG, "获取音量失败", e);
            return 0;
        }
    }
}