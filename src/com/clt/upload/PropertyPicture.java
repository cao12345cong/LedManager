package com.clt.upload;
/**
 * 图片节目属性
 *
 */
public class PropertyPicture extends PropertyItem
{
	public String pictureWidth="";
	
	public String pictureHeight="";
	
	public String pictureName="";
	
	public String picturePath="";
	
	//fileSource
	public String fsIsRelative="1";
	
	public String fsFilePath="";
	
	public String fsMd5="";
	
	public String reserveAs="0"; //是否保持高度比
	
	
	
	public PropertyPicture()
	{
		materialName="image";
		materialType=String.valueOf(MaterialType.Picture);
	}
	
}
