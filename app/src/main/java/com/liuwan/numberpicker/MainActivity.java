package com.liuwan.numberpicker;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

/**
 * Created by liuwan on 2016/10/14.
 */
public class MainActivity extends AppCompatActivity {

    private EditText edWorkingAge;
    private Button submit_workingAge;
    private PopupWindow popupWindow;
    private NumberPicker numberPicker;
    private View workingAge_view;
    private int workingAge = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edWorkingAge = (EditText) findViewById(R.id.edWorkingAge);
        edWorkingAge.setText(workingAge + "年");

        initNumberPicker();

        // 选择服务年限
        edWorkingAge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // 设置初始值
                numberPicker.setValue(workingAge);

                // 强制隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                // 填充布局并设置弹出窗体的宽,高
                popupWindow = new PopupWindow(workingAge_view,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                // 设置弹出窗体可点击
                popupWindow.setFocusable(true);
                // 设置弹出窗体动画效果
                popupWindow.setAnimationStyle(R.style.AnimBottom);
                // 触屏位置如果在选择框外面则销毁弹出框
                popupWindow.setOutsideTouchable(true);
                // 设置弹出窗体的背景
                popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                popupWindow.showAtLocation(workingAge_view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                // 设置背景透明度
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.5f;
                getWindow().setAttributes(lp);

                // 添加窗口关闭事件
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1f;
                        getWindow().setAttributes(lp);
                    }

                });
            }

        });

        // 确定服务年限
        submit_workingAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingAge = numberPicker.getValue();
                edWorkingAge.setText(workingAge + "年");
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 初始化滚动框布局
     */
    private void initNumberPicker() {
        workingAge_view = LayoutInflater.from(this).inflate(R.layout.popupwindow, null);
        submit_workingAge = (Button) workingAge_view.findViewById(R.id.submit_workingAge);
        numberPicker = (NumberPicker) workingAge_view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(0);
        numberPicker.setFocusable(false);
        numberPicker.setFocusableInTouchMode(false);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setNumberPickerDividerColor(numberPicker);
    }

    /**
     * 自定义滚动框分隔线颜色
     */
    private void setNumberPickerDividerColor(NumberPicker number) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(number, new ColorDrawable(ContextCompat.getColor(this, R.color.numberpicker_line)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
