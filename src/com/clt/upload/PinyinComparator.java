package com.clt.upload;

import java.util.Comparator;

import com.clt.entity.FileSortModel;

/**
 * 拼音比较器
 * 
 */
public class PinyinComparator implements Comparator<FileSortModel>
{
	
	public int compare(FileSortModel o1, FileSortModel o2)
	{
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#"))
		{
			return -1;
		}
		else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@"))
		{
			return 1;
		}
		else
		{
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}
	
}
