#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
API 文档生成工具的单元测试

运行方法：
    python3 tests/test_generate_api_docs.py
"""

import sys
import os

# 添加 tools 目录到路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'tools'))


class TestGenerateApiDocs:
    """API 文档生成工具测试类"""

    def test_script_exists(self):
        """测试脚本文件存在"""
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_api_docs.py')
        assert os.path.exists(script_path), "generate_api_docs.py 脚本不存在"

    def test_script_runs(self):
        """测试脚本可以正常运行"""
        import subprocess
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_api_docs.py')
        result = subprocess.run([sys.executable, script_path], 
                              capture_output=True, text=True,
                              cwd=os.path.join(os.path.dirname(__file__), '..'))
        assert result.returncode == 0, f"脚本运行失败: {result.stderr}"

    def test_output_file_exists(self):
        """测试生成的输出文件存在"""
        output_path = os.path.join(os.path.dirname(__file__), '..', 'docs', 'JS_API_REFERENCE.md')
        # 先运行脚本生成文件
        import subprocess
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_api_docs.py')
        subprocess.run([sys.executable, script_path], 
                      capture_output=True, text=True,
                      cwd=os.path.join(os.path.dirname(__file__), '..'))
        assert os.path.exists(output_path), "生成的 JS_API_REFERENCE.md 文件不存在"

    def test_output_has_content(self):
        """测试生成的文件有内容"""
        output_path = os.path.join(os.path.dirname(__file__), '..', 'docs', 'JS_API_REFERENCE.md')
        # 先运行脚本生成文件
        import subprocess
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_api_docs.py')
        subprocess.run([sys.executable, script_path], 
                      capture_output=True, text=True,
                      cwd=os.path.join(os.path.dirname(__file__), '..'))
        
        with open(output_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        assert len(content) > 100, "生成的文件内容太少"
        assert 'JS API' in content or 'JavaScript' in content or '接口' in content, "文件标题不正确"
        assert '方法' in content or 'method' in content.lower(), "缺少方法说明"

    def test_output_has_key_methods(self):
        """测试生成的文件包含关键方法"""
        output_path = os.path.join(os.path.dirname(__file__), '..', 'docs', 'JS_API_REFERENCE.md')
        # 先运行脚本生成文件
        import subprocess
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_api_docs.py')
        subprocess.run([sys.executable, script_path], 
                      capture_output=True, text=True,
                      cwd=os.path.join(os.path.dirname(__file__), '..'))
        
        with open(output_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 检查是否包含一些关键方法
        assert 'startCamera360' in content or '360' in content, "缺少 360 全景相关方法"
        assert 'setLowBeamLight' in content or '近光灯' in content or '灯光' in content, "缺少灯光控制方法"
        assert 'setDriveMode' in content or '驾驶模式' in content, "缺少驾驶模式方法"

    def test_output_has_categories(self):
        """测试生成的文件有分类"""
        output_path = os.path.join(os.path.dirname(__file__), '..', 'docs', 'JS_API_REFERENCE.md')
        # 先运行脚本生成文件
        import subprocess
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_api_docs.py')
        subprocess.run([sys.executable, script_path], 
                      capture_output=True, text=True,
                      cwd=os.path.join(os.path.dirname(__file__), '..'))
        
        with open(output_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 检查是否有分类标题（## 开头的）
        import re
        categories = re.findall(r'^##\s+', content, re.MULTILINE)
        assert len(categories) >= 5, f"分类太少，只有 {len(categories)} 个"


def run_tests():
    """运行所有测试"""
    import traceback
    
    test_class = TestGenerateApiDocs()
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
