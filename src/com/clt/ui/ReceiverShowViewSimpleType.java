package com.clt.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Scroller;

import com.clt.entity.ReceiverLinkMode;
import com.clt.entity.ReceiverSetting;

/**
 * 接收卡设置主要显示视图
 *
 */
public class ReceiverShowViewSimpleType extends View
{
    //颜色
    private int pieceColorNor,pieceColorSelected,numColor,lineColor;
    
    private Canvas myCanvas;

    private Paint myPaint;

    private int column = 1;// 列

    private int row = 1;// 行

    private int pieceWidth = 64;

    private int pieceHeight = 64;

    private int linkMode = -1;

    private ReceiverPiece [][] pieceArr;

    private Context context;

    private boolean needDrawArrow = false;

    int num = 0;

    int fromX = 0, fromY = 0, toX = 0, toY = 0;

    public ReceiverShowViewSimpleType(Context context, AttributeSet attrs,
            int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        defaultInit();
    }

   

    public ReceiverShowViewSimpleType(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        defaultInit();
    }

    public ReceiverShowViewSimpleType(Context context)
    {
        super(context);
        this.context = context;
        defaultInit();
    }
    private void defaultInit()
    {
        myPaint = new Paint();
        
        pieceArr = new ReceiverPiece [row] [column];
        
        pieceColorNor=Color.rgb(0, 255, 255);
        pieceColorSelected=Color.rgb(176, 32, 176);
        numColor=Color.BLACK;
        lineColor=Color.rgb(0, 255, 255);
    }
    /**
     * 开始画图
     * @param linkMode
     * @param column
     * @param row
     * @param width
     * @param height
     */
    public void startDraw(int linkMode, int column, int row, int width,
            int height)
    {
        // this.linkMode = linkMode;
        // this.column = column;
        // this.row = row;
        // this.width = width;
        // this.height = height;
        // this.needDraw = true;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        this.myCanvas = canvas;
        

        if (needDrawArrow)
        {
            drawPiecesSelected();
            drawNumAndArrow();
        }
        else
        {

            drawPiecesNormal();
        }

    }

