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
import android.widget.Scroller;

import com.clt.entity.ReceiverLinkMode;
import com.clt.entity.ReceiverSetting;

/**
 * 接收卡设置主要显示视图
 *
 */
public class ReceiverShowView extends View
{

    private Canvas myCanvas;

    private Paint myPaint = new Paint();

    private int column = 1;// 列

    private int row = 1;// 行

    private int pieceWidth = 64;

    private int pieceHeight = 64;

    private int linkMode = -1;

    private ReceiverPiece [][] pieceArr = new ReceiverPiece [row] [column];

    private boolean showLines = false;

    private static final int NONE = 0;

    private static final int DRAG = 1;

    private static final int ZOOM = 2;

    private int mode = NONE;

    float oldDist;

    private boolean isMovingChangeColor = true;// 是否移动的时候改变颜色

    private int startX, startY, endX, endY;

    private int currentPort = 0;

    private int currentSenderCard = 0;

    private ArrayList<SlideArea> slideAreas = new ArrayList<ReceiverShowView.SlideArea>();// 需要滑动的区域

    private SlideArea nowSlideArea;

    private int width, height;

    private Context context;

    private Scroller scroller;

    // private ArrayList<SenderPort> hasDrawdSenderPort=new
    // ArrayList<SenderPort>();//已经划过的发送卡，网口

    public ReceiverShowView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public ReceiverShowView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ReceiverShowView(Context context)
    {
        super(context);
        this.context = context;
        init();
    }

    /**
     * 初始化
     */
    private void init()
    {
        scroller = new Scroller(this.context);
    }

    public int getCurrentPort()
    {
        return currentPort;
    }

    public void setCurrentPort(int currentPort)
    {
        this.currentPort = currentPort;
    }

    public int getCurrentSenderCard()
    {
        return currentSenderCard;
    }

    public void setCurrentSenderCard(int currentSenderCard)
    {
        this.currentSenderCard = currentSenderCard;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        this.myCanvas = canvas;
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setAntiAlias(true);
        canvas.drawColor(Color.WHITE);
        int id = 0;
        // 两步，画矩形，画箭头
        // 画矩形
        for (int i = 1; i <= row; i++)
        {
            for (int j = 1; j <= column; j++)
            {
                int left = pieceWidth * (j - 1);
                int top = pieceHeight * (i - 1);
                int right = left + pieceWidth;
                int bottom = top + pieceHeight;
                // 画矩形
                Log.i("xy", startX + "," + endX + ";" + startY + "," + endY);
                boolean flag = isInArea(i, j) || isInSlidingArea(i, j);
                Log.i("flag", flag + "");
                if (isMovingChangeColor && flag)
                {
                    myPaint.setColor(Color.BLACK);
                    id++;
                }
                else
                {
                    myPaint.setColor(Color.BLUE);
                }

                Rect targetRect = new Rect(left, top, right, bottom);
                canvas.drawRect(targetRect, myPaint);
                // 边框
                drawStrokes(left, top, right, bottom);
                // 文字，居中显示
                ReceiverPiece piece = pieceArr[i - 1][j - 1];

                myPaint.setColor(Color.WHITE);
                // FontMetricsInt fontMetrics = myPaint.getFontMetricsInt();
                // int baseline = targetRect.top
                // + (targetRect.bottom - targetRect.top
                // - fontMetrics.bottom + fontMetrics.top) / 2
                // - fontMetrics.top;
                // myPaint.setTextAlign(Paint.Align.CENTER);
                // String idStr = null;
                // if (piece != null)
                // {
                // idStr = String.valueOf(piece.getId());
                // Log.i("piece.getId", piece.getId()+"");
                // }
                // else
                // {
                // idStr = String.valueOf(id);
                // }
                // //画编号
                // if(isMovingChangeColor&&flag){
                // canvas.drawText(String.valueOf(id), targetRect.centerX(),
                // baseline, myPaint);
                // }else{
                // canvas.drawText(String.valueOf(0), targetRect.centerX(),
                // baseline, myPaint);
                // }

                if (piece == null)
                {
                    piece = new ReceiverPiece();
                    piece.setBeginX(left);
                    piece.setBeginY(top);
                    piece.setRowHeight(pieceHeight);
                    piece.setColumnWidth(pieceWidth);
                    piece.setIndexX(i);
                    piece.setIndexY(j);
                    piece.setId(id);
                    pieceArr[i - 1][j - 1] = piece;
                }

            }
        }
        // 画箭头
        if (showLines)
        {
            for (SlideArea slidearea : slideAreas)
            {
                drawAllArrows(slidearea.startX, slidearea.startY,
                        slidearea.endX, slidearea.endY);
            }
            if (nowSlideArea != null)
            {
                drawAllArrows(nowSlideArea.startX, nowSlideArea.startY,
                        nowSlideArea.endX, nowSlideArea.endY);
            }
        }

    }

