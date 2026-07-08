const PanelController = (function() {
    function showSettingsModal() {
        const modal = document.getElementById('settingsModal');
        if (!modal) return;
        modal.style.display = 'flex';
        modal.classList.add('active');
    }

    function hideSettingsModal() {
        const modal = document.getElementById('settingsModal');
        if (!modal) return;
        modal.style.display = 'none';
        modal.classList.remove('active');
    }

    function showAppsModal() {
        const modal = document.getElementById('appsModal');
        if (!modal) return;
        modal.style.display = 'flex';
        modal.classList.add('active');
    }

    function hideAppsModal() {
        const modal = document.getElementById('appsModal');
        if (!modal) return;
        modal.style.display = 'none';
        modal.classList.remove('active');
    }

    function initWallpaperDoubleClick() {
        const bgContainer = document.querySelector('.background-container');
        if (!bgContainer) return;

        let lastClickTime = 0;
        bgContainer.addEventListener('click', function(e) {
            const now = Date.now();
            if (now - lastClickTime < 300) {
                toggleWallpaperCarousel();
            }
            lastClickTime = now;
        });
    }

    function handleWallpaperLongPress() {
        const bgContainer = document.querySelector('.background-container');
        if (!bgContainer) return;

        let longPressTimer = null;
        let isLongPress = false;

        bgContainer.addEventListener('touchstart', function(e) {
            isLongPress = false;
            longPressTimer = setTimeout(function() {
                isLongPress = true;
                showSettingsModal();
            }, 500);
        });

        bgContainer.addEventListener('touchend', function() {
            if (longPressTimer) {
                clearTimeout(longPressTimer);
                longPressTimer = null;
            }
        });

        bgContainer.addEventListener('touchmove', function() {
            if (longPressTimer) {
                clearTimeout(longPressTimer);
                longPressTimer = null;
            }
        });
    }

    return {
        showSettingsModal,
        hideSettingsModal,
        showAppsModal,
        hideAppsModal,
        initWallpaperDoubleClick,
        handleWallpaperLongPress
    };
})();

// 显式挂载到 window，使 index.js 适配层的 if (window.PanelController) 判断生效
window.PanelController = PanelController;
