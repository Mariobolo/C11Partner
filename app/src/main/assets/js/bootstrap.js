const AppBootstrap = (function() {
    const initQueue = [];

    function safeInit(name, fn) {
        try {
            fn();
        } catch (e) {
            console.error('[Init] ' + name + ' 失败:', e);
        }
    }

    function registerInit(name, fn) {
        initQueue.push({ name: name, fn: fn });
    }

    function runInit() {
        var backgroundElement = document.querySelector('.background-image');
        if (backgroundElement) {
            backgroundElement.style.backgroundImage = "url('images/default_bg_1.jpg')";
        }

        for (var i = 0; i < initQueue.length; i++) {
            safeInit(initQueue[i].name, initQueue[i].fn);
        }

        if (typeof updateNetworkAndBluetoothStatus === 'function') {
            updateNetworkAndBluetoothStatus();
        }

        var statusIntervalId = setInterval(function() {
            if (typeof updateNetworkAndBluetoothStatus === 'function') {
                updateNetworkAndBluetoothStatus();
            }
        }, 5000);

        if (window._musicProgressInterval) setInterval(window._musicProgressInterval, 2000);

        window.addEventListener('beforeunload', function() {
            clearInterval(statusIntervalId);
            if (window._musicProgressInterval) clearInterval(window._musicProgressInterval);
        });
    }

    window.addEventListener('load', runInit);

    window.AppBootstrap = {
        safeInit: safeInit,
        registerInit: registerInit
    };

    return window.AppBootstrap;
})();
