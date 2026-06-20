#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
代码索引生成工具的单元测试
"""

import sys
import os
import tempfile

# 添加 tools 目录到路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'tools'))

# 直接 import 模块
from generate_code_index import (
    extract_java_methods,
    extract_js_functions,
    categorize_java_methods,
    generate_markdown,
    main
)


class TestGenerateCodeIndex:
    """代码索引生成工具测试类"""

    def test_extract_java_methods(self):
        """测试提取Java方法"""
        test_code = "public class Test {\n    public void testMethod() {\n    }\n}"
        with tempfile.NamedTemporaryFile(mode='w', suffix='.java', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            methods = extract_java_methods(temp_path)
            assert isinstance(methods, list)
            assert len(methods) > 0
        finally:
            os.unlink(temp_path)

    def test_extract_java_methods_with_params(self):
        """测试提取带参数的Java方法"""
        test_code = (
            "public class Test {\n"
            "    public void methodNoParams() {\n"
            "    }\n"
            "    public String methodWithParams(int p1, String p2) {\n"
            "        return null;\n"
            "    }\n"
            "    private void privateMethod() {\n"
            "    }\n"
            "}"
        )
        with tempfile.NamedTemporaryFile(mode='w', suffix='.java', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            methods = extract_java_methods(temp_path)
            assert isinstance(methods, list)
            assert len(methods) >= 3
        finally:
            os.unlink(temp_path)

    def test_extract_js_functions(self):
        """测试提取JS函数"""
        test_code = "function testFunc() {\n}\n"
        with tempfile.NamedTemporaryFile(mode='w', suffix='.js', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            functions = extract_js_functions(temp_path)
            assert isinstance(functions, list)
            assert len(functions) > 0
        finally:
            os.unlink(temp_path)

    def test_extract_js_functions_various(self):
        """测试提取各种类型的JS函数"""
        test_code = (
            "function regularFunction() {}\n"
            "const arrowFunc = () => {};\n"
            "const obj = { method1: function() {} };\n"
            "class TestClass { method() {} }\n"
        )
        with tempfile.NamedTemporaryFile(mode='w', suffix='.js', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            functions = extract_js_functions(temp_path)
            assert isinstance(functions, list)
            assert len(functions) > 0
        finally:
            os.unlink(temp_path)

    def test_categorize_java_methods(self):
        """测试分类Java方法"""
        methods = [
            {'name': 'setAcEnabled', 'line_num': 10},
            {'name': 'getMusicInfo', 'line_num': 20},
            {'name': 'startCamera360', 'line_num': 30},
        ]
        categorized = categorize_java_methods(methods, 'Test.java')
        assert isinstance(categorized, dict)
        assert len(categorized) > 0

    def test_categorize_java_methods_various(self):
        """测试分类各种类型的Java方法"""
        methods = [
            {'name': 'onCreate', 'line_num': 10},
            {'name': 'initData', 'line_num': 20},
            {'name': 'saveSettings', 'line_num': 30},
            {'name': 'setLowBeamLight', 'line_num': 40},
            {'name': 'getWindLevel', 'line_num': 50},
        ]
        categorized = categorize_java_methods(methods, 'Test.java')
        assert isinstance(categorized, dict)
        # 应该有多个分类
        assert len(categorized) >= 2

    def test_generate_markdown(self):
        """测试生成Markdown"""
        markdown = generate_markdown()
        assert isinstance(markdown, str)
        assert len(markdown) > 0
        assert '#' in markdown

    def test_generate_markdown_has_sections(self):
        """测试生成的Markdown包含多个章节"""
        markdown = generate_markdown()
        # 应该包含后端和前端两大部分
        assert '后端' in markdown or 'Java' in markdown or 'java' in markdown.lower()
        assert '前端' in markdown or 'JS' in markdown or 'js' in markdown.lower()

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
        import subprocess
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_code_index.py')
        subprocess.run([sys.executable, script_path], 
                      capture_output=True, text=True,
                      cwd=os.path.join(os.path.dirname(__file__), '..'))
        assert os.path.exists(output_path), "生成的 CODE_INDEX.md 文件不存在"

    def test_main_function_is_callable(self):
        """测试main函数是可调用的"""
        assert callable(main), "main 函数应该是可调用的"


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
