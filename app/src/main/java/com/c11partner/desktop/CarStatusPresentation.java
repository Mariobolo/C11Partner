package com.c11partner.desktop;
import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 副屏车辆状态显示Presentation
 * 
 * 在副屏上显示车辆状态信息：
 * - 车速
 * - 档位
 * - 胎压胎温（四个轮胎）
 * - 车门状态
 * - 转向灯状态
 * - 车辆状态指示（灯光、蓝牙、锁车等）
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
    
    // 胎压视图
    private TextView mFrontLeftTireText;
    private TextView mFrontRightTireText;
    private TextView mRearLeftTireText;
    private TextView mRearRightTireText;
    
    // 状态指示器
    private TextView mStatusIndicators;
    
    // 车辆状态
    private float mSpeed = 0;
    private int mGear = LeapMotorCarState.GEAR_P;
    private int mOpenDoorCount = 0;
    private boolean mLeftTurnOn = false;
    private boolean mRightTurnOn = false;
    
    // 胎压数据
    private float mFrontLeftTirePressure = 0;
    private float mFrontRightTirePressure = 0;
    private float mRearLeftTirePressure = 0;
    private float mRearRightTirePressure = 0;
    
    // 其他状态
    private boolean mLowBeamLightOn = false;
    private boolean mBluetoothConnected = false;
    private boolean mLocked = true;
    
    // 时间更新
    private android.os.Handler mTimeHandler = new android.os.Handler();
    private Runnable mTimeUpdateRunnable;
    private java.text.SimpleDateFormat mTimeFormat;
    
    public CarStatusPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 创建布局
        View contentView = createContentView();
        setContentView(contentView);
        
        // 初始化时间格式
        mTimeFormat = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        
        // 启动时间更新
        startTimeUpdate();

        // 更新显示
        updateDisplay();
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        stopTimeUpdate();
    }
    
    /**
     * 启动时间更新（每分钟更新一次，精确对齐分钟）
     */
    private void startTimeUpdate() {
        if (mTimeUpdateRunnable == null) {
            mTimeUpdateRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        updateCurrentTime();
                    } finally {
                        // 计算到下一分钟的延迟时间
                        long now = System.currentTimeMillis();
                        long delay = 60000 - (now % 60000);
                        mTimeHandler.postDelayed(this, delay);
                    }
                }
            };
        }
        
        // 立即更新一次
        updateCurrentTime();
        
        // 计算到下一分钟的延迟，然后开始定时更新
        long now = System.currentTimeMillis();
        long delay = 60000 - (now % 60000);
        mTimeHandler.postDelayed(mTimeUpdateRunnable, delay);
    }
    
    /**
     * 停止时间更新
     */
    private void stopTimeUpdate() {
        if (mTimeHandler != null && mTimeUpdateRunnable != null) {
            mTimeHandler.removeCallbacks(mTimeUpdateRunnable);
        }
    }
    
    /**
     * 更新当前时间显示
     */
    private void updateCurrentTime() {
        if (mTimeText != null && mTimeFormat != null) {
            String time = mTimeFormat.format(new java.util.Date());
            mTimeText.setText(time);
        }
    }
    
    /**
     * 创建内容视图
     */
    private View createContentView() {
        // 根布局
        LinearLayout root = new LinearLayout(getContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFF000000); // 黑色背景
        root.setGravity(Gravity.CENTER);
        root.setPadding(40, 30, 40, 30);
        
        LinearLayout.LayoutParams params;
        
        // 标题
        mTitleText = new TextView(getContext());
        mTitleText.setText("C11伙伴 · 副驾显示");
        mTitleText.setTextColor(0xFF888888);
        mTitleText.setTextSize(16);
        mTitleText.setGravity(Gravity.CENTER);
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 20;
        mTitleText.setLayoutParams(params);
        root.addView(mTitleText);
        
        // 车速
        mSpeedText = new TextView(getContext());
        mSpeedText.setText("0 km/h");
        mSpeedText.setTextColor(0xFF00FF00); // 绿色
        mSpeedText.setTextSize(64);
        mSpeedText.setGravity(Gravity.CENTER);
        mSpeedText.getPaint().setFakeBoldText(true);
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 10;
        mSpeedText.setLayoutParams(params);
        root.addView(mSpeedText);
        
        // 档位
        mGearText = new TextView(getContext());
        mGearText.setText("P");
        mGearText.setTextColor(0xFFFFA500); // 橙色
        mGearText.setTextSize(36);
        mGearText.setGravity(Gravity.CENTER);
        mGearText.getPaint().setFakeBoldText(true);
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 20;
        mGearText.setLayoutParams(params);
        root.addView(mGearText);
        
        // 胎压区域标题
        TextView tireTitle = new TextView(getContext());
        tireTitle.setText("— 胎压监测 —");
        tireTitle.setTextColor(0xFF666666);
        tireTitle.setTextSize(14);
        tireTitle.setGravity(Gravity.CENTER);
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 10;
        tireTitle.setLayoutParams(params);
        root.addView(tireTitle);
        
        // 胎压显示（2x2布局）
        // 第一行：前左、前右
        LinearLayout frontTireRow = new LinearLayout(getContext());
        frontTireRow.setOrientation(LinearLayout.HORIZONTAL);
        frontTireRow.setGravity(Gravity.CENTER);
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 8;
        frontTireRow.setLayoutParams(params);
        
        mFrontLeftTireText = createTireTextView("前左", "--");
        frontTireRow.addView(mFrontLeftTireText);
        
        mFrontRightTireText = createTireTextView("前右", "--");
        frontTireRow.addView(mFrontRightTireText);
        
        root.addView(frontTireRow);
        
        // 第二行：后左、后右
        LinearLayout rearTireRow = new LinearLayout(getContext());
        rearTireRow.setOrientation(LinearLayout.HORIZONTAL);
        rearTireRow.setGravity(Gravity.CENTER);
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 20;
        rearTireRow.setLayoutParams(params);
        
        mRearLeftTireText = createTireTextView("后左", "--");
        rearTireRow.addView(mRearLeftTireText);
        
        mRearRightTireText = createTireTextView("后右", "--");
        rearTireRow.addView(mRearRightTireText);
        
        root.addView(rearTireRow);
        
        // 状态指示器
        mStatusIndicators = new TextView(getContext());
        mStatusIndicators.setText("💡  📱  🔒");
        mStatusIndicators.setTextColor(0xFF888888);
        mStatusIndicators.setTextSize(18);
        mStatusIndicators.setGravity(Gravity.CENTER);
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 15;
        mStatusIndicators.setLayoutParams(params);
        root.addView(mStatusIndicators);
        
        // 车门状态
        mDoorText = new TextView(getContext());
        mDoorText.setText("车门全部关闭");
        mDoorText.setTextColor(0xFFFFFFFF);
        mDoorText.setTextSize(16);
        mDoorText.setGravity(Gravity.CENTER);
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 15;
        mDoorText.setLayoutParams(params);
        root.addView(mDoorText);
        
        // 转向灯状态
        LinearLayout turnLayout = new LinearLayout(getContext());
        turnLayout.setOrientation(LinearLayout.HORIZONTAL);
        turnLayout.setGravity(Gravity.CENTER);
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 15;
        turnLayout.setLayoutParams(params);
        
        mLeftTurnText = new TextView(getContext());
        mLeftTurnText.setText("◀");
        mLeftTurnText.setTextColor(0xFF444444); // 暗灰色
        mLeftTurnText.setTextSize(28);
        turnLayout.addView(mLeftTurnText);
        
        TextView space = new TextView(getContext());
        space.setText("          ");
        space.setTextSize(28);
        turnLayout.addView(space);
        
        mRightTurnText = new TextView(getContext());
        mRightTurnText.setText("▶");
        mRightTurnText.setTextColor(0xFF444444); // 暗灰色
        mRightTurnText.setTextSize(28);
        turnLayout.addView(mRightTurnText);
        
        root.addView(turnLayout);
        
        // 时间
        mTimeText = new TextView(getContext());
        mTimeText.setText("");
        mTimeText.setTextColor(0xFF555555);
        mTimeText.setTextSize(14);
        mTimeText.setGravity(Gravity.CENTER);
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        mTimeText.setLayoutParams(params);
        root.addView(mTimeText);
        
        return root;
    }
    
    /**
     * 创建胎压显示TextView
     */
    private TextView createTireTextView(String label, String value) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        );
        layout.setLayoutParams(params);
        
        // 标签
        TextView labelText = new TextView(getContext());
        labelText.setText(label);
        labelText.setTextColor(0xFF666666);
        labelText.setTextSize(12);
        labelText.setGravity(Gravity.CENTER);
        layout.addView(labelText);
        
        // 数值
        TextView valueText = new TextView(getContext());
        valueText.setText(value);
        valueText.setTextColor(0xFF00FF00); // 绿色
        valueText.setTextSize(20);
        valueText.setGravity(Gravity.CENTER);
        valueText.getPaint().setFakeBoldText(true);
        layout.addView(valueText);
        
        // 单位
        TextView unitText = new TextView(getContext());
        unitText.setText("bar");
        unitText.setTextColor(0xFF444444);
        unitText.setTextSize(10);
        unitText.setGravity(Gravity.CENTER);
        layout.addView(unitText);
        
        // 我们需要返回valueText的引用，所以用tag的方式存储
        valueText.setTag("tire_value");
        labelText.setTag("tire_label");
        
        // 为了简化，我们直接返回一个TextView，但这样布局不好看
        // 改用另一种方式：把valueText作为成员变量，通过findViewWithTag获取
        // 不，这样太麻烦了。我们还是直接创建独立的TextView
        
        // 重新设计：返回一个简单的TextView，包含标签和数值
        TextView tireView = new TextView(getContext());
        tireView.setText(label + "\n" + value + " bar");
        tireView.setTextColor(0xFF00FF00);
        tireView.setTextSize(16);
        tireView.setGravity(Gravity.CENTER);
        tireView.setLines(2);
        
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        );
        textParams.leftMargin = 10;
        textParams.rightMargin = 10;
        tireView.setLayoutParams(textParams);
        
        return tireView;
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
                mDoorText.setText("✅ 车门全部关闭");
                mDoorText.setTextColor(0xFF00FF00); // 绿色
            } else {
                mDoorText.setText(String.format("⚠️ %d 个车门未关", openDoorCount));
                mDoorText.setTextColor(0xFFFF0000); // 红色警告
            }
        }
        updateStatusIndicators();
    }
    
    /**
     * 更新转向灯状态
     */
    public void updateTurnLights(boolean leftOn, boolean rightOn) {
        mLeftTurnOn = leftOn;
        mRightTurnOn = rightOn;
        
        if (mLeftTurnText != null) {
            mLeftTurnText.setTextColor(leftOn ? 0xFFFFFF00 : 0xFF444444); // 黄色/暗灰色
        }
        if (mRightTurnText != null) {
            mRightTurnText.setTextColor(rightOn ? 0xFFFFFF00 : 0xFF444444); // 黄色/暗灰色
        }
    }
    
    /**
     * 更新胎压
     */
    public void updateTirePressure(
            float frontLeft, float frontRight,
            float rearLeft, float rearRight) {
        mFrontLeftTirePressure = frontLeft;
        mFrontRightTirePressure = frontRight;
        mRearLeftTirePressure = rearLeft;
        mRearRightTirePressure = rearRight;
        
        updateTireTextView(mFrontLeftTireText, "前左", frontLeft);
        updateTireTextView(mFrontRightTireText, "前右", frontRight);
        updateTireTextView(mRearLeftTireText, "后左", rearLeft);
        updateTireTextView(mRearRightTireText, "后右", rearRight);
    }
    
    /**
     * 更新单个胎压显示
     */
    private void updateTireTextView(TextView textView, String label, float pressure) {
        if (textView == null) return;
        
        if (pressure <= 0) {
            textView.setText(label + "\n-- bar");
            textView.setTextColor(0xFF666666); // 灰色
        } else {
            textView.setText(String.format("%s\n%.1f bar", label, pressure));
            // 胎压异常时显示红色（<2.0或>3.0）
            if (pressure < 2.0f || pressure > 3.0f) {
                textView.setTextColor(0xFFFF0000); // 红色警告
            } else {
                textView.setTextColor(0xFF00FF00); // 绿色正常
            }
        }
    }
    
    /**
     * 更新近光灯状态
     */
    public void updateLowBeamLight(boolean isOn) {
        mLowBeamLightOn = isOn;
        updateStatusIndicators();
    }
    
    /**
     * 更新蓝牙连接状态
     */
    public void updateBluetoothState(boolean connected) {
        mBluetoothConnected = connected;
        updateStatusIndicators();
    }
    
    /**
     * 更新锁车状态
     */
    public void updateLockState(boolean locked) {
        mLocked = locked;
        updateStatusIndicators();
    }
    
    /**
     * 更新状态指示器
     */
    private void updateStatusIndicators() {
        if (mStatusIndicators == null) return;
        
        StringBuilder sb = new StringBuilder();
        
        // 近光灯
        sb.append(mLowBeamLightOn ? "💡" : "⚫");
        sb.append("  ");
        
        // 蓝牙
        sb.append(mBluetoothConnected ? "📱" : "⚫");
        sb.append("  ");
        
        // 锁车
        sb.append(mLocked ? "🔒" : "🔓");
        
        mStatusIndicators.setText(sb.toString());
    }
    
    /**
     * 更新时间
     */
    /**
     * 更新时间显示（外部调用接口）
     * 注意：副屏已内置每分钟自动更新时间，通常不需要外部调用此方法
     *
     * @param time 时间字符串
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
        updateTirePressure(
            mFrontLeftTirePressure, mFrontRightTirePressure,
            mRearLeftTirePressure, mRearRightTirePressure
        );
        updateStatusIndicators();
    }
}
