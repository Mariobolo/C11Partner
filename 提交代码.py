import os
import subprocess
import sys
from datetime import datetime

def run_command(cmd, cwd=None, ignore_error=False, timeout=30):
    """执行命令并返回结果（带超时，避免卡住）"""
    try:
        print(f"[执行命令] {' '.join(cmd)}")
        result = subprocess.run(
            cmd,
            cwd=cwd,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            encoding='utf-8',
            shell=True,
            timeout=timeout
        )
        # 打印输出/错误信息
        if result.stdout:
            print(f"[输出]\n{result.stdout.strip()}")
        if result.stderr and not ignore_error:
            print(f"[错误]\n{result.stderr.strip()}")
        
        # 非忽略错误且返回码非0则终止
        if result.returncode != 0 and not ignore_error:
            raise Exception(f"命令执行失败（返回码：{result.returncode}）")
        return result.returncode, result.stdout, result.stderr
    except subprocess.TimeoutExpired:
        print(f"[超时] 命令执行超过 {timeout} 秒，自动终止")
        sys.exit(1)
    except Exception as e:
        print(f"[失败] {str(e)}")
        sys.exit(1)

def main():
    # ===================== 可选配置项 =====================
    COMMIT_MSG = f"自动提交 - {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}"  # 提交信息
    BRANCH_NAME = "master"  # 操作的分支名
    # ======================================================

    # 1. 获取脚本所在的项目目录（确保在仓库根目录执行）
    project_dir = os.path.dirname(os.path.abspath(__file__))
    print(f"[1/5] 当前操作目录：{project_dir}")

    # 2. 检查是否为 Git 仓库（非仓库则终止）
    print("\n[2/5] 检查是否为 Git 仓库...")
    ret, _, _ = run_command(["git", "rev-parse", "--is-inside-work-tree"], cwd=project_dir, ignore_error=True)
    if ret != 0:
        print("错误：当前目录不是 Git 仓库！请先执行 git init 初始化。")
        sys.exit(1)
    print("✅ 已确认是 Git 仓库")

    # 3. 执行 git add .（暂存所有修改）
    print("\n[3/5] 执行 git add . 暂存所有修改...")
    run_command(["git", "add", "."], cwd=project_dir)

    # 4. 执行 git commit（提交，允许空提交避免无修改时报错）
    print("\n[4/5] 执行 git commit 提交修改...")
    ret, _, _ = run_command(["git", "commit", "-m", COMMIT_MSG], cwd=project_dir, ignore_error=True)
    if ret == 0:
        print(f"✅ 提交成功：{COMMIT_MSG}")
    else:
        print("ℹ️ 无本地修改，无需提交")

    # 5. 执行 git pull（拉取远程分支最新代码）
    print(f"\n[5/5] 执行 git pull 拉取远程 {BRANCH_NAME} 分支...")
    run_command(["git", "pull", "origin", BRANCH_NAME], cwd=project_dir, ignore_error=True)

    # ========== 新增：执行 git push 推送到远程仓库 ==========
    print(f"\n[6/6] 执行 git push 推送到远程 {BRANCH_NAME} 分支...")
    # 首次推送加 -u 关联分支，后续推送可省略，但保留 -u 不影响
    run_command(["git", "push", "-u", "origin", BRANCH_NAME], cwd=project_dir, ignore_error=True)

    print("\n===================== 操作完成 =====================")
    input("按回车键退出...")

if __name__ == "__main__":
    # 解决中文乱码
    os.environ["PYTHONIOENCODING"] = "utf-8"
    # 可选：添加 Git 路径（如果提示 git 命令找不到，取消注释并修改）
    # os.environ["PATH"] += ";C:\\Program Files\\Git\\bin"
    main()