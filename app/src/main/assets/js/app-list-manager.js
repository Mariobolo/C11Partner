const AppListManager = (function() {
    let cachedAppsData = null;
    let lastAppListLoadTime = 0;
    const APP_LIST_CACHE_DURATION = 5 * 60 * 1000;
    let currentAppCategory = 'all';

    function generateAppData() {
        const apps = {
            'A': [
                { name: '支付宝', packageName: 'com.eg.android.AlipayGphone', icon: 'images/ic_launcher.png' },
                { name: '高德地图', packageName: 'com.autonavi.minimap', icon: 'images/ic_launcher.png' }
            ],
            'B': [
                { name: '百度地图', packageName: 'com.baidu.BaiduMap', icon: 'images/ic_launcher.png' },
                { name: '微信', packageName: 'com.tencent.mm', icon: 'images/ic_launcher.png' }
            ],
            'C': [
                { name: 'Chrome', packageName: 'com.android.chrome', icon: 'images/ic_launcher.png' },
                { name: '携程旅行', packageName: 'ctrip.android.view', icon: 'images/ic_launcher.png' }
            ],
            'D': [
                { name: '滴滴出行', packageName: 'com.sdu.didi', icon: 'images/ic_launcher.png' },
                { name: '钉钉', packageName: 'com.alibaba.android.rimet', icon: 'images/ic_launcher.png' }
            ],
            'E': [
                { name: '饿了么', packageName: 'me.ele', icon: 'images/ic_launcher.png' }
            ],
            'F': [
                { name: '飞书', packageName: 'com.ss.android.enterprise.im', icon: 'images/ic_launcher.png' },
                { name: '小红书', packageName: 'com.xingin.xhs', icon: 'images/ic_launcher.png' }
            ],
            'G': [
                { name: '高德地图', packageName: 'com.autonavi.minimap', icon: 'images/ic_launcher.png' }
            ],
            'H': [
                { name: '华为商城', packageName: 'com.huawei.appmarket', icon: 'images/ic_launcher.png' },
                { name: '虎牙直播', packageName: 'com.duowan.kiwi', icon: 'images/ic_launcher.png' }
            ],
            'I': [],
            'J': [
                { name: '京东', packageName: 'com.jingdong.app.mall', icon: 'images/ic_launcher.png' },
                { name: '今日头条', packageName: 'com.ss.android.article.news', icon: 'images/ic_launcher.png' }
            ],
            'K': [
                { name: '酷狗音乐', packageName: 'com.kugou.android', icon: 'images/ic_launcher.png' }
            ],
            'L': [
                { name: 'QQ音乐', packageName: 'com.tencent.qqmusic', icon: 'images/ic_launcher.png' },
                { name: '乐视视频', packageName: 'com.leeco.music', icon: 'images/ic_launcher.png' }
            ],
            'M': [
                { name: '美团', packageName: 'com.meituan.android', icon: 'images/ic_launcher.png' },
                { name: '芒果TV', packageName: 'com.hunantv.imgo.activity', icon: 'images/ic_launcher.png' }
            ],
            'N': [
                { name: '网易云音乐', packageName: 'com.netease.cloudmusic', icon: 'images/ic_launcher.png' },
                { name: 'QQ', packageName: 'com.tencent.mobileqq', icon: 'images/ic_launcher.png' }
            ],
            'O': [],
            'P': [
                { name: '拼多多', packageName: 'com.xunmeng.pinduoduo', icon: 'images/ic_launcher.png' },
                { name: '平安好车主', packageName: 'com.pingan.pacar', icon: 'images/ic_launcher.png' }
            ],
            'Q': [
                { name: 'QQ', packageName: 'com.tencent.mobileqq', icon: 'images/ic_launcher.png' },
                { name: 'QQ浏览器', packageName: 'com.tencent.mtt', icon: 'images/ic_launcher.png' }
            ],
            'R': [
                { name: '人人车', packageName: 'com.renrenche.android', icon: 'images/ic_launcher.png' }
            ],
            'S': [
                { name: '搜狗输入法', packageName: 'com.sohu.inputmethod.sogou', icon: 'images/ic_launcher.png' },
                { name: '淘宝', packageName: 'com.taobao.taobao', icon: 'images/ic_launcher.png' }
            ],
            'T': [
                { name: '天猫', packageName: 'com.tmall.wireless', icon: 'images/ic_launcher.png' },
                { name: '腾讯视频', packageName: 'com.tencent.qqlive', icon: 'images/ic_launcher.png' }
            ],
            'U': [],
            'V': [],
            'W': [
                { name: '微信', packageName: 'com.tencent.mm', icon: 'images/ic_launcher.png' },
                { name: '网易云音乐', packageName: 'com.netease.cloudmusic', icon: 'images/ic_launcher.png' }
            ],
            'X': [
                { name: '小米商城', packageName: 'com.xiaomi.shop', icon: 'images/ic_launcher.png' },
                { name: '携程旅行', packageName: 'ctrip.android.view', icon: 'images/ic_launcher.png' }
            ],
            'Y': [
                { name: '优酷视频', packageName: 'com.youku.phone', icon: 'images/ic_launcher.png' },
                { name: '音乐播放器', packageName: 'com.android.music', icon: 'images/ic_launcher.png' }
            ],
            'Z': [
                { name: '支付宝', packageName: 'com.eg.android.AlipayGphone', icon: 'images/ic_launcher.png' },
                { name: '知乎', packageName: 'com.zhihu.android', icon: 'images/ic_launcher.png' }
            ]
        };
        return apps;
    }

    function escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    function normalizeAppIcon(icon) {
        if (!icon) return 'images/ic_launcher.png';
        if (icon.startsWith('data:image')) return icon;
        if (icon.startsWith('http')) return icon;
        return 'images/ic_launcher.png';
    }

    function initAlphabetNav() {
        const alphabetList = document.getElementById('alphabetList');
        if (!alphabetList) return;

        const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        alphabetList.innerHTML = '';

        for (let i = 0; i < alphabet.length; i++) {
            const letter = alphabet[i];
            const li = document.createElement('li');
            li.textContent = letter;
            li.setAttribute('data-letter', letter);
            li.addEventListener('click', function () {
                const section = document.getElementById(`section-${letter}`);
                if (section) {
                    section.scrollIntoView({ behavior: 'smooth' });
                }
            });
            alphabetList.appendChild(li);
        }
    }

    function renderAppsList(appsData) {
        const appsList = document.getElementById('appsList');
        if (!appsList) return;

        appsList.innerHTML = '';

        const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

        alphabet.forEach(letter => {
            const apps = appsData[letter] || [];
            if (apps.length === 0) return;

            const section = document.createElement('div');
            section.id = `section-${letter}`;
            section.className = 'app-section';

            const sectionHeader = document.createElement('div');
            sectionHeader.className = 'app-section-header';
            sectionHeader.textContent = letter;
            section.appendChild(sectionHeader);

            const appGrid = document.createElement('div');
            appGrid.className = 'app-grid';

            apps.forEach(app => {
                const appItem = document.createElement('div');
                appItem.className = 'app-item';
                appItem.dataset.packageName = app.packageName;
                appItem.dataset.isSystemApp = app.isSystemApp ? 'true' : 'false';

                const appSafeIcon = normalizeAppIcon(app.icon);
                const appSafeName = escapeHtml(app.name);

                appItem.innerHTML = `
                    <div class="app-icon" style="background-image: url('${appSafeIcon}');"></div>
                    <div class="app-name">${appSafeName}</div>
                `;

                appItem.addEventListener('click', function() {
                    if (typeof Android !== 'undefined' && Android.launchApp) {
                        Android.launchApp(app.packageName);
                    }
                });

                appItem.addEventListener('contextmenu', function(e) {
                    e.preventDefault();
                    if (typeof showAddToQuickAppsDialog === 'function') {
                        showAddToQuickAppsDialog(app);
                    }
                });

                appGrid.appendChild(appItem);
            });

            section.appendChild(appGrid);
            appsList.appendChild(section);
        });
    }

    function initAppsModal() {
        const appsBtn = document.getElementById('appsBtn');
        const closeApps = document.getElementById('closeApps');
        const appsModal = document.getElementById('appsModal');
        const appSearch = document.getElementById('appSearch');
        const appsLoading = document.getElementById('appsLoading');
        const appsList = document.getElementById('appsList');

        if (!appsModal) return;

        function setAppListCache(appListJson) {
            try {
                if (appListJson && appListJson !== "") {
                    cachedAppsData = JSON.parse(appListJson);
                    lastAppListLoadTime = Date.now();
                }
            } catch (e) {
                console.error('解析预加载应用列表时出错:', e);
            }
        }
        window.setAppListCache = setAppListCache;

        function loadAppList() {
            const now = Date.now();

            if (cachedAppsData && (now - lastAppListLoadTime < APP_LIST_CACHE_DURATION)) {
                renderAppsList(cachedAppsData);
                appsLoading.style.display = 'none';
                appsList.style.display = 'block';
                return;
            }

            if (typeof Android !== 'undefined' && Android.getAllApps) {
                try {
                    const appsJson = Android.getAllApps();
                    const appsData = JSON.parse(appsJson);
                    cachedAppsData = appsData;
                    lastAppListLoadTime = Date.now();
                    renderAppsList(appsData);
                    appsLoading.style.display = 'none';
                    appsList.style.display = 'block';
                } catch (e) {
                    console.error('加载应用列表失败:', e);
                    appsLoading.style.display = 'none';
                    appsList.style.display = 'block';
                    renderAppsList(generateAppData());
                }
            } else {
                appsLoading.style.display = 'none';
                appsList.style.display = 'block';
                renderAppsList(generateAppData());
            }
        }

        if (appsBtn) {
            appsBtn.addEventListener('click', function () {
                if (appsModal.style.display !== 'none') {
                    hideAppsModal();
                } else {
                    showAppsModal();
                    appsList.style.display = 'none';
                    appsLoading.style.display = 'block';
                    setTimeout(() => {
                        loadAppList();
                    }, 1);
                }
            });
        }

        if (closeApps) {
            closeApps.addEventListener('click', hideAppsModal);
        }

        appsModal.addEventListener('click', function (event) {
            if (appsModal.style.display !== 'none') {
                const interactive = event.target.closest('.app-tab, .app-search input, .app-item, .app-icon, .app-name, #closeApps, .close-btn');
                if (!interactive) {
                    hideAppsModal();
                }
            }
        });

        const categoryTabs = document.querySelectorAll('.app-tab');
        categoryTabs.forEach(tab => {
            tab.addEventListener('click', function () {
                categoryTabs.forEach(t => t.classList.remove('active'));
                this.classList.add('active');
                currentAppCategory = this.dataset.category;
                filterAppList();
            });
        });

        if (appSearch) {
            appSearch.addEventListener('input', filterAppList);
        }

        function filterAppList() {
            if (!appSearch) return;
            const searchTerm = appSearch.value.toLowerCase();
            const appItems = document.querySelectorAll('.app-item');

            appItems.forEach(item => {
                const appName = item.querySelector('.app-name').textContent.toLowerCase();
                const isSystemApp = item.dataset.isSystemApp === 'true';

                let categoryMatch = true;
                if (currentAppCategory === 'user') {
                    categoryMatch = !isSystemApp;
                } else if (currentAppCategory === 'system') {
                    categoryMatch = isSystemApp;
                }

                const searchMatch = appName.includes(searchTerm);

                item.style.display = (categoryMatch && searchMatch) ? 'flex' : 'none';
            });

            updateAlphabetNavVisibility();
        }

        function updateAlphabetNavVisibility() {
            const alphabetList = document.getElementById('alphabetList');
            if (!alphabetList) return;

            const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

            alphabet.forEach(letter => {
                const section = document.getElementById(`section-${letter}`);
                const visibleItems = section ? section.querySelectorAll('.app-item[style*="display: flex"]') : [];
                const navItem = alphabetList.querySelector(`[data-letter="${letter}"]`);

                if (navItem) {
                    navItem.style.display = visibleItems.length > 0 ? 'block' : 'none';
                }
            });
        }
    }

    function showAddToQuickAppsDialog(app) {
        const dialog = document.createElement('div');
        dialog.className = 'modal-overlay';
        dialog.innerHTML = `
            <div class="modal-content">
                <h3>添加到快速启动</h3>
                <p>确定将 "${escapeHtml(app.name)}" 添加到快速启动栏吗？</p>
                <div class="modal-buttons">
                    <button id="cancelBtn">取消</button>
                    <button id="confirmBtn">确定</button>
                </div>
            </div>
        `;

        document.body.appendChild(dialog);

        const confirmBtn = dialog.querySelector('#confirmBtn');
        const cancelBtn = dialog.querySelector('#cancelBtn');

        function closeDialogFunc() {
            dialog.classList.add('closing');
            setTimeout(() => {
                if (dialog.parentNode) {
                    dialog.parentNode.removeChild(dialog);
                }
            }, 300);
        }

        cancelBtn.addEventListener('click', closeDialogFunc);

        confirmBtn.addEventListener('click', function () {
            if (typeof Android !== 'undefined' && Android.addQuickApp) {
                Android.addQuickApp(app.name, app.packageName, app.icon || '');
                if (typeof loadQuickApps === 'function') {
                    loadQuickApps();
                }
            }
            closeDialogFunc();
        });

        dialog.addEventListener('click', function (event) {
            if (event.target === dialog) {
                closeDialogFunc();
            }
        });

        dialog.querySelector('.modal-content').addEventListener('click', function (event) {
            event.stopPropagation();
        });
    }

    function showRemoveFromQuickAppsDialog(app) {
        const dialog = document.createElement('div');
        dialog.className = 'modal-overlay';
        dialog.innerHTML = `
            <div class="modal-content">
                <h3>移除快速启动</h3>
                <p>确定将 "${escapeHtml(app.name)}" 从快速启动栏移除吗？</p>
                <div class="modal-buttons">
                    <button id="cancelBtn">取消</button>
                    <button id="confirmBtn">确定</button>
                </div>
            </div>
        `;

        document.body.appendChild(dialog);

        const confirmBtn = dialog.querySelector('#confirmBtn');
        const cancelBtn = dialog.querySelector('#cancelBtn');

        function closeDialogFunc() {
            dialog.classList.add('closing');
            setTimeout(() => {
                if (dialog.parentNode) {
                    dialog.parentNode.removeChild(dialog);
                }
            }, 300);
        }

        cancelBtn.addEventListener('click', closeDialogFunc);

        confirmBtn.addEventListener('click', function () {
            if (typeof Android !== 'undefined' && Android.removeQuickApp) {
                Android.removeQuickApp(app.packageName);
                if (typeof loadQuickApps === 'function') {
                    loadQuickApps();
                }
            }
            closeDialogFunc();
        });

        dialog.addEventListener('click', function (event) {
            if (event.target === dialog) {
                closeDialogFunc();
            }
        });

        dialog.querySelector('.modal-content').addEventListener('click', function (event) {
            event.stopPropagation();
        });
    }

    return {
        initAppsModal,
        renderAppsList,
        escapeHtml,
        normalizeAppIcon,
        initAlphabetNav,
        showAddToQuickAppsDialog,
        showRemoveFromQuickAppsDialog
    };
})();

// 显式挂载到 window，使 index.js 适配层的 if (window.AppListManager) 判断生效
window.AppListManager = AppListManager;
