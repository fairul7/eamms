package com.tms.fms.util;

import java.util.*;

public class PagingUtil {
	public static Collection getPagedCollection(Collection col, int start, int rows) {
		if (start < col.size()) {
			if (col instanceof ArrayList) {
				int end = getRowEnd(col.size(), start, rows);
				return ((ArrayList) col).subList(start, end);
			} else {
				return col;
			}
		} else {
			return new ArrayList();
		}
	}
	
	public static boolean hasLastRecord(Collection col, int start, int rows) {
		if (start < col.size()) {
			int size = col.size();
			int end = getRowEnd(size, start, rows);
			if (size == end) {
				return true;
			}
		}
		return false;
	}
	
	private static int getRowEnd(int size, int start, int rows) {
		if (rows == -1 || (start + rows) >= size) {
			return size;
		} else {
			return start + rows;
		}
	}
}
