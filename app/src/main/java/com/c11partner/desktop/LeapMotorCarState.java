package com.c11partner.desktop;

/**
 * 零跑C11车辆状态数据类
 * 基于实车日志分析的CAN信号映射
 */
public class LeapMotorCarState {
    
    // 档位定义
    public static final int GEAR_P = 4; // P挡
    public static final int GEAR_R = 1; // R挡
    public static final int GEAR_N = 2; // N挡
    public static final int GEAR_D = 3; // D挡
    
    // 车门状态
    public static final int DOOR_CLOSED = 0;
    public static final int DOOR_OPEN = 1;
    
    // 转向灯状态
    public static final int TURN_LIGHT_OFF = 0;
    public static final int TURN_LIGHT_ON = 1;
    
    // 车辆状态字段
    private int gear = GEAR_P;                    // 当前档位
    private int leftTurnLight = TURN_LIGHT_OFF;   // 左转向灯
    private int rightTurnLight = TURN_LIGHT_OFF;  // 右转向灯
    
    // 车门状态
    private int frontLeftDoor = DOOR_CLOSED;      // 左前门
    private int frontRightDoor = DOOR_CLOSED;     // 右前门
    private int rearLeftDoor = DOOR_CLOSED;       // 左后门
    private int rearRightDoor = DOOR_CLOSED;      // 右后门
    private int trunkDoor = DOOR_CLOSED;          // 后备箱
    private int hoodDoor = DOOR_CLOSED;           // 前机盖
    
    // 天窗和遮阳帘
    private int sunroof = DOOR_CLOSED;            // 天窗
    private int sunshade = 4;                     // 遮阳帘 3=开, 4=关
    
    // 锁车状态
    private int lockState = 1;                    // 0=解锁, 1=上锁
    
    // 车速
    private float speed = 0;                      // 当前车速 km/h
    
    // 近光灯状态
    private int lowBeamLight = 1;                 // 近光灯 0=开, 1=关（反向逻辑）
    
    // 胎压胎温
    private float frontLeftTirePressure = 0;      // 左前轮胎压 kPa
    private float frontRightTirePressure = 0;     // 右前轮胎压 kPa
    private float rearLeftTirePressure = 0;       // 左后轮胎压 kPa
    private float rearRightTirePressure = 0;      // 右后轮胎压 kPa
    private int frontLeftTireTemp = 0;            // 左前轮胎温 ℃
    private int frontRightTireTemp = 0;           // 右前轮胎温 ℃
    private int rearLeftTireTemp = 0;             // 左后轮胎温 ℃
    private int rearRightTireTemp = 0;            // 右后轮胎温 ℃
    
    // 蓝牙状态
    private boolean bluetoothConnected = false;   // 蓝牙连接状态
    
    // 屏幕状态
    private boolean screenOn = true;              // 屏幕点亮状态
    
    // 空调页面状态
    private boolean acPageOpen = false;           // 空调页面是否打开
    
    // 360全景状态
    private boolean camera360Visible = false;     // 360全景是否显示
    
    // 时间戳
    private long lastUpdateTime = 0;
    
    public LeapMotorCarState() {
        lastUpdateTime = System.currentTimeMillis();
    }
    
    // Getter 和 Setter 方法
    public int getGear() { return gear; }
    public void setGear(int gear) { this.gear = gear; updateTime(); }
    
    public int getLeftTurnLight() { return leftTurnLight; }
    public void setLeftTurnLight(int state) { this.leftTurnLight = state; updateTime(); }
    
    public int getRightTurnLight() { return rightTurnLight; }
    public void setRightTurnLight(int state) { this.rightTurnLight = state; updateTime(); }
    
    public int getFrontLeftDoor() { return frontLeftDoor; }
    public void setFrontLeftDoor(int state) { this.frontLeftDoor = state; updateTime(); }
    
    public int getFrontRightDoor() { return frontRightDoor; }
    public void setFrontRightDoor(int state) { this.frontRightDoor = state; updateTime(); }
    
    public int getRearLeftDoor() { return rearLeftDoor; }
    public void setRearLeftDoor(int state) { this.rearLeftDoor = state; updateTime(); }
    
    public int getRearRightDoor() { return rearRightDoor; }
    public void setRearRightDoor(int state) { this.rearRightDoor = state; updateTime(); }
    
    public int getTrunkDoor() { return trunkDoor; }
    public void setTrunkDoor(int state) { this.trunkDoor = state; updateTime(); }
    
    public int getHoodDoor() { return hoodDoor; }
    public void setHoodDoor(int state) { this.hoodDoor = state; updateTime(); }
    
    public int getSunroof() { return sunroof; }
    public void setSunroof(int state) { this.sunroof = state; updateTime(); }
    
    public int getSunshade() { return sunshade; }
    public void setSunshade(int state) { this.sunshade = state; updateTime(); }
    