    /**
     * 改变了行列数
     * @param row
     * @param column
     */
    public void changeRowColumn(int row, int column)
    {
        resetView();
        this.column = column;
        this.row = row;
        this.pieceArr = null;
        this.pieceArr = new ReceiverPiece [row] [column];
        showLines = false;
        invalidate();
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
    }

    // 是否显示箭头
    public void setShowLines(boolean showLines)
    {
        this.showLines = showLines;
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

    public int getColumn()
    {
        return column;
    }

    public void setColumn(int column)
    {
        this.column = column;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getLinkMode()
    {
        return linkMode;
    }

    public void setLinkMode(int linkMode)
    {
        this.linkMode = linkMode;
    }

    /**
     * 根据选择的模式，画箭头
     */
    public void drawAllArrows(int startX, int startY, int endX, int endY)
    {
        if (this.linkMode <= 0)
        {
            return;
        }
        showLines = false;
        switch (this.linkMode)
        {
            case ReceiverLinkMode.ONE:
                drawModeOne(startX, startY, endX, endY);
                break;
            case ReceiverLinkMode.TWO:
                drawModeTwo(startX, startY, endX, endY);
                break;
            case ReceiverLinkMode.THREE:
                drawModeThree(startX, startY, endX, endY);
                break;
            case ReceiverLinkMode.FOUR:
                drawModeFour(startX, startY, endX, endY);
                break;
            case ReceiverLinkMode.FIVE:
                drawModeFive(startX, startY, endX, endY);
                break;
            case ReceiverLinkMode.SIX:
                drawModeSix(startX, startY, endX, endY);
                break;
            case ReceiverLinkMode.SEVEN:
                drawModeSeven(startX, startY, endX, endY);
                break;
            case ReceiverLinkMode.ENIGT:
                drawModeNight(startX, startY, endX, endY);
                break;
        }
    }

    /**
     * 画箭头
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     */
    public void drawAL(int sx, int sy, int ex, int ey)
    {
        myPaint.setColor(Color.parseColor("#00FFFF"));
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

    /**
     * 画第一种模式
     */
    private void drawModeOne(int startX, int startY, int endX, int endY)
    {
        int id = 0;
        int column = endX;
        int row = endY;
        int fromX = -1, fromY = -1, toX = -1, toY = -1;
        for (int i = startY; i <= row; i++)
        {
            if (i % 2 == 1)
            { // 奇数行
                for (int j = startX; j <= column; j++)
                {
                    ReceiverPiece piece = pieceArr[i - 1][j - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
            else if (i % 2 == 0)
            {// 偶数行
                for (int j = column; j >= startX; j--)
                {
                    ReceiverPiece piece = pieceArr[i - 1][j - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
        }
    }

    /**
     * 画第二种模式
     */
    private void drawModeTwo(int startX, int startY, int endX, int endY)
    {
        int id = 0;
        int column = endX;
        int row = endY;
        int fromX = -1, fromY = -1, toX = -1, toY = -1;
        for (int i = startY; i <= row; i++)
        {
            if (i % 2 == 0)
            { // 偶数行
                for (int j = startX; j <= column; j++)
                {
                    ReceiverPiece piece = pieceArr[i - 1][j - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
            else if (i % 2 == 1)
            {// 奇数行
                for (int j = column; j >= startX; j--)
                {
                    // if(j==column&&i==startX)
                    ReceiverPiece piece = pieceArr[i - 1][j - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
        }
    }

    /**
     * 画第三种模式
     */
    private void drawModeThree(int startX, int startY, int endX, int endY)
    {
        int id = 0;
        int column = endX;
        int row = endY;
        int fromX = -1, fromY = -1, toX = -1, toY = -1;
        for (int i = row; i >= startY; i--)
        {
            if ((row + 1 - i) % 2 == 1)
            { // 奇数行
                for (int j = startX; j <= column; j++)
                {
                    ReceiverPiece piece = pieceArr[i - 1][j - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
            else if ((row + 1 - i) % 2 == 0)
            {// 偶数行
                for (int j = column; j >= startX; j--)
                {
                    ReceiverPiece piece = pieceArr[i - 1][j - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
        }
    }

    /**
     * 画第四种模式
     */
    private void drawModeFour(int startX, int startY, int endX, int endY)
    {
        int id = 0;
        int column = endX;
        int row = endY;
        int fromX = -1, fromY = -1, toX = -1, toY = -1;
        for (int i = row; i >= startY; i--)
        {
            if ((row + 1 - i) % 2 == 0)
            { // 偶数行
                for (int j = startX; j <= column; j++)
                {
                    ReceiverPiece piece = pieceArr[i - 1][j - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
            else if ((row + 1 - i) % 2 == 1)
            {// 奇数行
                for (int j = column; j >= startX; j--)
                {
                    ReceiverPiece piece = pieceArr[i - 1][j - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
        }
    }

    /**
     * 画第五种模式
     */
    private void drawModeFive(int startX, int startY, int endX, int endY)
    {
        int id = 0;
        int column = endX;
        int row = endY;
        int fromX = -1, fromY = -1, toX = -1, toY = -1;
        for (int i = startX; i <= column; i++)
        {
            if (i % 2 == 1)
            { // 奇数行
                for (int j = startY; j <= row; j++)
                {
                    ReceiverPiece piece = pieceArr[j - 1][i - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
            else if (i % 2 == 0)
            {// 偶数行
                for (int j = row; j >= startY; j--)
                {
                    ReceiverPiece piece = pieceArr[j - 1][i - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
        }
    }

    /**
     * 画第六种模式
     */
    private void drawModeSix(int startX, int startY, int endX, int endY)
    {

        int id = 0;
        int column = endX;
        int row = endY;
        int fromX = -1, fromY = -1, toX = -1, toY = -1;
        for (int i = startX; i <= column; i++)
        {
            if (i % 2 == 0)// 偶数行
            {
                for (int j = startY; j <= row; j++)
                {
                    ReceiverPiece piece = pieceArr[j - 1][i - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
            else if (i % 2 == 1)// 奇数行
            {
                for (int j = row; j >= startY; j--)
                {
                    ReceiverPiece piece = pieceArr[j - 1][i - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
        }
    }

    /**
     * 画第七种模式
     */
    private void drawModeSeven(int startX, int startY, int endX, int endY)
    {
        int id = 0;
        int column = endX;
        int row = endY;
        int fromX = -1, fromY = -1, toX = -1, toY = -1;
        for (int i = column; i >= startX; i--)
        {
            if ((column + 1 - i) % 2 == 1)// 偶数行
            {
                for (int j = startY; j <= row; j++)
                {
                    ReceiverPiece piece = pieceArr[j - 1][i - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
            else if ((column + 1 - i) % 2 == 0)// 奇数行
            {
                for (int j = row; j >= startY; j--)
                {
                    ReceiverPiece piece = pieceArr[j - 1][i - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);
                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
        }
    }

    /**
     * 画第八种模式
     */
    private void drawModeNight(int startX, int startY, int endX, int endY)
    {
        int id = 0;
        int column = endX;
        int row = endY;
        int fromX = -1, fromY = -1, toX = -1, toY = -1;
        for (int i = column; i >= startX; i--)
        {
            if ((column + 1 - i) % 2 == 0)// 偶数行
            {
                for (int j = startY; j <= row; j++)
                {
                    ReceiverPiece piece = pieceArr[j - 1][i - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);

                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }
            else if ((column + 1 - i) % 2 == 1)// 奇数行
            {
                for (int j = row; j >= startY; j--)
                {
                    ReceiverPiece piece = pieceArr[j - 1][i - 1];
                    // piece.setId(++id);
                    toX = (piece.getBeginX() + piece.getBeginX() + piece
                            .getColumnWidth()) / 2;
                    toY = (piece.getBeginY() + piece.getBeginY() + piece
                            .getRowHeight()) / 2;
                    if (fromX != -1 && fromY != -1)
                    {
                        drawAL(fromX, fromY, toX, toY);

                    }
                    drawNum(++id, toX, toY);
                    fromX = toX;
                    fromY = toY;
                }
            }

        }

        Log.i("piece.getId", "saf");
    }

    private void drawNum(int id, int x, int y)
    {
        myCanvas.drawText(String.valueOf(id), x, y, myPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        //
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                break;
            case MotionEvent.ACTION_UP:
                actionUp();
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
        }
        return true;
    }

    public void actionMove(MotionEvent event)
    {
        if (mode == ZOOM)
        {
            float newDist = spacing(event);

            if (newDist > oldDist)
            {
                // zoomOut();
            }
            if (newDist < oldDist)
            {
                // zoomIn();
                pieceWidth = 32;
                pieceHeight = 32;
                invalidate();
            }

        }
        int xx = (int) event.getX();
        int yy = (int) event.getY();
        endX = xx / pieceWidth;
        if (xx % pieceWidth != 0)
        {
            endX = endX + 1;
        }
        if (endX > column)
        {
            endX = column;
        }
        endY = yy / pieceHeight;
        if (yy % pieceHeight != 0)
        {
            endY = endY + 1;
        }
        if (endY > row)
        {
            endY = row;
        }
        isMovingChangeColor = true;
        showLines = false;

        nowSlideArea.setEndXY(endX, endY);
        // handleXY();
        // slideAreas.add(slideArea);
        invalidate();
    }

    public void actionUp()
    {
        Log.i("up", "ACTION_UP");
        mode = NONE;
        isMovingChangeColor = true;
        showLines = true;
        int hasDeawed = hasDrawed(currentSenderCard, currentPort);
        if (hasDeawed != -1)
        {
            slideAreas.remove(hasDeawed);
        }
        nowSlideArea.sendCardNum = currentSenderCard;
        nowSlideArea.port = currentPort;
        slideAreas.add(nowSlideArea);
        nowSlideArea = null;
        invalidate();
    }

    public void actionDown(MotionEvent event)
    {
        // resetView();
        mode = DRAG;
        int x = (int) event.getX();
        int y = (int) event.getY();
        showLines = false;
        startX = x / pieceWidth;
        if (x % pieceWidth != 0)
        {
            startX++;
        }
        startY = y / pieceHeight;
        if (y % pieceHeight != 0)
        {
            startY++;
        }
        nowSlideArea = new SlideArea();
        nowSlideArea.setStartXY(startX, startY);
        nowSlideArea.linkMode = linkMode;
        nowSlideArea.width = width;
        nowSlideArea.height = height;
    }

    /**
     * 处理xy的大小
     * @param event
     */
    private void handleXY()
    {
        if (startX > endX)
        {
            int xx = startX;
            startX = endX;
            endX = xx;
        }
        if (startY > endY)
        {
            int yy = startY;
            startY = endY;
            endY = yy;
        }
    }

    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
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

    /**
     * 重置
     */
    public void resetView()
    {
        isMovingChangeColor = false;
        pieceArr = new ReceiverPiece [row] [column];
        showLines = false;
        slideAreas.clear();
        invalidate();
    }

    /**
     * 全选
     */
    public void selectAll()
    {
        isMovingChangeColor = true;
        showLines = true;
        startX = 1;
        endX = column;
        startY = 1;
        endY = row;
        invalidate();

    }

    /**
     * 是否在要画的区域内
     * @param i
     * @param j
     * @return
     */
    private boolean isInArea(int i, int j)
    {
        if (slideAreas == null || slideAreas.isEmpty())
        {
            return false;
        }
        for (int z = 0; z < slideAreas.size(); z++)
        {
            SlideArea slideArea = slideAreas.get(z);
            slideArea.sortXY();
            if ((j >= slideArea.startX && j <= slideArea.endX)
                    && (i >= slideArea.startY && i <= slideArea.endY))
            {
                return true;
            }
        }

        return false;
    }

    private boolean isInSlidingArea(int i, int j)
    {
        if (nowSlideArea == null)
        {
            return false;
        }
        return (j >= nowSlideArea.startX && j <= nowSlideArea.endX)
                && (i >= nowSlideArea.startY && i <= nowSlideArea.endY);
    }

    /**
     * 是否已经画过
     * @param sendCard
     * @param port
     * @return
     */
    public int hasDrawed(int sendCard, int port)
    {
        if (slideAreas == null || slideAreas.isEmpty())
        {
            return -1;
        }
        SlideArea slideArea = null;
        for (int i = 0; i < slideAreas.size(); i++)
        {
            slideArea = slideAreas.get(i);
            if (slideArea.sendCardNum == sendCard && slideArea.port == port)
            {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<ReceiverSetting> getSlideAreas()
    {
        if (slideAreas == null || slideAreas.isEmpty())
        {
            return null;
        }
        ArrayList<ReceiverSetting> receiverSettings = new ArrayList<ReceiverSetting>();
        ReceiverSetting receiverSetting = null;
        for (SlideArea slideArea : slideAreas)
        {
            receiverSetting = new ReceiverSetting();
            slideArea.sortXY();
            receiverSetting.setColumn(slideArea.endX - slideArea.startX + 1);
            receiverSetting.setRow(slideArea.endY - slideArea.startY + 1);
            receiverSetting.setWidth(slideArea.width);
            receiverSetting.setHeight(slideArea.height);
            receiverSetting.setMode(slideArea.linkMode);
            receiverSetting.setSender(slideArea.sendCardNum);
            receiverSetting.setPort(slideArea.port);
            receiverSettings.add(receiverSetting);
        }
        return receiverSettings;
    }

    // /**
    // * 是否已经画过
    // * @param senderNum
    // * @param port
    // * @return
    // */
    // public int hasDrawed(int senderNum,int port){
    // if(hasDrawdSenderPort==null||hasDrawdSenderPort.isEmpty()){
    // return -1;
    // }
    // for (int i = 0; i < hasDrawdSenderPort.size(); i++)
    // {
    // SenderPort senderPort = hasDrawdSenderPort.get(i);
    // if(senderNum==senderPort.sendCardNum&&port==senderPort.port){
    // return i;
    // }
    // }
    // return -1;
    // }

    /**
     * 
     *滑动区域
     */
    private class SlideArea
    {
        int linkMode;

        int startX, startY, endX, endY;

        int sendCardNum, port;

        int width, height;

        public SlideArea()
        {
        }

        public SlideArea(int startX, int startY, int endX, int endY)
        {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }

        public void setStartXY(int startX, int startY)
        {
            this.startX = startX;
            this.startY = startY;
        }

        public void setEndXY(int endX, int endY)
        {
            this.endX = endX;
            this.endY = endY;
        }

        private void sortXY()
        {
            if (startX > endX)
            {
                int xx = startX;
                startX = endX;
                endX = xx;
            }
            if (startY > endY)
            {
                int yy = startY;
                startY = endY;
                endY = yy;
            }
        }
    }

    /**
     *  向下
     * @param width 
     */
    public void goDown(int height)
    {
        //scroller.startScroll(getWidth(), 0, getWidth()+100, 0, 1000);
        int viewHeight=getHeight();
        int scrollY=getScrollY();
        int d=10;
        int value=viewHeight-scrollY-height;
        if(value<=0){
            return;
        }
        if(viewHeight-scrollY-height<d){
            d=viewHeight-scrollY-height;
            
        }
        scrollBy(0, d);
    }
    /**
     * 向右
     */
    public void goRight()
    {
        
        //scroller.startScroll(getWidth(), 0, getWidth()+100, 0, 1000);
        scrollBy(10,0);
    }

}
