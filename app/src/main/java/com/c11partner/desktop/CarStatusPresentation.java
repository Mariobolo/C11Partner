package com.c11partner.desktop;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 副屏车辆状态显示Presentation
 * 
 * 在副屏上显示车辆状态信息：
 * - 车速
 * - 档位
 * - 车门状态
 * - 转向灯状态
 * - 时间
 */
public class CarStatusPresentation extends Presentation {
    
    private static final String TAG = "CarStatusPresentation";
    
    // 视图组件
    private TextView mSpeedText;
    private TextView mGearText;
    private TextView mDoorText;
    private TextView mLeftTurnText;
    private TextView mRightTurnText;
    private TextView mTimeText;
    private TextView mTitleText;
    
    // 车辆状态
    private float mSpeed = 0;
    private int mGear = LeapMotorCarState.GEAR_P;
    private int mOpenDoorCount = 0;
    private boolean mLeftTurnOn = false;
    private boolean mRightTurnOn = false;
    
    public CarStatusPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 创建简单的布局
        View contentView = createContentView();
        setContentView(contentView);
        
        // 初始化视图
        initViews(contentView);
        
        // 更新显示
        updateDisplay();
    }
    
    /**
     * 创建内容视图
     */
    private View createContentView() {
        // 使用代码创建布局，避免依赖XML布局文件
        android.widget.LinearLayout root = new android.widget.LinearLayout(getContext());
        root.setOrientation(android.widget.LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFF000000); // 黑色背景
        root.setGravity(android.view.Gravity.CENTER);
        root.setPadding(50, 50, 50, 50);
        
        // 标题
        mTitleText = new TextView(getContext());
        mTitleText.setText("C11伙伴 - 副屏显示");
        mTitleText.setTextColor(0xFFFFFFFF);
        mTitleText.setTextSize(24);
        mTitleText.setGravity(android.view.Gravity.CENTER);
        root.addView(mTitleText);
        
        // 间距
        android.widget.LinearLayout.LayoutParams params = 
            new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                100
            );
        params.topMargin = 50;
        
        // 车速
        mSpeedText = new TextView(getContext());
        mSpeedText.setText("0 km/h");
        mSpeedText.setTextColor(0xFF00FF00); // 绿色
        mSpeedText.setTextSize(72);
        mSpeedText.setGravity(android.view.Gravity.CENTER);
        mSpeedText.setLayoutParams(params);
        root.addView(mSpeedText);
        
        // 档位
        mGearText = new TextView(getContext());
        mGearText.setText("P");
        mGearText.setTextColor(0xFFFFA500); // 橙色
        mGearText.setTextSize(48);
        mGearText.setGravity(android.view.Gravity.CENTER);
        params = new android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 30;
        mGearText.setLayoutParams(params);
        root.addView(mGearText);
        
        // 车门状态
        mDoorText = new TextView(getContext());
        mDoorText.setText("车门全部关闭");
        mDoorText.setTextColor(0xFFFFFFFF);
        mDoorText.setTextSize(20);
        mDoorText.setGravity(android.view.Gravity.CENTER);
        params = new android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 30;
        mDoorText.setLayoutParams(params);
        root.addView(mDoorText);
        
        // 转向灯状态
        android.widget.LinearLayout turnLayout = new android.widget.LinearLayout(getContext());
        turnLayout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        turnLayout.setGravity(android.view.Gravity.CENTER);
        params = new android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 30;
        turnLayout.setLayoutParams(params);
        
        mLeftTurnText = new TextView(getContext());
        mLeftTurnText.setText("◀");
        mLeftTurnText.setTextColor(0xFF888888); // 灰色
        mLeftTurnText.setTextSize(36);
        turnLayout.addView(mLeftTurnText);
        
        TextView space = new TextView(getContext());
        space.setText("     ");
        space.setTextSize(36);
        turnLayout.addView(space);
        
        mRightTurnText = new TextView(getContext());
        mRightTurnText.setText("▶");
        mRightTurnText.setTextColor(0xFF888888); // 灰色
        mRightTurnText.setTextSize(36);
        turnLayout.addView(mRightTurnText);
        
        root.addView(turnLayout);
        
        // 时间
        mTimeText = new TextView(getContext());
        mTimeText.setText("");
        mTimeText.setTextColor(0xFFAAAAAA);
        mTimeText.setTextSize(18);
        mTimeText.setGravity(android.view.Gravity.CENTER);
        params = new android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 50;
        mTimeText.setLayoutParams(params);
        root.addView(mTimeText);
        
        return root;
    }
    
    /**
     * 初始化视图
     */
    private void initViews(View view) {
        // 视图已经在createContentView中创建
    }
    
    /**
     * 更新车速
     */
    public void updateSpeed(float speed) {
        mSpeed = speed;
        if (mSpeedText != null) {
            mSpeedText.setText(String.format("%.0f km/h", speed));
        }
    }
    
    /**
     * 更新档位
     */
    public void updateGear(int gear) {
        mGear = gear;
        if (mGearText != null) {
            String gearText = "P";
            int color = 0xFFFFA500; // 橙色
            
            switch (gear) {
                case LeapMotorCarState.GEAR_P:
                    gearText = "P";
                    color = 0xFFFFA500; // 橙色
                    break;
                case LeapMotorCarState.GEAR_R:
                    gearText = "R";
                    color = 0xFFFF0000; // 红色
                    break;
                case LeapMotorCarState.GEAR_N:
                    gearText = "N";
                    color = 0xFFFFFF00; // 黄色
                    break;
                case LeapMotorCarState.GEAR_D:
                    gearText = "D";
                    color = 0xFF00FF00; // 绿色
                    break;
            }
            
            mGearText.setText(gearText);
            mGearText.setTextColor(color);
        }
    }
    
    /**
     * 更新车门状态
     */
    public void updateDoorStatus(int openDoorCount) {
        mOpenDoorCount = openDoorCount;
        if (mDoorText != null) {
            if (openDoorCount == 0) {
                mDoorText.setText("车门全部关闭");
                mDoorText.setTextColor(0xFFFFFFFF);
            } else {
                mDoorText.setText(String.format("%d 个车门未关", openDoorCount));
                mDoorText.setTextColor(0xFFFF0000); // 红色警告
            }
        }
    }
    
    /**
     * 更新转向灯状态
     */
    public void updateTurnLights(boolean leftOn, boolean rightOn) {
        mLeftTurnOn = leftOn;
        mRightTurnOn = rightOn;
        
        if (mLeftTurnText != null) {
            mLeftTurnText.setTextColor(leftOn ? 0xFFFFFF00 : 0xFF888888); // 黄色/灰色
        }
        if (mRightTurnText != null) {
            mRightTurnText.setTextColor(rightOn ? 0xFFFFFF00 : 0xFF888888); // 黄色/灰色
        }
    }
    
    /**
     * 更新时间
     */
    public void updateTime(String time) {
        if (mTimeText != null) {
            mTimeText.setText(time);
        }
    }
    
    /**
     * 更新所有显示
     */
    private void updateDisplay() {
        updateSpeed(mSpeed);
        updateGear(mGear);
        updateDoorStatus(mOpenDoorCount);
        updateTurnLights(mLeftTurnOn, mRightTurnOn);
    }
}
