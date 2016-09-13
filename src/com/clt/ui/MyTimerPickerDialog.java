package com.clt.ui;

import java.util.ArrayList;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * 时间选择
 *
 */
public class MyTimerPickerDialog extends TimePickerDialog
{
    private ArrayList<Button> btns;

    public MyTimerPickerDialog(Context context, int theme,
            OnTimeSetListener callBack, int hourOfDay, int minute,
            boolean is24HourView)
    {
        super(context, theme, callBack, hourOfDay, minute, is24HourView);
    }

    public MyTimerPickerDialog(Context context, OnTimeSetListener callBack,
            int hourOfDay, int minute, boolean is24HourView)
    {
        super(context, callBack, hourOfDay, minute, is24HourView);
    }

    public void setButton(ArrayList<Button> btns)
    {
        this.btns = btns;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
    {
        super.onTimeChanged(view, hourOfDay, minute);
        if (btns != null && !btns.isEmpty())
        {
            for (Button button : btns)
            {
                button.setText(hourOfDay + ":" + minute);
            }
        }
    }

}
