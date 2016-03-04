package markharder.koreanzombie.korean;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * KoreanString is a string that can hold Korean syllables
 * Characters may be added one at a time
 * When a Korean character is added, see if a new syllable can be built
 * For more information on how Korean syllables are formed,
 *   see the KoreanSyllable class
 */
public class KoreanString {
    private ArrayList<KoreanSyllable> text;

    public KoreanString() {
        this("");
    }

    public KoreanString(String text) {
        this.text = new ArrayList<KoreanSyllable>();
        for (int i = 0; i < text.length(); i++) {
            this.text.add(new KoreanSyllable(text.charAt(i)));
        }
    }

    // copy constructor
    public KoreanString(KoreanString text) {
        this.text = new ArrayList<KoreanSyllable>();
        for (KoreanSyllable ks : text.text) {
            this.text.add(new KoreanSyllable(ks));
        }
    }

    public void add(char c) {
        if (c == 0x0008 || c == 0x007f) { // delete and backspace
            // remove a character if delete or backspace is pressed
            if (text.size() > 0) {
                text.get(text.size() - 1).delete();
                if (text.get(text.size() - 1).empty()) {
                    // if all the characters in the syllable have been deleted,
                    //   remove that empty syllable
                    text.remove(text.size() - 1);
                }
            }
        } else {
            // if the string is empty, add an empty syllable to use
            if (text.size() == 0) {
                text.add(new KoreanSyllable());
            }

            KoreanSyllable top = text.get(text.size() - 1);

            // append a new syllable if the last syllable is not Korean
            if (top.notKorean()) {
                text.add(new KoreanSyllable());
                top = text.get(text.size() - 1);
            }

            if (Arrays.binarySearch(KoreanSyllable.CONSONANTS, c) >= 0) {
                // if the typed character is a Korean consonant
                if (top.hasJungseong() && !top.hasChoseong()) {
                    // if there is a vowel, but no top consonant
                    //   start a new syllable
                    top = new KoreanSyllable();
                    top.add(c);
                    text.add(top);
                } else if (!top.hasChoseong() || (!top.hasJongseong() && top.hasJungseong())) {
                    // if there is no top consonant or if there is
                    //   a top consonant with a vowel and no bottom consonant
                    //   add to the current syllable
                    if (top.hasChoseong() && (c == 0x3138 || c == 0x3143 || c == 0x3149)) { // ㄸ, ㅃ, ㅉ
                        // however if there is already a top consonant,
                        //   ㄸ, ㅃ, ㅉ cannot be added as bottom consonants -
                        //   they can only be added as top consonants
                        //   so start a new syllable
                        text.add(new KoreanSyllable());
                        top = text.get(text.size() - 1);
                    }

                    top.add(c);
                } else if (top.hasJongseong()) {
                    // if there is already a bottom consonant
                    if (top.canCompose(c)) {
                        // check if you can create a compound bottom consonant
                        top.compose(c);
                    } else {
                        // if not, start a new syllable
                        top = new KoreanSyllable();
                        top.add(c);
                        text.add(top);
                    }
                } else {
                    // otherwise start a new syllable
                    top = new KoreanSyllable();
                    top.add(c);
                    text.add(top);
                }
            } else if (Arrays.binarySearch(KoreanSyllable.VOWELS, c) >= 0) {
                // if the typed character is Korean vowel
                if (!top.hasJungseong()) {
                    // if there is no vowel, add it
                    top.add(c);
                } else if (!top.hasJongseong()) {
                    // if there is no bottom consonant
                    if (top.canCompose(c)) {
                        // if possible, create a compound vowel
                        top.compose(c);
                    } else {
                        // otherwise start a new syllable
                        top = new KoreanSyllable();
                        top.add(c);
                        text.add(top);
                    }
                } else {
                    // there is a bottom consonant, remove it and use it
                    //   as the new top consonant for this vowel
                    char cons = top.delete();
                    top = new KoreanSyllable();
                    top.add(cons);
                    top.add(c);
                    text.add(top);
                }
            } else if (Arrays.binarySearch(KoreanSyllable.ENGLISH, c) >= 0 || Arrays.binarySearch(KoreanSyllable.PUNCTUATION, c) >= 0) {
                // if the typed character is not a Korean vowel or consonant
                //   then it must be an English character or punctuation
                if (!top.notKorean()) {
                    text.add(new KoreanSyllable());
                    top = text.get(text.size() - 1);
                }
                top.add(c);
            } else {
                // if it's not Korean, English, or punctuation
                //   then we just ignore it
            }
        }
    }

    public String toString() {
        String output = "";
        for (int i = 0; i < text.size(); i++) {
            output += text.get(i);
        }
        return output;
    }
}
