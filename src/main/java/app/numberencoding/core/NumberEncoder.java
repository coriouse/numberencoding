package app.numberencoding.core;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import app.numberencoding.models.Result;
import app.numberencoding.models.Word;
import app.numberencoding.util.EncoderUtils;

/**
 * Class core of the algorithm 1)init of dictionary 2)search match words for
 * phone number
 * 
 * @author Sergey Ogarkov
 *
 */
public class NumberEncoder {

	private final static List<Word> WORDS = new ArrayList<Word>();
	private final static List<Result> RESULT = new ArrayList<Result>();

	private boolean isTesting = false;

	public void loadDictionary(String[] words) throws IllegalArgumentException {
		if (words == null || words.length == 0) {
			throw new IllegalArgumentException("Can't be null or empty");
		}
		for (String word : words) {
			addWord(word);
		}
	}

	private void loadDictionary(List<String> words) throws IllegalArgumentException {
		if (words == null || words.size() == 0) {
			throw new IllegalArgumentException("Can't be null or empty");
		}
		for (String word : words) {
			addWord(word);
		}
	}

	private void addWord(String word) {
		WORDS.add(new Word(word));
	}

	public List<Result> getResults() {
		return RESULT;
	}

	

	public void readDictionary(Path path) {
		try {
			loadDictionary(Files.readAllLines(path, Charset.defaultCharset()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Word> variants(String prefix) {
		List<Word> words = new ArrayList<Word>();
		for (Word s : getWords()) {
			int index = prefix.indexOf(s.getDigit());
			if ((index == 0))
				words.add(s);
		}
		return words;
	}

	public List<Word> getWords() {
		return WORDS;
	}
	
	private List<Word> initWordsList(String number) {
		List<Word> itemsFirst = new ArrayList<Word>();
		List<Word> itemsSecond = new ArrayList<Word>();
		boolean isFirst = false;
		for (Word s : getWords()) {
			int index = number.indexOf(s.getDigit());
			if (index == 0) {
				itemsFirst.add(s);
				isFirst = true;
			} else if (index == 1) {
				itemsSecond.add(s);
			}
		}
		return isFirst ? itemsFirst : itemsSecond;
	}

	private void addResult(Result result) {
		RESULT.add(result);
	}

	public void enableTesting() {
		isTesting = true;
	}

	private boolean isTesting() {
		return isTesting;
	}

	public void clearResut() {
		RESULT.clear();
	}

	public void clearDictionary() {
		WORDS.clear();
	}

	/**
	 * Method search match from dictionary for number
	 * 
	 * @param phone
	 *            number
	 */
	public void encode(String number) {
		if (number == null || number.length() == 0) {
			throw new IllegalArgumentException("Can't be null or empty");
		}
		String originalNumber = number;
		number = EncoderUtils.cleanDigits(number);
		for (Word word : initWordsList(number)) {

			word.prefix = "";
			List<Word> result = new LinkedList<Word>();
			boolean isDigitFirst = false;
			result.add(new Word(originalNumber));
			if (number.indexOf(word.getDigit()) == 1) {
				Word digit = new Word(String.valueOf(number.charAt(0)));
				result.add(digit);
				digit.prefix = "";
				isDigitFirst = true;
				find(digit, result, 0);
				if (isDigitFirst)
					break;

			} else {

				if (number.length() == word.getDigit().length()) {
					result.add(word);
					report(new Result(result));
					result.remove(result.size() - 1);
				} else {
					result.add(word);
					find(word, result, 0);
				}
			}
		}
	}

	/**
	 * Recursive algorithm of the matching words for phone number
	 * 
	 * @param parentItem
	 *            = prefix of the item
	 * @param result
	 *            - save result for reporting
	 * @param doubleDigitCounter
	 *            = check if tow digit in row
	 */
	private void find(Word parentItem, List<Word> result, Integer doubleDigitCounter) {
		String tail = null;
		if (parentItem.prefix.length() == 0) {
			tail = result.get(0).getDigit().substring(parentItem.getDigit().length(),
					result.get(0).getDigit().length());
		} else {
			tail = result.get(0).getDigit().substring((parentItem.prefix.length() + parentItem.getDigit().length()),
					result.get(0).getDigit().length());
		}

		List<Word> list = variants(tail); // Search words for next
		if (list.size() > 0) {
			for (Word itm : list) {
				Word item = new Word(itm.getOriginal());
				item.prefix = parentItem.prefix + parentItem.getDigit();
				if (result.get(0).getDigit().length() == (item.prefix.length() + item.getDigit().length())) {
					result.add(item);
					report(new Result(result));
					result.remove(result.size() - 1);
				} else {
					if (doubleDigitCounter == 1) {
						doubleDigitCounter = 0;
					}
					result.add(item);
					find(item, result, doubleDigitCounter);
				}
			}
			result.remove(result.size() - 1);
		} else {
			// if digit one
			if (tail.length() == 1) { // if it the last digit
				if (doubleDigitCounter == 0) { // if it the first digit
					if (parentItem.prefix.length() == 0) {
						result.add(new Word(String.valueOf(tail.charAt(0))));
						report(new Result(result));

					} else {
						if ((result.get(0).getDigit()
								.length() == (parentItem.prefix.length() + parentItem.getDigit().length() + 1))) {
							result.add(new Word(String.valueOf(tail.charAt(0))));
							report(new Result(result));
							result.remove(result.size() - 1);
						}
					}
				}
				result.remove(result.size() - 1);
			} else { // if symbol is not last
				doubleDigitCounter++;
				if (doubleDigitCounter == 1) { //
					Word word = new Word(String.valueOf(tail.charAt(0)));
					result.add(word);
					word.prefix = parentItem.prefix + parentItem.getDigit();
					find(word, result, doubleDigitCounter);
				}
				result.remove(result.size() - 1);
			}
		}
	}

	/**
	 * print result
	 * 
	 * @param report
	 */
	private void report(Result report) {
		if (isTesting()) {
			addResult(report);
		} else {
			System.out.println(report);
		}
	}
}
