package ddwucom.mobile.finalproject.ma01_20181033;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class MovieXmlParser {

    public enum TagType { NONE, TITLE, DIRECTOR, ACTOR, IMAGE };

    final static String TAG_ITEM = "item";
    final static String TAG_TITLE = "title";
    final static String TAG_DIRECTOR = "director";
    final static String TAG_ACTOR = "actor";
    final static String TAG_IMAGE = "image";

    public MovieXmlParser() { }

    public ArrayList<MovieDto> parse(String xml) {

        ArrayList<MovieDto> resultList = new ArrayList<MovieDto>();
        MovieDto dto = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals(TAG_ITEM)) {
                            dto = new MovieDto();
                        } else if(parser.getName().equals(TAG_TITLE)) {
                            if (dto != null) tagType = TagType.TITLE;
                        } else if(parser.getName().equals(TAG_DIRECTOR)) {
                            if (dto != null) tagType = TagType.DIRECTOR;
                        } else if(parser.getName().equals(TAG_ACTOR)) {
                            if (dto != null) tagType = TagType.ACTOR;
                        } else if(parser.getName().equals(TAG_IMAGE)) {
                            if (dto != null) tagType = TagType.IMAGE;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals(TAG_ITEM)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case TITLE:
                                dto.setTitle(parser.getText());
                                break;
                            case DIRECTOR:
                                dto.setDirector(parser.getText());
                                break;
                            case ACTOR:
                                dto.setActor(parser.getText());
                                break;
                            case IMAGE:
                                dto.setImageLink(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
