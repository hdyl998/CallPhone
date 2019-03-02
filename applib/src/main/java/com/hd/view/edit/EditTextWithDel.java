package com.hd.view.edit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hd.R;
import com.hd.utils.StringUtils;
import com.hd.utils.TextViewUtils;


/**
 * edit的编辑，带清空
 */
public class EditTextWithDel extends AppCompatEditText implements View.OnTouchListener,
        View.OnFocusChangeListener {


    @Override
    public int length() {
        return getText().toString().trim().length();
    }

    /**
     * 获得文本
     *
     * @return
     */
    public String getTextTrim() {
        return getText().toString().trim();
    }

    private Drawable xD;

    public EditTextWithDel(Context context) {
        super(context);
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    private OnTouchListener l;
    private OnFocusChangeListener f;


    private OnClickListener onDelClickListener;


    public void setOnDelClickListener(OnClickListener onDelClickListener) {
        this.onDelClickListener = onDelClickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                    .getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (onDelClickListener != null) {
                        onDelClickListener.onClick(this);
                    }
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(!TextUtils.isEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    private void init() {
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            xD = getResources().getDrawable(getDefaultClearIconId());
        }
        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
        setClearIconVisible(false);
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                setClearIconVisible(!TextUtils.isEmpty(getText()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        // this.setPaddingRelative(getPaddingLeft(), getPaddingTop(),
        // getPaddingRight(), getPaddingBottom());
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);

    }

    private int getDefaultClearIconId() {
        int id = R.mipmap.ic_cross;
        return id;
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable x = visible ? xD : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
    }


    @Override
    public void setSelection(int index) {
        try {
            super.setSelection(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPasswordFilter() {
        StringUtils.setEditTextPasswordFilter(this);
    }
}