    /**
     * 绘制单元格，常规情况下
     */
    private void drawPiecesNormal()
    {
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setAntiAlias(true);
        myCanvas.drawColor(Color.WHITE);
        int num = 0;
        ReceiverPiece piece = null;
        int left, top, right, bottom;
        // 画矩形
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < column; j++)
            {
                left = pieceWidth * j;
                top = pieceHeight * i;
                right = left + pieceWidth;
                bottom = top + pieceHeight;
                myPaint.setColor(pieceColorNor);
                Rect targetRect = new Rect(left, top, right, bottom);
                myCanvas.drawRect(targetRect, myPaint);
                // 边框
                drawStrokes(left, top, right, bottom);
                // 文字，居中显示
                piece = new ReceiverPiece();
                piece.setBeginX(left);
                piece.setBeginY(top);
                pieceArr[i][j] = piece;
                // /////////绘制文字/////////////////////////
                drawNum(++num, targetRect);
            }
        }
    }

    /**
     * 选中情况下绘制单元格
     */
    private void drawPiecesSelected()
    {
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setAntiAlias(true);
        myCanvas.drawColor(Color.WHITE);
        int num = 0;
        // 画矩形
        for (int i = 1; i <= row; i++)
        {
            for (int j = 1; j <= column; j++)
            {
                int left = pieceWidth * (j - 1);
                int top = pieceHeight * (i - 1);
                int right = left + pieceWidth;
                int bottom = top + pieceHeight;
                myPaint.setColor(pieceColorSelected);
                Rect targetRect = new Rect(left, top, right, bottom);
                myCanvas.drawRect(targetRect, myPaint);
                // 边框
                drawStrokes(left, top, right, bottom);

            }
        }
    }

    /**
     * 给矩形描边
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param myPaint2
     */
    private void drawStrokes(int left, int top, int right, int bottom)
    {
        Paint myPaint = new Paint();
        myPaint.setColor(Color.parseColor("#FFFF00"));
        myCanvas.drawLine(left, top, right, top, myPaint);
        myCanvas.drawLine(right, top, right, bottom, myPaint);
        myCanvas.drawLine(left, bottom, right, bottom, myPaint);
        myCanvas.drawLine(left, top, left, bottom, myPaint);

    }

    /**
     * 绘制数字和连线
     */
    public void drawNumAndArrow()
    {
        if (this.linkMode <= 0)
        {
            return;
        }
        ReceiverPiece piece = null;
        num = 0;
        switch (this.linkMode)
        {
            case ReceiverLinkMode.ONE:
            {

                for (int i = 0; i < row; i++)
                {
                    if (i % 2 == 0)
                    { // 奇数行
                        for (int j = 0; j < column; j++)
                        {
                            piece = pieceArr[i][j];
                            drawPiece(piece);
                        }
                    }
                    else
                    {// 偶数行
                        for (int j = column - 1; j >= 0; j--)
                        {
                            piece = pieceArr[i][j];
                            drawPiece(piece);
                        }
                    }
                }
            }
                break;
            case ReceiverLinkMode.TWO:
            {

                for (int i = 0; i < row; i++)
                {
                    if (i % 2 == 1)
                    {
                        for (int j = 0; j < column; j++)
                        {
                            piece = pieceArr[i][j];
                            drawPiece(piece);
                        }
                    }
                    else
                    {
                        for (int j = column - 1; j >= 0; j--)
                        {
                            piece = pieceArr[i][j];
                            drawPiece(piece);
                        }
                    }
                }

            }
                break;
            case ReceiverLinkMode.THREE:
            {
                for (int i = row - 1; i >= 0; i--)
                {
                    if ((row - i) % 2 == 1)
                    { // 奇数行
                        for (int j = 0; j < column; j++)
                        {
                            piece = pieceArr[i][j];
                            drawPiece(piece);
                        }
                    }
                    else
                    {// 偶数行
                        for (int j = column - 1; j >= 0; j--)
                        {
                            piece = pieceArr[i][j];
                            drawPiece(piece);
                        }
                    }
                }

            }
                break;
            case ReceiverLinkMode.FOUR:
            {
                for (int i = row - 1; i >= 0; i--)
                {
                    if ((row - i) % 2 == 0)
                    {
                        for (int j = 0; j < column; j++)
                        {
                            piece = pieceArr[i][j];
                            drawPiece(piece);
                        }
                    }
                    else
                    {
                        for (int j = column - 1; j >= 0; j--)
                        {
                            piece = pieceArr[i][j];
                            drawPiece(piece);
                        }
                    }
                }

            }
                break;
            case ReceiverLinkMode.FIVE:
            {
                for (int i = 0; i < column; i++)
                {
                    if (i % 2 == 0)
                    { // 奇数行
                        for (int j = 0; j < row; j++)
                        {
                            piece = pieceArr[j][i];
                            drawPiece(piece);
                        }
                    }
                    else
                    {// 偶数行
                        for (int j = row - 1; j >= 0; j--)
                        {
                            piece = pieceArr[j][i];
                            drawPiece(piece);
                        }
                    }
                }

            }
                break;
            case ReceiverLinkMode.SIX:
            {
                for (int i = 0; i < column; i++)
                {
                    if (i % 2 == 1)
                    {
                        for (int j = 0; j < row; j++)
                        {
                            piece = pieceArr[j][i];
                            drawPiece(piece);
                        }
                    }
                    else
                    {
                        for (int j = row - 1; j >= 0; j--)
                        {
                            piece = pieceArr[j][i];
                            drawPiece(piece);
                        }
                    }
                }

            }
                break;
            case ReceiverLinkMode.SEVEN:
            {
                for (int i = column - 1; i >= 0; i--)
                {
                    if ((column - i) % 2 == 1)// 偶数行
                    {
                        for (int j = 0; j < row; j++)
                        {
                            piece = pieceArr[j][i];
                            drawPiece(piece);
                        }
                    }
                    else
                    {
                        for (int j = row - 1; j >= 0; j--)
                        {
                            piece = pieceArr[j][i];
                            drawPiece(piece);
                        }
                    }
                }
            }
                break;
            case ReceiverLinkMode.ENIGT:
            {
                for (int i = column - 1; i >= 0; i--)
                {
                    if ((column - i) % 2 == 0)// 偶数行
                    {
                        for (int j = 0; j < row; j++)
                        {
                            piece = pieceArr[j][i];
                            drawPiece(piece);
                        }
                    }
                    else
                    {
                        for (int j = row - 1; j >= 0; j--)
                        {
                            piece = pieceArr[j][i];
                            drawPiece(piece);
                        }
                    }

                }

            }
                break;
        }
    }

    /**
     * 绘制单元格
     * @param piece
     */
    public void drawPiece(ReceiverPiece piece)
    {
        Rect rect = new Rect(piece.getBeginX(), piece.getBeginY(),
                piece.getBeginX() + pieceWidth, piece.getBeginY() + pieceHeight);
        drawNum(num + 1, rect);
        if (num == 0)
        {
            fromX = (piece.getBeginX() * 2 + pieceWidth) / 2;
            fromY = (piece.getBeginY() * 2 + pieceHeight) / 2;

        }
        else
        {
            toX = (piece.getBeginX() * 2 + pieceWidth) / 2;
            toY = (piece.getBeginY() * 2 + pieceHeight) / 2;
            //
            drawArrow(fromX, fromY, toX, toY);
            if (num < (row * column - 1))
            {

                fromX = toX;
                fromY = toY;
            }

        }
        num++;

    }

    /**
     * 画箭头
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     */
    public void drawArrow(int sx, int sy, int ex, int ey)
    {
        myPaint.setColor(lineColor);
        double H = 8; // 箭头高度
        double L = 3.5; // 底边的一半
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        double awrad = Math.atan(L / H); // 箭头角度
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
        double [] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
        double [] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true,
                arraow_len);
        double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
        double y_3 = ey - arrXY_1[1];
        double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
        double y_4 = ey - arrXY_2[1];
        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();
        // 画线
        myCanvas.drawLine(sx, sy, ex, ey, myPaint);
        Path triangle = new Path();
        triangle.moveTo(ex, ey);
        triangle.lineTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.close();
        myCanvas.drawPath(triangle, myPaint);

    }

    // 计算
    public double [] rotateVec(int px, int py, double ang, boolean isChLen,
            double newLen)
    {
        double mathstr[] = new double [2];
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen)
        {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }

    // 绘制文字
    private void drawNum(int num, Rect targetRect)
    {
        myPaint.setColor(numColor);
        FontMetricsInt fontMetrics = myPaint.getFontMetricsInt();
        int baseline = targetRect.top
                + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top)
                / 2 - fontMetrics.top;
        myPaint.setTextAlign(Paint.Align.CENTER);
        myPaint.setTextSize(16.0f);
        myCanvas.drawText(String.valueOf(num), targetRect.centerX(), baseline,
                myPaint);
    }

    public void changeColumn(int column)
    {
        this.column = column;
        needDrawArrow = false;
        pieceArr = null;
        pieceArr = new ReceiverPiece [row] [column];
        updateView();
    }

    public void changeRow(int row)
    {
        this.row = row;
        needDrawArrow = false;
        pieceArr = null;
        pieceArr = new ReceiverPiece [row] [column];
        updateView();
    }

    public void changeLinkMode(int linkMode)
    {
        this.linkMode = linkMode;
        needDrawArrow = true;
        updateView();
    }

    public void updateView()
    {
        myCanvas.drawColor(Color.WHITE);
        invalidate();

    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //
        // final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //
        // setMeasuredDimension(widthSize, heightSize);
        int widthSpec = MeasureSpec.makeMeasureSpec(column * pieceWidth,
                MeasureSpec.AT_MOST);
        int heightSpec = MeasureSpec.makeMeasureSpec(row * pieceHeight,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthSpec, heightSpec);
    }


}
