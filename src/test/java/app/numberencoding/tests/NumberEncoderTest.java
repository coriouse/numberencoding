package app.numberencoding.tests;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Result;

import app.numberencoding.core.NumberEncoder;

public class NumberEncoderTest {

	private String[] WORDS_DICTIONARY = { "an", "blau", "Bo\"", "Boot", "bo\"s", "da", "Fee", "fern", "Fest", "fort",
			"je", "jemand", "mir", "Mix", "Mixer", "Name", "neu", "o\"d", "Ort", "so", "Tor", "Torf", "Wasser" };

	@Test
	public void testFindWordsByNumber() {

		NumberEncoder encoding = new NumberEncoder();
		encoding.clearDictionary();
		encoding.loadDictionary(WORDS_DICTIONARY);
		encoding.enableTesting();

		encoding.encode("5624-82");
		assertEquals(2, encoding.getResults().size());
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("5624-82", "mir Tor")));
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("5624-82", "Mix Tor")));
		encoding.clearResut();

		encoding.encode("4824");
		assertEquals(3, encoding.getResults().size());
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("4824", "Torf")));
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("4824", "fort")));
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("4824", "Tor 4")));
		encoding.clearResut();

		encoding.encode("10/783--5");
		assertEquals(3, encoding.getResults().size());
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("10/783--5", "neu o\"d 5")));
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("10/783--5", "je bo\"s 5")));
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("10/783--5", "je Bo\" da")));
		encoding.clearResut();

		encoding.encode("381482");
		assertEquals(1, encoding.getResults().size());
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("381482", "so 1 Tor")));
		encoding.clearResut();

		encoding.encode("04824");
		assertEquals(3, encoding.getResults().size());
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("04824", "0 Torf")));
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("04824", "0 fort")));
		assertTrue(encoding.getResults().contains(new app.numberencoding.models.Result("04824", "0 Tor 4")));
		encoding.clearResut();

		encoding.encode("078-913-5");
		assertEquals(0, encoding.getResults().size());
		encoding.clearResut();

		encoding.encode("0721/608-4067");
		assertEquals(0, encoding.getResults().size());
		encoding.clearResut();

		int catchs = 0;
		try {
			encoding.encode("");
		} catch (IllegalArgumentException e) {
			catchs++;
		}
		assertTrue(catchs == 1);

		catchs = 0;
		try {
			encoding.encode(null);
		} catch (IllegalArgumentException e) {
			catchs++;
		}
		assertTrue(catchs == 1);
	}

}
