package app.numberencoding.core;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import app.numberencoding.models.Result;
import app.numberencoding.models.Item;
import app.numberencoding.util.Constants;
import app.numberencoding.util.Utils;

/**
 * Class core of the algorithm
 * 1)init of dictionary
 * 2)search match words for phone number  
 *
 */
public class Encoding {
	
	private final static List<Item> WORDS =  new ArrayList<Item>();
	private final static List<Result> RESULT = new ArrayList<Result>();
	
	private boolean isTesting = false;
	
	public void loadDictionary(String[] words) throws IllegalArgumentException {
		if(words == null || words.length == 0) {
			throw new IllegalArgumentException("Can't be null or empty");
		}
		for(String word : words) {
			addItem(word);
		}
	}
	
	private void loadDictionary(List<String> words)  throws IllegalArgumentException {
		if(words == null || words.size() == 0) {
			throw new IllegalArgumentException("Can't be null or empty");
		}
		for(String word : words) {
			addItem(word);
		}	
	}
	
	private void addItem(String word) {
		WORDS.add(new Item(word));
	}
	
	public List<Result> getResults() {
		return RESULT;
	}
	
	public List<Item> getItems() {
		return WORDS;
	}

	public void readDictionary(Path path) {
		try {
			loadDictionary(Files.readAllLines(
					path, 
					Charset.defaultCharset()));
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	private List<Item> variants(String prefix) {
		List<Item> words = new ArrayList<Item>();
		for(Item s : getItems()) {
			int index = prefix.indexOf(s.getDigit());
			if((index == 0)) 
				words.add(s);
		}
		return words;
	}
	
	
	private List<Item> initWordsList(String number) {
		List<Item> itemsFirst = new ArrayList<Item>();
		List<Item> itemsSecond = new ArrayList<Item>();
		boolean isFirst = false;
		for(Item s : getItems()) {
			int index = number.indexOf(s.getDigit());
			if(index == 0) { 
				itemsFirst.add(s);
				isFirst = true;
			} else if(index == 1) {
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
	 * @param phone number
	 */
	public void encode(String number) {
		if(number == null || number.length() == 0) {
			throw new IllegalArgumentException("Can't be null or empty");
		}
		String originalNumber = number;
		number = Utils.cleanDigits(number);
		
		for(Item word :  initWordsList(number)) {
			
			word.prefix = "";
			List<Item> result = new LinkedList<Item>();
			boolean isDigitFirst = false;
			result.add(new Item(originalNumber));
			if(number.indexOf(word.getDigit()) == 1) {
				Item digit = new Item(String.valueOf(number.charAt(0)));
				result.add(digit);
				digit.prefix = "";				
				isDigitFirst = true;
				find(digit, result, 0);
				if(isDigitFirst) break;
				
			} else {
				
				if(number.length() == word.getDigit().length()) {
					result.add(word);
					report(new Result(result));
					result.remove(result.size()-1);
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
	 * @param parentItem = prefix of the item
	 * @param result - save result for reporting
	 * @param doubleDigitCounter = check if tow digit in row
	 */
	private void find(Item parentItem, List<Item> result, Integer doubleDigitCounter) {
		String tail = null;
		if(parentItem.prefix.length() == 0) {
			tail = result.get(0).getDigit().substring(parentItem.getDigit().length(),result.get(0).getDigit().length());
		} else {				
			tail = result.get(0).getDigit().substring((parentItem.prefix.length()+parentItem.getDigit().length()), result.get(0).getDigit().length());
		}
			
		List<Item> list = variants(tail); //Search words for next 
			if(list.size() > 0) {
				for(Item itm : list) {
					Item item =  new Item(itm.getOriginal());
					item.prefix = parentItem.prefix+parentItem.getDigit();
					if(result.get(0).getDigit().length() ==  (item.prefix.length()+item.getDigit().length())) {
						result.add(item);
						report(new Result(result));
						result.remove(result.size()-1);	
					} else {
						if(doubleDigitCounter == 1){
							doubleDigitCounter = 0;
						}	
						result.add(item);
						find(item, result, doubleDigitCounter);
					}
				} 
				result.remove(result.size()-1);
			} else { 
				 //if digit one
				if(tail.length() == 1) { // if it the last digit
					if(doubleDigitCounter == 0) { //if it the first digit
						if(parentItem.prefix.length() == 0) {
							result.add(new Item(String.valueOf(tail.charAt(0))));
							report(new Result(result));
					
						} else {
							if((result.get(0).getDigit().length() == (parentItem.prefix.length()+parentItem.getDigit().length()+1))) {
								result.add(new Item(String.valueOf(tail.charAt(0))));
								report(new Result(result));
								result.remove(result.size()-1);
							}
						}
					}
					result.remove(result.size()-1);
				} else { //if symbol is not last
					doubleDigitCounter++;
					if(doubleDigitCounter == 1) { //
						Item word = new Item(String.valueOf(tail.charAt(0)));
						result.add(word);	
						word.prefix = parentItem.prefix+parentItem.getDigit();
						find(word, result, doubleDigitCounter);
					}
					result.remove(result.size()-1);
				}
			}
		}
	
	/**
	 * print result
	 * 
	 * @param report
	 */
	private void report(Result report) {
		if(isTesting()) {
			addResult(report);
		} else {
			System.out.println(report);	
		}
	}	
}
