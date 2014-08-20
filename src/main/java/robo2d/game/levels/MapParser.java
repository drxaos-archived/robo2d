package robo2d.game.levels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapParser {
    public static class MapDesc {
        public Map<String, MapObject> objects = new HashMap<String, MapObject>();
        public Map<String, MapPolygon> polygons = new HashMap<String, MapPolygon>();
    }

    public static class MapObject {
        public float x, y, r;

        MapObject(float x, float y, float r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }
    }

    public static class MapPolygon {
        public ArrayList<Point2D> points = new ArrayList<Point2D>();

        MapPolygon(ArrayList<Point2D> points) {
            this.points = points;
        }
    }

    public static MapDesc parseFodg(String filePath, float scale) {
        MapDesc mapDesc = new MapDesc();
        try {
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("draw:custom-shape");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String name;
                    float r = 0, x = 0, y = 0, w = 0, h = 0;

                    {
                        name = eElement.getAttribute("draw:name");
                    }
                    {
                        String t = eElement.getAttribute("draw:transform");
                        Pattern pattern = Pattern.compile("rotate \\(([0-9.-]+)\\) translate \\(([0-9.-]+)cm ([0-9.-]+)cm\\)");
                        Matcher matcher = pattern.matcher(t);
                        if (matcher.find()) {
                            r = Float.parseFloat(matcher.group(1));
                            x = Float.parseFloat(matcher.group(2)) * 1000;
                            y = Float.parseFloat(matcher.group(3)) * 1000;
                        }
                    }
                    {
                        String wStr = eElement.getAttribute("svg:width");
                        Pattern pattern = Pattern.compile("([0-9.-]+)cm");
                        Matcher matcher = pattern.matcher(wStr);
                        if (matcher.find()) {
                            w = Float.parseFloat(matcher.group(1)) * 1000;
                        }
                    }
                    {
                        String hStr = eElement.getAttribute("svg:height");
                        Pattern pattern = Pattern.compile("([0-9.-]+)cm");
                        Matcher matcher = pattern.matcher(hStr);
                        if (matcher.find()) {
                            h = Float.parseFloat(matcher.group(1)) * 1000;
                        }
                    }

                    mapDesc.objects.put(name, new MapObject((x + w / 2) * scale, -(y + h / 2) * scale, r));
                }
            }
            nList = doc.getElementsByTagName("draw:polygon");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String name;
                    float x = 0, y = 0;
                    ArrayList<Point2D> points = new ArrayList<Point2D>();

                    {
                        name = eElement.getAttribute("draw:name");
                    }
                    {
                        String xStr = eElement.getAttribute("svg:x");
                        Pattern pattern = Pattern.compile("([0-9.-]+)cm");
                        Matcher matcher = pattern.matcher(xStr);
                        if (matcher.find()) {
                            x = Float.parseFloat(matcher.group(1)) * 1000;
                        }
                    }
                    {
                        String yStr = eElement.getAttribute("svg:y");
                        Pattern pattern = Pattern.compile("([0-9.-]+)cm");
                        Matcher matcher = pattern.matcher(yStr);
                        if (matcher.find()) {
                            y = Float.parseFloat(matcher.group(1)) * 1000;
                        }
                    }
                    {
                        String pStr = eElement.getAttribute("draw:points");
                        Pattern pattern = Pattern.compile("([0-9-]+,[0-9-]+)");
                        Matcher matcher = pattern.matcher(pStr);
                        while (matcher.find()) {
                            String[] split = matcher.group().split(",");
                            points.add(new Point2D.Float((x + Float.parseFloat(split[0])) * scale, -(y + Float.parseFloat(split[1])) * scale));
                        }
                    }

                    mapDesc.polygons.put(name, new MapPolygon(points));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapDesc;
    }

}
