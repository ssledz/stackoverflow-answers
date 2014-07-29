package pl.softech.stackoverflow.search;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Deque;
import java.util.LinkedList;

//http://stackoverflow.com/questions/25016797/java-search-text-with-a-length-of-100-million-characters
public class SearchForPhrase {

    static int hash(String phrase) {
	int hash = 0;
	for (int i = 0; i < phrase.length(); i++) {
	    hash += phrase.codePointAt(i);
	}
	return hash;
    }

    static boolean equals(Deque<Character> txt, String phrase) {
	int i = 0;
	for(Character c : txt) {
	    if(!c.equals(phrase.charAt(i++))) {
		return false;
	    }
	}
	return true;
    }
    
    static int find(String phrase, Reader in) throws Exception {

	int phash = hash(phrase);
	int hash;
	
	BufferedReader bin = new BufferedReader(in);
	char[] buffer = new char[phrase.length()];
	
	
	int readed = bin.read(buffer);
	
	if(readed < phrase.length()) {
	    return -1;
	}
	
	String tmp = new String(buffer);
	hash = hash(tmp);
	if(hash == phash && tmp.equals(phrase)) {
	    return 0;
	}
	
	Deque<Character> queue = new LinkedList<>();
	for(char c : buffer) {
	    queue.add(c);
	}
	
	int curr;
	int index = 1;
	while((curr = bin.read()) != -1) {
	    
	    hash = hash - queue.removeFirst() + curr;
	    queue.add((char) curr);
	    
	    if(hash == phash && equals(queue, phrase)) {
		return index;
	    }
	    
	    index++;
	    
	}
	
	return -1;
	
    }

    public static void main(String[] args) throws Exception {

	StringWriter writer = new StringWriter();
	PrintWriter out = new PrintWriter(writer);
	out.println("Discuss the person's qualifications for the graduate study in the chosen field. Statements of past");
	out.println("performance, accomplishments, and contributions are helpful. The more relevant the items mentioned, andd");
	out.flush();
	
	System.out.println(find("Discuss", new StringReader(writer.toString())));
	System.out.println(find("the", new StringReader(writer.toString())));
	System.out.println(find("qualifications", new StringReader(writer.toString())));
	System.out.println(find("andd", new StringReader(writer.toString())));
	
    }

}
