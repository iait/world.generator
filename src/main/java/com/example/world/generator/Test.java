package com.example.world.generator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class Test {

    public static void main(String[] args) throws Exception {
        
    }
    
    /*
     * Gazebo world
     */
    
    public void boxTest() throws Exception {
        Path resourcesPath = Paths.get(this.getClass().getResource("/").getPath());
        File outputFile = new File(resourcesPath.toAbsolutePath() + "/box.model");
        
        Document document = new Document(createBox(0.32, 40, 0.3, Material.GREEN, "crop_line"));
        
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat().setLineSeparator("\n"));
        xmlOutput.output(document, new FileWriter(outputFile));
    }
    
    private Element createBox(double x, double y, double z, Material material, String name) {
        
        Element geometry = new Element("geometry")
                .addContent(new Element("box")
                        .addContent(new Element("size")
                                .setText(String.format("%.2f %.2f %.2f", x, y, z))));
        
        return new Element("model").setAttribute("name", name)
                .addContent(new Element("link").setAttribute("name", name)
                        .addContent(new Pose(0, 0, 0, 0, 0, 0).createElement())
                        .addContent(new Element("visual").setAttribute("name", "visual")
                                .addContent(new Pose(0, 0, 0, 0, 0, 0).createElement())
                                .addContent((Element) geometry.clone())
                                .addContent(new Element("cast_shadows").setText("1"))
                                .addContent(material.createElement()))
                        .addContent(new Element("collision").setAttribute("name", "collision")
                                .addContent(new Element("max_contacts").setText("10"))
                                .addContent(new Pose(0, 0, 0, 0, 0, 0).createElement())
                                .addContent((Element) geometry.clone())))
                .addContent(new Element("static").setText("1"));
    }
    
    /*
     * Map image
     */
    
    public void imageTest1() throws Exception {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        
        img.setRGB(0, 0, Color.white.getRGB());
        img.setRGB(0, 1, Color.black.getRGB());
        img.setRGB(1, 0, Color.lightGray.getRGB());
        img.setRGB(1, 1, Color.white.getRGB());
        
        Path resourcesPath = Paths.get(this.getClass().getResource("/").getPath());
        File outputFile = new File(resourcesPath.toAbsolutePath() + "/image.png");
        boolean success = ImageIO.write(img, "png", outputFile);
        if (!success) {
            throw new RuntimeException("Error al grabar el archivo");
        }
        
    }
    
    public void imageTest2() throws Exception {
        
        int resolution = 50; // 1m = 50px
        
        // in meters
        int width = 10;
        int depth = 30;
        double ridge = 0.32;
        double furrow = 0.20;
        double ridgeBorder = 0.04;
        int leftRightBorder = 2;
        int topBottomBorder = 5;
        
        // in pixels
        int widthPx = width * resolution;                     //  500px
        int depthPx = depth * resolution;                     // 1500px
        int ridgePx = (int) (ridge * resolution);             //   16px
        int furrowPx = (int) (furrow * resolution);           //   10px
        int ridgeBorderPx = (int) (ridgeBorder * resolution); //    2px
        int linePx = ridgePx + furrowPx;                      //   26px
        int leftRightBorderPx = leftRightBorder * resolution; //  100px
        int topBottomBorderPx = topBottomBorder * resolution; //  250px
        
        BufferedImage img = new BufferedImage(widthPx, depthPx, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < depthPx; y++) {
            for (int x = 0; x < widthPx; x++) {
                
                int color = Color.white.getRGB();
                
                if (y >= topBottomBorderPx && y < depthPx - topBottomBorderPx) {
                    if (x >= leftRightBorderPx && x < widthPx - leftRightBorderPx) {
                        int mod = (x - leftRightBorderPx) % linePx;
                        if (mod < ridgeBorderPx) {
                            color = Color.black.getRGB();
                        } else if (mod < ridgePx - ridgeBorderPx) {
                            if (y < ridgeBorderPx + topBottomBorderPx) {
                                color = Color.black.getRGB();
                            } else if (y < depthPx - topBottomBorderPx - ridgeBorderPx) {
                                color = Color.lightGray.getRGB();
                            } else {
                                color = Color.black.getRGB();
                            }
                        } else if (mod < ridgePx) {
                            color = Color.black.getRGB();
                        }
                    }
                }
                img.setRGB(x, y, color);
            }
        }
        
        Path resourcesPath = Paths.get(this.getClass().getResource("/").getPath());
        File outputFile = new File(resourcesPath.toAbsolutePath() + "/image.png");
        boolean success = ImageIO.write(img, "png", outputFile);
        if (!success) {
            throw new RuntimeException("Error al grabar el archivo");
        }
        
    }
}
