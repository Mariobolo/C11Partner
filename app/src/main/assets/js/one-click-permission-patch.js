// 一键权限授权功能补丁
// 为系统管理添加一键授权功能

document.addEventListener('DOMContentLoaded', function() {
    // 等待设置面板初始化完成
    setTimeout(function() {
        // 查找系统管理TAB
        const systemTab = document.getElementById('apps-tab');
        if (systemTab) {
            // 添加一键权限授权按钮事件监听器
            const oneClickPermissionBtn = document.getElementById('oneClickPermissionBtn');
            if (oneClickPermissionBtn && !oneClickPermissionBtn.dataset.listenerAdded) {
                oneClickPermissionBtn.addEventListener('click', function () {
                    console.log('一键权限授权按钮被点击');
                    
                    // 显示提示信息
                    if (typeof showToast !== 'undefined') {
                        showToast('正在授予必要权限...');
                    }
                    
                    // 使用现有的executeAdbCommand方法执行权限授予命令
                    if (typeof Android !== 'undefined' && Android.executeAdbCommand) {
                        try {
                            console.log('执行权限授予命令...');
                            
                            // 授予READ_LOGS权限
                            Android.executeAdbCommand('adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS');
                            
                            // 稍微延迟后授予DUMP权限
                            setTimeout(() => {
                                Android.executeAdbCommand('adb shell pm grant com.c11partner.desktop android.permission.DUMP');
                                
                                if (typeof showToast !== 'undefined') {
                                    showToast('权限授予命令已发送');
                                }
                                
                                console.log('权限授予命令执行完成');
                            }, 500);
                            
                        } catch (e) {
                            console.error('执行权限授予命令时出错:', e);
                            if (typeof showToast !== 'undefined') {
                                showToast('权限授予失败: ' + e.message);
                            }
                        }
                    } else {
                        // 如果没有可用的接口，显示提示
                        console.log('权限授予功能需要在原生代码中实现');
                        alert('权限授予功能需要在原生代码中实现');
                    }
                });
                
                // 标记已添加事件监听器
                oneClickPermissionBtn.dataset.listenerAdded = 'true';
                console.log('一键权限授权按钮事件监听器已添加');
            }
        }
    }, 2000); // 延迟2秒确保DOM完全加载
});