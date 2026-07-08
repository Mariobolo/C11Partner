const AppBootstrap = (function() {
    const state = {
        isLoadingSettings: false,
        categoryCheckboxEventsInitialized: false,
        componentConfigListenersAdded: false,
        currentDialog: null,
        isConfigurableButtonsInitialized: false
    };

    function debounce(fn, delay = 300) {
        let timer = null;
        return function (...args) {
            if (timer) clearTimeout(timer);
            timer = setTimeout(() => {
                fn.apply(this, args);
            }, delay);
        };
    }

    function throttle(fn, interval = 300) {
        let lastTime = 0;
        return function (...args) {
            const now = Date.now();
            if (now - lastTime >= interval) {
                lastTime = now;
                fn.apply(this, args);
            }
        };
    }

    function addDebouncedClick(element, handler, delay = 300, useThrottle = false) {
        if (!element) return;
        const wrappedHandler = useThrottle ? throttle(handler, delay) : debounce(handler, delay);
        element.addEventListener('click', wrappedHandler);
    }

    function safeInit(name, fn) {
        try {
            fn();
        } catch (e) {
            console.error('[Init] ' + name + ' 失败:', e);
        }
    }

    const initQueue = [];

    function registerInit(name, fn) {
        initQueue.push({ name, fn });
    }

    function runInit() {
        const backgroundElement = document.querySelector('.background-image');
        if (backgroundElement) {
            backgroundElement.style.backgroundImage = "url('images/default_bg_1.jpg')";
        }

        initQueue.forEach(item => {
            safeInit(item.name, item.fn);
        });

        if (typeof updateNetworkAndBluetoothStatus === 'function') {
            updateNetworkAndBluetoothStatus();
        }

        const statusIntervalId = setInterval(function() {
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
        state,
        debounce,
        throttle,
        addDebouncedClick,
        safeInit,
        registerInit
    };

    return window.AppBootstrap;
})();
