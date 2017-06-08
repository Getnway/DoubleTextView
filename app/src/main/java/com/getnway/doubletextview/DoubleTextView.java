package com.getnway.doubletextview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * TextView + TextView
 * <p>
 * Author: getnway
 * Date: 17-06-09
 */
public class DoubleTextView extends LinearLayout {
    private static final int MAX_LEVEL = 10000; // ScaleDrawable#MAX_LEVEL
    private static final int DEFAULT_TEXT_SIZE = 15;
    private TextView textLeft, textRight;
    private Drawable leftDrawable, rightDrawable;

    public DoubleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textLeft = new TextView(getContext());
        textRight = new TextView(getContext());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DoubleTextView);
        textLeft.setText(a.getText(R.styleable.DoubleTextView_textLeft));
        ColorStateList textLeftColor = a.getColorStateList(R.styleable.DoubleTextView_textLeftColor);
        if (textLeftColor != null) textLeft.setTextColor(textLeftColor);
        textLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                a.getDimensionPixelSize(R.styleable.DoubleTextView_textLeftSize, DEFAULT_TEXT_SIZE));
        textLeft.setPadding(a.getDimensionPixelSize(R.styleable.DoubleTextView_textLeftPaddingLeft, 0), 0,
                a.getDimensionPixelSize(R.styleable.DoubleTextView_textLeftPaddingRight, 0), 0);
        textLeft.setGravity(a.getInt(R.styleable.DoubleTextView_textLeftGravity, Gravity.CENTER_VERTICAL));
        textLeft.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        textRight.setText(a.getText(R.styleable.DoubleTextView_textRight));
        ColorStateList textRightColor = a.getColorStateList(R.styleable.DoubleTextView_textRightColor);
        if (textRightColor != null) textRight.setTextColor(textRightColor);
        textRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                a.getDimensionPixelSize(R.styleable.DoubleTextView_textRightSize, DEFAULT_TEXT_SIZE));
        int textRightGravity = a.getInt(R.styleable.DoubleTextView_textRightGravity, Gravity.RIGHT);
        textRight.setPadding(a.getDimensionPixelSize(R.styleable.DoubleTextView_textRightPaddingLeft, 0), 0,
                a.getDimensionPixelSize(R.styleable.DoubleTextView_textRightPaddingRight, 0), 0);
        textRight.setGravity(textRightGravity | Gravity.CENTER_VERTICAL);
        textRight.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));

        leftDrawable = a.getDrawable(R.styleable.DoubleTextView_drawableLeft);
        if (leftDrawable != null) {
            textLeft.setCompoundDrawablePadding(a.getDimensionPixelSize(R.styleable.DoubleTextView_drawableLeftPadding, 0));
            float leftDrawableScale = a.getFloat(R.styleable.DoubleTextView_drawableLeftScale, 1.0f);
            if (leftDrawableScale != 1.0f && leftDrawableScale > 0) {
                ScaleDrawable leftScaleDrawable = new ScaleDrawable(leftDrawable, Gravity.CENTER, 1.0f, 1.0f);
                leftScaleDrawable.setLevel((int) (leftDrawableScale * MAX_LEVEL));
                leftDrawable = leftScaleDrawable;
            }
            Log.d(getClass().getSimpleName(), String.format("DoubleTextView: leftDrawable:%s leftDrawableScale:%s", leftDrawable, leftDrawableScale));
            textLeft.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
            // If the text is changed, we need to re-register the Drawable to recompute the bounds given the new TextView height
            textLeft.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    textLeft.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
                }
            });
        }

        rightDrawable = a.getDrawable(R.styleable.DoubleTextView_drawableRight);
        if (rightDrawable != null) {
            textRight.setCompoundDrawablePadding(a.getDimensionPixelSize(R.styleable.DoubleTextView_drawableRightPadding, 0));
            float rightDrawableScale = a.getFloat(R.styleable.DoubleTextView_drawableRightScale, 1.0f);
            if (rightDrawableScale != 1.0f && rightDrawableScale > 0) {
                ScaleDrawable rightScaleDrawable = new ScaleDrawable(rightDrawable, Gravity.CENTER, 1.0f, 1.0f);
                rightScaleDrawable.setLevel((int) (rightDrawableScale*MAX_LEVEL));
                rightDrawable = rightScaleDrawable;
            }
            // If the text is changed, we need to re-register the Drawable to recompute the bounds given the new TextView height
            textRight.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
            textRight.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    textRight.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
                }
            });
        }
        a.recycle();
        setOrientation(HORIZONTAL);
        setClickable(true);
        addView(textLeft);
        addView(textRight);
    }
}
