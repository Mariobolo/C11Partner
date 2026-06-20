#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
API 文档生成工具的单元测试
"""

import sys
import os

# 添加 tools 目录到路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'tools'))

# 直接 import 模块
from generate_api_docs import MethodInfo, parse_method_signature, categorize_method, generate_markdown


def make_method(name, return_type='void', params=None, description='', line_num=0):
    """创建 MethodInfo 对象的辅助函数
    params 可以是字符串列表（自动解析）或 (type, name) 元组列表
    """
    m = MethodInfo()
    m.name = name
    m.return_type = return_type
    # 处理 params 格式
    if params:
        parsed_params = []
        for p in params:
            if isinstance(p, tuple) and len(p) == 2:
                parsed_params.append(p)
            elif isinstance(p, str):
                # 从字符串解析类型和名称，如 "String param1"
                parts = p.strip().split()
                if len(parts) >= 2:
                    parsed_params.append((parts[0], parts[-1]))
                else:
                    parsed_params.append(('', p))
        m.params = parsed_params
    else:
        m.params = []
    m.description = description
    m.line_num = line_num
    return m


class TestGenerateApiDocs:
    """API 文档生成工具测试类"""

    def test_method_info_class(self):
        """测试 MethodInfo 类"""
        method = make_method('testMethod', 'void', ['String param1', 'int param2'], '测试方法', 10)
        assert method.name == 'testMethod'
        assert method.return_type == 'void'
        assert len(method.params) == 2
        assert method.line_num == 10

    def test_parse_method_signature(self):
        """测试解析方法签名"""
        line = '    public void testMethod() {'
        method = parse_method_signature(line)
        assert method is not None
        assert method.name == 'testMethod'
        assert method.return_type == 'void'
        assert len(method.params) == 0

    def test_parse_method_with_params(self):
        """测试解析带参数的方法"""
        line = '    public boolean setAcEnabled(boolean enabled) {'
        method = parse_method_signature(line)
        assert method is not None
        assert method.name == 'setAcEnabled'
        assert method.return_type == 'boolean'
        assert len(method.params) == 1

    def test_categorize_method(self):
        """测试方法分类"""
        method = make_method('setAcEnabled', 'boolean', ['boolean enabled'])
        category = categorize_method(method)
        assert isinstance(category, str)
        assert len(category) > 0

    def test_categorize_light_method(self):
        """测试灯光方法分类"""
        method = make_method('setLowBeamLight', 'boolean', ['boolean on'])
        category = categorize_method(method)
        assert isinstance(category, str)
        # 应该分类到灯光相关
        assert '灯' in category or 'light' in category.lower() or 'Light' in category

    def test_generate_markdown(self):
        """测试生成 Markdown"""
        methods = [
            make_method('testMethod1', 'void', [], '测试方法1', 10),
            make_method('testMethod2', 'boolean', ['String param'], '测试方法2', 20),
        ]
        
        markdown = generate_markdown(methods, '测试文档')
        assert isinstance(markdown, str)
        assert len(markdown) > 0
        assert '测试文档' in markdown
        assert 'testMethod1' in markdown
        assert 'testMethod2' in markdown

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
        import subprocess
        script_path = os.path.join(os.path.dirname(__file__), '..', 'tools', 'generate_api_docs.py')
        subprocess.run([sys.executable, script_path], 
                      capture_output=True, text=True,
                      cwd=os.path.join(os.path.dirname(__file__), '..'))
        assert os.path.exists(output_path), "生成的 JS_API_REFERENCE.md 文件不存在"


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
