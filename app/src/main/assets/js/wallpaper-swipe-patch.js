// 壁纸滑动动画补丁文件
// 用于替换原有的简单滑动实现

// 替换原有的addTouchSwipeListener函数
window.addTouchSwipeListener = function() {
    // 壁纸滑动功能已由WallpaperSwipeManager接管
    console.log('壁纸滑动监听器已由WallpaperSwipeManager管理');
};

// 确保WallpaperSwipeManager已初始化
document.addEventListener('DOMContentLoaded', function() {
    if (typeof WallpaperSwipeManager !== 'undefined' && !window.wallpaperSwipeManager) {
        window.wallpaperSwipeManager = new WallpaperSwipeManager();
    }
});