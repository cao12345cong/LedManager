package com.clt.ui;

import java.io.Serializable;

/**
 * 接收卡参数设置，单个发送卡视图
 * @author Administrator
 *
 */
public class ReceiverPiece implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -6690734282071443664L;

    private int id;// 编号

    private int columnWidth;// 列长

    private int rowHeight;// 行高

    private int beginX;

    private int beginY;

    // 在piece[][]二位数组中第一维的索引值
    private int indexX;

    // 在piece[][]二位数组中第二维的索引值
    private int indexY;

    private int senderCardNum = -1;// 发送卡号

    private int portNum = -1;// 网口序号

    public int getBeginX()
    {
        return beginX;
    }

    public void setBeginX(int beginX)
    {
        this.beginX = beginX;
    }

    public int getBeginY()
    {
        return beginY;
    }

    public void setBeginY(int beginY)
    {
        this.beginY = beginY;
    }

    public int getIndexX()
    {
        return indexX;
    }

    public void setIndexX(int indexX)
    {
        this.indexX = indexX;
    }

    public int getIndexY()
    {
        return indexY;
    }

    public void setIndexY(int indexY)
    {
        this.indexY = indexY;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getColumnWidth()
    {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth)
    {
        this.columnWidth = columnWidth;
    }

    public int getRowHeight()
    {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight)
    {
        this.rowHeight = rowHeight;
    }
    
    public int getSenderCardNum()
    {
        return senderCardNum;
    }

    public void setSenderCardNum(int senderCardNum)
    {
        this.senderCardNum = senderCardNum;
    }

    public int getPortNum()
    {
        return portNum;
    }

    public void setPortNum(int portNum)
    {
        this.portNum = portNum;
    }

    @Override
    public String toString()
    {
        return "ReceiverPiece [id=" + id + ", columnWidth=" + columnWidth
                + ", rowHeight=" + rowHeight + ", beginX=" + beginX
                + ", beginY=" + beginY + ", indexX=" + indexX + ", indexY="
                + indexY + "]";
    }

}
