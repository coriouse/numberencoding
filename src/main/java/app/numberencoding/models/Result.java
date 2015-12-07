package app.numberencoding.models;

import java.util.List;

/**
 * 
 * Class representation of the result
 * 
 * @author Sergey Ogarkov
 *
 */
public class Result {

	public static final String SPACE = " ";
	public static final String SEPARATOR = ": ";

	public String number;
	public String words;

	public Result(String number, String words) {
		super();
		this.number = number.trim();
		this.words = words.trim();
	}

	public Result(List<Word> encoding) {
		this.number = encoding.get(0).getOriginal();
		StringBuffer sb = new StringBuffer();
		int pos = 0;
		for (Word word : encoding) {
			if (pos > 0) {
				sb.append(word.getOriginal()).append(SPACE);
			}
			pos++;
		}
		this.words = sb.toString().trim();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((words == null) ? 0 : words.hashCode());
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
		Result other = (Result) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (words == null) {
			if (other.words != null)
				return false;
		} else if (!words.equals(other.words))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return number + SEPARATOR + words;
	}
}
