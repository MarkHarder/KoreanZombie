package markharder.koreanzombie.korean;

import java.util.Arrays;

/*
 * KoreanSyllable contains the information necessary to make a Korean syllable
 *   or a non-Korean syllable
 * A Korean syllable pattern is either a consonant-vowel
 *   or consonant-vowel-consonant
 * As characters are added, a new Korean syllable is built
 * Some vowels and bottom consonants can be combined to form compound characters
 *   see the arrays compose and decompose for a list of those
 */
public class KoreanSyllable {
	public static final char[] ENGLISH = {
        0x0041, // A
        0x0042,
        0x0043,
        0x0044,
        0x0045,
        0x0046,
        0x0047,
        0x0048,
        0x0049,
        0x004a,
        0x004b,
        0x004c,
        0x004d,
        0x004e,
        0x004f,
        0x0050,
        0x0051,
        0x0052,
        0x0053,
        0x0054,
        0x0055,
        0x0056,
        0x0057,
        0x0058,
        0x0059,
        0x005a, // Z

        0x0061, // a
        0x0062,
        0x0063,
        0x0064,
        0x0065,
        0x0066,
        0x0067,
        0x0068,
        0x0069,
        0x006a,
        0x006b,
        0x006c,
        0x006d,
        0x006e,
        0x006f,
        0x0070,
        0x0071,
        0x0072,
        0x0073,
        0x0074,
        0x0075,
        0x0076,
        0x0077,
        0x0078,
        0x0079,
        0x007a, // z

        0xff21, // Ａ non-standard English alphabet
        0xff22,
        0xff23,
        0xff24,
        0xff25,
        0xff26,
        0xff27,
        0xff28,
        0xff29,
        0xff2a,
        0xff2b,
        0xff2c,
        0xff2d,
        0xff2e,
        0xff2f,
        0xff30,
        0xff31,
        0xff32,
        0xff33,
        0xff34,
        0xff35,
        0xff36,
        0xff37,
        0xff38,
        0xff39,
        0xff3a, // Ｚ

        0xff41, // ａ non-standard English alphabet
        0xff42,
        0xff43,
        0xff44,
        0xff45,
        0xff46,
        0xff47,
        0xff48,
        0xff49,
        0xff4a,
        0xff4b,
        0xff4c,
        0xff4d,
        0xff4e,
        0xff4f,
        0xff50,
        0xff51,
        0xff52,
        0xff53,
        0xff54,
        0xff55,
        0xff56,
        0xff57,
        0xff58,
        0xff59,
        0xff5a, // ｚ
    };

	public static final char[] PUNCTUATION = {
        0x0020, // space
        0x0021, // !
        0x0022, // "
        0x0023, // #
        0x0024, // $
        0x0025, // %
        0x0026, // &
        0x0027, // '
        0x0028, // (
        0x0029, // )
        0x002a, // *
        0x002b, // +
        0x002c, // ,
        0x002d, // -
        0x002e, // .
        0x002f, // /
        0x0030, // 0
        0x0031,
        0x0032,
        0x0033,
        0x0034,
        0x0035,
        0x0036,
        0x0037,
        0x0038,
        0x0039, // 9
        0x003a, // :
        0x003b, // ;
        0x003c, // <
        0x003d, // =
        0x003e, // >
        0x003f, // ?
        0x0040, // @

        0x005b, // [
        0x005c, // \
        0x005d, // ]
        0x005e, // ^
        0x005f, // _
        0x0060, // `

        0x007b, // {
        0x007c, // |
        0x007d, // }
        0x007e, // ~

        0xff01, // ！ non-standard English alphabet
        0xff02, // ＂
        0xff03, // ＃
        0xff04, // ＄
        0xff05, // ％
        0xff06, // ＆
        0xff07, // ＇
        0xff08, // （
        0xff09, // ）
        0xff0a, // ＊
        0xff0b, // ＋
        0xff0c, // ，
        0xff0d, // －
        0xff0e, // ．
        0xff0f, // ／
        0xff10, // ０
        0xff11,
        0xff12,
        0xff13,
        0xff14,
        0xff15,
        0xff16,
        0xff17,
        0xff18,
        0xff19, // ９
        0xff1a, // ：
        0xff1b, // ；
        0xff1c, // ＜
        0xff1d, // ＝
        0xff1e, // ＞
        0xff1f, // ？
        0xff20, // ＠

        0xff3b, // ［ non-standard English alphabet
        0xff3c, // ＼
        0xff3d, // ］
        0xff3e, // ＾
        0xff3f, // ＿
        0xff40, // ｀

        0xff5b, // ｛ non-standard English alphabet
        0xff5c, // ｜
        0xff5d, // ｝
    };

