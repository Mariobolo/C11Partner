
// 页面加载时立即显示默认天气
document.addEventListener('DOMContentLoaded', function() {
    initWeatherDisplay();
});

// 初始化天气显示（默认占位）
function initWeatherDisplay() {
    const temperatureElement = document.querySelector('.temperature');
    const conditionElement = document.querySelector('.condition');
    const weatherIconElement = document.querySelector('.weather-icon');

    if (temperatureElement) {
        temperatureElement.textContent = '--°';
    }
    if (conditionElement) {
        conditionElement.textContent = '加载中...';
    }
    if (weatherIconElement) {
        weatherIconElement.style.backgroundImage = "url('images/nav_weather.png')";
    }
}

// 天气API配置
const WEATHER_API_BASE = 'https://api.open-meteo.com/v1/forecast';

// 更新天气信息
function updateWeatherInfo() {

    // 使用默认位置（北京）
    fetchWeatherData('34.44', '115.65');

}

// 获取天气数据
function fetchWeatherData(lat, lon) {
    // 使用新的API接口
    const url = `${WEATHER_API_BASE}?latitude=${lat}&longitude=${lon}&current_weather=true&hourly=temperature_2m,relativehumidity_2m,precipitation`;

    // 首先尝试使用fetch API
    if (typeof fetch !== 'undefined') {
        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                updateWeatherDisplay(data);
            })
            .catch(error => {
                // 如果fetch失败，尝试使用XMLHttpRequest
                fetchWeatherDataWithXHR(url);
            });
    } else {
        // 如果fetch不可用，使用XMLHttpRequest
        fetchWeatherDataWithXHR(url);
    }
}

// 使用XMLHttpRequest获取天气数据
function fetchWeatherDataWithXHR(url) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);

    xhr.onload = function () {
        if (xhr.status >= 200 && xhr.status < 300) {
            try {
                const data = JSON.parse(xhr.responseText);
                updateWeatherDisplay(data);
            } catch (e) {
                updateWeatherDisplay(null);
            }
        } else {
            updateWeatherDisplay(null);
        }
    };

    xhr.onerror = function () {
        updateWeatherDisplay(null);
    };

    xhr.send();
}

// 更新天气显示
function updateWeatherDisplay(weatherData) {
    const temperatureElement = document.querySelector('.temperature');
    const conditionElement = document.querySelector('.condition');
    const weatherIconElement = document.querySelector('.weather-icon');

    if (weatherData && weatherData.current_weather) {
        // 更新温度
        if (temperatureElement) {
            const temperature = Math.round(weatherData.current_weather.temperature);
            temperatureElement.textContent = `${temperature}°`;
        }

        // 更新天气状况
        if (conditionElement) {
            // 根据天气代码获取天气描述
            const weatherCode = weatherData.current_weather.weathercode;
            const weatherDescription = getWeatherDescription(weatherCode);
            conditionElement.textContent = weatherDescription;
        }

        // 更新天气图标
        if (weatherIconElement) {
            // 根据天气代码选择合适的图标
            const weatherCode = weatherData.current_weather.weathercode;
            const weatherIcon = getWeatherIcon(weatherCode);
            weatherIconElement.style.backgroundImage = `url('images/${weatherIcon}')`;
        }
    } else {
        // 获取失败，显示占位
        if (temperatureElement) {
            temperatureElement.textContent = '--°';
        }

        if (conditionElement) {
            conditionElement.textContent = '暂无数据';
        }

        if (weatherIconElement) {
            weatherIconElement.style.backgroundImage = "url('images/nav_weather.png')";
            weatherIconElement.style.opacity = '0.6';
        }
    }
}

// 根据天气代码获取天气描述
function getWeatherDescription(weatherCode) {
    const descriptionMap = {
        0: '晴天',
        1: '多云',
        2: '阴天',
        3: '有雾',
        45: '有雾',
        48: '有雾',
        51: '小雨',
        53: '中雨',
        55: '大雨',
        56: '冻雨',
        57: '冻雨',
        61: '小雨',
        63: '中雨',
        65: '大雨',
        66: '冻雨',
        67: '冻雨',
        71: '小雪',
        73: '中雪',
        75: '大雪',
        77: '阵雪',
        80: '小雨',
        81: '中雨',
        82: '大雨',
        85: '小雪',
        86: '大雪',
        95: '雷雨',
        96: '雷雨伴冰雹',
        99: '雷雨伴冰雹'
    };

    return descriptionMap[weatherCode] || '未知';
}

// 根据天气代码获取对应的图标
function getWeatherIcon(weatherCode) {
    // 根据天气代码分组
    if (weatherCode === 0) {
        return 'nav_weather_sunny.png'; // 晴天
    } else if (weatherCode === 1 || weatherCode === 2) {
        return 'nav_weather_cloudy.png'; // 多云/阴天
    } else if (weatherCode === 3 || weatherCode === 45 || weatherCode === 48) {
        return 'nav_weather_foggy.png'; // 雾
    } else if ([51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82].includes(weatherCode)) {
        return 'nav_weather_rainy.png'; // 雨
    } else if ([71, 73, 75, 77, 85, 86].includes(weatherCode)) {
        return 'nav_weather_snowy.png'; // 雪
    } else if ([95, 96, 99].includes(weatherCode)) {
        return 'nav_weather_thunderstorm.png'; // 雷雨
    } else {
        return 'nav_weather.png'; // 默认
    }
}