package io.trane.ndbc.mysql.proto;

import java.util.HashMap;
import java.util.Map;

public class Collation {
	final public int id;
	final public String name;
	final public String charset;
	final public boolean isDefault;

	public Collation(final int id, final String name, final String charset, final boolean isDefault) {
		this.id = id;
		this.name = name;
		this.charset = charset;
		this.isDefault = isDefault;
	}

	public static Collation getCollationById(final int collationId) {
		if (collationId > 250 || collationId < 1)
			throw new IllegalArgumentException("Invalid collationId");

		final Collation collation = collationById[collationId];

		if (collation == null)
			throw new IllegalArgumentException("Invalid collationId");

		return collation;
	}

	public static Collation getCollationByEncoding(final String encoding) {
		if (encoding == null)
			throw new IllegalArgumentException("charset cannot be null");

		final Collation collation = collationByCharset.getOrDefault(encoding, collationByName.get(encoding));

		if (collation == null)
			throw new IllegalArgumentException("Invalid encoding");

		return collation;
	}

	final static private Collation[] collationById = new Collation[251];
	final static private Map<String, Collation> collationByName = new HashMap<>();
	final static private Map<String, Collation> collationByCharset = new HashMap<>();

	private static void addCollation(final Collation collation) {
		collationById[collation.id] = collation;
		collationByName.put(collation.name, collation);
		if (collation.isDefault) {
			collationByCharset.put(collation.charset, collation);
		}
	}
	static {
		addCollation(new Collation(13, "sjis_japanese_ci", "sjis", true));
		addCollation(new Collation(14, "cp1251_bulgarian_ci", "cp1251", false));
		addCollation(new Collation(15, "latin1_danish_ci", "latin1", false));
		addCollation(new Collation(16, "hebrew_general_ci", "hebrew", true));
		addCollation(new Collation(18, "tis620_thai_ci", "tis620", true));
		addCollation(new Collation(19, "euckr_korean_ci", "euckr", true));
		addCollation(new Collation(20, "latin7_estonian_cs", "latin7", false));
		addCollation(new Collation(21, "latin2_hungarian_ci", "latin2", false));
		addCollation(new Collation(22, "koi8u_general_ci", "koi8u", true));
		addCollation(new Collation(23, "cp1251_ukrainian_ci", "cp1251", false));
		addCollation(new Collation(24, "gb2312_chinese_ci", "gb2312", true));
		addCollation(new Collation(25, "greek_general_ci", "greek", true));
		addCollation(new Collation(26, "cp1250_general_ci", "cp1250", true));
		addCollation(new Collation(27, "latin2_croatian_ci", "latin2", false));
		addCollation(new Collation(28, "gbk_chinese_ci", "gbk", true));
		addCollation(new Collation(29, "cp1257_lithuanian_ci", "cp1257", false));
		addCollation(new Collation(30, "latin5_turkish_ci", "latin5", true));
		addCollation(new Collation(31, "latin1_german2_ci", "latin1", false));
		addCollation(new Collation(32, "armscii8_general_ci", "armscii8", true));
		addCollation(new Collation(33, "utf8_general_ci", "utf8", true));
		addCollation(new Collation(34, "cp1250_czech_cs", "cp1250", false));
		addCollation(new Collation(35, "ucs2_general_ci", "ucs2", true));
		addCollation(new Collation(36, "cp866_general_ci", "cp866", true));
		addCollation(new Collation(37, "keybcs2_general_ci", "keybcs2", true));
		addCollation(new Collation(38, "macce_general_ci", "macce", true));
		addCollation(new Collation(39, "macroman_general_ci", "macroman", true));
		addCollation(new Collation(40, "cp852_general_ci", "cp852", true));
		addCollation(new Collation(41, "latin7_general_ci", "latin7", true));
		addCollation(new Collation(42, "latin7_general_cs", "latin7", false));
		addCollation(new Collation(43, "macce_bin", "macce", false));
		addCollation(new Collation(44, "cp1250_croatian_ci", "cp1250", false));
		addCollation(new Collation(45, "utf8mb4_general_ci", "utf8mb4", true));
		addCollation(new Collation(46, "utf8mb4_bin", "utf8mb4", false));
		addCollation(new Collation(47, "latin1_bin", "latin1", false));
		addCollation(new Collation(48, "latin1_general_ci", "latin1", false));
		addCollation(new Collation(49, "latin1_general_cs", "latin1", false));
		addCollation(new Collation(50, "cp1251_bin", "cp1251", false));
		addCollation(new Collation(51, "cp1251_general_ci", "cp1251", true));
		addCollation(new Collation(52, "cp1251_general_cs", "cp1251", false));
		addCollation(new Collation(53, "macroman_bin", "macroman", false));
		addCollation(new Collation(54, "utf16_general_ci", "utf16", true));
		addCollation(new Collation(55, "utf16_bin", "utf16", false));
		addCollation(new Collation(56, "utf16le_general_ci", "utf16le", true));
		addCollation(new Collation(57, "cp1256_general_ci", "cp1256", true));
		addCollation(new Collation(58, "cp1257_bin", "cp1257", false));
		addCollation(new Collation(59, "cp1257_general_ci", "cp1257", true));
		addCollation(new Collation(60, "utf32_general_ci", "utf32", true));
		addCollation(new Collation(61, "utf32_bin", "utf32", false));
		addCollation(new Collation(62, "utf16le_bin", "utf16le", false));
		addCollation(new Collation(63, "binary", "binary", true));
		addCollation(new Collation(64, "armscii8_bin", "armscii8", false));
		addCollation(new Collation(65, "ascii_bin", "ascii", false));
		addCollation(new Collation(66, "cp1250_bin", "cp1250", false));
		addCollation(new Collation(67, "cp1256_bin", "cp1256", false));
		addCollation(new Collation(68, "cp866_bin", "cp866", false));
		addCollation(new Collation(69, "dec8_bin", "dec8", false));
		addCollation(new Collation(70, "greek_bin", "greek", false));
		addCollation(new Collation(71, "hebrew_bin", "hebrew", false));
		addCollation(new Collation(72, "hp8_bin", "hp8", false));
		addCollation(new Collation(73, "keybcs2_bin", "keybcs2", false));
		addCollation(new Collation(74, "koi8r_bin", "koi8r", false));
		addCollation(new Collation(75, "koi8u_bin", "koi8u", false));
		addCollation(new Collation(77, "latin2_bin", "latin2", false));
		addCollation(new Collation(78, "latin5_bin", "latin5", false));
		addCollation(new Collation(79, "latin7_bin", "latin7", false));
		addCollation(new Collation(80, "cp850_bin", "cp850", false));
		addCollation(new Collation(81, "cp852_bin", "cp852", false));
		addCollation(new Collation(82, "swe7_bin", "swe7", false));
		addCollation(new Collation(83, "utf8_bin", "utf8", false));
		addCollation(new Collation(84, "big5_bin", "big5", false));
		addCollation(new Collation(85, "euckr_bin", "euckr", false));
		addCollation(new Collation(86, "gb2312_bin", "gb2312", false));
		addCollation(new Collation(87, "gbk_bin", "gbk", false));
		addCollation(new Collation(88, "sjis_bin", "sjis", false));
		addCollation(new Collation(89, "tis620_bin", "tis620", false));
		addCollation(new Collation(90, "ucs2_bin", "ucs2", false));
		addCollation(new Collation(91, "ujis_bin", "ujis", false));
		addCollation(new Collation(92, "geostd8_general_ci", "geostd8", true));
		addCollation(new Collation(93, "geostd8_bin", "geostd8", false));
		addCollation(new Collation(94, "latin1_spanish_ci", "latin1", false));
		addCollation(new Collation(95, "cp932_japanese_ci", "cp932", true));
		addCollation(new Collation(96, "cp932_bin", "cp932", false));
		addCollation(new Collation(97, "eucjpms_japanese_ci", "eucjpms", true));
		addCollation(new Collation(98, "eucjpms_bin", "eucjpms", false));
		addCollation(new Collation(99, "cp1250_polish_ci", "cp1250", false));
		addCollation(new Collation(101, "utf16_unicode_ci", "utf16", false));
		addCollation(new Collation(102, "utf16_icelandic_ci", "utf16", false));
		addCollation(new Collation(103, "utf16_latvian_ci", "utf16", false));
		addCollation(new Collation(104, "utf16_romanian_ci", "utf16", false));
		addCollation(new Collation(105, "utf16_slovenian_ci", "utf16", false));
		addCollation(new Collation(106, "utf16_polish_ci", "utf16", false));
		addCollation(new Collation(107, "utf16_estonian_ci", "utf16", false));
		addCollation(new Collation(108, "utf16_spanish_ci", "utf16", false));
		addCollation(new Collation(109, "utf16_swedish_ci", "utf16", false));
		addCollation(new Collation(110, "utf16_turkish_ci", "utf16", false));
		addCollation(new Collation(111, "utf16_czech_ci", "utf16", false));
		addCollation(new Collation(112, "utf16_danish_ci", "utf16", false));
		addCollation(new Collation(113, "utf16_lithuanian_ci", "utf16", false));
		addCollation(new Collation(114, "utf16_slovak_ci", "utf16", false));
		addCollation(new Collation(115, "utf16_spanish2_ci", "utf16", false));
		addCollation(new Collation(116, "utf16_roman_ci", "utf16", false));
		addCollation(new Collation(117, "utf16_persian_ci", "utf16", false));
		addCollation(new Collation(118, "utf16_esperanto_ci", "utf16", false));
		addCollation(new Collation(119, "utf16_hungarian_ci", "utf16", false));
		addCollation(new Collation(120, "utf16_sinhala_ci", "utf16", false));
		addCollation(new Collation(121, "utf16_german2_ci", "utf16", false));
		addCollation(new Collation(122, "utf16_croatian_ci", "utf16", false));
		addCollation(new Collation(123, "utf16_unicode_520_ci", "utf16", false));
		addCollation(new Collation(124, "utf16_vietnamese_ci", "utf16", false));
		addCollation(new Collation(128, "ucs2_unicode_ci", "ucs2", false));
		addCollation(new Collation(129, "ucs2_icelandic_ci", "ucs2", false));
		addCollation(new Collation(130, "ucs2_latvian_ci", "ucs2", false));
		addCollation(new Collation(131, "ucs2_romanian_ci", "ucs2", false));
		addCollation(new Collation(132, "ucs2_slovenian_ci", "ucs2", false));
		addCollation(new Collation(133, "ucs2_polish_ci", "ucs2", false));
		addCollation(new Collation(134, "ucs2_estonian_ci", "ucs2", false));
		addCollation(new Collation(135, "ucs2_spanish_ci", "ucs2", false));
		addCollation(new Collation(136, "ucs2_swedish_ci", "ucs2", false));
		addCollation(new Collation(137, "ucs2_turkish_ci", "ucs2", false));
		addCollation(new Collation(138, "ucs2_czech_ci", "ucs2", false));
		addCollation(new Collation(139, "ucs2_danish_ci", "ucs2", false));
		addCollation(new Collation(140, "ucs2_lithuanian_ci", "ucs2", false));
		addCollation(new Collation(141, "ucs2_slovak_ci", "ucs2", false));
		addCollation(new Collation(142, "ucs2_spanish2_ci", "ucs2", false));
		addCollation(new Collation(143, "ucs2_roman_ci", "ucs2", false));
		addCollation(new Collation(144, "ucs2_persian_ci", "ucs2", false));
		addCollation(new Collation(145, "ucs2_esperanto_ci", "ucs2", false));
		addCollation(new Collation(146, "ucs2_hungarian_ci", "ucs2", false));
		addCollation(new Collation(147, "ucs2_sinhala_ci", "ucs2", false));
		addCollation(new Collation(148, "ucs2_german2_ci", "ucs2", false));
		addCollation(new Collation(149, "ucs2_croatian_ci", "ucs2", false));
		addCollation(new Collation(150, "ucs2_unicode_520_ci", "ucs2", false));
		addCollation(new Collation(151, "ucs2_vietnamese_ci", "ucs2", false));
		addCollation(new Collation(159, "ucs2_general_mysql500_ci", "ucs2", false));
		addCollation(new Collation(160, "utf32_unicode_ci", "utf32", false));
		addCollation(new Collation(161, "utf32_icelandic_ci", "utf32", false));
		addCollation(new Collation(162, "utf32_latvian_ci", "utf32", false));
		addCollation(new Collation(163, "utf32_romanian_ci", "utf32", false));
		addCollation(new Collation(164, "utf32_slovenian_ci", "utf32", false));
		addCollation(new Collation(165, "utf32_polish_ci", "utf32", false));
		addCollation(new Collation(166, "utf32_estonian_ci", "utf32", false));
		addCollation(new Collation(167, "utf32_spanish_ci", "utf32", false));
		addCollation(new Collation(168, "utf32_swedish_ci", "utf32", false));
		addCollation(new Collation(169, "utf32_turkish_ci", "utf32", false));
		addCollation(new Collation(170, "utf32_czech_ci", "utf32", false));
		addCollation(new Collation(171, "utf32_danish_ci", "utf32", false));
		addCollation(new Collation(172, "utf32_lithuanian_ci", "utf32", false));
		addCollation(new Collation(173, "utf32_slovak_ci", "utf32", false));
		addCollation(new Collation(174, "utf32_spanish2_ci", "utf32", false));
		addCollation(new Collation(175, "utf32_roman_ci", "utf32", false));
		addCollation(new Collation(176, "utf32_persian_ci", "utf32", false));
		addCollation(new Collation(177, "utf32_esperanto_ci", "utf32", false));
		addCollation(new Collation(178, "utf32_hungarian_ci", "utf32", false));
		addCollation(new Collation(179, "utf32_sinhala_ci", "utf32", false));
		addCollation(new Collation(180, "utf32_german2_ci", "utf32", false));
		addCollation(new Collation(181, "utf32_croatian_ci", "utf32", false));
		addCollation(new Collation(182, "utf32_unicode_520_ci", "utf32", false));
		addCollation(new Collation(183, "utf32_vietnamese_ci", "utf32", false));
		addCollation(new Collation(192, "utf8_unicode_ci", "utf8", false));
		addCollation(new Collation(193, "utf8_icelandic_ci", "utf8", false));
		addCollation(new Collation(194, "utf8_latvian_ci", "utf8", false));
		addCollation(new Collation(195, "utf8_romanian_ci", "utf8", false));
		addCollation(new Collation(196, "utf8_slovenian_ci", "utf8", false));
		addCollation(new Collation(197, "utf8_polish_ci", "utf8", false));
		addCollation(new Collation(198, "utf8_estonian_ci", "utf8", false));
		addCollation(new Collation(199, "utf8_spanish_ci", "utf8", false));
		addCollation(new Collation(200, "utf8_swedish_ci", "utf8", false));
		addCollation(new Collation(201, "utf8_turkish_ci", "utf8", false));
		addCollation(new Collation(202, "utf8_czech_ci", "utf8", false));
		addCollation(new Collation(203, "utf8_danish_ci", "utf8", false));
		addCollation(new Collation(204, "utf8_lithuanian_ci", "utf8", false));
		addCollation(new Collation(205, "utf8_slovak_ci", "utf8", false));
		addCollation(new Collation(206, "utf8_spanish2_ci", "utf8", false));
		addCollation(new Collation(207, "utf8_roman_ci", "utf8", false));
		addCollation(new Collation(208, "utf8_persian_ci", "utf8", false));
		addCollation(new Collation(209, "utf8_esperanto_ci", "utf8", false));
		addCollation(new Collation(210, "utf8_hungarian_ci", "utf8", false));
		addCollation(new Collation(211, "utf8_sinhala_ci", "utf8", false));
		addCollation(new Collation(212, "utf8_german2_ci", "utf8", false));
		addCollation(new Collation(213, "utf8_croatian_ci", "utf8", false));
		addCollation(new Collation(214, "utf8_unicode_520_ci", "utf8", false));
		addCollation(new Collation(215, "utf8_vietnamese_ci", "utf8", false));
		addCollation(new Collation(223, "utf8_general_mysql500_ci", "utf8", false));
		addCollation(new Collation(224, "utf8mb4_unicode_ci", "utf8mb4", false));
		addCollation(new Collation(225, "utf8mb4_icelandic_ci", "utf8mb4", false));
		addCollation(new Collation(226, "utf8mb4_latvian_ci", "utf8mb4", false));
		addCollation(new Collation(227, "utf8mb4_romanian_ci", "utf8mb4", false));
		addCollation(new Collation(228, "utf8mb4_slovenian_ci", "utf8mb4", false));
		addCollation(new Collation(229, "utf8mb4_polish_ci", "utf8mb4", false));
		addCollation(new Collation(230, "utf8mb4_estonian_ci", "utf8mb4", false));
		addCollation(new Collation(231, "utf8mb4_spanish_ci", "utf8mb4", false));
		addCollation(new Collation(232, "utf8mb4_swedish_ci", "utf8mb4", false));
		addCollation(new Collation(233, "utf8mb4_turkish_ci", "utf8mb4", false));
		addCollation(new Collation(234, "utf8mb4_czech_ci", "utf8mb4", false));
		addCollation(new Collation(235, "utf8mb4_danish_ci", "utf8mb4", false));
		addCollation(new Collation(236, "utf8mb4_lithuanian_ci", "utf8mb4", false));
		addCollation(new Collation(237, "utf8mb4_slovak_ci", "utf8mb4", false));
		addCollation(new Collation(238, "utf8mb4_spanish2_ci", "utf8mb4", false));
		addCollation(new Collation(239, "utf8mb4_roman_ci", "utf8mb4", false));
		addCollation(new Collation(240, "utf8mb4_persian_ci", "utf8mb4", false));
		addCollation(new Collation(241, "utf8mb4_esperanto_ci", "utf8mb4", false));
		addCollation(new Collation(242, "utf8mb4_hungarian_ci", "utf8mb4", false));
		addCollation(new Collation(243, "utf8mb4_sinhala_ci", "utf8mb4", false));
		addCollation(new Collation(244, "utf8mb4_german2_ci", "utf8mb4", false));
		addCollation(new Collation(245, "utf8mb4_croatian_ci", "utf8mb4", false));
		addCollation(new Collation(246, "utf8mb4_unicode_520_ci", "utf8mb4", false));
		addCollation(new Collation(247, "utf8mb4_vietnamese_ci", "utf8mb4", false));
		addCollation(new Collation(248, "gb18030_chinese_ci", "gb18030", true));
		addCollation(new Collation(249, "gb18030_bin", "gb18030", false));
		addCollation(new Collation(250, "gb18030_unicode_520_ci", "gb18030", false));
	}

}
