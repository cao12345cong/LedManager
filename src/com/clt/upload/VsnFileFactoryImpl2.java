//package com.clt.upload;
//
//import java.io.File;
//import java.io.FileWriter;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//import org.dom4j.io.OutputFormat;
//import org.dom4j.io.XMLWriter;
//
//import com.clt.upload.PropertyItem.MaterialType;
///**
// * vsn文件工厂实现
// */
//public class VsnFileFactoryImpl2 implements VsnFileFactory
//{
//	public VsnFileFactoryImpl2()
//	{
//		
//	}
//
//	public File createVsnFile(PropertyItem property,String vsnPath){
//		if(property instanceof PropertyPicture){
//			return createPicVsnFile(property,vsnPath);
//		}else if(property instanceof PropertyVedio){
//			return createVideoVsnFile(property, vsnPath);
//		}
//		return null;
//	}
//    /**
//     * 生成图片VSN文件
//     * @param filePath
//     * @return
//     */
//    public File createPicVsnFile(PropertyItem property,String filePath)
//    {
//    	if(property==null){
//    		return null;
//    	}
//    	PropertyPicture p;
//    	if(property instanceof PropertyPicture){
//    		p=(PropertyPicture) property;
//    		
//    	}else{
//    		return null;
//    	}
//        File vsnFile = new File(filePath);
//        if (vsnFile.exists())
//        {
//            vsnFile.delete();
//        }
//        try
//        {
//            Document document = DocumentHelper.createDocument(); // 创建文档
//            // Programs
//            Element Programs = document.addElement("Programs");
//            // Programs
//            Element BePacked = Programs.addElement("BePacked");
//            BePacked.setText(p.projectBePacked);
//            // FrameTime
//            Element FrameTime = Programs.addElement("FrameTime");
//            FrameTime.setText(p.projectFrameTime);
//            // Program
//            Element Program = Programs.addElement("Program");
//
//            Element Id = Program.addElement("Id");
//            Id.setText(p.proId);
//
//            Element Name = Program.addElement("Name");
//            Name.setText(p.proName);
//
//            Element Version = Program.addElement("Version");
//            Version.setText(p.proVersion);
//
//            // Information
//            Element Information = Program.addElement("Information");
//
//            Element Width = Information.addElement("Width");
//            Width.setText(p.infoWidth);
//            Element Height = Information.addElement("Height");
//            Height.setText(p.infoHeight);
//
//            Element Duration = Information.addElement("Duration");
//            Duration.setText(p.infoDuration);
//
//            Element Description = Information.addElement("Description");
//            Element Creator = Information.addElement("Creator");
//            Element CreateTime = Information.addElement("CreateTime");
//            Element LastModifyTime = Information.addElement("LastModifyTime");
//
//            Element Pages = Program.addElement("Pages");
//            Element Page = Pages.addElement("Page");
//            Id = Page.addElement("Id");
//            Name = Page.addElement("Name");
//            Element Visibale = Page.addElement("Visibale");
//            Visibale.setText(p.pageVisibale);
//            Element Globle = Page.addElement("Globle");
//            Globle.setText(p.pageGloble);
//            Element AppointDuration = Page.addElement("AppointDuration");
//            AppointDuration.setText(p.pageAppointDuration);
//            Element PlayOneTime = Page.addElement("PlayOneTime");
//            PlayOneTime.setText(p.pagePlayOneTime);
//            Element LoopType = Page.addElement("LoopType");
//            LoopType.setText(p.pageLoopType);
//            Element BgColor = Page.addElement("BgColor");
//            BgColor.setText(p.pageBgColor);
//            Element BgFile = Page.addElement("BgFile");
//            Element IsRelative = BgFile.addElement("IsRelative");
//            IsRelative.setText(p.bgFileIsRelative);
//            Element FilePath = BgFile.addElement("FilePath");
//            FilePath.setText(p.bgFileFilePath);
//
//            Element BgAudios = Page.addElement("BgAudios");
//            Element Regions = Page.addElement("Regions");
//            Element Region = Regions.addElement("Region");
//
//            Id = Region.addElement("Id");
//            Name = Region.addElement("Name");
//            Name.setText(p.regionName);
//            Element type = Region.addElement("type");
//            type.setText(p.regionType);
//            Element Show = Region.addElement("Show");
//            Show.setText(p.regionShow);
//            Element Layer = Region.addElement("Layer");
//            Layer.setText(p.regionLayer);
//            Element Locked = Region.addElement("Locked");
//            Locked.setText(p.regionlocked);
//            Element IsSameAnim = Region.addElement("IsSameAnim");
//            IsSameAnim.setText(p.regionIsSameAnim);
//            // Rect
//            Element Rect = Region.addElement("Rect");
//            Element X = Rect.addElement("X");
//            X.setText(p.rectX);
//            Element Y = Rect.addElement("Y");
//            Y.setText(p.rectY);
//            Width = Rect.addElement("Width");
//            Width.setText(p.rectWidth);
//            Height = Rect.addElement("Height");
//            Height.setText(p.rectHeight);
//            Element BorderWidth = Rect.addElement("BorderWidth");
//            BorderWidth.setText(p.rectBorderWidth);
//            Element BorderColor = Rect.addElement("BorderColor");
//            BorderColor.setText(p.rectBorderColor);
//
//            Element Items = Region.addElement("Items");
//            Element Item = Items.addElement("Item");
//            Id = Item.addElement("Id");
//            Id.setText(p.materialId);
//            Name = Item.addElement("Name");
//            Name.setText(p.materialName);
//            Element Type = Item.addElement("Type");
//            Type.setText(p.materialType);
//            Version = Item.addElement("Version");
//            Version.setText(p.materialVersion);
//            Element BackColor = Item.addElement("BackColor");
//            BackColor.setText(p.materialBackColor);
//            Element Alhpa = Item.addElement("Alhpa");
//            Alhpa.setText(p.materialAlhpa);
//            Duration = Item.addElement("Duration");
//            Duration.setText(p.materialDuration);
//            Element BeGlaring = Item.addElement("BeGlaring");
//            BeGlaring.setText(p.materialBeGlaring);
//            Element IsNeedUpdate = Item.addElement("IsNeedUpdate");
//            IsNeedUpdate.setText(p.materialIsNeedUpdate);
//            Element UpdateInterval = Item.addElement("UpdateInterval");
//            UpdateInterval.setText("10000");
//            Element MirrorOrHandstand = Item.addElement("MirrorOrHandstand");
//            MirrorOrHandstand.setText(p.materialMirrorHandstand);
//            Element PlayTimes = Item.addElement("PlayTimes");
//            PlayTimes.setText(p.materialPlayTimes);
//            // effect
//            Element effect = Item.addElement("effect");
//            Element IsStatic = effect.addElement("effect");
//            IsStatic.setText(p.effectIsStatic);
//            Element StayType = effect.addElement("StayType");
//            StayType.setText(p.effectStayType);
//            // inEffect
//            Element inEffect = Item.addElement("inEffect");
//            Type = inEffect.addElement("Type");
//            Type.setText(p.inEffectType);
//            Element Time = inEffect.addElement("Time");
//            Time.setText(p.inEffectTime);
//            Element repeatX = inEffect.addElement("repeatX");
//            repeatX.setText(p.inEffectRepeatX);
//            Element repeatY = inEffect.addElement("repeatY");
//            repeatY.setText(p.inEffectRepeatY);
//            Element IsTran = inEffect.addElement("IsTran");
//            IsTran.setText(p.inEffectIsTran);
//
//            // outEffect
//            Element outEffect = Item.addElement("outEffect");
//            Type = outEffect.addElement("Type");
//            Type.setText(p.outEffectType);
//            Time = outEffect.addElement("Time");
//            Time.setText(p.outEffectTime);
//            repeatX = outEffect.addElement("repeatX");
//            repeatX.setText(p.outEffectRepeatX);
//            repeatY = outEffect.addElement("repeatY");
//            repeatY.setText(p.outEffectRepeatY);
//            IsTran = outEffect.addElement("IsTran");
//            IsTran.setText(p.outEffectIsTran);
//
//            // MultiPicInfo
//            Element MultiPicInfo = Item.addElement("MultiPicInfo");
//            Element Source = MultiPicInfo.addElement("Source");
//            Source.setText(p.multiPicInfoSource);
//            Element PicCount = MultiPicInfo.addElement("PicCount");
//            PicCount.setText(p.multiPicInfoSource);
//            Element OnePicDuration = MultiPicInfo.addElement("OnePicDuration");
//            OnePicDuration.setText(p.multiPicInfoSource);
//
//            Width = Item.addElement("Width");
//            Width.setText(p.pictureWidth);
//            Height = Item.addElement("Height");
//            Height.setText(p.pictureHeight);
//            // fileSource
//            Element FileSource = Item.addElement("FileSource");
//            IsRelative = FileSource.addElement("IsRelative");
//            IsRelative.setText(p.fsIsRelative);
//            FilePath = FileSource.addElement("FilePath");
//            FilePath.setText("./" + p.programName + ".files/" + p.pictureName);
//            Element MD5 = FileSource.addElement("MD5");
//            MD5.setText(p.fsMd5);
//
//            Element ReserveAS = Item.addElement("ReserveAS");
//            ReserveAS.setText(p.reserveAs);
//
//            // write xml
//            FileWriter fileWriter = new FileWriter(filePath);
//            // 设置文件编码
//            OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
//            xmlFormat.setEncoding("UTF-8");
//            // xmlFormat.setTrimText(false);//设置text中是否要删除其中多余的空格
//            XMLWriter xmlWriter = new XMLWriter(fileWriter, xmlFormat);
//            xmlWriter.write(document); // 写入文件中
//            xmlWriter.close();
//            return new File(filePath);
//            
//        }
//        catch (Exception e1)
//        {
//            return null;
//        }
//    }
//
//    
//    /**
//     * 生成图片VSN文件
//     * @param filePath
//     * @return
//     */
//    public File createVideoVsnFile(PropertyItem property,String filePath)
//    {
//    	if(property==null){
//    		return null;
//    	}
//    	PropertyVedio p;
//    	if(property instanceof PropertyVedio){
//    		p=(PropertyVedio) property;
//    		
//    	}else{
//    		return null;
//    	}
//        File vsnFile = new File(filePath);
//        if (vsnFile.exists())
//        {
//            vsnFile.delete();
//        }
//        try
//        {
//            Document document = DocumentHelper.createDocument(); // 创建文档
//            // Programs
//            Element Programs = document.addElement("Programs");
//            // Programs
//            Element BePacked = Programs.addElement("BePacked");
//            BePacked.setText(p.projectBePacked);
//            // FrameTime
//            Element FrameTime = Programs.addElement("FrameTime");
//            FrameTime.setText(p.projectFrameTime);
//            // Program
//            Element Program = Programs.addElement("Program");
//
//            Element Id = Program.addElement("Id");
//            Id.setText(p.proId);
//
//            Element Name = Program.addElement("Name");
//            Name.setText(p.proName);
//
//            Element Version = Program.addElement("Version");
//            Version.setText(p.proVersion);
//
//            // Information
//            Element Information = Program.addElement("Information");
//
//            Element Width = Information.addElement("Width");
//            Width.setText(p.infoWidth);
//            Element Height = Information.addElement("Height");
//            Height.setText(p.infoHeight);
//
//            Element Duration = Information.addElement("Duration");
//            Duration.setText(p.infoDuration);
//
//            Element Description = Information.addElement("Description");
//            Element Creator = Information.addElement("Creator");
//            Element CreateTime = Information.addElement("CreateTime");
//            Element LastModifyTime = Information.addElement("LastModifyTime");
//
//            Element Pages = Program.addElement("Pages");
//            Element Page = Pages.addElement("Page");
//            Id = Page.addElement("Id");
//            Name = Page.addElement("Name");
//            Element Visibale = Page.addElement("Visibale");
//            Visibale.setText(p.pageVisibale);
//            Element Globle = Page.addElement("Globle");
//            Globle.setText(p.pageGloble);
//            Element AppointDuration = Page.addElement("AppointDuration");
//            AppointDuration.setText(p.pageAppointDuration);
//            Element PlayOneTime = Page.addElement("PlayOneTime");
//            PlayOneTime.setText(p.pagePlayOneTime);
//            Element LoopType = Page.addElement("LoopType");
//            LoopType.setText(p.pageLoopType);
//            Element BgColor = Page.addElement("BgColor");
//            BgColor.setText(p.pageBgColor);
//            Element BgFile = Page.addElement("BgFile");
//            Element IsRelative = BgFile.addElement("IsRelative");
//            IsRelative.setText(p.bgFileIsRelative);
//            Element FilePath = BgFile.addElement("FilePath");
//            FilePath.setText(p.bgFileFilePath);
//
//            Element BgAudios = Page.addElement("BgAudios");
//            Element Regions = Page.addElement("Regions");
//            Element Region = Regions.addElement("Region");
//
//            Id = Region.addElement("Id");
//            BgColor.setText(p.regionId);
//            Name = Region.addElement("Name");
//            Name.setText(p.regionName);
//            Element type = Region.addElement("type");
//            type.setText(p.regionType);
//            Element Show = Region.addElement("Show");
//            Show.setText(p.regionShow);
//            Element Layer = Region.addElement("Layer");
//            Layer.setText(p.regionLayer);
//            Element Locked = Region.addElement("Locked");
//            Locked.setText(p.regionlocked);
//            Element IsSameAnim = Region.addElement("IsSameAnim");
//            IsSameAnim.setText(p.regionIsSameAnim);
//            // Rect
//            Element Rect = Region.addElement("Rect");
//            Element X = Rect.addElement("X");
//            X.setText(p.rectX);
//            Element Y = Rect.addElement("Y");
//            Y.setText(p.rectY);
//            Width = Rect.addElement("Width");
//            Width.setText(p.rectWidth);
//            Height = Rect.addElement("Height");
//            Height.setText(p.rectHeight);
//            Element BorderWidth = Rect.addElement("BorderWidth");
//            BorderWidth.setText(p.rectBorderWidth);
//            Element BorderColor = Rect.addElement("BorderColor");
//            BorderColor.setText(p.rectBorderColor);
//
//            Element Items = Region.addElement("Items");
//            Element Item = Items.addElement("Item");
//            Id = Item.addElement("Id");
//            Id.setText(p.materialId);
//            Name = Item.addElement("Name");
//            Name.setText(p.materialName);
//            Element Type = Item.addElement("Type");
//            Type.setText(p.materialType);
//            Version = Item.addElement("Version");
//            Version.setText(p.materialVersion);
//            Element BackColor = Item.addElement("BackColor");
//            BackColor.setText(p.materialBackColor);
//            Element Alhpa = Item.addElement("Alhpa");
//            Alhpa.setText(p.materialAlhpa);
//            Duration = Item.addElement("Duration");
//            Duration.setText(p.materialDuration);
//            Element BeGlaring = Item.addElement("BeGlaring");
//            BeGlaring.setText(p.materialBeGlaring);
//            Element IsNeedUpdate = Item.addElement("IsNeedUpdate");
//            IsNeedUpdate.setText(p.materialIsNeedUpdate);
//            Element UpdateInterval = Item.addElement("UpdateInterval");
//            UpdateInterval.setText(p.materialUpdateInterval);
//            Element MirrorOrHandstand = Item.addElement("MirrorOrHandstand");
//            MirrorOrHandstand.setText(p.materialMirrorHandstand);
//            Element PlayTimes = Item.addElement("PlayTimes");
//            PlayTimes.setText(p.materialPlayTimes);
//            // effect
//            Element effect = Item.addElement("effect");
//            Element IsStatic = effect.addElement("effect");
//            IsStatic.setText(p.effectIsStatic);
//            Element StayType = effect.addElement("StayType");
//            StayType.setText(p.effectStayType);
//            // inEffect
//            Element inEffect = Item.addElement("inEffect");
//            Type = inEffect.addElement("Type");
//            Type.setText(p.inEffectType);
//            Element Time = inEffect.addElement("Time");
//            Time.setText(p.inEffectTime);
//            Element repeatX = inEffect.addElement("repeatX");
//            repeatX.setText(p.inEffectRepeatX);
//            Element repeatY = inEffect.addElement("repeatY");
//            repeatY.setText(p.inEffectRepeatY);
//            Element IsTran = inEffect.addElement("IsTran");
//            IsTran.setText(p.inEffectIsTran);
//
//            // outEffect
//            Element outEffect = Item.addElement("outEffect");
//            Type = outEffect.addElement("Type");
//            Type.setText(p.outEffectType);
//            Time = outEffect.addElement("Time");
//            Time.setText(p.outEffectTime);
//            repeatX = outEffect.addElement("repeatX");
//            repeatX.setText(p.outEffectRepeatX);
//            repeatY = outEffect.addElement("repeatY");
//            repeatY.setText(p.outEffectRepeatY);
//            IsTran = outEffect.addElement("IsTran");
//            IsTran.setText(p.outEffectIsTran);
//
//            // MultiPicInfo
//            Element MultiPicInfo = Item.addElement("MultiPicInfo");
//            Element Source = MultiPicInfo.addElement("Source");
//            Source.setText(p.multiPicInfoSource);
//            Element PicCount = MultiPicInfo.addElement("PicCount");
//            PicCount.setText(p.multiPicInfoPicCount);
//            Element OnePicDuration = MultiPicInfo.addElement("OnePicDuration");
//            OnePicDuration.setText(p.multiPicInfoOnePicDuration);
//            
//            Element Length = Item.addElement("Length");
//            Length.setText(p.length);
//            Width = Item.addElement("VideoWidth");
//            Width.setText(p.vedioWidth);
//            Height = Item.addElement("VideoHeight");
//            Height.setText(p.vedioHeight);
//            
//            Element InOffset = Item.addElement("InOffset");
//            InOffset.setText(p.inOffset);
//            Element PlayLength = Item.addElement("PlayLength");
//            PlayLength.setText(p.playLength);
//            Element Volume = Item.addElement("Volume");
//            Volume.setText(p.volume);
//            Element Loop = Item.addElement("Loop");
//            Loop.setText(p.loop);
//            // fileSource
//            Element FileSource = Item.addElement("FileSource");
//            IsRelative = FileSource.addElement("IsRelative");
//            IsRelative.setText(p.fsIsRelation);
//            FilePath = FileSource.addElement("FilePath");
//            FilePath.setText("./" + p.programName + ".files/" +p.vedioName);
//            Element MD5 = FileSource.addElement("MD5");
//            MD5.setText(p.fsMd5);
//
//            Element ReserveAS = Item.addElement("ReserveAS");
//            ReserveAS.setText(p.reserveAs);
//            
//            Element showX = Item.addElement("showX");
//            showX.setText(p.showX);
//            Element showY = Item.addElement("showY");
//            showY.setText(p.showY);
//            Element showWidth = Item.addElement("showWidth");
//            showWidth.setText(p.showWidth);
//            Element showHeight = Item.addElement("showHeight");
//            showHeight.setText(p.showHeight);
//            Element IsSetShowRegion = Item.addElement("IsSetShowRegion");
//            IsSetShowRegion.setText(p.isSetShowRegion);
//            Element IsSetPlayLen = Item.addElement("IsSetPlayLen");
//            IsSetPlayLen.setText(p.isSetPlayLen);
//
//            // write xml
//            FileWriter fileWriter = new FileWriter(filePath);
//            // 设置文件编码
//            OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
//            xmlFormat.setEncoding("UTF-8");
//            // xmlFormat.setTrimText(false);//设置text中是否要删除其中多余的空格
//            XMLWriter xmlWriter = new XMLWriter(fileWriter, xmlFormat);
//            xmlWriter.write(document); // 写入文件中
//            xmlWriter.close();
//            return new File(filePath);
//            
//        }
//        catch (Exception e1)
//        {
//            return null;
//        }
//    }
//
//
//
//}
