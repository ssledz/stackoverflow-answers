package pl.softech.stackoverflow.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleRegex {

    static void example1() {
	String s = "@123456@ 123456789[ 123456@ 12";
	Pattern p = Pattern.compile("@(\\d{6})@\\s?(\\d{9})\\[\\s?(\\d{6})@\\s?(\\d{2})");
	Matcher m = p.matcher(s);
	if (m.matches()) {
	    System.out.println(m.group(1));
	    System.out.println(m.group(2));
	    System.out.println(m.group(3));
	    System.out.println(m.group(4));
	}
    }

    public static void main(String[] args) {

	example1();

    }

}