    public int getLockState() { return lockState; }
    public void setLockState(int state) { this.lockState = state; updateTime(); }
    
    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; updateTime(); }
    
    public int getLowBeamLight() { return lowBeamLight; }
    public void setLowBeamLight(int state) { this.lowBeamLight = state; updateTime(); }
    
    // 胎压胎温
    public float getFrontLeftTirePressure() { return frontLeftTirePressure; }
    public void setFrontLeftTirePressure(float value) { this.frontLeftTirePressure = value; updateTime(); }
    
    public float getFrontRightTirePressure() { return frontRightTirePressure; }
    public void setFrontRightTirePressure(float value) { this.frontRightTirePressure = value; updateTime(); }
    
    public float getRearLeftTirePressure() { return rearLeftTirePressure; }
    public void setRearLeftTirePressure(float value) { this.rearLeftTirePressure = value; updateTime(); }
    
    public float getRearRightTirePressure() { return rearRightTirePressure; }
    public void setRearRightTirePressure(float value) { this.rearRightTirePressure = value; updateTime(); }
    
    public int getFrontLeftTireTemp() { return frontLeftTireTemp; }
    public void setFrontLeftTireTemp(int value) { this.frontLeftTireTemp = value; updateTime(); }
    
    public int getFrontRightTireTemp() { return frontRightTireTemp; }
    public void setFrontRightTireTemp(int value) { this.frontRightTireTemp = value; updateTime(); }
    
    public int getRearLeftTireTemp() { return rearLeftTireTemp; }
    public void setRearLeftTireTemp(int value) { this.rearLeftTireTemp = value; updateTime(); }
    
    public int getRearRightTireTemp() { return rearRightTireTemp; }
    public void setRearRightTireTemp(int value) { this.rearRightTireTemp = value; updateTime(); }
    
    public boolean isBluetoothConnected() { return bluetoothConnected; }
    public void setBluetoothConnected(boolean connected) { this.bluetoothConnected = connected; updateTime(); }
    
    public boolean isScreenOn() { return screenOn; }
    public void setScreenOn(boolean on) { this.screenOn = on; updateTime(); }
    
    public boolean isAcPageOpen() { return acPageOpen; }
    public void setAcPageOpen(boolean open) { this.acPageOpen = open; updateTime(); }
    
    public boolean isCamera360Visible() { return camera360Visible; }
    public void setCamera360Visible(boolean visible) { this.camera360Visible = visible; updateTime(); }
    
    public long getLastUpdateTime() { return lastUpdateTime; }
    
    private void updateTime() {
        lastUpdateTime = System.currentTimeMillis();
    }
    
    // 辅助方法
    public boolean isLeftTurnLightOn() { return leftTurnLight == TURN_LIGHT_ON; }
    public boolean isRightTurnLightOn() { return rightTurnLight == TURN_LIGHT_ON; }
    public boolean isAnyTurnLightOn() { return isLeftTurnLightOn() || isRightTurnLightOn(); }
    
    public boolean isReverseGear() { return gear == GEAR_R; }
    public boolean isDriveGear() { return gear == GEAR_D; }
    public boolean isParkGear() { return gear == GEAR_P; }
    
    public boolean isAnyDoorOpen() {
        return frontLeftDoor == DOOR_OPEN || frontRightDoor == DOOR_OPEN ||
               rearLeftDoor == DOOR_OPEN || rearRightDoor == DOOR_OPEN ||
               trunkDoor == DOOR_OPEN || hoodDoor == DOOR_OPEN;
    }
    
    public boolean isLocked() { return lockState == 1; }
    public boolean isUnlocked() { return lockState == 0; }
    
    // 近光灯（注意：反向逻辑，0=开，1=关）
    public boolean isLowBeamLightOn() { return lowBeamLight == 0; }
    public boolean isLowBeamLightOff() { return lowBeamLight == 1; }
    
    // 天窗和遮阳帘
    public boolean isSunroofOpen() { return sunroof == 3; }
    public boolean isSunshadeOpen() { return sunshade == 3; }
    
    // 获取开门数量
    public int getOpenDoorCount() {
        int count = 0;
        if (frontLeftDoor == DOOR_OPEN) count++;
        if (frontRightDoor == DOOR_OPEN) count++;
        if (rearLeftDoor == DOOR_OPEN) count++;
        if (rearRightDoor == DOOR_OPEN) count++;
        if (trunkDoor == DOOR_OPEN) count++;
        if (hoodDoor == DOOR_OPEN) count++;
        return count;
    }
    
    public String getGearText() {
        switch(gear) {
            case GEAR_P: return "P";
            case GEAR_R: return "R";
            case GEAR_N: return "N";
            case GEAR_D: return "D";
            default: return "?";
        }
    }
    
    @Override
    public String toString() {
        return String.format("档位:%s 左转:%d 右转:%d 车速:%.1f 车门:%s",
            getGearText(), leftTurnLight, rightTurnLight, speed, 
            isAnyDoorOpen() ? "开" : "关");
    }
}
