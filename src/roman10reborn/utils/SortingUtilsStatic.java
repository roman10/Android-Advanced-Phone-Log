package roman10reborn.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import roman10reborn.apl.data2.AdvancedPhoneLogRecord;

public class SortingUtilsStatic {
	//sort a list of phone records by date ascending
	public static void sortRecordsByDateAsc(ArrayList<AdvancedPhoneLogRecord> _records) {
		Comparator<AdvancedPhoneLogRecord> comparator = new Comparator<AdvancedPhoneLogRecord>() {
			public int compare(AdvancedPhoneLogRecord arg0,
					AdvancedPhoneLogRecord arg1) {
				return arg0.aplr_time.compareTo(arg1.aplr_time);
			}
		};
		Collections.sort(_records, comparator);
	}
	
	public static void sortRecordsByDateDesc(ArrayList<AdvancedPhoneLogRecord> _records) {
		Comparator<AdvancedPhoneLogRecord> comparator = new Comparator<AdvancedPhoneLogRecord>() {
			public int compare(AdvancedPhoneLogRecord arg0,
					AdvancedPhoneLogRecord arg1) {
				return arg0.aplr_time.compareTo(arg1.aplr_time)*-1;
			}
		};
		Collections.sort(_records, comparator);
	}
	
	public static void sortRecordsByNameAsc(ArrayList<AdvancedPhoneLogRecord> _records) {
		Comparator<AdvancedPhoneLogRecord> comparator = new Comparator<AdvancedPhoneLogRecord>() {
			public int compare(AdvancedPhoneLogRecord arg0,
					AdvancedPhoneLogRecord arg1) {
				if (arg0.aplr_name == null) {
					arg0.aplr_name = ConstantStatic.UNKNOWN;
				}
				if (arg1.aplr_name == null) {
					arg1.aplr_name = ConstantStatic.UNKNOWN;
				}
				return arg0.aplr_name.compareTo(arg1.aplr_name);
			}
		};
		Collections.sort(_records, comparator);
	}
	
	public static void sortRecordsByNameDesc(ArrayList<AdvancedPhoneLogRecord> _records) {
		Comparator<AdvancedPhoneLogRecord> comparator = new Comparator<AdvancedPhoneLogRecord>() {
			public int compare(AdvancedPhoneLogRecord arg0,
					AdvancedPhoneLogRecord arg1) {
				if (arg0.aplr_name == null) {
					arg0.aplr_name = ConstantStatic.UNKNOWN;
				}
				if (arg1.aplr_name == null) {
					arg1.aplr_name = ConstantStatic.UNKNOWN;
				}
				return arg0.aplr_name.compareTo(arg1.aplr_name)*-1;
			}
		};
		Collections.sort(_records, comparator);
	}
	
	public static void sortRecordsByDurationAsc(ArrayList<AdvancedPhoneLogRecord> _records) {
		Comparator<AdvancedPhoneLogRecord> comparator = new Comparator<AdvancedPhoneLogRecord>() {
			public int compare(AdvancedPhoneLogRecord arg0,
					AdvancedPhoneLogRecord arg1) {
				Integer l_arg0 = new Integer(arg0.aplr_duration);
				Integer l_arg1 = new Integer(arg1.aplr_duration);
				return l_arg0.compareTo(l_arg1);
			}
		};
		Collections.sort(_records, comparator);
	}
	
	public static void sortRecordsByDurationDesc(ArrayList<AdvancedPhoneLogRecord> _records) {
		Comparator<AdvancedPhoneLogRecord> comparator = new Comparator<AdvancedPhoneLogRecord>() {
			public int compare(AdvancedPhoneLogRecord arg0,
					AdvancedPhoneLogRecord arg1) {
				Integer l_arg0 = new Integer(arg0.aplr_duration);
				Integer l_arg1 = new Integer(arg1.aplr_duration);
				return l_arg0.compareTo(l_arg1)*-1;
			}
		};
		Collections.sort(_records, comparator);
	}
	
	public static void sortStringAsc(ArrayList<String> _records) {
		Comparator<String> comparator = new Comparator<String>() {
			public int compare(String arg0,
					String arg1) {
				if (arg0 == null) {
					arg0 = ConstantStatic.UNKNOWN;
				}
				if (arg1 == null) {
					arg1 = ConstantStatic.UNKNOWN;
				}
				return arg0.compareTo(arg1);
			}
		};
		Collections.sort(_records, comparator);
	}
}
