// 显示地图应用选择对话框
function showMapAppSelectionDialog(buttonId) {
    // 创建对话框元素
    const dialog = document.createElement('div');
    dialog.className = 'modal';
    dialog.style.display = 'block';
    dialog.innerHTML = `
        <div class="modal-content" style="width: 300px; height: auto; margin: 15% auto;">
            <div class="modal-header" style="padding: 15px; background-color: rgba(30, 30, 30, 0.8);">
                <span class="close" id="closeDialog" style="float: right; font-size: 28px; font-weight: bold; cursor: pointer;">&times;</span>
                <h2 style="margin: 0; font-size: 18px;">选择地图应用</h2>
            </div>
            <div class="modal-body" style="padding: 20px; text-align: center;">
                <p style="margin: 10px 0; font-size: 14px; color: #aaa;">请选择要配置的地图应用</p>
                <div id="appListContainer" style="max-height: 300px; overflow-y: auto; margin: 10px 0;">
                    <!-- 应用列表将通过JavaScript动态生成 -->
                </div>
                <div style="margin-top: 20px;">
                    <button id="cancelBtn" class="setting-button" style="margin: 0 10px; background-color: #666;">取消</button>
                </div>
            </div>
        </div>
    `;

    // 添加到页面
    document.body.appendChild(dialog);

    // 获取按钮元素
    const closeDialog = dialog.querySelector('#closeDialog');
    const cancelBtn = dialog.querySelector('#cancelBtn');
    const appListContainer = dialog.querySelector('#appListContainer');

    // 关闭对话框函数
    function closeDialogFunc() {
        document.body.removeChild(dialog);
    }

    // 绑定事件
    closeDialog.addEventListener('click', closeDialogFunc);
    cancelBtn.addEventListener('click', closeDialogFunc);

    // 点击对话框外部区域关闭
    dialog.addEventListener('click', function (event) {
        if (event.target === dialog) {
            closeDialogFunc();
        }
    });

    // 加载地图应用列表
    loadMapAppListForSelection(buttonId, appListContainer);
}

// 加载地图应用列表用于选择
function loadMapAppListForSelection(buttonId, container) {
    // 清空现有内容
    container.innerHTML = '<p>加载中...</p>';

    if (typeof Android !== 'undefined' && Android.getAppList) {
        try {
            // 从原生代码获取应用列表
            const appListJson = Android.getAppList();
            const appsData = JSON.parse(appListJson);

            // 生成地图应用列表
            generateMapAppListForSelection(buttonId, appsData, container);
        } catch (e) {
            container.innerHTML = '<p>加载应用列表失败</p>';
        }
    } else {
        // 非Android环境，使用模拟数据
        const mockAppsData = generateMapAppData();
        generateMapAppListForSelection(buttonId, mockAppsData, container);
    }
}

// 生成地图应用列表用于选择
function generateMapAppListForSelection(buttonId, appsData, container) {
    // 清空现有内容
    container.innerHTML = '';

    // 创建应用列表
    const appList = document.createElement('div');
    appList.style.display = 'grid';
    appList.style.gridTemplateColumns = 'repeat(3, 1fr)';
    appList.style.gap = '10px';
    appList.style.marginTop = '10px';

    // 定义地图相关关键词
    const mapKeywords = ['地图', '导航', 'Map', 'Navigation', '高德', '百度', '腾讯', '谷歌'];

    // 遍历所有应用，筛选地图相关应用
    let mapApps = [];
    for (const letter in appsData) {
        if (appsData.hasOwnProperty(letter) && appsData[letter].length > 0) {
            appsData[letter].forEach(app => {
                // 检查应用名称是否包含地图相关关键词
                const appName = app.name.toLowerCase();
                const isMapApp = mapKeywords.some(keyword =>
                    appName.includes(keyword.toLowerCase())
                );

                if (isMapApp) {
                    mapApps.push(app);
                }
            });
        }
    }

    // 如果没有找到地图应用，则显示前12个应用
    if (mapApps.length === 0) {
        for (const letter in appsData) {
            if (appsData.hasOwnProperty(letter) && appsData[letter].length > 0 && mapApps.length < 12) {
                appsData[letter].forEach(app => {
                    if (mapApps.length < 12) {
                        mapApps.push(app);
                    }
                });
            }
        }
    }

    // 生成应用列表
    mapApps.forEach(app => {
        const appItem = document.createElement('div');
        appItem.className = 'app-item';
        appItem.style.display = 'flex';
        appItem.style.flexDirection = 'column';
        appItem.style.alignItems = 'center';
        appItem.style.cursor = 'pointer';
        appItem.style.padding = '10px';
        appItem.style.borderRadius = '5px';
        appItem.style.transition = 'background-color 0.3s';

        appItem.innerHTML = `
            <div class="app-icon" style="background-image: url('${app.icon}'); width: 50px; height: 50px; background-size: contain; background-repeat: no-repeat; background-position: center; margin-bottom: 5px;"></div>
            <div class="app-name" style="font-size: 12px; text-align: center; word-break: break-word; max-width: 80px;">${app.name}</div>
        `;

        // 添加点击事件
        appItem.addEventListener('click', function () {
            // 选择地图应用
            // 保存配置的应用
            if (typeof Android !== 'undefined' && Android.saveConfigApp) {
                try {
                    Android.saveConfigApp(buttonId, app.name, app.packageName, app.icon);
                    // 地图应用配置已保存

                    // 关闭对话框
                    const dialog = document.querySelector('.modal');
                    if (dialog) {
                        document.body.removeChild(dialog);
                    }

                    // 显示成功提示
                    showSuccessMessage(`已配置${getButtonDisplayName(buttonId)}为${app.name}`);
                } catch (e) {
                    showErrorMessage('配置应用失败');
                }
            }
        });

        // 添加悬停效果
        appItem.addEventListener('mouseenter', function () {
            this.style.backgroundColor = 'rgba(255, 255, 255, 0.1)';
        });

        appItem.addEventListener('mouseleave', function () {
            this.style.backgroundColor = 'transparent';
        });

        appList.appendChild(appItem);
    });

    container.appendChild(appList);
}

// 生成地图应用模拟数据
function generateMapAppData() {
    // 模拟地图应用数据
    const mapApps = {
        'A': [
            { name: '高德地图', packageName: 'com.autonavi.minimap', icon: 'images/ic_launcher.png' },
            { name: '百度地图', packageName: 'com.autonavi.minimap', icon: 'images/ic_launcher.png' }
        ],
        'B': [
            { name: '腾讯地图', packageName: 'com.tencent.map', icon: 'images/ic_launcher.png' },
            { name: '谷歌地图', packageName: 'com.google.android.apps.maps', icon: 'images/ic_launcher.png' }
        ],
        'C': [
            { name: '苹果地图', packageName: 'com.apple.Maps', icon: 'images/ic_launcher.png' }
        ]
    };

    return mapApps;
}