#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
提交信息检查工具的单元测试

运行方法：
    python3 -m pytest tests/test_check_commit_msg.py -v
    或者
    python3 tests/test_check_commit_msg.py
"""

import sys
import os

# 添加 tools 目录到路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'tools'))

from check_commit_msg import check_commit_message


class TestCheckCommitMessage:
    """提交信息检查测试类"""

    def test_valid_feat_commit(self):
        """测试正确的 feat 提交"""
        result = check_commit_message("feat: 添加新功能")
        assert result['valid'] == True
        assert result['type'] == 'feat'
        assert result['subject'] == '添加新功能'
        assert len(result['errors']) == 0

    def test_valid_fix_commit(self):
        """测试正确的 fix 提交"""
        result = check_commit_message("fix: 修复某个bug")
        assert result['valid'] == True
        assert result['type'] == 'fix'

    def test_valid_docs_commit(self):
        """测试正确的 docs 提交"""
        result = check_commit_message("docs: 更新文档")
        assert result['valid'] == True
        assert result['type'] == 'docs'

    def test_valid_refactor_commit(self):
        """测试正确的 refactor 提交"""
        result = check_commit_message("refactor: 重构代码")
        assert result['valid'] == True
        assert result['type'] == 'refactor'

    def test_invalid_format_no_colon(self):
        """测试格式错误：没有冒号"""
        result = check_commit_message("添加新功能")
        assert result['valid'] == False
        assert len(result['errors']) > 0

    def test_invalid_type(self):
        """测试 type 类型错误"""
        result = check_commit_message("xxx: 测试")
        assert result['valid'] == False
        assert len(result['errors']) > 0

    def test_empty_subject(self):
        """测试空描述"""
        result = check_commit_message("feat: ")
        assert result['valid'] == False

    def test_empty_message(self):
        """测试空提交信息"""
        result = check_commit_message("")
        assert result['valid'] == False

    def test_subject_too_long_warning(self):
        """测试描述过长警告"""
        long_subject = "添加了一个" + "非常" * 20 + "长的功能描述"
        result = check_commit_message(f"feat: {long_subject}")
        assert result['valid'] == True  # 警告不影响合法性
        assert len(result['warnings']) > 0

    def test_with_body(self):
        """测试带正文的提交"""
        message = """feat: 添加新功能

这是详细说明
- 功能1
- 功能2
"""
        result = check_commit_message(message)
        assert result['valid'] == True
        assert result['type'] == 'feat'
        assert '详细说明' in result['body']

    def test_case_insensitive_type(self):
        """测试 type 大小写不敏感"""
        result = check_commit_message("FEAT: 添加新功能")
        assert result['valid'] == True
        assert result['type'] == 'feat'  # 应该转为小写

    def test_all_types(self):
        """测试所有允许的 type"""
        types = ['feat', 'fix', 'docs', 'style', 'refactor', 'perf', 
                 'test', 'chore', 'ci', 'revert']
        for t in types:
            result = check_commit_message(f"{t}: 测试")
            assert result['valid'] == True, f"Type {t} should be valid"


def run_tests():
    """运行所有测试"""
    import traceback
    
    test_class = TestCheckCommitMessage()
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
