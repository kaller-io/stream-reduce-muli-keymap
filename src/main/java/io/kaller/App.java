package io.kaller;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
        Path path = Paths.get("/your/destination/folder/file.mp4");

        Metadata metadata = new Metadata();
        AutoDetectParser parser = new AutoDetectParser();
        getText(Files.newInputStream(path), parser, new ParseContext(), metadata);

        Map<String, String> result = Arrays.stream(metadata.names())
                .collect(Collectors.toMap(App::cleanMetaName, metadata::get, (a, b) -> a));

        result.forEach((k, v) -> System.out.println(k + " : " + v));
    }

    private static String cleanMetaName(String s) {
        return s.substring(s.indexOf(":") + 1).trim();
    }

    public static String getText(InputStream is, Parser parser, ParseContext context, Metadata metadata) throws Exception{
        ContentHandler handler = new BodyContentHandler(1000000);
        try {
            parser.parse(is, handler, metadata, context);
        } finally {
            is.close();
        }
        return handler.toString();
    }

}
