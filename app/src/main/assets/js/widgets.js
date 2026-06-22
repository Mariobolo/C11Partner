/**
 * Widgets - 桌面小组件管理模块
 * 
 * 功能说明：
 * 1. 组件注册/销毁机制
 * 2. 数据更新机制
 * 3. 组件生命周期管理
 * 4. 通用渲染工具函数
 * 5. 组件状态管理
 * 
 * 使用方式：
 * - Widgets.register(id, widget) - 注册组件
 * - Widgets.unregister(id) - 注销组件
 * - Widgets.get(id) - 获取组件实例
 * - Widgets.update(id, data) - 更新组件数据
 * - Widgets.render(id) - 渲染组件
 * - Widgets.renderAll() - 渲染所有组件
 * 
 * 组件接口规范：
 * {
 *   id: string,           // 组件唯一标识
 *   element: HTMLElement, // 组件DOM元素
 *   data: object,         // 组件数据
 *   init: function,       // 初始化方法
 *   render: function,     // 渲染方法
 *   update: function,     // 更新数据方法
 *   destroy: function     // 销毁方法
 * }
 */

(function() {
    'use strict';

    /**
     * 小组件管理模块
     */
    const Widgets = {
        // 已注册的组件
        widgets: {},
        
        // 组件状态
        states: {},

        /**
         * 注册组件
         * 
         * @param {string} id - 组件唯一标识
         * @param {object} widget - 组件实例
         * @returns {boolean} 是否注册成功
         */
        register: function(id, widget) {
            if (!id || !widget) {
                console.warn('Widgets.register: 组件ID和实例不能为空');
                return false;
            }

            if (this.widgets[id]) {
                console.warn(`Widgets.register: 组件 ${id} 已存在`);
                return false;
            }

            this.widgets[id] = widget;
            this.states[id] = {
                initialized: false,
                rendered: false,
                visible: true
            };

            // 如果组件有init方法，自动初始化
            if (typeof widget.init === 'function') {
                try {
                    widget.init();
                    this.states[id].initialized = true;
                } catch (e) {
                    console.error(`Widgets.register: 组件 ${id} 初始化失败`, e);
                }
            }

            return true;
        },

        /**
         * 注销组件
         * 
         * @param {string} id - 组件唯一标识
         * @returns {boolean} 是否注销成功
         */
        unregister: function(id) {
            if (!this.widgets[id]) {
                console.warn(`Widgets.unregister: 组件 ${id} 不存在`);
                return false;
            }

            const widget = this.widgets[id];

            // 如果组件有destroy方法，调用销毁
            if (typeof widget.destroy === 'function') {
                try {
                    widget.destroy();
                } catch (e) {
                    console.error(`Widgets.unregister: 组件 ${id} 销毁失败`, e);
                }
            }

            // 清理
            delete this.widgets[id];
            delete this.states[id];

            return true;
        },

        /**
         * 获取组件实例
         * 
         * @param {string} id - 组件唯一标识
         * @returns {object|null} 组件实例
         */
        get: function(id) {
            return this.widgets[id] || null;
        },

        /**
         * 检查组件是否存在
         * 
         * @param {string} id - 组件唯一标识
         * @returns {boolean} 是否存在
         */
        has: function(id) {
            return !!this.widgets[id];
        },

        /**
         * 更新组件数据
         * 
         * @param {string} id - 组件唯一标识
         * @param {object} data - 新数据
         * @param {boolean} autoRender - 是否自动渲染，默认true
         * @returns {boolean} 是否更新成功
         */
        update: function(id, data, autoRender = true) {
            const widget = this.widgets[id];
            
            if (!widget) {
                console.warn(`Widgets.update: 组件 ${id} 不存在`);
                return false;
            }

            // 如果组件有update方法，调用更新
            if (typeof widget.update === 'function') {
                try {
                    widget.update(data);
                } catch (e) {
                    console.error(`Widgets.update: 组件 ${id} 更新失败`, e);
                    return false;
                }
            } else {
                // 否则直接合并数据
                widget.data = { ...widget.data, ...data };
            }

            // 自动渲染
            if (autoRender) {
                this.render(id);
            }

            return true;
        },

        /**
         * 渲染组件
         * 
         * @param {string} id - 组件唯一标识
         * @returns {boolean} 是否渲染成功
         */
        render: function(id) {
            const widget = this.widgets[id];
            
            if (!widget) {
                console.warn(`Widgets.render: 组件 ${id} 不存在`);
                return false;
            }

            // 如果组件有render方法，调用渲染
            if (typeof widget.render === 'function') {
                try {
                    widget.render();
                    this.states[id].rendered = true;
                    return true;
                } catch (e) {
                    console.error(`Widgets.render: 组件 ${id} 渲染失败`, e);
                    return false;
                }
            }

            return false;
        },

        /**
         * 渲染所有组件
         * 
         * @returns {number} 成功渲染的组件数量
         */
        renderAll: function() {
            let count = 0;
            
            Object.keys(this.widgets).forEach(id => {
                if (this.render(id)) {
                    count++;
                }
            });

            return count;
        },

        /**
         * 显示组件
         * 
         * @param {string} id - 组件唯一标识
         * @returns {boolean} 是否成功
         */
        show: function(id) {
            const widget = this.widgets[id];
            
            if (!widget) {
                return false;
            }

            if (widget.element) {
                widget.element.style.display = '';
            }

            this.states[id].visible = true;
            return true;
        },

        /**
         * 隐藏组件
         * 
         * @param {string} id - 组件唯一标识
         * @returns {boolean} 是否成功
         */
        hide: function(id) {
            const widget = this.widgets[id];
            
            if (!widget) {
                return false;
            }

            if (widget.element) {
                widget.element.style.display = 'none';
            }

            this.states[id].visible = false;
            return true;
        },

        /**
         * 切换组件显示/隐藏
         * 
         * @param {string} id - 组件唯一标识
         * @returns {boolean} 切换后的显示状态
         */
        toggle: function(id) {
            if (this.states[id]?.visible) {
                this.hide(id);
                return false;
            } else {
                this.show(id);
                return true;
            }
        },

        /**
         * 获取组件状态
         * 
         * @param {string} id - 组件唯一标识
         * @returns {object|null} 组件状态
         */
        getState: function(id) {
            return this.states[id] || null;
        },

        /**
         * 获取所有已注册的组件ID
         * 
         * @returns {Array} 组件ID数组
         */
        getAllIds: function() {
            return Object.keys(this.widgets);
        },

        /**
         * 获取已注册组件数量
         * 
         * @returns {number} 组件数量
         */
        count: function() {
            return Object.keys(this.widgets).length;
        },

        /**
         * 批量更新多个组件的数据
         * 
         * @param {object} dataMap - 组件ID到数据的映射 { id1: data1, id2: data2 }
         * @param {boolean} autoRender - 是否自动渲染，默认true
         * @returns {number} 成功更新的组件数量
         */
        batchUpdate: function(dataMap, autoRender = true) {
            let count = 0;
            
            Object.entries(dataMap).forEach(([id, data]) => {
                if (this.update(id, data, autoRender)) {
                    count++;
                }
            });

            return count;
        },

        // ==================== 工具函数 ====================

        /**
         * 创建组件元素
         * 
         * @param {string} tag - HTML标签名
         * @param {object} options - 选项 { className, id, text, html }
         * @returns {HTMLElement} 创建的元素
         */
        createElement: function(tag, options = {}) {
            const element = document.createElement(tag);
            
            if (options.className) {
                element.className = options.className;
            }
            
            if (options.id) {
                element.id = options.id;
            }
            
            if (options.text !== undefined) {
                element.textContent = options.text;
            }
            
            if (options.html !== undefined) {
                element.innerHTML = options.html;
            }

            return element;
        },

        /**
         * 安全设置元素文本
         * 
         * @param {HTMLElement} element - 目标元素
         * @param {string} text - 文本内容
         */
        setText: function(element, text) {
            if (element) {
                element.textContent = text !== undefined && text !== null ? String(text) : '';
            }
        },

        /**
         * 安全设置元素HTML
         * 
         * @param {HTMLElement} element - 目标元素
         * @param {string} html - HTML内容
         */
        setHtml: function(element, html) {
            if (element) {
                element.innerHTML = html || '';
            }
        },

        /**
         * 安全切换元素类名
         * 
         * @param {HTMLElement} element - 目标元素
         * @param {string} className - 类名
         * @param {boolean} force - 强制添加或移除
         */
        toggleClass: function(element, className, force) {
            if (element && className) {
                element.classList.toggle(className, force);
            }
        },

        /**
         * 格式化数字（添加千分位）
         * 
         * @param {number} num - 数字
         * @param {number} decimals - 小数位数
         * @returns {string} 格式化后的字符串
         */
        formatNumber: function(num, decimals = 0) {
            if (num === undefined || num === null || isNaN(num)) {
                return '--';
            }
            
            return Number(num).toLocaleString('zh-CN', {
                minimumFractionDigits: decimals,
                maximumFractionDigits: decimals
            });
        },

        /**
         * 格式化温度
         * 
         * @param {number} temp - 温度值
         * @param {string} unit - 单位，默认°C
         * @returns {string} 格式化后的温度
         */
        formatTemperature: function(temp, unit = '°') {
            if (temp === undefined || temp === null || isNaN(temp)) {
                return '--' + unit;
            }
            
            return Math.round(temp) + unit;
        },

        /**
         * 格式化百分比
         * 
         * @param {number} value - 百分比值（0-1或0-100）
         * @param {number} decimals - 小数位数
         * @returns {string} 格式化后的百分比
         */
        formatPercent: function(value, decimals = 0) {
            if (value === undefined || value === null || isNaN(value)) {
                return '--%';
            }
            
            // 如果值小于等于1，认为是小数形式（0.5 = 50%）
            const percent = value <= 1 ? value * 100 : value;
            
            return percent.toFixed(decimals) + '%';
        }
    };

    // 暴露到全局
    window.Widgets = Widgets;

})();
