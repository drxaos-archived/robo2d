package robo2d.game.levels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import straightedge.geom.KPoint;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static MapDesc parseXml(String filePath, float scale) {
        MapDesc mapDesc = new MapDesc();
        try {
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("area");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String name = eElement.getAttribute("title");
                    String type = eElement.getAttribute("shape");
                    String coords = eElement.getAttribute("coords");

                    if (type.equals("poly")) {
                        ArrayList<Point2D> points = new ArrayList<Point2D>();
                        String[] split = coords.split(",");
                        for (int i = 0; i < split.length; i += 2) {
                            float x = Float.parseFloat(split[i].trim());
                            float y = Float.parseFloat(split[i + 1].trim());
                            points.add(new Point2D.Float(x * scale, -y * scale));
                        }
                        if (mapDesc.polygons.containsKey(name)) {
                            System.out.println("Duplicate " + type + ": " + name);
                        }
                        mapDesc.polygons.put(name, new MapPolygon(points));
                    } else if (type.equals("vector")) {
                        String[] split = coords.split(",");
                        float x = Float.parseFloat(split[0].trim());
                        float y = Float.parseFloat(split[1].trim());
                        float x2 = Float.parseFloat(split[2].trim());
                        float y2 = Float.parseFloat(split[3].trim());
                        double angle = KPoint.findAngle(x, -y, x2, -y2);
                        if (mapDesc.objects.containsKey(name)) {
                            System.out.println("Duplicate " + type + ": " + name);
                        }
                        mapDesc.objects.put(name, new MapObject(x * scale, -y * scale, (float) angle));
                    } else {
                        System.out.println("Unknown type: " + type);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapDesc;
    }

}
