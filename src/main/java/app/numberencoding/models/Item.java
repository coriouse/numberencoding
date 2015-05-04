package app.numberencoding.models;

import java.util.LinkedList;
import java.util.List;

import app.numberencoding.core.Encoding;
import app.numberencoding.util.Constants;
import app.numberencoding.util.Utils;

/**
 * Class representation of the prepared word
 * 
 *
 */
public class Item {
	
	private String original;
	private String digit;
	public String prefix;
	
	public Item(String item) {
		this.original = item;
		if(!Utils.cleanDigits(item).matches("\\d+")) {
			toDigit(item);
		} else {
			this.digit = Utils.cleanDigits(item);
		}
	}
					
	private void toDigit(String word) {
		word = Utils.cleanWord(word);
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<word.length();i++ ) {
			sb.append(Utils.getCharacter(word.charAt(i)));
		}
		this.digit = sb.toString();
	}

	public String getOriginal() {
		return original;
	}

	public String getDigit() {
		return digit;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((digit == null) ? 0 : digit.hashCode());
		result = prime * result
				+ ((original == null) ? 0 : original.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		
		if (digit == null) {
			if (other.digit != null)
				return false;
		} else if (!digit.equals(other.digit))
			return false;
		if (original == null) {
			if (other.original != null)
				return false;
		} else if (!original.equals(other.original))
			return false;
		return true;
	}	

	@Override
	public String toString() {
		return "Word [original=" + original + ", digit=" + digit+", prefix="+prefix+"]";
	}
}
