#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
代码索引生成工具的单元测试

运行方法：
    python3 tests/test_generate_code_index.py
    # 或者带覆盖率
    coverage run --source=tools tests/test_generate_code_index.py
"""

import sys
import os
import tempfile

# 添加 tools 目录到路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'tools'))

# 直接 import 模块
from generate_code_index import extract_java_methods, extract_js_functions, categorize_java_methods


class TestGenerateCodeIndex:
    """代码索引生成工具测试类"""

    def test_extract_java_methods(self):
        """测试提取 Java 方法"""
        # 创建一个临时 Java 文件
        with tempfile.NamedTemporaryFile(mode='w', suffix='.java', delete=False) as f:
            f.write('''
public class TestClass {
    public void method1() {
        // 方法1
    }
    
    public int method2(String param) {
        return 0;
    }
    
    private void privateMethod() {
        // 私有方法
    }
    
    protected void protectedMethod() {
        // 受保护方法
    }
}
''')
            temp_path = f.name
        
        try:
            methods = extract_java_methods(temp_path)
            # 应该能提取到方法
            assert len(methods) > 0
            # 检查是否有 method1
            method_names = [m['name'] for m in methods]
            assert 'method1' in method_names
            assert 'method2' in method_names
        finally:
            os.unlink(temp_path)

    def test_extract_js_functions(self):
        """测试提取 JS 函数"""
        # 创建一个临时 JS 文件
        with tempfile.NamedTemporaryFile(mode='w', suffix='.js', delete=False) as f:
            f.write('''
function func1() {
    // 函数1
}

const func2 = function() {
    // 函数2
};

const obj = {
    method1: function() {},
    method2: () => {}
};
''')
            temp_path = f.name
        
        try:
            functions = extract_js_functions(temp_path)
            # 应该能提取到函数
            assert len(functions) > 0
        finally:
            os.unlink(temp_path)

    def test_categorize_java_methods(self):
        """测试分类 Java 方法"""
        methods = [
            {'name': 'onCreate', 'line': 10},
            {'name': 'onDestroy', 'line': 20},
            {'name': 'setAcEnabled', 'line': 30},
            {'name': 'setLowBeamLight', 'line': 40},
            {'name': 'startCamera360', 'line': 50},
        ]
        
        categories = categorize_java_methods(methods, 'Test.java')
        # 应该有分类
        assert isinstance(categories, dict)
        assert len(categories) > 0

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
