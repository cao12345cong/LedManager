package com.clt.upload;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.clt.upload.PropertyItem.MaterialType;

/**
 * vsn文件工厂方法实现
 */
public class VsnFileFactoryImpl implements VsnFileFactory
{
	public VsnFileFactoryImpl()
	{
		
	}
	@Override
	public File createVsnFile(PropertyCommon pc, List<PropertyItem> ptList,
			String vsnPath)
	{
		try
		{
			if (pc == null||ptList==null)
			{
				return null;
			}
			File vsnFile = new File(vsnPath);
			if (vsnFile.exists())
			{
				vsnFile.delete();
			}
			
			Document document = DocumentHelper.createDocument(); // 创建文档
			//vsn公共属性
			Element Pages = createProgramNode(pc, document);
			Element Regions = createPageNode(pc, Pages);
			Element Items = createRegionNode(pc, Regions);
			
			PropertyItem property=null;
			for (int i = 0; i < ptList.size(); i++)
			{
				property=ptList.get(i);
				if (property instanceof PropertyPicture)
				{
					createPicProgramNode((PropertyPicture)property, Items);
				}
				else if (property instanceof PropertyVedio)
				{
					createVedioProgramNode((PropertyVedio)property, Items);
				}
				else if (property instanceof PropertySingleLineText)
				{
					createSingleLineTextProgramNode((PropertySingleLineText)property, Items);
				}else if(property instanceof PropertyMultiLineText){
					createMultiLineTextProgramNode((PropertyMultiLineText)property, Items);
				}
				else{
					return null;
				}
			}
			
			// write xml
			FileWriter fileWriter = new FileWriter(vsnPath);
			// 设置文件编码
			OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
			xmlFormat.setEncoding("UTF-8");
			// xmlFormat.setTrimText(false);//设置text中是否要删除其中多余的空格
			XMLWriter xmlWriter = new XMLWriter(fileWriter, xmlFormat);
			xmlWriter.write(document); // 写入文件中
			xmlWriter.close();
			return new File(vsnPath) ;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public File createVsnFile(PropertyItem property, String vsnPath)
	{
//		try
//		{
//			if (property == null)
//			{
//				return null;
//			}
//			File vsnFile = new File(vsnPath);
//			if (vsnFile.exists())
//			{
//				vsnFile.delete();
//			}
//			
//			Document document = DocumentHelper.createDocument(); // 创建文档
//			//vsn公共属性
//			Element Pages = createProgramNode(property, document);
//			Element Regions = createPageNode(property, Pages);
//			Element Items = createRegionNode(property, Regions);
//			if (property instanceof PropertyPicture)
//			{
//				createPicProgramNode((PropertyPicture)property, Items);
//			}
//			else if (property instanceof PropertyVedio)
//			{
//				createVedioProgramNode((PropertyVedio)property, Items);
//			}
//			else if (property instanceof PropertySingleLineText)
//			{
//				createSingleLineTextProgramNode((PropertySingleLineText)property, Items);
//			}else if(property instanceof PropertyMultiLineText){
//				createMultiLineTextProgramNode((PropertyMultiLineText)property, Items);
//			}
//			else{
//				return null;
//			}
//			// write xml
//			FileWriter fileWriter = new FileWriter(vsnPath);
//			// 设置文件编码
//			OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
//			xmlFormat.setEncoding("UTF-8");
//			// xmlFormat.setTrimText(false);//设置text中是否要删除其中多余的空格
//			XMLWriter xmlWriter = new XMLWriter(fileWriter, xmlFormat);
//			xmlWriter.write(document); // 写入文件中
//			xmlWriter.close();
//			return new File(vsnPath) ;
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//			return null;
//		}
		return null;
	}
	
	
	/**
	 * 创建图片节点
	 * @param p
	 * @param Items
	 * @return
	 */
	private Element createPicProgramNode(PropertyPicture p, Element Items)
	{
		Element Item = Items.addElement("Item");
		Element Id = Item.addElement("Id");
		Id.setText(p.materialId);
		Element Name = Item.addElement("Name");
		Name.setText(p.materialName);
		Element Type = Item.addElement("Type");
		Type.setText(p.materialType);
		Element Version = Item.addElement("Version");
		Version.setText(p.materialVersion);
		Element BackColor = Item.addElement("BackColor");
		BackColor.setText(p.materialBackColor);
		Element Alhpa = Item.addElement("Alhpa");
		Alhpa.setText(p.materialAlhpa);
		Element Duration = Item.addElement("Duration");
		Duration.setText(p.materialDuration);
		Element BeGlaring = Item.addElement("BeGlaring");
		BeGlaring.setText(p.materialBeGlaring);
		Element IsNeedUpdate = Item.addElement("IsNeedUpdate");
		IsNeedUpdate.setText(p.materialIsNeedUpdate);
		Element UpdateInterval = Item.addElement("UpdateInterval");
		UpdateInterval.setText("10000");
		Element MirrorOrHandstand = Item.addElement("MirrorOrHandstand");
		MirrorOrHandstand.setText(p.materialMirrorHandstand);
		Element PlayTimes = Item.addElement("PlayTimes");
		PlayTimes.setText(p.materialPlayTimes);
		// effect
		Element effect = Item.addElement("effect");
		Element IsStatic = effect.addElement("effect");
		IsStatic.setText(p.effectIsStatic);
		Element StayType = effect.addElement("StayType");
		StayType.setText(p.effectStayType);
		// inEffect
		Element inEffect = Item.addElement("inEffect");
		Type = inEffect.addElement("Type");
		Type.setText(p.inEffectType);
		Element Time = inEffect.addElement("Time");
		Time.setText(p.inEffectTime);
		Element repeatX = inEffect.addElement("repeatX");
		repeatX.setText(p.inEffectRepeatX);
		Element repeatY = inEffect.addElement("repeatY");
		repeatY.setText(p.inEffectRepeatY);
		Element IsTran = inEffect.addElement("IsTran");
		IsTran.setText(p.inEffectIsTran);
		
		// outEffect
		Element outEffect = Item.addElement("outEffect");
		Type = outEffect.addElement("Type");
		Type.setText(p.outEffectType);
		Time = outEffect.addElement("Time");
		Time.setText(p.outEffectTime);
		repeatX = outEffect.addElement("repeatX");
		repeatX.setText(p.outEffectRepeatX);
		repeatY = outEffect.addElement("repeatY");
		repeatY.setText(p.outEffectRepeatY);
		IsTran = outEffect.addElement("IsTran");
		IsTran.setText(p.outEffectIsTran);
		
		// MultiPicInfo
		Element MultiPicInfo = Item.addElement("MultiPicInfo");
		Element Source = MultiPicInfo.addElement("Source");
		Source.setText(p.multiPicInfoSource);
		Element PicCount = MultiPicInfo.addElement("PicCount");
		PicCount.setText(p.multiPicInfoSource);
		Element OnePicDuration = MultiPicInfo.addElement("OnePicDuration");
		OnePicDuration.setText(p.multiPicInfoSource);
		
		Element Width = Item.addElement("Width");
		Width.setText(p.pictureWidth);
		Element Height = Item.addElement("Height");
		Height.setText(p.pictureHeight);
		// fileSource
		Element FileSource = Item.addElement("FileSource");
		Element IsRelative = FileSource.addElement("IsRelative");
		IsRelative.setText(p.fsIsRelative);
		Element FilePath = FileSource.addElement("FilePath");
		FilePath.setText(p.fsFilePath);
		Element MD5 = FileSource.addElement("MD5");
		MD5.setText(p.fsMd5);
		
		Element ReserveAS = Item.addElement("ReserveAS");
		ReserveAS.setText(p.reserveAs);
		return null;
	}
	
	/**
	 * 创建视频节点
	 * @param p
	 * @param Items
	 * @return
	 */
	private Element createVedioProgramNode(PropertyVedio p, Element Items){
		
		 Element Item = Items.addElement("Item");
         Element Id = Item.addElement("Id");
         Id.setText(p.materialId);
         Element Name = Item.addElement("Name");
         Name.setText(p.materialName);
         Element Type = Item.addElement("Type");
         Type.setText(p.materialType);
         Element Version = Item.addElement("Version");
         Version.setText(p.materialVersion);
         Element BackColor = Item.addElement("BackColor");
         BackColor.setText(p.materialBackColor);
         Element Alhpa = Item.addElement("Alhpa");
         Alhpa.setText(p.materialAlhpa);
         Element Duration = Item.addElement("Duration");
         Duration.setText(p.materialDuration);
         Element BeGlaring = Item.addElement("BeGlaring");
         BeGlaring.setText(p.materialBeGlaring);
         Element IsNeedUpdate = Item.addElement("IsNeedUpdate");
         IsNeedUpdate.setText(p.materialIsNeedUpdate);
         Element UpdateInterval = Item.addElement("UpdateInterval");
         UpdateInterval.setText(p.materialUpdateInterval);
         Element MirrorOrHandstand = Item.addElement("MirrorOrHandstand");
         MirrorOrHandstand.setText(p.materialMirrorHandstand);
         Element PlayTimes = Item.addElement("PlayTimes");
         PlayTimes.setText(p.materialPlayTimes);
         // effect
         Element effect = Item.addElement("effect");
         Element IsStatic = effect.addElement("effect");
         IsStatic.setText(p.effectIsStatic);
         Element StayType = effect.addElement("StayType");
         StayType.setText(p.effectStayType);
         // inEffect
         Element inEffect = Item.addElement("inEffect");
         Type = inEffect.addElement("Type");
         Type.setText(p.inEffectType);
         Element Time = inEffect.addElement("Time");
         Time.setText(p.inEffectTime);
         Element repeatX = inEffect.addElement("repeatX");
         repeatX.setText(p.inEffectRepeatX);
         Element repeatY = inEffect.addElement("repeatY");
         repeatY.setText(p.inEffectRepeatY);
         Element IsTran = inEffect.addElement("IsTran");
         IsTran.setText(p.inEffectIsTran);

         // outEffect
         Element outEffect = Item.addElement("outEffect");
         Type = outEffect.addElement("Type");
         Type.setText(p.outEffectType);
         Time = outEffect.addElement("Time");
         Time.setText(p.outEffectTime);
         repeatX = outEffect.addElement("repeatX");
         repeatX.setText(p.outEffectRepeatX);
         repeatY = outEffect.addElement("repeatY");
         repeatY.setText(p.outEffectRepeatY);
         IsTran = outEffect.addElement("IsTran");
         IsTran.setText(p.outEffectIsTran);

         // MultiPicInfo
         Element MultiPicInfo = Item.addElement("MultiPicInfo");
         Element Source = MultiPicInfo.addElement("Source");
         Source.setText(p.multiPicInfoSource);
         Element PicCount = MultiPicInfo.addElement("PicCount");
         PicCount.setText(p.multiPicInfoPicCount);
         Element OnePicDuration = MultiPicInfo.addElement("OnePicDuration");
         OnePicDuration.setText(p.multiPicInfoOnePicDuration);
         
         Element Length = Item.addElement("Length");
         Length.setText(p.length);
         Element Width = Item.addElement("VideoWidth");
         Width.setText(p.vedioWidth);
         Element Height = Item.addElement("VideoHeight");
         Height.setText(p.vedioHeight);
         
         Element InOffset = Item.addElement("InOffset");
         InOffset.setText(p.inOffset);
         Element PlayLength = Item.addElement("PlayLength");
         PlayLength.setText(p.playLength);
         Element Volume = Item.addElement("Volume");
         Volume.setText(p.volume);
         Element Loop = Item.addElement("Loop");
         Loop.setText(p.loop);
         // fileSource
         Element FileSource = Item.addElement("FileSource");
         Element IsRelative = FileSource.addElement("IsRelative");
         IsRelative.setText(p.fsIsRelation);
         Element FilePath = FileSource.addElement("FilePath");
         FilePath.setText(p.fsFilePath);
         Element MD5 = FileSource.addElement("MD5");
         MD5.setText(p.fsMd5);

         Element ReserveAS = Item.addElement("ReserveAS");
         ReserveAS.setText(p.reserveAs);
         
         Element showX = Item.addElement("showX");
         showX.setText(p.showX);
         Element showY = Item.addElement("showY");
         showY.setText(p.showY);
         Element showWidth = Item.addElement("showWidth");
         showWidth.setText(p.showWidth);
         Element showHeight = Item.addElement("showHeight");
         showHeight.setText(p.showHeight);
         Element IsSetShowRegion = Item.addElement("IsSetShowRegion");
         IsSetShowRegion.setText(p.isSetShowRegion);
         Element IsSetPlayLen = Item.addElement("IsSetPlayLen");
         IsSetPlayLen.setText(p.isSetPlayLen);
         return null;
	}
	
	/**
	 * 创建单行文本节点
	 * @param p
	 * @param Items
	 * @return
	 */
	private Element createSingleLineTextProgramNode(PropertySingleLineText p, Element Items){
		
		 Element Item = Items.addElement("Item");
         Element Id = Item.addElement("Id");
         Id.setText(p.materialId);
         Element Name = Item.addElement("Name");
         Name.setText(p.materialName);
         Element Type = Item.addElement("Type");
         Type.setText(p.materialType);
         Element Version = Item.addElement("Version");
         Version.setText(p.materialVersion);
         Element BackColor = Item.addElement("BackColor");
         BackColor.setText(p.materialBackColor);
         Element Alhpa = Item.addElement("Alhpa");
         Alhpa.setText(p.materialAlhpa);
         Element Duration = Item.addElement("Duration");
         Duration.setText(p.materialDuration);
         Element BeGlaring = Item.addElement("BeGlaring");
         BeGlaring.setText(p.materialBeGlaring);
         Element IsNeedUpdate = Item.addElement("IsNeedUpdate");
         IsNeedUpdate.setText(p.materialIsNeedUpdate);
         Element UpdateInterval = Item.addElement("UpdateInterval");
         UpdateInterval.setText(p.materialUpdateInterval);
         Element MirrorOrHandstand = Item.addElement("MirrorOrHandstand");
         MirrorOrHandstand.setText(p.materialMirrorHandstand);
         Element PlayTimes = Item.addElement("PlayTimes");
         PlayTimes.setText(p.materialPlayTimes);
         // effect
         Element effect = Item.addElement("effect");
         Element IsStatic = effect.addElement("effect");
         IsStatic.setText(p.effectIsStatic);
         Element StayType = effect.addElement("StayType");
         StayType.setText(p.effectStayType);
         // inEffect
         Element inEffect = Item.addElement("inEffect");
         Type = inEffect.addElement("Type");
         Type.setText(p.inEffectType);
         Element Time = inEffect.addElement("Time");
         Time.setText(p.inEffectTime);
         Element repeatX = inEffect.addElement("repeatX");
         repeatX.setText(p.inEffectRepeatX);
         Element repeatY = inEffect.addElement("repeatY");
         repeatY.setText(p.inEffectRepeatY);
         Element IsTran = inEffect.addElement("IsTran");
         IsTran.setText(p.inEffectIsTran);

         // outEffect
         Element outEffect = Item.addElement("outEffect");
         Type = outEffect.addElement("Type");
         Type.setText(p.outEffectType);
         Time = outEffect.addElement("Time");
         Time.setText(p.outEffectTime);
         repeatX = outEffect.addElement("repeatX");
         repeatX.setText(p.outEffectRepeatX);
         repeatY = outEffect.addElement("repeatY");
         repeatY.setText(p.outEffectRepeatY);
         IsTran = outEffect.addElement("IsTran");
         IsTran.setText(p.outEffectIsTran);

         // MultiPicInfo
         Element MultiPicInfo = Item.addElement("MultiPicInfo");
         Element Source = MultiPicInfo.addElement("Source");
         Source.setText(p.multiPicInfoSource);
         Element PicCount = MultiPicInfo.addElement("PicCount");
         PicCount.setText(p.multiPicInfoPicCount);
         Element OnePicDuration = MultiPicInfo.addElement("OnePicDuration");
         OnePicDuration.setText(p.multiPicInfoOnePicDuration);
         
         Element TextColor=Item.addElement("TextColor");
         TextColor.setText(p.textColor);
         //LogFont
         Element LogFont=Item.addElement("LogFont");
         Element lfHeight=LogFont.addElement("lfHeight");
         lfHeight.setText(p.logFontIfHeight);
         Element lfWidth=LogFont.addElement("lfWidth");
         lfWidth.setText(p.logFontIfWidth);
         Element lfEscapement=LogFont.addElement("lfEscapement");
         lfEscapement.setText(p.logFontlfEscapement);
         Element lfOrientation=LogFont.addElement("lfOrientation");
         lfOrientation.setText(p.logFontlfOrientation);
         Element lfWeight=LogFont.addElement("lfWeight");
         lfWeight.setText(p.logFontlfWeight);
         Element lfItalic=LogFont.addElement("lfItalic");
         lfItalic.setText(p.logFontlfItalic);
         Element lfUnderline=LogFont.addElement("lfUnderline");
         lfUnderline.setText(p.logFontlfUnderline);
         Element lfStrikeOut=LogFont.addElement("lfStrikeOut");
         lfStrikeOut.setText(p.logFontlfStrikeOut);
         Element lfCharSet=LogFont.addElement("lfCharSet");
         lfCharSet.setText(p.logFontlfCharSet);
         Element lfOutPrecision=LogFont.addElement("lfOutPrecision");
         lfOutPrecision.setText(p.logFontlfOutPrecision);
         Element lfQuality=LogFont.addElement("lfQuality");
         lfQuality.setText(p.logFontlfQuality);
         Element lfPitchAndFamily=LogFont.addElement("lfPitchAndFamily");
         lfPitchAndFamily.setText(p.logFontlfPitchAndFamily);
         Element lfFaceName=LogFont.addElement("lfFaceName");
         lfFaceName.setText(p.logFontlfFaceName);
         //text
         Element Text=Item.addElement("Text");
         Text.setText(p.text);
         //FileSource
         Element FileSource = Item.addElement("FileSource");
         Element IsRelative = FileSource.addElement("IsRelative");
         IsRelative.setText(p.fsIsIsRelative);
         Element FilePath = FileSource.addElement("FilePath");
         FilePath.setText(p.fsFilePath);
         Element MD5 = FileSource.addElement("MD5");
         MD5.setText(p.fsMD5);
         //
         Element IsFromFile = Item.addElement("IsFromFile");
         IsFromFile.setText(p.isFromFile);
         Element IsScrol = Item.addElement("IsScroll");
         IsScrol.setText(p.isScroll);
         Element Speed= Item.addElement("Speed");
         Speed.setText(p.speed);
         Element IsHeadConnectTail= Item.addElement("IsHeadConnectTail");
         IsHeadConnectTail.setText(p.isHeadConnectTail);
         Element WordSpacing= Item.addElement("WordSpacing");
         WordSpacing.setText(p.wordSpacing);
         Element RepeatCount= Item.addElement("RepeatCount");
         RepeatCount.setText(p.repeatCount);
         Element IsScrollByTime= Item.addElement("IsScrollByTime");
         IsScrollByTime.setText(p.isScrollByTime);
         Element PlayLenth= Item.addElement("PlayLenth");
         PlayLenth.setText(p.playLenth);
         Element MoveDir= Item.addElement("MoveDir");
         MoveDir.setText(p.moveDir);
         Element IfSpeedByFrame= Item.addElement("IfSpeedByFrame");
         IfSpeedByFrame.setText(p.ifSpeedByFrame);
         Element SpeedByFrame= Item.addElement("SpeedByFrame");
         SpeedByFrame.setText(p.speedByFrame);
         Element WordType= Item.addElement("WordType");
         WordType.setText(p.wordType);
         
         return null;
	}
	
	
	/**
	 * 创建多行文本节点
	 * @param p
	 * @param Items
	 * @return
	 */
	private Element createMultiLineTextProgramNode(PropertyMultiLineText p, Element Items){
		
		 Element Item = Items.addElement("Item");
         Element Id = Item.addElement("Id");
         Id.setText(p.materialId);
         Element Name = Item.addElement("Name");
         Name.setText(p.materialName);
         Element Type = Item.addElement("Type");
         Type.setText(p.materialType);
         Element Version = Item.addElement("Version");
         Version.setText(p.materialVersion);
         Element BackColor = Item.addElement("BackColor");
         BackColor.setText(p.materialBackColor);
         Element Alhpa = Item.addElement("Alhpa");
         Alhpa.setText(p.materialAlhpa);
         Element Duration = Item.addElement("Duration");
         Duration.setText(p.materialDuration);
         Element BeGlaring = Item.addElement("BeGlaring");
         BeGlaring.setText(p.materialBeGlaring);
         Element IsNeedUpdate = Item.addElement("IsNeedUpdate");
         IsNeedUpdate.setText(p.materialIsNeedUpdate);
         Element UpdateInterval = Item.addElement("UpdateInterval");
         UpdateInterval.setText(p.materialUpdateInterval);
         Element MirrorOrHandstand = Item.addElement("MirrorOrHandstand");
         MirrorOrHandstand.setText(p.materialMirrorHandstand);
         Element PlayTimes = Item.addElement("PlayTimes");
         PlayTimes.setText(p.materialPlayTimes);
         // effect
         Element effect = Item.addElement("effect");
         Element IsStatic = effect.addElement("effect");
         IsStatic.setText(p.effectIsStatic);
         Element StayType = effect.addElement("StayType");
         StayType.setText(p.effectStayType);
         // inEffect
         Element inEffect = Item.addElement("inEffect");
         Type = inEffect.addElement("Type");
         Type.setText(p.inEffectType);
         Element Time = inEffect.addElement("Time");
         Time.setText(p.inEffectTime);
         Element repeatX = inEffect.addElement("repeatX");
         repeatX.setText(p.inEffectRepeatX);
         Element repeatY = inEffect.addElement("repeatY");
         repeatY.setText(p.inEffectRepeatY);
         Element IsTran = inEffect.addElement("IsTran");
         IsTran.setText(p.inEffectIsTran);

         // outEffect
         Element outEffect = Item.addElement("outEffect");
         Type = outEffect.addElement("Type");
         Type.setText(p.outEffectType);
         Time = outEffect.addElement("Time");
         Time.setText(p.outEffectTime);
         repeatX = outEffect.addElement("repeatX");
         repeatX.setText(p.outEffectRepeatX);
         repeatY = outEffect.addElement("repeatY");
         repeatY.setText(p.outEffectRepeatY);
         IsTran = outEffect.addElement("IsTran");
         IsTran.setText(p.outEffectIsTran);

         // MultiPicInfo
         Element MultiPicInfo = Item.addElement("MultiPicInfo");
         Element Source = MultiPicInfo.addElement("Source");
         Source.setText(p.multiPicInfoSource);
         Element PicCount = MultiPicInfo.addElement("PicCount");
         PicCount.setText(p.multiPicInfoPicCount);
         Element OnePicDuration = MultiPicInfo.addElement("OnePicDuration");
         OnePicDuration.setText(p.multiPicInfoOnePicDuration);
         
         Element SourceType=Item.addElement("SourceType");
         SourceType.setText(p.sourceType);
         
         Element TextColor=Item.addElement("TextColor");
         TextColor.setText(p.textColor);
         //LogFont
         Element LogFont=Item.addElement("LogFont");
         Element lfHeight=LogFont.addElement("lfHeight");
         lfHeight.setText(p.logFontIfHeight);
         Element lfWidth=LogFont.addElement("lfWidth");
         lfWidth.setText(p.logFontIfWidth);
         Element lfEscapement=LogFont.addElement("lfEscapement");
         lfEscapement.setText(p.logFontlfEscapement);
         Element lfOrientation=LogFont.addElement("lfOrientation");
         lfOrientation.setText(p.logFontlfOrientation);
         Element lfWeight=LogFont.addElement("lfWeight");
         lfWeight.setText(p.logFontlfWeight);
         Element lfItalic=LogFont.addElement("lfItalic");
         lfItalic.setText(p.logFontlfItalic);
         Element lfUnderline=LogFont.addElement("lfUnderline");
         lfUnderline.setText(p.logFontlfUnderline);
         Element lfStrikeOut=LogFont.addElement("lfStrikeOut");
         lfStrikeOut.setText(p.logFontlfStrikeOut);
         Element lfCharSet=LogFont.addElement("lfCharSet");
         lfCharSet.setText(p.logFontlfCharSet);
         Element lfOutPrecision=LogFont.addElement("lfOutPrecision");
         lfOutPrecision.setText(p.logFontlfOutPrecision);
         Element lfQuality=LogFont.addElement("lfQuality");
         lfQuality.setText(p.logFontlfQuality);
         Element lfPitchAndFamily=LogFont.addElement("lfPitchAndFamily");
         lfPitchAndFamily.setText(p.logFontlfPitchAndFamily);
         Element lfFaceName=LogFont.addElement("lfFaceName");
         lfFaceName.setText(p.logFontlfFaceName);
         
         Element RepeatCount= Item.addElement("RepeatCount");
         RepeatCount.setText(p.repeatCount);
         
         Element Speed= Item.addElement("Speed");
         Speed.setText(p.speed);
         
         Element IsScrollByTime= Item.addElement("IsScrollByTime");
         IsScrollByTime.setText(p.isScrollByTime);
         
         Element IsScroll = Item.addElement("IsScroll");
         IsScroll.setText(p.isScroll);
         
         Element PlayLenth= Item.addElement("PlayLenth");
         PlayLenth.setText(p.playLenth);
         
         Element Text= Item.addElement("Text");
         Text.setText(p.text);
         
         Element IfSpeedByFrame= Item.addElement("IfSpeedByFrame");
         IfSpeedByFrame.setText(p.ifSpeedByFrame);
         
         Element SpeedByFrame= Item.addElement("SpeedByFrame");
         SpeedByFrame.setText(p.speedByFrame);
         
         Element centeralAlign= Item.addElement("centeralAlign");
         centeralAlign.setText(p.centeralAlign);
         //FileSource
         Element FileSource = Item.addElement("FileSource");
         Element IsRelative = FileSource.addElement("IsRelative");
         IsRelative.setText(p.fsIsIsRelative);
         Element FilePath = FileSource.addElement("FilePath");
         FilePath.setText(p.fsFilePath);
         //
         return null;
	}
	
	
	
	
	
	// /////////////////////////////////////////////////////////////////////////////
	// /创建公共节点
	// /////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 构建节目属性节点 Program
	 * 
	 * @param p
	 * @return Pages
	 */
	private Element createProgramNode(PropertyCommon p, Document document)
	{
		
		// Programs
		Element Programs = document.addElement("Programs");
		// Programs
		Element BePacked = Programs.addElement("BePacked");
		BePacked.setText(p.projectBePacked);
		// FrameTime
		Element FrameTime = Programs.addElement("FrameTime");
		FrameTime.setText(p.projectFrameTime);
		// Program
		Element Program = Programs.addElement("Program");
		
		Element Id = Program.addElement("Id");
		Id.setText(p.proId);
		
		Element Name = Program.addElement("Name");
		Name.setText(p.proName);
		
		Element Version = Program.addElement("Version");
		Version.setText(p.proVersion);
		
		// Information
		Element Information = Program.addElement("Information");
		
		Element Width = Information.addElement("Width");
		Width.setText(p.infoWidth);
		Element Height = Information.addElement("Height");
		Height.setText(p.infoHeight);
		
		Element Duration = Information.addElement("Duration");
		Duration.setText(p.infoDuration);
		
		Element Description = Information.addElement("Description");
		Element Creator = Information.addElement("Creator");
		Element CreateTime = Information.addElement("CreateTime");
		Element LastModifyTime = Information.addElement("LastModifyTime");
		
		Element Pages = Program.addElement("Pages");
		return Pages;
	}
	
	/**
	 * 构建节目页属性节点page
	 * 
	 * @return Regions
	 */
	private Element createPageNode(PropertyCommon p, Element Pages)
	{
		Element Page = Pages.addElement("Page");
		Element Id = Page.addElement("Id");
		Element Name = Page.addElement("Name");
		Element Visibale = Page.addElement("Visibale");
		Visibale.setText(p.pageVisibale);
		Element Globle = Page.addElement("Globle");
		Globle.setText(p.pageGloble);
		Element AppointDuration = Page.addElement("AppointDuration");
		AppointDuration.setText(p.pageAppointDuration);
		Element PlayOneTime = Page.addElement("PlayOneTime");
		PlayOneTime.setText(p.pagePlayOneTime);
		Element LoopType = Page.addElement("LoopType");
		LoopType.setText(p.pageLoopType);
		Element BgColor = Page.addElement("BgColor");
		BgColor.setText(p.pageBgColor);
		Element BgFile = Page.addElement("BgFile");
		Element IsRelative = BgFile.addElement("IsRelative");
		IsRelative.setText(p.bgFileIsRelative);
		Element FilePath = BgFile.addElement("FilePath");
		FilePath.setText(p.bgFileFilePath);
		
		Element BgAudios = Page.addElement("BgAudios");
		Element Regions = Page.addElement("Regions");
		return Regions;
	}
	
	/**
	 * 构建区域属性节点page
	 * 
	 * @param p
	 * @return Items
	 */
	private Element createRegionNode(PropertyCommon p, Element Regions)
	{
		Element Region = Regions.addElement("Region");
		
		Element Id = Region.addElement("Id");
		Element Name = Region.addElement("Name");
		Name.setText(p.regionName);
		Element type = Region.addElement("type");
		type.setText(p.regionType);
		Element Show = Region.addElement("Show");
		Show.setText(p.regionShow);
		Element Layer = Region.addElement("Layer");
		Layer.setText(p.regionLayer);
		Element Locked = Region.addElement("Locked");
		Locked.setText(p.regionlocked);
		Element IsSameAnim = Region.addElement("IsSameAnim");
		IsSameAnim.setText(p.regionIsSameAnim);
		// Rect
		Element Rect = Region.addElement("Rect");
		Element X = Rect.addElement("X");
		X.setText(p.rectX);
		Element Y = Rect.addElement("Y");
		Y.setText(p.rectY);
		Element Width = Rect.addElement("Width");
		Width.setText(p.rectWidth);
		Element Height = Rect.addElement("Height");
		Height.setText(p.rectHeight);
		Element BorderWidth = Rect.addElement("BorderWidth");
		BorderWidth.setText(p.rectBorderWidth);
		Element BorderColor = Rect.addElement("BorderColor");
		BorderColor.setText(p.rectBorderColor);
		Element Items = Region.addElement("Items");
		return Items;
		
	}

	

}
