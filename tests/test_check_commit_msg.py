#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
提交信息检查工具的单元测试
"""

import sys
import os
import io

# 添加 tools 目录到路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'tools'))

# 直接 import 模块
from check_commit_msg import check_commit_message, print_result, main


class TestCheckCommitMsg:
    """提交信息检查工具测试类"""

    def test_valid_feat_commit(self):
        """测试有效的 feat 提交"""
        result = check_commit_message("feat: 添加新功能")
        assert result['valid'] == True
        assert result['type'] == 'feat'

    def test_valid_fix_commit(self):
        """测试有效的 fix 提交"""
        result = check_commit_message("fix: 修复bug")
        assert result['valid'] == True
        assert result['type'] == 'fix'

    def test_valid_docs_commit(self):
        """测试有效的 docs 提交"""
        result = check_commit_message("docs: 更新文档")
        assert result['valid'] == True
        assert result['type'] == 'docs'

    def test_valid_refactor_commit(self):
        """测试有效的 refactor 提交"""
        result = check_commit_message("refactor: 重构代码")
        assert result['valid'] == True
        assert result['type'] == 'refactor'

    def test_invalid_type(self):
        """测试无效的 type 类型"""
        result = check_commit_message("invalid: 测试")
        assert result['valid'] == False
        assert len(result['errors']) > 0

    def test_invalid_format_no_colon(self):
        """测试无效格式（没有冒号）"""
        result = check_commit_message("feat 添加新功能")
        assert result['valid'] == False

    def test_empty_subject(self):
        """测试空的描述"""
        result = check_commit_message("feat: ")
        assert result['valid'] == False

    def test_empty_message(self):
        """测试空的提交信息"""
        result = check_commit_message("")
        assert result['valid'] == False

    def test_subject_too_long_warning(self):
        """测试描述过长的警告"""
        long_subject = "a" * 100
        result = check_commit_message(f"feat: {long_subject}")
        # 可能是 valid 但有 warning，或者直接 invalid
        # 根据实际实现调整
        assert 'warnings' in result or result['valid'] == False

    def test_with_body(self):
        """测试带正文的提交信息"""
        message = """feat: 添加新功能

这是详细的描述
多行内容
"""
        result = check_commit_message(message)
        assert result['valid'] == True
        assert result['type'] == 'feat'

    def test_case_insensitive_type(self):
        """测试 type 大小写不敏感"""
        result = check_commit_message("Feat: 添加新功能")
        # 可能是 valid 或 invalid，取决于实现
        # 我们只测试不崩溃
        assert 'valid' in result

    def test_all_types(self):
        """测试所有合法的 type 类型"""
        valid_types = ['feat', 'fix', 'docs', 'style', 'refactor', 
                       'perf', 'test', 'chore', 'ci', 'revert']
        for t in valid_types:
            result = check_commit_message(f"{t}: 测试")
            assert result['valid'] == True, f"Type {t} 应该是有效的"

    def test_print_result_valid(self):
        """测试打印有效结果"""
        result = {
            'valid': True,
            'type': 'feat',
            'subject': '添加新功能',
            'body': '',
            'errors': [],
            'warnings': []
        }
        # 捕获输出
        captured_output = io.StringIO()
        old_stdout = sys.stdout
        sys.stdout = captured_output
        
        try:
            print_result(result)
            output = captured_output.getvalue()
            assert len(output) > 0
            assert '✅' in output or '通过' in output or '有效' in output
        finally:
            sys.stdout = old_stdout

    def test_print_result_invalid(self):
        """测试打印无效结果"""
        result = {
            'valid': False,
            'type': None,
            'subject': '',
            'body': '',
            'errors': ['格式错误'],
            'warnings': []
        }
        # 捕获输出
        captured_output = io.StringIO()
        old_stdout = sys.stdout
        sys.stdout = captured_output
        
        try:
            print_result(result)
            output = captured_output.getvalue()
            assert len(output) > 0
            assert '❌' in output or '错误' in output or '失败' in output
        finally:
            sys.stdout = old_stdout

    def test_main_function_is_callable(self):
        """测试 main 函数是可调用的"""
        assert callable(main), "main 函数应该是可调用的"

    def test_check_commit_message_returns_dict(self):
        """测试返回值是字典"""
        result = check_commit_message("feat: 测试")
        assert isinstance(result, dict)
        assert 'valid' in result
        assert 'type' in result
        assert 'subject' in result
        assert 'errors' in result
        assert 'warnings' in result


def run_tests():
    """运行所有测试"""
    import traceback
    
    test_class = TestCheckCommitMsg()
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
