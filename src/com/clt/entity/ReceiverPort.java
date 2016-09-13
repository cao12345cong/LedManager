package com.clt.entity;

import java.io.Serializable;

public class ReceiverPort implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -3029433764961959408L;
    private String port;
    private int startx;
    private int starty;
    private int width;
    private int height;
    
    public ReceiverPort()
    {
        super();
    }
    public ReceiverPort(String port, int startx, int starty, int width,
            int height)
    {
        super();
        this.port = port;
        this.startx = startx;
        this.starty = starty;
        this.width = width;
        this.height = height;
    }
    public String getPort()
    {
        return port;
    }
    public void setPort(String port)
    {
        this.port = port;
    }
    public int getStartx()
    {
        return startx;
    }
    public void setStartx(int startx)
    {
        this.startx = startx;
    }
    public int getStarty()
    {
        return starty;
    }
    public void setStarty(int starty)
    {
        this.starty = starty;
    }
    public int getWidth()
    {
        return width;
    }
    public void setWidth(int width)
    {
        this.width = width;
    }
    public int getHeight()
    {
        return height;
    }
    public void setHeight(int height)
    {
        this.height = height;
    }
    
}
