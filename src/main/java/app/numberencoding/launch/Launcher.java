package app.numberencoding.launch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import java.nio.file.Path;

import app.numberencoding.core.NumberEncoder;

/**
 * Class entry point in programm This class get params for working of the
 * numberencoding
 * 
 * @author Sergey Ogarkov
 */
public class Launcher {

	public static void main(String[] args) {
		Path input = null;
		Path dictionary = null;

		/*
		 * args = new String[2]; args[0] = "input=c:\\temp\\input.txt"; args[1]
		 * = "dictionary=c:\\temp\\dictionary.txt";
		 */

		if (args.length == 0) {
			System.out.println(
					"Example : java -jar numberencoding.jar input=<path>/input.txt dictionary=<path>/dictionary.txt");
			System.exit(1);
		}

		for (String param : args) {
			String[] p = param.split("=");
			switch (p[0]) {
			case "input":
				input = new File(p[1]).toPath();
				break;
			case "dictionary":
				dictionary = new File(p[1]).toPath();
				break;
			default:
				System.out.println(
						"Example : java -jar numberencoding.jar input=<path>/input.txt dictionary=<path>/dictionary.txt");
				System.exit(1);
			}
		}

		NumberEncoder numberEncoder = new NumberEncoder();
		numberEncoder.readDictionary(dictionary);
		try {
			BufferedReader reader = Files.newBufferedReader(input, Charset.defaultCharset());
			String number = null;
			while ((number = reader.readLine()) != null) {
				numberEncoder.encode(number);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
