package app.numberencoding.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.numberencoding.models.Word;
/**
 * 
 * Util class
 * 
 * @author Sergey Ogarkov
 *
 */
public class EncoderUtils {

	private static final String DASHE = "-";
	private static final String DOUBLE_QUOTE = "\"";
	private static final String EMPTY = "";
	private static final String SLASH = "/";

	/*
	 * E | J N Q | R W X | D S Y | F T | A M | C I V | B K U | L O P | G H Z e |
	 * j n q | r w x | d s y | f t | a m | c i v | b k u | l o p | g h z 0 | 1 |
	 * 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
	 */
	private final static Map<Character, Integer> DICTIONARY = new HashMap<Character, Integer>() {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;

		{

			put('E', 0);
			put('e', 0);
			put('J', 1);
			put('j', 1);
			put('N', 1);
			put('n', 1);
			put('Q', 1);
			put('q', 1);
			put('R', 2);
			put('r', 2);
			put('W', 2);
			put('w', 2);
			put('X', 2);
			put('x', 2);
			put('D', 3);
			put('d', 3);
			put('S', 3);
			put('s', 3);
			put('Y', 3);
			put('y', 3);
			put('F', 4);
			put('f', 4);
			put('T', 4);
			put('t', 4);
			put('A', 5);
			put('a', 5);
			put('M', 5);
			put('m', 5);
			put('C', 6);
			put('c', 6);
			put('I', 6);
			put('i', 6);
			put('V', 6);
			put('v', 6);
			put('B', 7);
			put('b', 7);
			put('K', 7);
			put('k', 7);
			put('U', 7);
			put('u', 7);
			put('L', 8);
			put('l', 8);
			put('O', 8);
			put('o', 8);
			put('P', 8);
			put('p', 8);
			put('G', 9);
			put('g', 9);
			put('H', 9);
			put('h', 9);
			put('Z', 9);
			put('z', 9);
		}
	};

	public static Integer getDigit(Character character) {
		return DICTIONARY.get(character);
	}

	public static void compareNumberAndWord(String number, List<Word> result) {
		String words = cleanWords(result);
		for (int i = 0; i < number.length(); i++) {
			Integer n = Integer.parseInt(String.valueOf(number.charAt(i)));
			System.out.println(number.charAt(i) + " = "
					+ (DICTIONARY.containsKey(words.charAt(i)) ? DICTIONARY.get(words.charAt(i)) : n) + " "
					+ words.charAt(i) + " " + " "
					+ (n == (DICTIONARY.containsKey(words.charAt(i)) ? DICTIONARY.get(words.charAt(i)) : n)));
		}
	}

	private static String cleanWords(List<Word> result) {
		int pos = 0;
		StringBuffer sb = new StringBuffer();
		for (Word word : result) {
			if (pos > 0) {
				sb.append(EncoderUtils.cleanWord(word.getOriginal()));
			}
			pos++;
		}
		return sb.toString();
	}

	public static String cleanDigits(String number) {
		return number.replace(SLASH, EMPTY).replace(DASHE, EMPTY);
	}

	public static String cleanWord(String word) {
		return word.replace(DASHE, EMPTY).replace(DOUBLE_QUOTE, EMPTY);
	}

}
