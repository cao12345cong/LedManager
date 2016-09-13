package com.clt.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;

import com.clt.activity.UploadProgramAcitvity3.ProgramItem;
import com.clt.ledmanager.R;
import com.clt.ui.CustomerSpinner;
import com.clt.upload.PropertyCommon;
import com.clt.upload.PropertyMultiLineText;
import com.clt.upload.PropertyPicture;
import com.clt.upload.PropertyVedio;
import com.clt.util.Const;
import com.clt.util.Tools;

public class ExpandableListAdapter extends BaseExpandableListAdapter
{
	// 字体
	public static final String[] fontFamily = { "宋体", "黑体", "楷体", "隶书", "仿宋" };
	
	// 字体大小
	public static final String[] fontSize = { "8", "9", "10", "11", "12", "14",
			"16", "18", "20", "22", "24", "26", "28", "36", "48", "72", "128" };
	
	// 文字颜色
	public static final int[] textColorArr = { Color.BLACK, Color.DKGRAY,
			Color.GRAY, Color.LTGRAY, Color.WHITE, Color.RED, Color.GREEN,
			Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA
	
	};
	
	private Context mContext;
	
	private ArrayList<ProgramItem> mFileItemList;
	
	private LayoutInflater inflater;
	
	private PropertyCommon propertyCommon;
	
	public ExpandableListAdapter(Context mContext,
			ArrayList<ProgramItem> fileItemList)
	{
		this.mContext = mContext;
		this.mFileItemList = fileItemList;
		this.inflater = LayoutInflater.from(mContext);
	}
	public void setPropertyCommon(PropertyCommon pc){
		propertyCommon=pc;
	}
	public void setData(ArrayList<ProgramItem> fileItemList)
	{
		if (fileItemList != null)
		{
			this.mFileItemList = fileItemList;
		}
		else
		{
			this.mFileItemList = new ArrayList<ProgramItem>();
		}
	}
	
	public void updateView(ArrayList<ProgramItem> fileItemList)
	{
		setData(fileItemList);
		notifyDataSetChanged();
	}
	
	// 重写ExpandableListAdapter中的各个方法
	@Override
	public int getGroupCount()
	{
		return mFileItemList.size();
	}
	
