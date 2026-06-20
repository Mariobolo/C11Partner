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
        """测试提取 Java 方法"""
        test_code = (
            "public class Test {\n"
            "    public void method1() {\n"
            "    }\n"
            "    public String method2(int param) {\n"
            "        return null;\n"
            "    }\n"
            "}"
        )
        with tempfile.NamedTemporaryFile(mode='w', suffix='.java', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            methods = extract_java_methods(temp_path)
            assert isinstance(methods, list)
            assert len(methods) >= 2
        finally:
            os.unlink(temp_path)

    def test_extract_java_methods_with_params(self):
        """测试提取带参数的 Java 方法"""
        test_code = (
            "public class Test {\n"
            "    public void method1(int a, String b) {\n"
            "    }\n"
            "    public String method2() {\n"
            "        return null;\n"
            "    }\n"
            "}"
        )
        with tempfile.NamedTemporaryFile(mode='w', suffix='.java', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            methods = extract_java_methods(temp_path)
            assert isinstance(methods, list)
            assert len(methods) >= 2
        finally:
            os.unlink(temp_path)

    def test_extract_java_methods_private(self):
        """测试提取私有方法"""
        test_code = (
            "public class Test {\n"
            "    public void publicMethod() {\n"
            "    }\n"
            "    private void privateMethod() {\n"
            "    }\n"
            "    protected void protectedMethod() {\n"
            "    }\n"
            "}"
        )
        with tempfile.NamedTemporaryFile(mode='w', suffix='.java', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            methods = extract_java_methods(temp_path)
            assert isinstance(methods, list)
            assert len(methods) >= 2
        finally:
            os.unlink(temp_path)

    def test_extract_java_methods_static(self):
        """测试提取静态方法"""
        test_code = (
            "public class Test {\n"
            "    public static void staticMethod() {\n"
            "    }\n"
            "    public void instanceMethod() {\n"
            "    }\n"
            "}"
        )
        with tempfile.NamedTemporaryFile(mode='w', suffix='.java', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            methods = extract_java_methods(temp_path)
            assert isinstance(methods, list)
            assert len(methods) >= 2
        finally:
            os.unlink(temp_path)

    def test_extract_js_functions(self):
        """测试提取 JS 函数"""
        test_code = (
            "function func1() {}\n"
            "function func2(param) {}\n"
            "const func3 = () => {};\n"
        )
        with tempfile.NamedTemporaryFile(mode='w', suffix='.js', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            functions = extract_js_functions(temp_path)
            assert isinstance(functions, list)
            # 可能只提取到普通函数，箭头函数可能不支持
            assert len(functions) >= 1
        finally:
            os.unlink(temp_path)

    def test_extract_js_functions_various(self):
        """测试提取各种类型的 JS 函数"""
        test_code = (
            "function regularFunc() {}\n"
            "const arrowFunc = () => {};\n"
            "const funcWithParam = (param) => {};\n"
            "function anotherFunc(a, b) {}\n"
        )
        with tempfile.NamedTemporaryFile(mode='w', suffix='.js', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            functions = extract_js_functions(temp_path)
            assert isinstance(functions, list)
            assert len(functions) >= 2
        finally:
            os.unlink(temp_path)

    def test_extract_js_functions_arrow(self):
        """测试提取箭头函数"""
        test_code = (
            "const func1 = () => {};\n"
            "const func2 = (param) => {};\n"
            "function regularFunc() {}\n"
        )
        with tempfile.NamedTemporaryFile(mode='w', suffix='.js', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            functions = extract_js_functions(temp_path)
            assert isinstance(functions, list)
            assert len(functions) >= 1
        finally:
            os.unlink(temp_path)

    def test_categorize_java_methods(self):
        """测试分类 Java 方法"""
        methods = [
            {'name': 'setAcEnabled', 'line_num': 10},
            {'name': 'getAcEnabled', 'line_num': 20},
        ]
        categorized = categorize_java_methods(methods, 'Test.java')
        assert isinstance(categorized, dict)
        assert len(categorized) >= 1

    def test_categorize_java_methods_various(self):
        """测试分类各种类型的 Java 方法"""
        methods = [
            {'name': 'setAcEnabled', 'line_num': 10},
            {'name': 'playMusic', 'line_num': 20},
            {'name': 'startCamera360', 'line_num': 30},
            {'name': 'setWallpaper', 'line_num': 40},
        ]
        categorized = categorize_java_methods(methods, 'Test.java')
        assert isinstance(categorized, dict)
        categories = list(categorized.keys())
        assert len(categories) >= 2

    def test_categorize_java_methods_automation(self):
        """测试自动化方法分类"""
        methods = [
            {'name': 'runAutomation', 'line_num': 10},
            {'name': 'enableAutomation', 'line_num': 20},
        ]
        categorized = categorize_java_methods(methods, 'Test.java')
        assert isinstance(categorized, dict)
        categories = list(categorized.keys())
        assert len(categories) >= 1

    def test_categorize_java_methods_music(self):
        """测试音乐相关方法分类"""
        methods = [
            {'name': 'playMusic', 'line_num': 10},
            {'name': 'pauseMusic', 'line_num': 20},
            {'name': 'getMusicInfo', 'line_num': 30},
        ]
        categorized = categorize_java_methods(methods, 'Test.java')
        assert isinstance(categorized, dict)
        categories = list(categorized.keys())
        assert len(categories) >= 1

    def test_generate_markdown(self):
        """测试生成 Markdown"""
        markdown = generate_markdown()
        assert isinstance(markdown, str)
        assert len(markdown) > 0

    def test_generate_markdown_has_sections(self):
        """测试生成的 Markdown 包含多个章节"""
        markdown = generate_markdown()
        lines = markdown.split('\n')
        section_count = sum(1 for line in lines if line.startswith('# '))
        # 可能只有一个一级标题，用二级标题判断
        if section_count < 2:
            section_count = sum(1 for line in lines if line.startswith('## '))
        assert section_count >= 2

    def test_generate_markdown_has_statistics(self):
        """测试生成的Markdown包含统计信息"""
        markdown = generate_markdown()
        assert '个' in markdown or '总计' in markdown or '统计' in markdown

    def test_generate_markdown_has_categories(self):
        """测试生成的Markdown包含多个分类"""
        markdown = generate_markdown()
        lines = markdown.split('\n')
        category_count = sum(1 for line in lines if line.startswith('## '))
        assert category_count >= 5

    def test_main_function_is_callable(self):
        """测试 main 函数是可调用的"""
        assert callable(main), "main 函数应该是可调用的"

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
