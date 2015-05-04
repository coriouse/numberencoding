package app.numberencoding.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import app.numberencoding.core.Encoding;

public class LoadDictionaryTest {
	
	
	private String[] WORDS_DICTIONARY = {
		"an",
		"blau",
		"Bo\"",
		"Boot",
		"bo\"s",
		"da",
		"Fee",
		"fern",
		"Fest",
		"fort",
		"je",
		"jemand",
		"mir",
		"Mix",
		"Mixer",
		"Name",
		"neu",
		"o\"d",
		"Ort", 
		"so",
		"Tor",
		"Torf",
		"Wasser"
};

	@Test
	public void testLoadDictionary() {
		Encoding encoding = new Encoding();
		encoding.clearDictionary();
		encoding.loadDictionary(WORDS_DICTIONARY);
		assertEquals(23, encoding.getItems().size());
		encoding.clearResut();
		
		int catchs = 0;
		try {
			encoding.clearDictionary();
			String[] words = null;
			encoding.loadDictionary(words);
		} catch(IllegalArgumentException e) {
			catchs++;
		}
		assertTrue(catchs == 1);
		
	} 
	
	@Test
	public void testLoadDictionaryFromFile() {
		File file = new File(getClass().getClassLoader().getResource("dictionary.txt").getFile());
		Encoding encoding = new Encoding();
		encoding.clearDictionary();
		encoding.readDictionary(file.toPath());
		assertEquals(73113, encoding.getItems().size());
	}
}
