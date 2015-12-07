package app.numberencoding.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import app.numberencoding.core.NumberEncoder;

public class LoadDictionaryTest {

	private String[] WORDS_DICTIONARY = { "an", "blau", "Bo\"", "Boot", "bo\"s", "da", "Fee", "fern", "Fest", "fort",
			"je", "jemand", "mir", "Mix", "Mixer", "Name", "neu", "o\"d", "Ort", "so", "Tor", "Torf", "Wasser" };

	@Test
	public void testLoadDictionary() {
		NumberEncoder numberEncoder = new NumberEncoder();
		numberEncoder.clearDictionary();
		numberEncoder.loadDictionary(WORDS_DICTIONARY);
		assertEquals(23, numberEncoder.getWords().size());
		numberEncoder.clearResut();

		int catchs = 0;
		try {
			numberEncoder.clearDictionary();
			String[] words = null;
			numberEncoder.loadDictionary(words);
		} catch (IllegalArgumentException e) {
			catchs++;
		}
		assertTrue(catchs == 1);

	}

	@Test
	public void testLoadDictionaryFromFile() {
		File file = new File(getClass().getClassLoader().getResource("dictionary.txt").getFile());
		NumberEncoder numberEncoder = new NumberEncoder();
		numberEncoder.clearDictionary();
		numberEncoder.readDictionary(file.toPath());
		assertEquals(73113, numberEncoder.getWords().size());
	}
}
