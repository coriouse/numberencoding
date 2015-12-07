package app.numberencoding.models;

import app.numberencoding.util.EncoderUtils;

/**
 * Class representation of the prepared word
 * 
 * @author Sergey Ogarkov
 *
 */
public class Word {

	private String original;
	private String digit;
	public String prefix;

	public Word(String word) {
		this.original = word;
		if (!EncoderUtils.cleanDigits(word).matches("\\d+")) {
			toDigit(word);
		} else {
			this.digit = EncoderUtils.cleanDigits(word);
		}
	}

	private void toDigit(String word) {
		word = EncoderUtils.cleanWord(word);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			sb.append(EncoderUtils.getDigit(word.charAt(i)));
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
		result = prime * result + ((original == null) ? 0 : original.hashCode());
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
		Word other = (Word) obj;

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
		return "Word [original=" + original + ", digit=" + digit + ", prefix=" + prefix + "]";
	}
}
