package com.getnway.doubletextview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
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
public class DoubleTextView extends LinearLayout implements View.OnLayoutChangeListener {
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
        leftDrawable = a.getDrawable(R.styleable.DoubleTextView_drawableLeft);
        leftDrawablePadding = a.getDimensionPixelSize(R.styleable.DoubleTextView_drawableLeftPadding, 0);
        leftDrawableScale = a.getFloat(R.styleable.DoubleTextView_drawableLeftScale, 1.0f);
        setTextViewDrawable(textLeft, leftDrawable, leftDrawablePadding, leftDrawableScale);

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
        textRight.setHint(a.getString(R.styleable.DoubleTextView_textRightHint));
        textRight.setHintTextColor(a.getColor(R.styleable.DoubleTextView_textRightHintColor, Color.parseColor("#666666")));
        Drawable rightBg = a.getDrawable(R.styleable.DoubleTextView_textRightBackground);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textRight.setBackground(rightBg);
        } else {
            textRight.setBackgroundDrawable(rightBg);
        }
        rightDrawable = a.getDrawable(R.styleable.DoubleTextView_drawableRight);
        rightDrawablePadding = a.getDimensionPixelSize(R.styleable.DoubleTextView_drawableRightPadding, 0);
        rightDrawableScale = a.getFloat(R.styleable.DoubleTextView_drawableRightScale, 1.0f);
        setTextViewDrawable(textRight, rightDrawable, rightDrawablePadding, rightDrawableScale);

        a.recycle();
        setOrientation(HORIZONTAL);
        setClickable(true);
        addView(textLeft);
        addView(textRight);
    }

    public TextView getTextLeft() {
        return textLeft;
    }

    public TextView getTextRight() {
        return textRight;
    }

    private int leftDrawablePadding, rightDrawablePadding;
    private float leftDrawableScale, rightDrawableScale;

    public void setLeftDrawableScale(float drawableScale) {
        this.leftDrawableScale = drawableScale;
        setTextViewDrawable(textLeft, leftDrawable, leftDrawablePadding, leftDrawableScale);
    }

    public void setRightDrawableScale(float drawableScale) {
        this.rightDrawableScale = drawableScale;
        setTextViewDrawable(textLeft, leftDrawable, leftDrawablePadding, leftDrawableScale);
    }

    public void setTextLeftDrawable(Drawable drawable) {
        this.leftDrawable = drawable;
        setTextViewDrawable(textLeft, leftDrawable, leftDrawablePadding, leftDrawableScale);
    }

    public void setTextRightDrawable(Drawable drawable) {
        this.rightDrawable = drawable;
        setTextViewDrawable(textRight, rightDrawable, rightDrawablePadding, rightDrawableScale);
    }

    private void setTextViewDrawable(TextView textView, Drawable drawable, int drawablePadding, final float drawableScale) {
        textView.setCompoundDrawablePadding(drawablePadding);
        if (drawableScale != 1.0f && drawableScale > 0) {
            ScaleDrawable rightScaleDrawable = new ScaleDrawable(drawable, Gravity.CENTER, 1.0f, 1.0f) {
                @Override
                public int getIntrinsicHeight() {
                    return (int) (super.getIntrinsicHeight() * drawableScale);
                }

                @Override
                public int getIntrinsicWidth() {
                    return (int) (super.getIntrinsicWidth() * drawableScale);
                }
            };
            rightScaleDrawable.setLevel((int) (drawableScale * MAX_LEVEL));
            drawable = rightScaleDrawable;
        }
        // If the text is changed, we need to re-register the Drawable to recompute the bounds given the new TextView height
        textView.addOnLayoutChangeListener(this);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (leftDrawable != null)
            textLeft.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
        if (rightDrawable != null)
            textRight.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
    }
}
