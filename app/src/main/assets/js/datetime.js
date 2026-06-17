// 更新时间显示
function updateTime() {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    
    // 更新顶部状态栏的时间显示（仅时分秒）
    const topTimeText = `${hours}:${minutes}:${seconds}`;
    document.querySelector('.top-left-time .time-text').textContent = topTimeText;
    
    // 更新原来的时间显示（仅时分秒）
    document.querySelector('.layout-left .time-text').textContent = topTimeText;
}

// 获取星期几
function getWeekDay(date) {
    const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
    return weekdays[date.getDay()];
}

// 更新日期显示
function updateDate() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    
    // 获取星期几
    const weekDay = getWeekDay(now);
    
    // 格式化日期显示
    const dateStr = `${year}年${month}月${day}日 ${weekDay}`;
    
    // 更新日期显示元素
    document.getElementById('dateDisplay').textContent = dateStr;
}

// 更新农历显示
function updateLunarDate() {
    try {
        // 使用原生Java方法获取准确的农历日期
        if (typeof Android !== 'undefined' && Android.getLunarCalendar) {
            const lunarDate = Android.getLunarCalendar();
            document.getElementById('lunarDisplay').textContent = lunarDate;
        } else {
            // 如果无法调用原生方法，使用简化的计算方法作为备选
            const now = new Date();
            const lunarDate = getLunarDate(now);
            document.getElementById('lunarDisplay').textContent = lunarDate;
        }
    } catch (error) {
        console.error('获取农历日期时出错:', error);
        // 出错时显示默认文本
        document.getElementById('lunarDisplay').textContent = '农历日期获取失败';
    }
}

// 农历天干地支纪年法
function getLunarYear(year) {
    const tianGan = ["甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"];
    const diZhi = ["子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"];
    const animals = ["鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"];
    
    const idx = (year - 4) % 60; // 以1904年（甲子年）为基准
    const ganIdx = idx % 10;
    const zhiIdx = idx % 12;
    
    return tianGan[ganIdx] + diZhi[zhiIdx] + "年" + "(" + animals[zhiIdx] + ")";
}

// 农历计算函数（简化的备选方法）
function getLunarDate(date) {
    // 简化的农历计算（实际项目中可以使用更精确的算法或库）
    // 这里提供一个基础的实现
    const lunarInfo = [
        0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
        0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
        0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
        0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
        0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
        0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
        0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
        0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
        0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
        0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
        0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
        0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
        0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
        0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0
    ];
    
    const monthNames = ["正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊"];
    const dayNames = [
        "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
        "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
        "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
    ];
    
    // 获取公历年份
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    
    // 简化的农历年份计算（实际应该使用精确算法）
    // 这里假设农历新年在一月底或二月底
    let lunarYear = year;
    if (month < 2 || (month === 2 && day < 10)) {
        lunarYear = year - 1;
    }
    
    // 获取天干地支年份
    const lunarYearName = getLunarYear(lunarYear);
    
    // 简化的农历月份和日期计算
    const lunarMonthIndex = (month + 10) % 12;
    const lunarDayIndex = (day + 19) % 30;
    
    // 确保索引在有效范围内
    const safeMonthIndex = Math.max(0, Math.min(lunarMonthIndex, 11));
    const safeDayIndex = Math.max(0, Math.min(lunarDayIndex, 29));
    
    return lunarYearName + " " + monthNames[safeMonthIndex] + "月" + dayNames[safeDayIndex];
}