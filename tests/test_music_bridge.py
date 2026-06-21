#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
MusicBridge 单元测试
测试音乐功能Bridge类的常量定义和方法逻辑
"""
import unittest
import re
from pathlib import Path


class TestMusicBridge(unittest.TestCase):
    """MusicBridge 类测试"""

    def setUp(self):
        """读取MusicBridge.java源码"""
        java_file = Path(__file__).parent.parent / "app/src/main/java/com/c11partner/desktop/bridge/MusicBridge.java"
        with open(java_file, 'r', encoding='utf-8') as f:
            self.source_code = f.read()

    def test_class_definition(self):
        """测试类定义和继承关系"""
        self.assertIn('public class MusicBridge extends BaseBridge', self.source_code)
        self.assertIn('private static final String TAG = "MusicBridge";', self.source_code)

    def test_member_variables(self):
        """测试成员变量定义"""
        self.assertIn('private MainActivity mActivity;', self.source_code)
        self.assertIn('private MusicUtils musicUtils;', self.source_code)
        self.assertIn('private MediaSessionService mediaSessionService;', self.source_code)
        self.assertIn('private boolean isMediaSessionServiceBound = false;', self.source_code)

    def test_constructor(self):
        """测试构造函数"""
        self.assertIn('public MusicBridge(Context context, MainActivity activity)', self.source_code)
        self.assertIn('super(context, activity);', self.source_code)
        self.assertIn('this.mActivity = activity;', self.source_code)
        self.assertIn('this.musicUtils = new MusicUtils(context);', self.source_code)

    def test_media_session_service_setter(self):
        """测试媒体会话服务设置方法"""
        self.assertIn('public void setMediaSessionService(MediaSessionService service, boolean isBound)', self.source_code)
        self.assertIn('this.mediaSessionService = service;', self.source_code)
        self.assertIn('this.isMediaSessionServiceBound = isBound;', self.source_code)

    def test_music_visualizer_methods(self):
        """测试音乐可视化方法"""
        self.assertIn('public void startMusicVisualizer()', self.source_code)
        self.assertIn('public void stopMusicVisualizer()', self.source_code)
        self.assertIn('mActivity.musicVisualizer.startVisualizer();', self.source_code)
        self.assertIn('mActivity.musicVisualizer.stopVisualizer();', self.source_code)
        self.assertIn('mActivity.runOnUiThread', self.source_code)

    def test_music_status_methods(self):
        """测试音乐状态查询方法"""
        self.assertIn('public boolean isMusicPlaying()', self.source_code)
        self.assertIn('public String getCurrentMusicName()', self.source_code)
        self.assertIn('public String getCurrentMusicArtist()', self.source_code)
        self.assertIn('public String getMusicProgressInfo()', self.source_code)
        self.assertIn('public String getSystemMusicInfo()', self.source_code)

    def test_music_status_fallback_values(self):
        """测试音乐状态默认返回值"""
        self.assertIn('此刻无声，佳音已备候君启...', self.source_code)
        self.assertIn('未知艺术家', self.source_code)
        self.assertIn('isPlaying', self.source_code)
        self.assertIn('currentPosition', self.source_code)
        self.assertIn('duration', self.source_code)

    def test_music_control_methods(self):
        """测试音乐播放控制方法"""
        self.assertIn('public void playPauseMusic()', self.source_code)
        self.assertIn('public void nextMusic()', self.source_code)
        self.assertIn('public void prevMusic()', self.source_code)
        self.assertIn('CarControlManager.getInstance(mContext)', self.source_code)
        self.assertIn('carControl.sendNextTrack()', self.source_code)
        self.assertIn('carControl.sendPrevTrack()', self.source_code)

    def test_deprecated_methods(self):
        """测试废弃兼容方法"""
        self.assertIn('@Deprecated', self.source_code)
        self.assertIn('public void playPause()', self.source_code)
        self.assertIn('public void playNext()', self.source_code)
        self.assertIn('public void playPrevious()', self.source_code)

    def test_helper_methods(self):
        """测试辅助方法"""
        self.assertIn('private void safeMediaSessionAction(Runnable action, String actionName)', self.source_code)
        self.assertIn('private <T> T safeMediaSessionGet(java.util.function.Supplier<T> supplier', self.source_code)
        self.assertIn('private void safeUiThreadAction(Runnable action, String actionName)', self.source_code)
        self.assertIn('private void safeStartActivity(Intent intent, String activityName)', self.source_code)
        self.assertIn('private boolean isMediaSessionAvailable()', self.source_code)
        self.assertIn('private void sendMediaButton(int keyCode)', self.source_code)

    def test_volume_control_methods(self):
        """测试音量控制方法"""
        self.assertIn('public void setMusicVolume(int volume)', self.source_code)
        self.assertIn('public int getMusicVolume()', self.source_code)
        self.assertIn('public void volumeUp()', self.source_code)
        self.assertIn('public void volumeDown()', self.source_code)
        self.assertIn('musicUtils.setMusicVolume(volume)', self.source_code)
        self.assertIn('musicUtils.getMusicVolume()', self.source_code)
        self.assertIn('Math.min(current + 1, 15)', self.source_code)
        self.assertIn('Math.max(current - 1, 0)', self.source_code)

    def test_playback_control_methods(self):
        """测试播放进度控制方法"""
        self.assertIn('public void seekTo(final long position)', self.source_code)
        self.assertIn('public void setPlaybackSpeed(final float speed)', self.source_code)

    def test_notification_listener_methods(self):
        """测试通知监听权限方法"""
        self.assertIn('public boolean isNotificationListenerEnabled()', self.source_code)
        self.assertIn('public void openNotificationListenerSettings()', self.source_code)
        self.assertIn('enabled_notification_listeners', self.source_code)
        self.assertIn('android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS', self.source_code)

    def test_playlist_methods(self):
        """测试播放列表方法"""
        self.assertIn('public String getPlaylist()', self.source_code)
        self.assertIn('public void playSongAtIndex(final int index)', self.source_code)
        self.assertIn('public void setRepeatMode(final int mode)', self.source_code)
        self.assertIn('public int getRepeatMode()', self.source_code)
        self.assertIn('public void setShuffleMode(final boolean enabled)', self.source_code)
        self.assertIn('public boolean isShuffleEnabled()', self.source_code)

    def test_imports(self):
        """测试import语句"""
        self.assertIn('import android.content.Context;', self.source_code)
        self.assertIn('import android.content.Intent;', self.source_code)
        self.assertIn('import android.view.KeyEvent;', self.source_code)
        self.assertIn('import android.webkit.JavascriptInterface;', self.source_code)
        self.assertIn('import org.json.JSONObject;', self.source_code)

    def test_javascript_interface_annotations(self):
        """测试JavascriptInterface注解数量"""
        annotation_count = self.source_code.count('@JavascriptInterface')
        self.assertGreaterEqual(annotation_count, 15, f"应该至少有15个@JavascriptInterface方法，当前只有{annotation_count}个")

    def test_error_handling(self):
        """测试异常处理"""
        catch_count = self.source_code.count('catch (Exception e)')
        self.assertGreaterEqual(catch_count, 10, f"应该有足够的异常处理，当前只有{catch_count}个")

    def test_logging(self):
        """测试日志输出"""
        log_d_count = self.source_code.count('logD(TAG,')
        log_e_count = self.source_code.count('logE(TAG,')
        self.assertGreaterEqual(log_d_count, 15, f"应该有足够的调试日志，当前只有{log_d_count}个")
        self.assertGreaterEqual(log_e_count, 10, f"应该有足够的错误日志，当前只有{log_e_count}个")

    def test_media_button_broadcast(self):
        """测试媒体按钮广播发送"""
        self.assertIn('Intent.ACTION_MEDIA_BUTTON', self.source_code)
        self.assertIn('KeyEvent.ACTION_DOWN', self.source_code)
        self.assertIn('KeyEvent.ACTION_UP', self.source_code)
        self.assertIn('KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE', self.source_code)
        self.assertIn('KeyEvent.KEYCODE_MEDIA_NEXT', self.source_code)
        self.assertIn('KeyEvent.KEYCODE_MEDIA_PREVIOUS', self.source_code)

    def test_method_count(self):
        """测试方法数量"""
        method_pattern = r'public\s+\w+\s+\w+\s*\([^)]*\)'
        methods = re.findall(method_pattern, self.source_code)
        self.assertGreaterEqual(len(methods), 15, f"应该至少有15个public方法，当前只有{len(methods)}个")

    def test_null_checks(self):
        """测试空值检查"""
        null_check_count = self.source_code.count('!= null')
        self.assertGreaterEqual(null_check_count, 8, f"应该有足够的空值检查，当前只有{null_check_count}个")

    def test_activity_validation(self):
        """测试Activity有效性检查"""
        self.assertIn('isActivityValid()', self.source_code)


if __name__ == '__main__':
    unittest.main()
