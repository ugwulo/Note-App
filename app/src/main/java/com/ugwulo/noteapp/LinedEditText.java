package com.ugwulo.noteapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

public class LinedEditText extends AppCompatEditText {
    private Paint mPaint;
    private Rect mRect;
    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mRect = new Rect();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0xFF018786);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = ((View)this.getParent()).getHeight();
        int lineHeight = getLineHeight();
        int numberOfLines = height / lineHeight;

        Rect rect = mRect;
        Paint paint = mPaint;
        int baseline = getLineBounds(0, rect);

        for(int i = 0; i < numberOfLines; i++){
            canvas.drawLine(rect.left, baseline + 1, rect.right, baseline + 1, paint);
            baseline += lineHeight;
        }
        super.onDraw(canvas);
    }
}