	@Override
	public ProgramItem getGroup(int groupPosition)
	{
		return mFileItemList.get(groupPosition);
	}
	
	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}
	
	@Override
	public int getChildrenCount(int groupPosition)
	{
		return 1;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return null;
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}
	
	@Override
	public boolean hasStableIds()
	{
		return true;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		final int index=groupPosition;
		GroupHolder holder = null;
		if (convertView == null)
		{
			holder = new GroupHolder();
			convertView = inflater
					.inflate(R.layout.upload_file_prop_item, null);
			holder.tvFileName = (TextView) convertView
					.findViewById(R.id.tv_file_name);
			holder.btnDelete = (Button) convertView
					.findViewById(R.id.btn_delete);
			holder.btnMoveUp = (Button) convertView
					.findViewById(R.id.btn_move_up);
			holder.btnMoveDown = (Button) convertView
					.findViewById(R.id.btn_move_down);
			convertView.setTag(holder);
		}
		else
		{
			holder = (GroupHolder) convertView.getTag();
		}
		ProgramItem pt = getGroup(groupPosition);
		holder.tvFileName.setText(pt.fileName);
		holder.btnDelete.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				mFileItemList.remove(index);
				notifyDataSetChanged();
			}
		});
		holder.btnMoveUp.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(index==0)
				{
					return;
				}else{
					ProgramItem pt=mFileItemList.get(index-1);
					mFileItemList.set(index-1, mFileItemList.get(index));
					mFileItemList.set(index, pt);
					notifyDataSetChanged();
				}
			}
		});
		holder.btnMoveDown.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(index==mFileItemList.size()-1)
				{
					return;
				}else{
					ProgramItem pt=mFileItemList.get(index+1);
					mFileItemList.set(index+1, mFileItemList.get(index));
					mFileItemList.set(index, pt);
					notifyDataSetChanged();
				}
			}
		});
		if(index==0){
			holder.btnMoveUp.setVisibility(View.INVISIBLE);
		}else{
			holder.btnMoveUp.setVisibility(View.VISIBLE);
		}
		if(index==mFileItemList.size()-1){
			
			holder.btnMoveDown.setVisibility(View.INVISIBLE);
		}else{
			holder.btnMoveDown.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{
		Holder holder = null;
		ProgramItem pt = getGroup(groupPosition);
		switch (pt.fileType)
		{
			case Const.PICTURE:
			{
				ProgramPicture p = new ProgramPicture(groupPosition);
				// convertView =
				// inflater.inflate(R.layout.upload_program_picture, null);
				convertView = p.getRootView();
				// initPicView(convertView);
			}
			break;
			
			case Const.VIDEO:
			{
				ProgramVideo p = new ProgramVideo(groupPosition);
				convertView = p.getRootView();
				// convertView = inflater.inflate(R.layout.upload_program_video,
				// null);
				// initVideoView(convertView);
			}
			break;
			case Const.TXT:
			{
				ProgramMultiLineText p = new ProgramMultiLineText(groupPosition);
				convertView = p.getRootView();
				// convertView = inflater.inflate(
				// R.layout.upload_program_multi_line_text, null);
				// initTxtView(convertView);
			}
			break;
		}
		return convertView;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}
	
	private void initTxtView(View convertView)
	{
		
	}
	
	private void initVideoView(View convertView)
	{
		
	}
	
	private void initPicView(View convertView)
	{
		
	}
	
	/**
	 * 
	 * 图片节目
	 */
	public class ProgramPicture
	{
		// view
		private View rootView;
		
		private TableLayout tlTable;
		
		private TextView tvPicWH;
		
		private SeekBar sbAlpha;
		
		private TextView tvAlpha;
		
		private CheckBox cblimitScale;
		
		private EditText etDuring;
		
		private int picWidth, picHeight;
		
		private PropertyPicture propertyPicture;
		
		private ImageView ivPicThunb;
		
		private Bitmap imageThumb;//缩略图
		
		private Handler picViewHandler = new Handler()
		{
			public void handleMessage(android.os.Message msg)
			{
				switch (msg.what)
				{
					case 1:
					{
//						tlTable.setVisibility(View.VISIBLE);
//						tvPicWH.setText(picWidth + "*" + picHeight);
						if(imageThumb!=null){
							ivPicThunb.setImageBitmap(imageThumb);
						}
						
					}
					break;
				
				}
			};
		};
		
		public ProgramPicture(int currentSelectedIndex)
		{
			try
			{
				ProgramItem fileItem = mFileItemList.get(currentSelectedIndex);
				propertyPicture = (PropertyPicture) fileItem.property;
				initMView(getRootView());
				initMListener();
				initMData();
				setSelectedFile(fileItem.filePath);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
		private void initMView(View view)
		{
			tlTable = (TableLayout) view.findViewById(R.id.tl_table);
			tvPicWH = (TextView) view.findViewById(R.id.tv_pic_wh);
			sbAlpha = (SeekBar) view.findViewById(R.id.sb_file_alpha);
			tvAlpha =  (TextView) view.findViewById(R.id.tv_file_alpha);
			cblimitScale = (CheckBox) view.findViewById(R.id.cb_reserve_as);
			etDuring = (EditText) view.findViewById(R.id.et_during);
			ivPicThunb=(ImageView) view.findViewById(R.id.iv_thumb);
		}
		
		private void initMData()
		{
			int time=Integer.parseInt(propertyPicture.materialDuration);
			etDuring.setText(time/1000+"");
			tvPicWH.setText(propertyPicture.pictureWidth + "*" + propertyPicture.pictureHeight);
		}
		
		private void initMListener()
		{
			sbAlpha.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
			{
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser)
				{
					tvAlpha.setText(progress + "%");
					propertyPicture.materialAlhpa = String.valueOf(sbAlpha
							.getProgress() * 1.00f / 100);
				}
			});
			cblimitScale
					.setOnCheckedChangeListener(new OnCheckedChangeListener()
					{
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked)
						{
							propertyPicture.reserveAs = String
									.valueOf(isChecked ? 1 : 0);
						}
					});
			etDuring.addTextChangedListener(new TextWatcher()
			{
				
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count)
				{
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after)
				{
					
				}
				
				@Override
				public void afterTextChanged(Editable s)
				{
					propertyPicture.materialDuration = s.toString() + "000";
				}
			});
			
		}
		
		public View getRootView()
		{
			if (rootView == null)
			{
				rootView = inflater.inflate(R.layout.upload_program_picture,
						null);
			}
			return rootView;
		}
		
		public void setSelectedFile(final String filePath)
		{
			try
			{
				new Thread()
				{
					public void run()
					{
//						Bitmap bf = BitmapFactory.decodeFile(filePath);
//						picWidth = bf.getWidth();
//						picHeight = bf.getHeight();
						imageThumb=Tools.getimage(filePath, 100, 100);
						picViewHandler.obtainMessage(1).sendToTarget();
						
						
					}
				}.start();
				
				
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * 
	 * 视频节目
	 */
	public class ProgramVideo
	{
		private final static int DECODE_SCUCESS = 0;
		
		private final static int DECODE_FAIL = 1;
		
		// view
		private View rootView;
		
		private TableLayout tlTable;
		
		private TextView tvVedioWH, tvVedioDuring;
		
		private SeekBar sbVolume, sbAlpha;
		
		private TextView tvVolume, tvAlpha;
		
		private CheckBox cblimitScale;
		
		// params
		private String filePath;
		
		private int vedioWidth, vedioHeight, vedioDuration;
		
		private PropertyVedio propertyVedio;
		
		private ImageView ivVedioThumb;
		
		private Bitmap imageThumb;
		
		private Handler vedioViewHandler = new Handler()
		{
			public void handleMessage(android.os.Message msg)
			{
				switch (msg.what)
				{
					case DECODE_SCUCESS:
					{
						if(imageThumb!=null)
						{
							ivVedioThumb.setImageBitmap(imageThumb);
						}
					}
					break;
				
				}
				
			};
		};
		
		public ProgramVideo(int currentSelectedIndex)
		{
			ProgramItem fileItem = mFileItemList.get(currentSelectedIndex);
			propertyVedio = (PropertyVedio) fileItem.property;
			initMView(getRootView());
			initMListener();
			initMData();
			//setSelectedFile(fileItem.filePath);
		}
		
		private void initMView(View view)
		{
			tlTable = (TableLayout) view.findViewById(R.id.tl_table);
			tvVedioWH = (TextView) view.findViewById(R.id.tv_video_wh);
			tvVedioDuring = (TextView) view.findViewById(R.id.tv_vedio_during);
			sbVolume = (SeekBar) view.findViewById(R.id.sb_vedio_volume);
			tvVolume = (TextView) view.findViewById(R.id.tv_vedio_volume);
			sbAlpha = (SeekBar) view.findViewById(R.id.sb_file_alpha);
			tvAlpha = (TextView) view.findViewById(R.id.tv_file_alpha);
			cblimitScale = (CheckBox) view.findViewById(R.id.cb_reserve_as);
			ivVedioThumb=(ImageView) view.findViewById(R.id.iv_thumb);
		}
		
		private void initMListener()
		{
			sbAlpha.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
			{
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser)
				{
					propertyVedio.materialAlhpa = String
							.valueOf(progress * 1.00f / 100);
					tvAlpha.setText(progress+"%");
				}
			});
			sbVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
			{
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser)
				{
					propertyVedio.volume = String
							.valueOf(progress * 1.00f / 100);
					tvVolume.setText(progress+"%");
				}
			});
			cblimitScale
					.setOnCheckedChangeListener(new OnCheckedChangeListener()
					{
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked)
						{
							propertyVedio.reserveAs = String
									.valueOf(isChecked ? 1 : 0);
						}
					});
		}
		
		private void initMData()
		{
			tvVedioWH.setText(propertyVedio.vedioWidth + "*" + propertyVedio.vedioHeight);
			tvVedioDuring
					.setText(Tools.formatDuring(Long.parseLong(propertyVedio.materialDuration)));
			
		}
		
		public View getRootView()
		{
			if (rootView == null)
			{
				rootView = inflater
						.inflate(R.layout.upload_program_video, null);
			}
			return rootView;
		}
		
		public void setSelectedFile(final String filePath)
		{
			new Thread()
			{
				public void run()
				{
					try
					{
						
						// 获得视频的元数据， 两种方式解析
						MediaMetadataRetriever retriever = new MediaMetadataRetriever();
						retriever.setDataSource(filePath);
						imageThumb=retriever.getFrameAtTime();
						vedioViewHandler.obtainMessage(DECODE_SCUCESS)
								.sendToTarget();
						retriever.release();
					}
					catch (Exception e)
					{
						vedioViewHandler.obtainMessage(DECODE_FAIL)
								.sendToTarget();
					}
				}
			}.start();
			
		}
		
	}
	
	/**
	 * 
	 * 多行文本节目
	 */
	public class ProgramMultiLineText
	{
		// view
		private View rootView;
		
		private TableLayout tlTable;
		
		private SeekBar sbAlpha;
		
		private EditText etAlpha;
		
		private CheckBox cbMoveUp, cbCentalAlign, cbAutoUpdate,
				cbUpdateInterval;
		
		private CustomerSpinner spinnerFont, spinnerFontSize;
		
		private CheckBox cbFontBold, cbFontItalic, cbFontUndeline;
		
		private CustomerSpinner spinnerTextColor, spinnerBgColor;
		
		private EditText etDuring;
		
		// params
		private String filePath;
		
		private int textSourceIndex = 0;
		
		private int textColor = -1, bgColor = -1;
		
		private PropertyMultiLineText propertyMultiLineText;
		
		public ProgramMultiLineText(int currentSelectedIndex)
		{
			try
			{
				ProgramItem fileItem = mFileItemList.get(currentSelectedIndex);
				propertyMultiLineText = (PropertyMultiLineText) fileItem.property;
				initMView(getRootView());
				initMListener();
				initMData();
			}
			catch (Exception e)
			{
				
			}
			
		}
		
		private void initMView(View view)
		{
			tlTable = (TableLayout) view.findViewById(R.id.tl_table);
			
			cbAutoUpdate = (CheckBox) view.findViewById(R.id.cb_left_move);
			cbUpdateInterval = (CheckBox) view
					.findViewById(R.id.cb_head_tail_conn);
			
			cbMoveUp = (CheckBox) view.findViewById(R.id.cb_up_move);
			cbCentalAlign = (CheckBox) view
					.findViewById(R.id.cb_centeral_align);
			
			spinnerFont = (CustomerSpinner) view
					.findViewById(R.id.spinner_text_font);
			spinnerFontSize = (CustomerSpinner) view
					.findViewById(R.id.spinner_font_size);
			cbFontBold = (CheckBox) view.findViewById(R.id.cb_font_bold);
			cbFontItalic = (CheckBox) view.findViewById(R.id.cb_font_italic);
			cbFontUndeline = (CheckBox) view
					.findViewById(R.id.cb_font_underline);
			
			spinnerTextColor = (CustomerSpinner) view
					.findViewById(R.id.spinner_text_color);
			spinnerBgColor = (CustomerSpinner) view
					.findViewById(R.id.spinner_back_color);
			etDuring = (EditText) view.findViewById(R.id.et_during);
		}
		
		private void initMListener()
		{
			
			cbMoveUp.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked)
				{
					propertyMultiLineText.isScroll = String
							.valueOf(isChecked ? 1 : 0);
					// 首尾相连
					int centalAlign = 0;
					
					if (isChecked)
					{
						
						rootView.findViewById(R.id.ll_centeral_align)
								.setVisibility(View.INVISIBLE);
						
					}
					else
					{
						rootView.findViewById(R.id.ll_centeral_align)
								.setVisibility(View.VISIBLE);
					}
				}
			});
			cbCentalAlign
					.setOnCheckedChangeListener(new OnCheckedChangeListener()
					{
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked)
						{
							int centalAlign = 0;
							if (cbMoveUp.isChecked() && isChecked)
							{
								centalAlign = 1;
							}
							propertyMultiLineText.centeralAlign = String
									.valueOf(centalAlign);
							
						}
					});
			spinnerFont.setOnItemClickListener(new OnItemClickListener()
			{
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id)
				{
					propertyMultiLineText.logFontlfFaceName = fontFamily[position]
							.toString();
				}
				
			});
			spinnerFontSize.setOnItemClickListener(new OnItemClickListener()
			{
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id)
				{
					int fs = Integer.parseInt(fontSize[position]);
					propertyMultiLineText.logFontIfHeight = String
							.valueOf((int) (97 * fs / 72));
				}
			});
			cbFontBold.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked)
				{
					propertyMultiLineText.logFontlfWeight = String
							.valueOf(isChecked ? 700 : 400);
					
				}
			});
			cbFontItalic
					.setOnCheckedChangeListener(new OnCheckedChangeListener()
					{
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked)
						{
							propertyMultiLineText.logFontlfItalic = String
									.valueOf(isChecked ? 1 : 0);
							
						}
					});
			cbFontUndeline
					.setOnCheckedChangeListener(new OnCheckedChangeListener()
					{
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked)
						{
							propertyMultiLineText.logFontlfUnderline = String
									.valueOf(isChecked ? 1 : 0);
						}
					});
			spinnerTextColor.setOnItemClickListener(new OnItemClickListener()
			{
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id)
				{
					textColor = textColorArr[position];
					propertyMultiLineText.textColor = "0x"
							+ Integer.toHexString(textColor).toUpperCase();
				}
			});
			spinnerBgColor.setOnItemClickListener(new OnItemClickListener()
			{
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id)
				{
					bgColor = textColorArr[position];
					propertyCommon.pageBgColor = "0x"
					 + Integer.toHexString(bgColor).toUpperCase();
				}
			});
			etDuring.addTextChangedListener(new TextWatcher()
			{
				
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count)
				{
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after)
				{
					
				}
				
				@Override
				public void afterTextChanged(Editable s)
				{
					propertyMultiLineText.materialDuration = s.toString()+"000";
				}
			});
		}
		
		private void initMData()
		{
			
			spinnerFont.initView(fontFamily);
			spinnerFont.setSelection(0, true);
			
			spinnerFontSize.initView(fontSize);
			spinnerFontSize.setSelection(6, true);
			
			spinnerTextColor.initView(R.array.text_color);
			spinnerTextColor.setSelection(4, true);
			
			spinnerBgColor.initView(R.array.text_color);
			spinnerBgColor.setSelection(0, true);
			
			int time=Integer.parseInt(propertyMultiLineText.materialDuration);
			etDuring.setText(time/1000+"");
		}
		
		public View getRootView()
		{
			if (rootView == null)
			{
				rootView = inflater.inflate(
						R.layout.upload_program_multi_line_text, null);
			}
			return rootView;
		}
		
		public void setSelectedFile(String filePath)
		{
			this.filePath = filePath;
			rootView.findViewById(R.id.tv_file_property).setVisibility(
					View.VISIBLE);
			tlTable.setVisibility(View.VISIBLE);
		}
		
	}
	
	/********************************************************************/
	interface Holder
	{
		
	}
	
	class GroupHolder implements Holder
	{
		TextView tvFileName;
		
		Button btnDelete, btnMoveUp, btnMoveDown;
	}
	
	class ItemPicHolder implements Holder
	{
		TextView tvSize;
		
		SeekBar sbAlpha;
		
		CheckBox cbConstrain;
		
		EditText etDuring;
	}
	
	class ItemVedioHolder implements Holder
	{
		TextView tvSize, tvDuring;
		
		SeekBar sbAlpha, sbVoice;
		
		CheckBox cbConstrain;
	}
	
	class ItemTxtHolder implements Holder
	{
		CheckBox cbMoveUp, cbVertCenter, cbBold, cbItalic, cbUndeline, cbConst;
		
		CustomerSpinner spinnerFont, spinnerFontSize, spinnerTxtColor,
				spinnerBgColor;
		
		EditText etDuring;
		
	}
	
}
