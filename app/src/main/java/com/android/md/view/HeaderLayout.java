package com.android.md.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.md.R;
import com.android.md.util.PixelUtil;

/**
 * 自定义头部布局
 * Created by mingdasen on 2015/11/23.
 */
public class HeaderLayout extends LinearLayout {

    private LayoutInflater mInflater;
    private View mHeader;
    private LinearLayout mLayoutLeftContainer;
    private LinearLayout mLayoutRightContainer;
    private TextView mHtvSubTitle;
    private LinearLayout mLayoutRightImageButtonLayout;
    private Button mRightImageButton;
    private onRightImageButtonClickListener mRightImageButtonClickListener;

    private LinearLayout mlayoutLeftImageButtonLayout;
    private ImageButton mLeftImageButton;
    private onLeftImageButtonClickListener mLeftImageButtonClickListener;

    public enum HeaderStyle {//头部整体样式
        DEFAULT_TITLE, TITLE_LEFT_IMAGEBUTTON, TITLE_RIGHT_IMAGEBUTTON, TITLE_DOUBLE_IMAGEBUTTON;
    }

    public HeaderLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mInflater = LayoutInflater.from(context);
        mHeader = mInflater.inflate(R.layout.common_header, null);
        addView(mHeader);
        initViews();
    }

    private void initViews() {
        mLayoutLeftContainer = (LinearLayout) findViewById(R.id.header_layout_leftview_container);
        mLayoutRightContainer = (LinearLayout) findViewById(R.id.header_layout_rightview_container);
        mHtvSubTitle = (TextView) findViewById(R.id.header_htv_subtitle);
    }

    public View findViewByHeaderId(int id) {
        return mHeader.findViewById(id);
    }

    public void init(HeaderStyle hStyle) {
        switch (hStyle) {
            case DEFAULT_TITLE:
                defaultTitle();
                break;
            case TITLE_LEFT_IMAGEBUTTON:
                defaultTitle();
                titleLeftImageButton();
                break;
            case TITLE_RIGHT_IMAGEBUTTON:
                defaultTitle();
                titleRightImageButton();
                break;
            case TITLE_DOUBLE_IMAGEBUTTON:
                defaultTitle();
                titleLeftImageButton();
                ;
                titleRightImageButton();
                break;
        }
    }

    /**
     * 右侧自定义按钮
     */
    private void titleRightImageButton() {
        View mRightImageButtonView = mInflater.inflate(R.layout.common_header_rightbutton, null);
        mLayoutRightContainer.addView(mRightImageButtonView);
        mLayoutRightImageButtonLayout = (LinearLayout) mRightImageButtonView.findViewById(R.id.header_layout_imagebuttonlayout);
        mRightImageButton = (Button) mRightImageButtonView.findViewById(R.id.header_ib_imagebutton);
        mLayoutRightImageButtonLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRightImageButtonClickListener != null) {
                    mRightImageButtonClickListener.onClick();
                }
            }
        });
    }

    /**
     * 左侧自定义按钮
     */
    private void titleLeftImageButton() {
        View mleftImageButtonView = mInflater.inflate(R.layout.common_header_button, null);
        mLayoutLeftContainer.addView(mleftImageButtonView);
        mlayoutLeftImageButtonLayout = (LinearLayout) mleftImageButtonView.findViewById(R.id.header_layout_imagebuttonlayout);
        mLeftImageButton = (ImageButton) mleftImageButtonView.findViewById(R.id.header_ib_imagebutton);
        mlayoutLeftImageButtonLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLeftImageButtonClickListener != null) {
                    mLeftImageButtonClickListener.onClick();
                }
            }
        });
    }

    /**
     * 默认字体标题
     */
    private void defaultTitle() {
        mLayoutLeftContainer.removeAllViews();
        mLayoutRightContainer.removeAllViews();
    }

    /**
     * 获取右边按钮
     *
     * @return
     */
    public Button getRightImageButton() {
        if (mRightImageButton != null) {
            return mRightImageButton;
        }
        return null;
    }

    public void setDefaultTitle(CharSequence title) {
        if (title != null) {
            mHtvSubTitle.setText(title);
        } else {
            mHtvSubTitle.setVisibility(View.GONE);
        }
    }

    public void setTitleAndRightButton(CharSequence title, int backid, String text,
                                       onRightImageButtonClickListener onRightImageButtonClickListener) {
        setDefaultTitle(title);
        mLayoutRightContainer.setVisibility(View.VISIBLE);
        if (mRightImageButton != null && backid > 0){
            mRightImageButton.setWidth(PixelUtil.dp2px(45));
            mRightImageButton.setHeight(PixelUtil.dp2px(40));
            mRightImageButton.setBackgroundResource(backid);
            mRightImageButton.setText(text);
            setOnRightImageButtonClickListener(onRightImageButtonClickListener);
        }
    }

    public void setTitleAndRightImageButton(CharSequence title,int backid,
                                            onRightImageButtonClickListener onRightImageButtonClickListener){
        setDefaultTitle(title);
        mLayoutLeftContainer.setVisibility(View.VISIBLE);
        if (mRightImageButton != null && backid > 0){
            mRightImageButton.setWidth(PixelUtil.dp2px(30));
            mRightImageButton.setHeight(PixelUtil.dp2px(30));
            mRightImageButton.setTextColor(getResources().getColor(R.color.transparent));
            mRightImageButton.setBackgroundResource(backid);
            setOnRightImageButtonClickListener(onRightImageButtonClickListener);
        }
    }

    public void setTitleAndLeftImageButton(CharSequence title,int id,onLeftImageButtonClickListener listener){
        setDefaultTitle(title);
        if (mLeftImageButton != null && id > 0){
            mLeftImageButton.setImageResource(id);
            setOnLeftImageButtonClickLisstener(listener);
        }
        mLayoutLeftContainer.setVisibility(View.INVISIBLE);
    }

    public void setOnRightImageButtonClickListener(onRightImageButtonClickListener listener){
        mRightImageButtonClickListener = listener;
    }

    public interface onRightImageButtonClickListener{
        void onClick();
    }

    public void setOnLeftImageButtonClickLisstener(onLeftImageButtonClickListener listener){
        mLeftImageButtonClickListener = listener;
    }

    public interface onLeftImageButtonClickListener{
        void onClick();
    }
}
