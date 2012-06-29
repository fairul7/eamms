package com.tms.collab.messaging.ui;

import org.apache.commons.collections.SequencedHashMap;

public class MailEncoding
{
    public static MailEncoding encoding;
    private SequencedHashMap encodings;

    private MailEncoding()
    {
        encodings = new SequencedHashMap();
        /*Populating encoding types. NOTE a map is used instead of an array for
        easier implementation when called*/
        encodings.put("IBM-864", "Arabic (IBM-864)");
        encodings.put("ISO-8859-6", "Arabic (ISO-8859-6)");
        encodings.put("MacArabic", "Arabic (MacArabic)");
        encodings.put("Windows-1256", "Arabic (Windows-1256)");
        encodings.put("-1", "-------------");
        encodings.put("ARMSCII-8", "Armenian (ARMSCII-8)");
        encodings.put("-2", "-------------");
        encodings.put("ISO-8859-13", "Baltic (ISO-8859-13)");
        encodings.put("ISO-8859-4", "Baltic (ISO-8859-4)");
        encodings.put("Windows-1257", "Baltic (Windows-1257)");
        encodings.put("-3", "-------------");
        encodings.put("ISO-8859-14", "Celtic (ISO-8859-14)");
        encodings.put("-4", "-------------");
        encodings.put("IBM-852", "Central European (IBM-852)");
        encodings.put("ISO-8859-2", "Central European (ISO-8859-2)");
        encodings.put("MacCE", "Central European (MacCE)");
        encodings.put("Windows-1250", "Central European (Windows-1250)");
        encodings.put("-5", "-------------");
        encodings.put("GB18030", "Chinese Simplified (GB18030)");
        encodings.put("GB2312", "Chinese Simplified (GB2312)");
        encodings.put("GBK", "Chinese Simplified (GBK)");
        encodings.put("HZ", "Chinese Simplified (HZ)");
        encodings.put("ISO-2022-CN", "Chinese Simplified (ISO-2022-CN)");
        encodings.put("Big5", "Chinese Traditional (Big5)");
        encodings.put("Big5-HKSCS", "Chinese Traditional (Big5-HKSCS)");
        encodings.put("EUC-TW", "Chinese Traditional (EUC-TW)");
        encodings.put("-6", "-------------");
        encodings.put("MacCroatian", "Croatian (MacCroation)");
        encodings.put("-7", "-------------");
        encodings.put("IBM-855", "Cyrillic (IBM-855)");
        encodings.put("ISO-8859-5", "Cyrillic (ISO-8859-5)");
        encodings.put("ISO-IR-111", "Cyrillic (ISO-IR-111)");
        encodings.put("KOI8-R", "Cyrillic (KOI8-R)");
        encodings.put("MacCyrillic", "Cyrillic (MacCyrillic)");
        encodings.put("Windows-1251", "Cyrillic (Windows-1251)");
        encodings.put("CP-866", "Cyrillic/Russian (CP-866)");
        encodings.put("KOI8-U", "Cyrillic/Ukrainian (KOI8-U)");
        encodings.put("MacUkrainian", "Cyrillic/Ukrainian (MacUkrainian)");
        encodings.put("-8", "-------------");
        encodings.put("MacFarsi", "Farsi (MacFarsi)");
        encodings.put("-9", "-------------");
        encodings.put("GEOSTD8", "Georgian (GEOSTD8)");
        encodings.put("-10", "-------------");
        encodings.put("ISO-8859-7", "Greek (ISO-8859-7)");
        encodings.put("MacGreek", "Greek (MacGreek)");
        encodings.put("Windows-1253", "Greek (Windows-1253)");
        encodings.put("-11", "-------------");
        encodings.put("MacGujarati", "Gujarati (MacGujarati)");
        encodings.put("-12", "-------------");
        encodings.put("MacGurmukhi", "Gurmukhi (MacGurmukhi)");
        encodings.put("-13", "-------------");
        encodings.put("IBM-862", "Hebrew (IBM-862)");
        encodings.put("ISO-8859-8-I", "Hebrew (ISO-8859-8-I)");
        encodings.put("MacHebrew", "Hebrew (MacHebrew)");
        encodings.put("Windows-1255", "Hebrew (Windows-1255)");
        encodings.put("ISO-8859-8", "Hebrew Visual (ISO-8859-8)");
        encodings.put("-14", "-------------");
        encodings.put("MacDevanagari", "Hindi (MacDevanagari)");
        encodings.put("-15", "-------------");
        encodings.put("", "Icelandic (MacIcelandic)");
        encodings.put("-16", "-------------");
        encodings.put("EUC-JP", "Japanese (EUC-JP)");
        encodings.put("ISO-2022-JP", "Japanese (ISO-2022-JP)");
        encodings.put("Shift_JIS", "Japanese (Shift_JIS)");
        encodings.put("-17", "-------------");
        encodings.put("EUC-KR", "Korean (EUC-KR)");
        encodings.put("ISO-2022-KR", "Korean (ISO-2022-KR)");
        encodings.put("JOHAB", "Korean (JOHAB)");
        encodings.put("UHC", "Korean (UHC)");
        encodings.put("-18", "-------------");
        encodings.put("ISO-8859-10", "Nordic (ISO-8859-10)");
        encodings.put("-19", "-------------");
        encodings.put("ISO-8859-16", "Romanian (ISO-8859-16)");
        encodings.put("MacRomanian", "Romanian (MacRomanian)");
        encodings.put("-20", "-------------");
        encodings.put("ISO-8859-3", "South European (ISO-8859-3)");
        encodings.put("-21", "-------------");
        encodings.put("ISO-8859-11", "Thai (ISO-8859-11)");
        encodings.put("TIS-620", "Thai (TIS-620)");
        encodings.put("Windows-874", "Thai (Windows-874)");
        encodings.put("-22", "-------------");
        encodings.put("IBM-857", "Turkish (IBM-857)");
        encodings.put("ISO-8859-9", "Turkish (ISO-8859-9)");
        encodings.put("MacTurkish", "Turkish (MacTurkish)");
        encodings.put("Windows-1254", "Turkish (Windows-1254)");
        encodings.put("-23", "-------------");
        encodings.put("UTF-16 Big Endian", "Unicode (UTF-16 Big Endian)");
        encodings.put("UTF-16 Little Endian", "Unicode (UTF-16 Little Endian)");
        encodings.put("UTF-16", "Unicode (UTF-16)");
        encodings.put("UTF-32 Big Endian", "Unicode (UTF-32 Big Endian)");
        encodings.put("UTF-32 Little Endian", "Unicode (UTF-32 Little Endian)");
        encodings.put("UTF-32", "Unicode (UTF-32)");
        encodings.put("UTF-7", "Unicode (UTF-7)");
        encodings.put("UTF-8", "Unicode (UTF-8)");
        encodings.put("-24", "-------------");
        encodings.put("TCVN", "Vietnamese (TCVN)");
        encodings.put("VISCII", "Vietnamese (VISCII)");
        encodings.put("VPS", "Vietnamese (VPS)");
        encodings.put("Windows-1258", "Vietnamese (Windows-1258)");
        encodings.put("-25", "-------------");
        encodings.put("IBM-850", "Western (IBM-850)");
        encodings.put("ISO-8859-1", "Western (ISO-8859-1)");
        encodings.put("ISO-8859-15", "Western (ISO-8859-15)");
        encodings.put("MacRoman", "Western (MacRoman)");
        encodings.put("Windows-1252", "Western (Windows-1252)");
    }

    public static MailEncoding getInstance()
    {
        if(encoding == null)
            encoding = new MailEncoding();
        return encoding;
    }

    public SequencedHashMap getEncodings()
    {
        return encodings;
    }
}
