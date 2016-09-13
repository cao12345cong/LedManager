package com.clt.upload;

import java.io.File;
import java.util.List;
/**
 * vsn文件工厂
 */
public interface VsnFileFactory
{
	
	/**
	 * 生成VSN节目文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param property
	 *            属性
	 * @return
	 */
	File createVsnFile(PropertyItem property,String vsnPath);
	
	File createVsnFile(PropertyCommon pc,List<PropertyItem> ptList,String vsnPath);
	
}
