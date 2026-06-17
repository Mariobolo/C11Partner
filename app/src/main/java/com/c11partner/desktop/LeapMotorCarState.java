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
