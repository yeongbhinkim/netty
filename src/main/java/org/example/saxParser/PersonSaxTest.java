package org.example.saxParser;

import java.io.File;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class PersonSaxTest {
    public static void main(String[] args) {
        //file 대신, api 키로 만들어진 uri 주소를 넣어도 가능하다.
        File file = new File("src/main/java/org/example/saxParser/people.xml");
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = factory.newSAXParser();
            PeopleSaxHandler handler = new PeopleSaxHandler();
            parser.parse(file, handler);

            List<Person> list = handler.getPersonList();

            for(Person p:list) {
                System.out.println(p);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