	public static final char[] CONSONANTS = {
		0x3131, // ㄱ
		0x3132, // ㄲ
		0x3134, // ㄴ
		0x3137, // ㄷ
		0x3138, // ㄸ
		0x3139, // ㄹ
		0x3141, // ㅁ
		0x3142, // ㅂ
		0x3143, // ㅃ
		0x3145, // ㅅ
		0x3146, // ㅆ
		0x3147, // ㅇ
		0x3148, // ㅈ
		0x3149, // ㅉ
		0x314a, // ㅊ
		0x314b, // ㅋ
		0x314c, // ㅌ
		0x314d, // ㅍ
		0x314e, // ㅎ
	};
	
	public static final char[] VOWELS = {
		0x314f, // ㅏ
		0x3150, // ㅐ
		0x3151, // ㅑ
		0x3152, // ㅒ
		0x3153, // ㅓ
		0x3154, // ㅔ
		0x3155, // ㅕ
		0x3156, // ㅖ
		0x3157, // ㅗ
		0x315b, // ㅛ
		0x315c, // ㅜ
		0x3160, // ㅠ
		0x3161, // ㅡ
		0x3163, // ㅣ
	};
	
	private static final char[][] compose = {
		{'ㄱ', 'ㅅ', 0x3133},
		{'ㄴ', 'ㅈ', 0x3135},
		{'ㄴ', 'ㅎ', 0x3136},
		{'ㄹ', 'ㄱ', 0x313a},
		{'ㄹ', 'ㅁ', 0x313b},
		{'ㄹ', 'ㅂ', 0x313c},
		{'ㄹ', 'ㅅ', 0x313d},
		{'ㄹ', 'ㅌ', 0x313e},
		{'ㄹ', 'ㅍ', 0x313f},
		{'ㄹ', 'ㅎ', 0x3140},
		{'ㅂ', 'ㅅ', 0x3144},
		{'ㅗ', 'ㅏ', 0x3158},
		{'ㅗ', 'ㅐ', 0x3159},
		{'ㅗ', 'ㅣ', 0x315a},
		{'ㅜ', 'ㅓ', 0x315d},
		{'ㅜ', 'ㅔ', 0x315e},
		{'ㅜ', 'ㅣ', 0x315f},
		{'ㅡ', 'ㅣ', 0x3162}
	};
	
	private static final char[][] decompose = {
		{0x3133, 'ㄱ', 'ㅅ'},
		{0x3135, 'ㄴ', 'ㅈ'},
		{0x3136, 'ㄴ', 'ㅎ'},
		{0x313a, 'ㄹ', 'ㄱ'},
		{0x313b, 'ㄹ', 'ㅁ'},
		{0x313c, 'ㄹ', 'ㅂ'},
		{0x313d, 'ㄹ', 'ㅅ'},
		{0x313e, 'ㄹ', 'ㅌ'},
		{0x313f, 'ㄹ', 'ㅍ'},
		{0x3140, 'ㄹ', 'ㅎ'},
		{0x3144, 'ㅂ', 'ㅅ'},
		{0x3158, 'ㅗ', 'ㅏ'},
		{0x3159, 'ㅗ', 'ㅐ'},
		{0x315a, 'ㅗ', 'ㅣ'},
		{0x315d, 'ㅜ', 'ㅓ'},
		{0x315e, 'ㅜ', 'ㅔ'},
		{0x315f, 'ㅜ', 'ㅣ'},
		{0x3162, 'ㅡ', 'ㅣ'}
	};
	
    // top consonant
	private char choseong;
    // vowel
	private char jungseong;
    // bottom consonant
	private char jongseong;
    // for non-Korean syllables
	private char c;
	
	public KoreanSyllable() {
		this((char) 0);
	}
	
	public KoreanSyllable(char c) {
		choseong = 0;
		jungseong = 0;
		jongseong = 0;
		this.c = c;
	}
	
	// copy constructor
	public KoreanSyllable(KoreanSyllable ks) {
		choseong = ks.choseong;
		jungseong = ks.jungseong;
		jongseong = ks.jongseong;
		c = ks.c;
	}
	
	public void add(char c) {
		this.c = 0;
		if (Arrays.binarySearch(CONSONANTS, c) >= 0) {
			if (choseong == 0) {
                // if there is no top consonant, put it there
				choseong = c;
			} else if (jongseong == 0 && (c != 0x3138 && c != 0x3143 && c != 0x3149)) {
                /*
                 * if there is not bottom consonant, put it there
                 * unless the character is:
                 *   0x3138 - ㄸ
                 *   0x3138 - ㅃ
                 *   0x3138 - ㅉ
                 * those characters cann't be a bottom consonant
                 */
				jongseong = c;
			}
		} else if (Arrays.binarySearch(VOWELS, c) >= 0) {
            // set the vowel
			jungseong = c;
		} else {
            // set the non-Korean character
			this.c = c;
		}
	}
	
	public boolean hasChoseong() {
		return choseong != 0;
	}
	
	public boolean hasJungseong() {
		return jungseong != 0;
	}
	
