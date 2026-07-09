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
}
