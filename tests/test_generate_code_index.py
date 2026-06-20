#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
代码索引生成工具的单元测试

运行方法：
    python3 tests/test_generate_code_index.py
"""

import sys
import os
import tempfile

# 添加 tools 目录到路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'tools'))


class TestGenerateCodeIndex:
    """代码索引生成工具测试类"""

    def test_script_exists(self):
        """测试脚本文件存在"""
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_code_index.py')
        assert os.path.exists(script_path), "generate_code_index.py 脚本不存在"

    def test_script_runs(self):
        """测试脚本可以正常运行"""
        import subprocess
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_code_index.py')
        result = subprocess.run([sys.executable, script_path], 
                              capture_output=True, text=True,
                              cwd=os.path.join(os.path.dirname(__file__), '..'))
        assert result.returncode == 0, f"脚本运行失败: {result.stderr}"

    def test_output_file_exists(self):
        """测试生成的输出文件存在"""
        output_path = os.path.join(os.path.dirname(__file__), '..', 'docs', 'CODE_INDEX.md')
        # 先运行脚本生成文件
        import subprocess
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_code_index.py')
        subprocess.run([sys.executable, script_path], 
                      capture_output=True, text=True,
                      cwd=os.path.join(os.path.dirname(__file__), '..'))
        assert os.path.exists(output_path), "生成的 CODE_INDEX.md 文件不存在"

    def test_output_has_content(self):
        """测试生成的文件有内容"""
        output_path = os.path.join(os.path.dirname(__file__), '..', 'docs', 'CODE_INDEX.md')
        # 先运行脚本生成文件
        import subprocess
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_code_index.py')
        subprocess.run([sys.executable, script_path], 
                      capture_output=True, text=True,
                      cwd=os.path.join(os.path.dirname(__file__), '..'))
        
        with open(output_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        assert len(content) > 100, "生成的文件内容太少"
        assert '# C11Partner' in content, "文件标题不正确"
        assert '后端' in content and 'Java' in content, "缺少后端 Java 部分"
        assert '前端' in content and 'JS' in content, "缺少前端 JS 部分"

    def test_output_has_key_classes(self):
        """测试生成的文件包含关键类"""
        output_path = os.path.join(os.path.dirname(__file__), '..', 'docs', 'CODE_INDEX.md')
        # 先运行脚本生成文件
        import subprocess
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_code_index.py')
        subprocess.run([sys.executable, script_path], 
                      capture_output=True, text=True,
                      cwd=os.path.join(os.path.dirname(__file__), '..'))
        
        with open(output_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 检查是否包含关键文件
        assert 'WebViewBridge.java' in content, "缺少 WebViewBridge.java"
        assert 'MainActivity.java' in content, "缺少 MainActivity.java"
        assert 'CarControlManager.java' in content, "缺少 CarControlManager.java"
        assert 'index.js' in content, "缺少 index.js"


def run_tests():
    """运行所有测试"""
    import traceback
    
    test_class = TestGenerateCodeIndex()
    test_methods = [m for m in dir(test_class) if m.startswith('test_')]
    
    passed = 0
    failed = 0
    
    print(f"运行 {len(test_methods)} 个测试...")
    print()
    
    for method_name in test_methods:
        try:
            getattr(test_class, method_name)()
            print(f"✅ {method_name}")
            passed += 1
        except Exception as e:
            print(f"❌ {method_name}")
            print(f"   错误: {e}")
            traceback.print_exc()
            failed += 1
    
    print()
    print(f"结果：{passed} 通过，{failed} 失败")
    
    return failed == 0


if __name__ == '__main__':
    success = run_tests()
    sys.exit(0 if success else 1)
