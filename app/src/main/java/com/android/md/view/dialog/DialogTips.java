package com.android.md.view.dialog;

import android.content.Context;

/**
 * 提示对话框，有一个确认、一个返回按钮
 * Created by mingdasen on 2015/11/24.
 */
public class DialogTips extends DialogBase{
    boolean hasNegative;
    boolean hasTitle;
    /**
     * 构造函数
     * @param context
     */
    public DialogTips(Context context,String title,String message,String buttonText,boolean hasNegative,boolean hasTitle) {
        super(context);
        super.setMessage(message);
        super.setNamePositiveButton(buttonText);
        this.hasNegative = hasNegative;
        this.hasTitle = hasTitle;
        super.setTitle(title);
    }

    public DialogTips(Context context, String message,String buttonText){
        super(context);
        super.setMessage(message);
        super.setNamePositiveButton(buttonText);
        this.hasNegative = false;
        this.hasTitle = true;
        super.setTitle("提示");
        super.setCancel(false);
    }

    public DialogTips(Context context, String message,String buttonText,String negetiveText,String title,boolean isCancel) {
        super(context);
        super.setMessage(message);
        super.setNamePositiveButton(buttonText);
        this.hasNegative=false;
        super.setNameNegativeButton(negetiveText);
        this.hasTitle = true;
        super.setTitle(title);
        super.setCancel(isCancel);
    }

    /**
     * 创建对话框
     */
    @Override
    protected void onBiulding() {
        super.setWidth(dip2px(mainContext, 300));
        if (hasNegative){
            super.setNameNegativeButton("取消");
        }
        if (!hasTitle){
            super.setHasTitle(false);
        }
    }

    public int dip2px(Context context,float dipValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(scale*dipValue + 0.5f);
    }

    /**
     * 确认按钮，触发onSuccessListener的onClick
     * @return
     */
    @Override
    protected boolean OnClickPositiveButton() {
        if (onSuccessListener != null){
            onSuccessListener.onClick(this,1);
        }
        return true;
    }

    @Override
    protected void OnClickNegativeButton() {
        if (onCancelListener != null){
            onCancelListener.onClick(this,0);
        }
    }

    @Override
    protected void onDismiss() {
    }
}