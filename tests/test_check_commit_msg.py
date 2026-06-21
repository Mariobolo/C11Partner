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
from check_commit_msg import check_commit_message, print_result, main, ALLOWED_TYPES, TYPE_DESCRIPTIONS
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
    def test_valid_perf_commit(self):
        """测试有效的 perf 提交"""
        result = check_commit_message("perf: 优化性能")
        assert result['valid'] == True
        assert result['type'] == 'perf'
    def test_valid_test_commit(self):
        """测试有效的 test 提交"""
        result = check_commit_message("test: 添加测试")
        assert result['valid'] == True
        assert result['type'] == 'test'
    def test_valid_chore_commit(self):
        """测试有效的 chore 提交"""
        result = check_commit_message("chore: 更新依赖")
        assert result['valid'] == True
        assert result['type'] == 'chore'
    def test_valid_ci_commit(self):
        """测试有效的 ci 提交"""
        result = check_commit_message("ci: 更新CI配置")
        assert result['valid'] == True
        assert result['type'] == 'ci'
    def test_valid_revert_commit(self):
        """测试有效的 revert 提交"""
        result = check_commit_message("revert: 回滚提交")
        assert result['valid'] == True
        assert result['type'] == 'revert'
    def test_valid_style_commit(self):
        """测试有效的 style 提交"""
        result = check_commit_message("style: 格式化代码")
        assert result['valid'] == True
        assert result['type'] == 'style'
    def test_invalid_type(self):
        """测试无效的 type 类型 - 现在是警告而非错误"""
        result = check_commit_message("invalid: 测试")
        assert result['valid'] == True  # 现在 type 不标准不会导致 valid=False
        assert len(result['warnings']) > 0  # 但会有警告
    def test_invalid_format_no_colon(self):
        """测试无效格式（没有冒号）- 现在是警告而非错误"""
        result = check_commit_message("feat 添加新功能")
        assert result['valid'] == True  # 现在格式不匹配不会导致valid=False
        assert len(result['warnings']) > 0  # 但会有警告
    def test_empty_subject(self):
        """测试空的描述 - 现在是警告而非错误"""
        result = check_commit_message("feat: ")
        assert result['valid'] == True  # 现在格式不匹配不会导致valid=False
        assert len(result['warnings']) > 0  # 但会有警告
    def test_empty_message(self):
        """测试空的提交信息"""
        result = check_commit_message("")
        assert result['valid'] == False
    def test_subject_too_long_warning(self):
        """测试描述过长的警告"""
        long_subject = "a" * 60
        result = check_commit_message(f"feat: {long_subject}")
        assert 'warnings' in result
        assert len(result['warnings']) > 0
    def test_subject_too_short_warning(self):
        """测试描述过短的警告"""
        result = check_commit_message("feat: 测")
        assert len(result['warnings']) > 0
    def test_subject_not_verb_start_warning(self):
        """测试不以动词开头的警告"""
        result = check_commit_message("feat: 新功能")
        assert len(result['warnings']) > 0
    def test_subject_ends_with_period_warning(self):
        """测试以句号结尾的警告"""
        result = check_commit_message("feat: 添加新功能。")
        assert len(result['warnings']) > 0
    def test_body_line_too_long_warning(self):
        """测试正文行过长的警告"""
        long_line = "a" * 80
        message = f"feat: 添加新功能\n\n{long_line}"
        result = check_commit_message(message)
        assert len(result['warnings']) > 0
    def test_with_body(self):
        """测试带正文的提交信息"""
        message = "feat: 添加新功能\n\n这是详细的描述\n多行内容"
        result = check_commit_message(message)
        assert result['valid'] == True
        assert result['type'] == 'feat'
    def test_case_insensitive_type(self):
        """测试 type 大小写不敏感"""
        result = check_commit_message("Feat: 添加新功能")
        assert 'valid' in result
    def test_all_types(self):
        """测试所有合法的 type 类型"""
        for t in ALLOWED_TYPES:
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
        captured_output = io.StringIO()
        old_stdout = sys.stdout
        sys.stdout = captured_output
        
        try:
            print_result(result)
            output = captured_output.getvalue()
            assert len(output) > 0
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
        captured_output = io.StringIO()
        old_stdout = sys.stdout
        sys.stdout = captured_output
        
        try:
            print_result(result)
            output = captured_output.getvalue()
            assert len(output) > 0
        finally:
            sys.stdout = old_stdout
    def test_print_result_with_warnings(self):
        """测试打印带警告的结果"""
        result = {
            'valid': True,
            'type': 'feat',
            'subject': '添加新功能',
            'body': '',
            'errors': [],
            'warnings': ['描述过长']
        }
        captured_output = io.StringIO()
        old_stdout = sys.stdout
        sys.stdout = captured_output
        
        try:
            print_result(result)
            output = captured_output.getvalue()
            assert len(output) > 0
        finally:
            sys.stdout = old_stdout
    def test_print_result_with_body(self):
        """测试打印带正文的结果"""
        result = {
            'valid': True,
            'type': 'feat',
            'subject': '添加新功能',
            'body': '这是详细的正文描述内容',
            'errors': [],
            'warnings': []
        }
        captured_output = io.StringIO()
        old_stdout = sys.stdout
        sys.stdout = captured_output
        
        try:
            print_result(result)
            output = captured_output.getvalue()
            assert len(output) > 0
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
    def test_multiline_body(self):
        """测试多行正文"""
        message = """feat: 添加新功能
第一行详细描述
第二行详细描述
"""
        result = check_commit_message(message)
        assert result['valid'] == True
        assert result['type'] == 'feat'
    def test_whitespace_only_message(self):
        """测试只有空白字符的提交信息"""
        result = check_commit_message("   \n\t  ")
        assert result['valid'] == False
    def test_type_descriptions_coverage(self):
        """测试 TYPE_DESCRIPTIONS 覆盖所有类型"""
        for t in ALLOWED_TYPES:
            assert t in TYPE_DESCRIPTIONS, f"Type {t} 应该在 TYPE_DESCRIPTIONS 中"
    
    def test_print_result_with_long_body_truncated(self):
        """测试打印长正文时被截断的情况"""
        long_body = "这是非常长的正文描述内容，超过50个字符就会被截断显示省略号" * 2
        result = {
            'valid': True,
            'type': 'feat',
            'subject': '添加新功能',
            'body': long_body,
            'errors': [],
            'warnings': []
        }
        captured_output = io.StringIO()
        old_stdout = sys.stdout
        sys.stdout = captured_output
        
        try:
            print_result(result)
            output = captured_output.getvalue()
            assert '...' in output  # 应该包含省略号表示被截断
        finally:
            sys.stdout = old_stdout
    
    def test_main_no_args(self):
        """测试 main 函数不带参数的情况（显示帮助）"""
        captured_output = io.StringIO()
        old_stdout = sys.stdout
        old_argv = sys.argv
        sys.argv = ['check_commit_msg.py']
        sys.stdout = captured_output
        
        try:
            main()
            output = captured_output.getvalue()
            assert '用法' in output
            assert '示例' in output
        finally:
            sys.stdout = old_stdout
            sys.argv = old_argv
    
    def test_main_with_valid_message(self):
        """测试 main 函数带有效提交信息"""
        captured_output = io.StringIO()
        old_stdout = sys.stdout
        old_argv = sys.argv
        sys.argv = ['check_commit_msg.py', 'feat: 添加新功能']
        sys.stdout = captured_output
        
        try:
            main()
        except SystemExit as e:
            assert e.code == 0  # 应该退出码为0
        finally:
            output = captured_output.getvalue()
            assert '格式正确' in output
            sys.stdout = old_stdout
            sys.argv = old_argv
    
    def test_main_with_invalid_message(self):
        """测试 main 函数带格式不匹配的提交信息 - 现在退出码为0（警告而非错误）"""
        old_argv = sys.argv
        sys.argv = ['check_commit_msg.py', 'invalid message']
        
        try:
            main()
        except SystemExit as e:
            assert e.code == 0  # 现在格式不匹配退出码为0（只有真正错误才返回1）
        finally:
            sys.argv = old_argv
    
    def test_main_with_file_path(self, tmp_path):
        """测试 main 函数从文件读取提交信息"""
        commit_file = tmp_path / "COMMIT_EDITMSG"
        commit_file.write_text("feat: 从文件读取的提交信息", encoding='utf-8')
        
        captured_output = io.StringIO()
        old_stdout = sys.stdout
        old_argv = sys.argv
        sys.argv = ['check_commit_msg.py', str(commit_file)]
        sys.stdout = captured_output
        
        try:
            main()
        except SystemExit as e:
            assert e.code == 0  # 应该退出码为0
        finally:
            output = captured_output.getvalue()
            assert '格式正确' in output
            sys.stdout = old_stdout
            sys.argv = old_argv
def run_tests():
    """运行所有测试"""
    import traceback
    import tempfile
    from pathlib import Path
    
    test_class = TestCheckCommitMsg()
    test_methods = [m for m in dir(test_class) if m.startswith('test_')]
    
    passed = 0
    failed = 0
    
    print(f"运行 {len(test_methods)} 个测试...")
    print()
    
    # 创建临时目录用于测试
    with tempfile.TemporaryDirectory() as tmp_dir:
        tmp_path = Path(tmp_dir)
        
        for method_name in test_methods:
            try:
                method = getattr(test_class, method_name)
                # 检查方法是否需要 tmp_path 参数
                import inspect
                sig = inspect.signature(method)
                if 'tmp_path' in sig.parameters:
                    method(tmp_path)
                else:
                    method()
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