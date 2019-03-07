package com.hd.base.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hd.R;
import com.hd.base.maininterface.IComCallBacks;

/**
 * Note：None
 * Created by lgd on 2019/1/10 14:17
 * E-Mail Address：986850427@qq.com
 */
public class InputDialog extends IBaseEditDialog {
    EditText editText;
    IComCallBacks<String> comCallBacks;
    TextView tvTitle;

    public InputDialog(Context context) {
        super(context);
        editText = findViewByID(R.id.edt_input_edit);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.GONE);
        setFocusEditText(editText);
    }

    public void setHint(String text) {
        editText.setHint(text);
    }

    public void setTitleText(String text) {
        tvTitle.setText(text);
        tvTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_inputer;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v.getId() == R.id.tv_enter) {
            String sendStr = editText.getText().toString().trim();
            if (comCallBacks != null) {
                comCallBacks.call(sendStr);
            }
        }
    }


    public void setComCallBacks(IComCallBacks<String> comCallBacks) {
        this.comCallBacks = comCallBacks;
    }

    @Override
    public int[] setClickIDs() {
        return new int[]{R.id.tv_cancle, R.id.tv_enter};
    }
}
