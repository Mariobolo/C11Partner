#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
MusicUtils 单元测试
测试音乐工具类的常量定义和方法逻辑
"""
import unittest
import re
from pathlib import Path
class TestMusicUtils(unittest.TestCase):
    """MusicUtils 类测试"""
    def setUp(self):
        """读取MusicUtils.java源码"""
        java_file = Path(__file__).parent.parent / "app/src/main/java/com/c11partner/desktop/utils/MusicUtils.java"
        with open(java_file, 'r', encoding='utf-8') as f:
            self.source_code = f.read()
    def test_class_structure(self):
        """测试类结构是否正确"""
        self.assertIn('public class MusicUtils', self.source_code)
        self.assertIn('private AudioManager audioManager;', self.source_code)
        self.assertIn('private Context mContext;', self.source_code)
    def test_constructor(self):
        """测试构造函数"""
        self.assertIn('public MusicUtils(Context context)', self.source_code)
        self.assertIn('mContext = context;', self.source_code)
        self.assertIn('audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);', self.source_code)
    def test_play_method(self):
        """测试播放方法"""
        self.assertIn('public void play()', self.source_code)
        self.assertIn('KEYCODE_MEDIA_PLAY_PAUSE', self.source_code)
        self.assertIn('KEYCODE_MEDIA_PLAY', self.source_code)
    def test_pause_method(self):
        """测试暂停方法"""
        self.assertIn('public void pause()', self.source_code)
        self.assertIn('KEYCODE_MEDIA_PAUSE', self.source_code)
    def test_previous_method(self):
        """测试上一首方法"""
        self.assertIn('public void previous()', self.source_code)
        self.assertIn('KEYCODE_MEDIA_PREVIOUS', self.source_code)
    def test_next_method(self):
        """测试下一首方法"""
        self.assertIn('public void next()', self.source_code)
        self.assertIn('KEYCODE_MEDIA_NEXT', self.source_code)
    def test_send_media_button_event_method(self):
        """测试发送媒体按键事件方法"""
        self.assertIn('private boolean sendMediaButtonEvent(int keyCode)', self.source_code)
        self.assertIn('SystemClock.uptimeMillis()', self.source_code)
        self.assertIn('KeyEvent.ACTION_DOWN', self.source_code)
        self.assertIn('KeyEvent.ACTION_UP', self.source_code)
    def test_reflection_handling(self):
        """测试反射处理逻辑"""
        self.assertIn('java.lang.reflect.Method method', self.source_code)
        self.assertIn('AudioManager.class.getMethod("dispatchMediaKeyEvent"', self.source_code)
        self.assertIn('method.getReturnType() == Boolean.TYPE', self.source_code)
        self.assertIn('method.invoke(audioManager,', self.source_code)
    def test_exception_handling(self):
        """测试异常处理"""
        self.assertIn('catch (Exception e)', self.source_code)
        self.assertIn('catch (Exception ex)', self.source_code)
        # 至少有2个异常处理
        catch_count = self.source_code.count('catch (Exception')
        self.assertGreaterEqual(catch_count, 2)
    def test_is_music_playing_method(self):
        """测试检查音乐是否正在播放方法"""
        self.assertIn('public boolean isMusicPlaying()', self.source_code)
        self.assertIn('audioManager.isMusicActive()', self.source_code)
        self.assertIn('Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP', self.source_code)
        self.assertIn('getStreamVolume(AudioManager.STREAM_MUSIC)', self.source_code)
        self.assertIn('getStreamMaxVolume(AudioManager.STREAM_MUSIC)', self.source_code)
    def test_imports(self):
        """测试导入语句"""
        self.assertIn('import android.content.Context;', self.source_code)
        self.assertIn('import android.media.AudioManager;', self.source_code)
        self.assertIn('import android.os.Build;', self.source_code)
        self.assertIn('import android.os.SystemClock;', self.source_code)
        self.assertIn('import android.view.KeyEvent;', self.source_code)
    def test_method_count(self):
        """测试方法数量"""
        method_pattern = r'public\s+\w+\s+\w+\s*\([^)]*\)'
        methods = re.findall(method_pattern, self.source_code)
        # 至少应有5个public方法
        self.assertGreaterEqual(len(methods), 5, f"应该至少有5个public方法，当前只有{len(methods)}个")
    def test_keycode_constants(self):
        """测试所有使用的KeyCode常量"""
        keycodes = [
            'KEYCODE_MEDIA_PLAY_PAUSE',
            'KEYCODE_MEDIA_PLAY',
            'KEYCODE_MEDIA_PAUSE',
            'KEYCODE_MEDIA_PREVIOUS',
            'KEYCODE_MEDIA_NEXT'
        ]
        for keycode in keycodes:
            self.assertIn(keycode, self.source_code, f"应该包含{keycode}常量")
    def test_fallback_mechanism(self):
        """测试降级机制"""
        # play方法有降级策略
        self.assertIn('if (!sendMediaButtonEvent(', self.source_code)
        # pause方法有降级策略
        self.assertIn('if (!sendMediaButtonEvent(', self.source_code)
    def test_volume_check_logic(self):
        """测试音量检查逻辑"""
        self.assertIn('if (streamVolume == 0)', self.source_code)
        self.assertIn('return false;', self.source_code)
        self.assertIn('if (isMusicActive && streamVolume > 0)', self.source_code)
        self.assertIn('return true;', self.source_code)
    def test_javadoc_comments(self):
        """测试JavaDoc注释"""
        # 检查是否有类级别的注释
        self.assertIn('/**', self.source_code)
        self.assertIn('音乐工具类', self.source_code)
        # 检查方法注释
        self.assertIn('播放音乐', self.source_code)
        self.assertIn('暂停音乐', self.source_code)
        self.assertIn('播放上一首', self.source_code)
        self.assertIn('播放下一首', self.source_code)
    
    # ==================== 反射机制测试 ====================
    
    def test_reflection_method_signature(self):
        """测试反射方法调用签名"""
        self.assertIn('AudioManager.class.getMethod("dispatchMediaKeyEvent", KeyEvent.class)', self.source_code)
        self.assertIn('method.getReturnType() == Boolean.TYPE', self.source_code)
    
    def test_reflection_fallback(self):
        """测试反射失败后的回退机制"""
        # 外层try-catch反射失败
        catch_count = self.source_code.count('catch (Exception')
        self.assertGreaterEqual(catch_count, 2, "应该有至少2层异常处理")
        # 失败时返回false
        self.assertIn('return false;', self.source_code)
    
    # ==================== KeyEvent 测试 ====================
    
    def test_keyevent_creation(self):
        """测试KeyEvent创建逻辑"""
        # 应该创建DOWN和UP两个事件
        self.assertIn('KeyEvent.ACTION_DOWN', self.source_code)
        self.assertIn('KeyEvent.ACTION_UP', self.source_code)
        # 使用相同的事件时间
        self.assertIn('SystemClock.uptimeMillis()', self.source_code)
    
    # ==================== 版本兼容性测试 ====================
    
    def test_version_check(self):
        """测试Android版本检查"""
        self.assertIn('Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP', self.source_code)
    
    # ==================== 返回值处理测试 ====================
    
    def test_return_value_handling(self):
        """测试返回值处理逻辑"""
        # 新版本返回boolean
        self.assertIn('Boolean downSuccess = (Boolean) method.invoke', self.source_code)
        self.assertIn('return downSuccess && upSuccess;', self.source_code)
        # 旧版本返回void
        self.assertIn('return true;', self.source_code)
    
    # ==================== 代码质量测试 ====================
    
    def test_naming_conventions(self):
        """测试命名规范"""
        # 成员变量使用m前缀
        self.assertIn('private Context mContext;', self.source_code)
        # 方法使用驼峰命名
        method_pattern = r'public\s+\w+\s+(\w+)\s*\('
        methods = re.findall(method_pattern, self.source_code)
        for method in methods:
            self.assertTrue(method[0].islower(), 
                f"方法名 '{method}' 应该以小写开头")
    
    # ==================== 逻辑完整性测试 ====================
    
    def test_play_pause_fallback(self):
        """测试播放暂停降级逻辑"""
        # play/pause方法都有降级逻辑
        self.assertIn('if (!sendMediaButtonEvent(', self.source_code)
    
    def test_is_music_playing_logic(self):
        """测试音乐播放状态判断逻辑"""
        # 标准检查
        self.assertIn('boolean isMusicActive = audioManager.isMusicActive();', self.source_code)
        # 音量检查
        self.assertIn('streamVolume == 0', self.source_code)
        self.assertIn('isMusicActive && streamVolume > 0', self.source_code)
    
    # ==================== 导入完整性测试 ====================
    
    def test_all_imports_present(self):
        """测试所有必要的导入都存在"""
        required_imports = [
            'import android.content.Context;',
            'import android.media.AudioManager;',
            'import android.os.Build;',
            'import android.os.SystemClock;',
            'import android.view.KeyEvent;'
        ]
        for imp in required_imports:
            self.assertIn(imp, self.source_code, f"缺少导入: {imp}")
if __name__ == '__main__':
    unittest.main()