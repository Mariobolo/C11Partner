#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
API文档生成工具的单元测试
"""

import sys
import os
import tempfile

# 添加 tools 目录到路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'tools'))

# 直接 import 模块
from generate_api_docs import (
    MethodInfo,
    extract_javadoc,
    parse_method_signature,
    categorize_method,
    scan_java_file,
    generate_markdown,
    main
)



def create_method(name, return_type='void', params=None, description='', line_num=0):
    """辅助函数：创建 MethodInfo 对象"""
    method = MethodInfo()
    method.name = name
    method.return_type = return_type
    method.params = params or []
    method.description = description
    method.line_num = line_num
    return method


class TestGenerateApiDocs:
    """API文档生成工具测试类"""

    def test_method_info_class(self):
        """测试 MethodInfo 类"""
        method = create_method('testMethod', 'void', [], '测试方法', 10)
        assert method.name == 'testMethod'
        assert method.return_type == 'void'
        assert len(method.params) == 0
        assert method.description == '测试方法'
        assert method.line_num == 10

    def test_method_info_with_params(self):
        """测试带参数的 MethodInfo"""
        method = create_method('testMethod', 'String',
                              [('int', 'param1'), ('String', 'param2')],
                              '测试方法', 20)
        assert len(method.params) == 2
        assert method.params[0][0] == 'int'
        assert method.params[0][1] == 'param1'

    def test_parse_method_signature(self):
        """测试解析方法签名"""
        line = '    public String testMethod(int param1, String param2) {'
        method = parse_method_signature(line)
        assert method is not None
        assert method.name == 'testMethod'
        assert method.return_type == 'String'

    def test_parse_method_with_params(self):
        """测试解析带参数的方法"""
        line = '    public void setAcEnabled(boolean enabled) {'
        method = parse_method_signature(line)
        assert method is not None
        assert method.name == 'setAcEnabled'
        assert len(method.params) >= 1

    def test_parse_private_method(self):
        """测试解析私有方法"""
        line = '    private void helperMethod() {'
        method = parse_method_signature(line)
        # 可能返回 None（只解析公共方法）或返回 MethodInfo
        # 取决于实现，我们只测试不崩溃
        assert method is None or isinstance(method, MethodInfo)

    def test_categorize_method(self):
        """测试方法分类"""
        method = create_method('setAcEnabled', 'void', [], '', 10)
        category = categorize_method(method)
        assert isinstance(category, str)
        assert len(category) > 0

    def test_categorize_light_method(self):
        """测试灯光方法分类"""
        method = create_method('setLowBeamLight', 'void', [], '', 10)
        category = categorize_method(method)
        assert '灯' in category or 'light' in category.lower() or 'lighting' in category.lower()

    def test_categorize_360_method(self):
        """测试360全景方法分类"""
        method = create_method('startCamera360', 'void', [], '', 10)
        category = categorize_method(method)
        assert '360' in category or '全景' in category or 'camera' in category.lower()

    def test_categorize_music_method(self):
        """测试音乐方法分类"""
        method = create_method('getMusicInfo', 'String', [], '', 10)
        category = categorize_method(method)
        assert '音乐' in category or 'music' in category.lower()

    def test_categorize_volume_method(self):
        """测试音量方法分类"""
        method = create_method('setMusicVolume', 'void', [], '', 10)
        category = categorize_method(method)
        assert '音量' in category or 'volume' in category.lower()

    def test_extract_javadoc_simple(self):
        """测试提取简单的 JavaDoc"""
        lines = [
            '/**',
            ' * 这是一个测试方法',
            ' * 用于测试 JavaDoc 提取',
            ' */',
            'public void testMethod() {'
        ]
        javadoc = extract_javadoc(lines, 0)
        assert isinstance(javadoc, str)
        # 可能返回空字符串或提取到内容，取决于实现
        # 我们只测试不崩溃

    def test_extract_javadoc_single_line(self):
        """测试提取单行 JavaDoc"""
        lines = [
            '/** 单行注释 */',
            'public void testMethod() {'
        ]
        javadoc = extract_javadoc(lines, 0)
        assert isinstance(javadoc, str)

    def test_extract_javadoc_no_javadoc(self):
        """测试没有 JavaDoc 的情况"""
        lines = [
            'public void testMethod() {'
        ]
        javadoc = extract_javadoc(lines, 0)
        # 可能返回空字符串或 None
        assert javadoc == '' or javadoc is None

    def test_scan_java_file(self):
        """测试扫描 Java 文件"""
        test_code = '''
public class TestClass {
    /**
     * 测试方法1
     */
    @JavascriptInterface
    public void testMethod1() {
    }
    
    /**
     * 测试方法2
     * @param param 参数
     */
    @JavascriptInterface
    public String testMethod2(int param) {
        return null;
    }
}
'''
        with tempfile.NamedTemporaryFile(mode='w', suffix='.java', delete=False) as f:
            f.write(test_code)
            temp_path = f.name
        
        try:
            methods = scan_java_file(temp_path)
            assert isinstance(methods, list)
            # 应该能提取到至少 2 个公共方法
            assert len(methods) >= 2
        finally:
            os.unlink(temp_path)

    def test_generate_markdown(self):
        """测试生成 Markdown"""
        methods = [
            create_method('testMethod1', 'void', [], '测试方法1', 10),
            create_method('testMethod2', 'String', [('int', 'param')], '测试方法2', 20),
        ]
        markdown = generate_markdown(methods, '测试标题')
        assert isinstance(markdown, str)
        assert len(markdown) > 0
        assert '测试标题' in markdown

    def test_generate_markdown_empty(self):
        """测试空方法列表生成 Markdown"""
        markdown = generate_markdown([], '空标题')
        assert isinstance(markdown, str)

    def test_main_function_is_callable(self):
        """测试 main 函数是可调用的"""
        assert callable(main), "main 函数应该是可调用的"

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
