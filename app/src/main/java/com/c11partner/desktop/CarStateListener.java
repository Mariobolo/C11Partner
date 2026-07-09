package com.c11partner.desktop;

/**
 * 车辆状态变化监听接口
 */
public interface CarStateListener {
    
    /**
     * 档位变化回调
     * @param oldGear 旧档位
     * @param newGear 新档位
     */
    void onGearChanged(int oldGear, int newGear);
    
    /**
     * 转向灯变化回调
     * @param isLeft 是否是左转向灯
     * @param state 状态 0=关, 1=开
     */
    void onTurnLightChanged(boolean isLeft, int state);
    
    /**
     * 车门状态变化回调
     * @param doorName 车门名称
     * @param state 状态 0=关, 1=开
     */
    void onDoorChanged(String doorName, int state);
    
    /**
     * 车速变化回调
     * @param speed 车速 km/h
     */
    void onSpeedChanged(float speed);
    
    /**
     * 锁车状态变化回调
     * @param isLocked 是否上锁
     */
    void onLockStateChanged(boolean isLocked);
    
    /**
     * 需要启动360全景的回调
     * @param reason 触发原因
     */
    void onNeedStart360(String reason);

    /**
     * 胎压变化回调（仅通知，需调用 getCurrentState 获取完整数据）
     * @param pos 轮胎位置 0=左前,1=右前,2=左后,3=右后
     */
    void onTirePressureChanged(int pos);

    /**
     * 天窗状态变化回调
     * @param state 状态 3=开, 4=关（实车日志确认）
     */
    void onSunroofChanged(int state);

    /**
     * 遮阳帘状态变化回调
     * @param state 状态 3=开, 4=关
     */
    void onSunshadeChanged(int state);

    /**
     * 近光灯状态变化回调（注意反向逻辑）
     * @param isOn true=近光灯亮, false=近光灯灭
     */
    void onLowBeamLightChanged(boolean isOn);

    /**
     * 蓝牙连接状态变化回调
     * @param connected true=已连接, false=已断开
     */
    void onBluetoothStateChanged(boolean connected);

    /**
     * 屏幕点亮状态变化回调
     * @param on true=点亮, false=熄灭
     */
    void onScreenStateChanged(boolean on);

    /**
     * 空调页面状态变化回调
     * @param open true=空调页打开, false=空调页关闭
     */
    void onAcPageChanged(boolean open);
}