	public boolean hasJongseong() {
		return jongseong != 0;
	}
	
    // if none of the korean characters are filled, it's a non-Korean character
	public boolean notKorean() {
		return choseong == 0 && jungseong == 0 && jongseong == 0;
	}
	
    // if none of the characters are filled, it's empty
	public boolean empty() {
		return choseong == 0 && jungseong == 0 && jongseong == 0 && c == 0;
	}
	
    // check to see if the incoming character can be added to the syllable
	public boolean canCompose(char c) {
		for (char[] set : compose) {
            /*
             * check if the incoming character matches the other half of the
             *   combination
             * e.g. 으 + ㅣ = 의, 목 + ㅅ = 몫
             *   so it checks if ㅡ,ㅣ and ㄱ,ㅅ is in the composition array
             */
			if (set[0] == jongseong && set[1] == c) {
				return true;
			} else if (set[0] == jungseong && set[1] == c) {
				return true;
			}
		}
		return false;
	}
	
    /*
     * combine two Korean characters,
     * either two consonants or two vowels
     * see the array compose for all combinations
     *   ㅡ+ㅣ = ㅢ, ㅗ+ㅏ = ㅘ, etc.
     *   목+ㅅ = 몫, 살+ㅁ = 삶, etc.
     * canCompose() should be checked before attempting to call compose()
     */
	public void compose(char c) {
		this.c = 0;
		for (char[] set : compose) {
			if (set[0] == jongseong && set[1] == c) {
				jongseong = set[2];
			} else if (set[0] == jungseong && set[1] == c) {
				jungseong = set[2];
			}
		}
	}
	
    /*
     * delete a single character
     * this removes the latest added Korean character if there is more than one
     * if there is only one Korean character or there is a non-Korean character,
     *   the syllable is emptied
     */
	public char delete() {
		c = 0;
		if (jongseong > 0) {
			for (char[] set : decompose) {
				if (jongseong == set[0]) {
					jongseong = set[1];
					return set[2];
				}
			}
            // if the bottom is a compound consonant, decompose it
            // otherwise erase it
			char temp = jongseong;
			jongseong = 0;
			return temp;
		} else if (jungseong > 0) {
			for (char[] set : decompose) {
				if (jungseong == set[0]) {
					jungseong = set[1];
					return set[2];
				}
			}
            // if the vowel is compound, decompose it
            // otherwise erase it
			char temp = jungseong;
			jungseong = 0;
			return temp;
		} else if (choseong > 0) {
            // if there are no other characters, remove the top consonant
			char temp = choseong;
			choseong = 0;
			return temp;
		}
		return 0;
	}
	
    /*
     * return the string form of the syllable
     * if the syllable is non-Korean, return that syllable
     * otherwise calculate the combined Korean syllable
     */
	@Override
	public String toString() {
        // return the string form of non-Korean syllable
		if (c != 0) {
			return Character.toString(c);
        // an empty syllable returns an empty string
		} else if (choseong == 0 && jungseong == 0 && jongseong == 0 && c == 0) {
			return "";
		}
		
        /* otherwise a Korean syllable remains
         * at this point a calculation is made to return the correct whole
         *   Korean syllable composed of some combination of top consonant,
         *   vowel, and bottom consonant.
         * The Korean consonants are spread across the Unicode character table
         * They are arranged alphabetically by top consonant, then by vowel
         *   and finally by bottom consonant.
         * So once the alphabetical position of each character has been
         *   calculated, the syllable position can be found based on these
         *   offsets. Remember that some top consonants are possible when the
         *   same consonants are not possible on the bottom like ㄸ, ㅃ, ㅉ
         */
		char choseongBase = 0x3131;
		char ch = (char) (choseong - choseongBase);
		if (choseong == 0) {
			ch = 0;
		}
		
		char jungseongBase = 0x314f;
		char ju = (char) (jungseong - jungseongBase);
		if (jungseong == 0) {
			ju = 0;
		}
		
		char jongseongBase = 0x3130;
		char jo = (char) (jongseong - jongseongBase);
		if (jongseong == 0) {
			jo = 0;
		}
		if (jo > 7) {
			jo -= 1;
		}
		if (jo > 17) {
			jo -= 1;
		}
		if (jo > 22) {
			jo -= 1;
		}

		char hangulBase = 0x3131;
		char chSeperation = 1;
		if (jungseong > 0) {
			hangulBase = 0xac00;
			chSeperation = 588;
			if (ch > 1) {
				ch -= 1;
			}
			if (ch > 2) {
				ch -= 2;
			}
			if (ch > 5) {
				ch -= 7;
			}
			if (ch > 8) {
				ch -= 1;
			}
		}
		char juSeperation = 28;
		if (choseong == 0) {
			juSeperation = 0;
			hangulBase = jungseong;
		}
		c = (char) (ch * chSeperation + ju * juSeperation + jo + hangulBase);
		return Character.toString(c);
	}
}
