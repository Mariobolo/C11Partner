// 显示滑动提示
function showSwipeHint(message) {
    
    // 检查是否已存在提示元素，如果存在则移除
    const existingHint = document.getElementById('swipeHint');
    if (existingHint) {
        existingHint.remove();
    }
    
    // 创建提示元素
    const hintElement = document.createElement('div');
    hintElement.id = 'swipeHint';
    hintElement.textContent = message;
    hintElement.style.cssText = `
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background-color: rgba(0, 0, 0, 0.7);
        color: white;
        padding: 15px 25px;
        border-radius: 8px;
        font-size: 16px;
        z-index: 1000;
        pointer-events: none;
        opacity: 0;
        transition: opacity 0.3s;
        text-align: center;
        min-width: 120px;
    `;
    
    // 添加到页面
    document.body.appendChild(hintElement);
    
    // 显示提示
    setTimeout(() => {
        hintElement.style.opacity = '1';
    }, 10);
    
    // 1.5秒后隐藏并移除提示
    setTimeout(() => {
        hintElement.style.opacity = '0';
        setTimeout(() => {
            if (hintElement.parentNode) {
                hintElement.parentNode.removeChild(hintElement);
            }
        }, 300);
    }, 1500);
}

// 显示Toast提示
function showToast(message, duration = 2000) {
    
    // 检查是否已存在Toast元素，如果存在则移除
    const existingToast = document.getElementById('toastMessage');
    if (existingToast) {
        existingToast.remove();
    }
    
    // 创建Toast元素
    const toastElement = document.createElement('div');
    toastElement.id = 'toastMessage';
    toastElement.textContent = message;
    toastElement.style.cssText = `
        position: fixed;
        bottom: 100px;
        left: 50%;
        transform: translateX(-50%);
        background-color: rgba(0, 0, 0, 0.7);
        color: white;
        padding: 12px 20px;
        border-radius: 20px;
        font-size: 14px;
        z-index: 1000;
        pointer-events: none;
        opacity: 0;
        transition: opacity 0.3s;
        text-align: center;
        min-width: 100px;
        max-width: 80%;
        word-wrap: break-word;
    `;
    
    // 添加到页面
    document.body.appendChild(toastElement);
    
    // 显示Toast
    setTimeout(() => {
        toastElement.style.opacity = '1';
    }, 10);
    
    // 指定时间后隐藏并移除Toast
    setTimeout(() => {
        toastElement.style.opacity = '0';
        setTimeout(() => {
            if (toastElement.parentNode) {
                toastElement.parentNode.removeChild(toastElement);
            }
        }, 300);
    }, duration);
}


// 显示成功消息
function showSuccessMessage(message) {
    showMessage(message, '#4CAF50');
}

// 显示错误消息
function showErrorMessage(message) {
    showMessage(message, '#F44336');
}

// 显示消息
function showMessage(message, backgroundColor) {
    // 创建消息元素
    const messageElement = document.createElement('div');
    messageElement.textContent = message;
    messageElement.style.cssText = `
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background-color: ${backgroundColor};
        color: white;
        padding: 15px 25px;
        border-radius: 5px;
        font-size: 16px;
        z-index: 10000;
        pointer-events: none;
        opacity: 0;
        transition: opacity 0.3s;
    `;

    // 添加到页面
    document.body.appendChild(messageElement);

    // 显示消息
    setTimeout(() => {
        messageElement.style.opacity = '1';
    }, 10);

    // 2秒后隐藏并移除消息
    setTimeout(() => {
        messageElement.style.opacity = '0';
        setTimeout(() => {
            if (messageElement.parentNode) {
                messageElement.parentNode.removeChild(messageElement);
            }
        }, 300);
    }, 2000);
}