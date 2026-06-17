// 多任务按钮ADB命令补丁
// 为tasksBtn添加ADB命令支持

document.addEventListener('DOMContentLoaded', function() {
    // 等待原有多任务按钮事件处理完成后再添加我们的处理
    setTimeout(function() {
        const tasksBtn = document.getElementById('tasksBtn');
        if (tasksBtn) {
            // 移除原有的事件监听器（如果有的话）
            const newTasksBtn = tasksBtn.cloneNode(true);
            tasksBtn.parentNode.replaceChild(newTasksBtn, tasksBtn);
            
            // 添加新的事件监听器
            newTasksBtn.addEventListener('click', function() {
                console.log('多任务按钮被点击');
                
                // 首先尝试使用ADB命令
                if (typeof Android !== 'undefined' && Android.executeAdbCommand) {
                    try {
                        console.log('尝试使用ADB命令打开多任务页面');
                        Android.executeAdbCommand('input keyevent 187');
                    } catch (e) {
                        console.error('ADB命令执行失败:', e);
                        // 回退到原有方法
                        if (typeof Android !== 'undefined' && Android.openRecents) {
                            Android.openRecents();
                        }
                    }
                } else if (typeof Android !== 'undefined' && Android.openRecentsViaAdb) {
                    // 如果有专门的ADB方法
                    try {
                        Android.openRecentsViaAdb();
                    } catch (e) {
                        console.error('ADB方法执行失败:', e);
                        if (typeof Android !== 'undefined' && Android.openRecents) {
                            Android.openRecents();
                        }
                    }
                } else if (typeof Android !== 'undefined' && Android.openRecents) {
                    // 使用原有的方法
                    Android.openRecents();
                } else {
                    console.log('没有可用的多任务打开方法');
                }
            });
            
            console.log('多任务按钮ADB命令支持已添加');
        }
    }, 1000);
});