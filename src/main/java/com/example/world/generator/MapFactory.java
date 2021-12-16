package com.example.world.generator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class MapFactory {

    public static void main(String[] args) throws Exception {
        
        System.out.println("Creando mapa del campo");
        MapFactory mf = new MapFactory();
        FieldParameters p = new FieldParameters()
                .setWidth(40)
                .setDepth(20)
                .setRidge(0.04)
                .setCropHeight(0.3)
                .setFurrow(0.48)
                .setNum(32)
                .setHeadland(4)
                .setBorder(0.04);
        mf.createFieldMap(p);
        System.out.println("Mapa creado!");
    }
    
    private void createFieldMap(FieldParameters p) throws Exception {
        
        int resolution = 25; // 1m = 25px
        
        // in pixels
        int widthPx = p.getWidth() * resolution;
        int depthPx = p.getDepth() * resolution;
        int ridgePx = (int) (p.getRidge() * resolution);
        int furrowPx = (int) (p.getFurrow() * resolution);
        int ridgeBorderPx = (int) (p.getBorder() * resolution);
        int linePx = ridgePx + furrowPx;
        int sidelandPx = (int) (p.getSideland() * resolution);
        int headlandPx = (int) (p.getHeadland() * resolution);
        
        System.out.println(String.format(
                "Map size: %dpx x %dpx\n" + 
                "Ridge: %dpx, Furrow: %dpx\n" +
                "Ridge border: %dpx\n" +
                "Sideland: %dpx, Headland: %dpx", 
                widthPx, depthPx, ridgePx, furrowPx, ridgeBorderPx, sidelandPx, headlandPx));
        
        BufferedImage img = new BufferedImage(widthPx, depthPx, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < depthPx; y++) {
            for (int x = 0; x < widthPx; x++) {
                
                int color = Color.white.getRGB();
                
                if (y >= headlandPx && y < depthPx - headlandPx) {
                    if (x >= sidelandPx && x < widthPx - sidelandPx) {
                        int mod = (x - sidelandPx) % linePx;
                        if (mod < ridgeBorderPx) {
                            color = Color.black.getRGB();
                        } else if (mod < ridgePx - ridgeBorderPx) {
                            if (y < ridgeBorderPx + headlandPx) {
                                color = Color.black.getRGB();
                            } else if (y < depthPx - headlandPx - ridgeBorderPx) {
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
        File outputFile = new File(resourcesPath.toAbsolutePath() + "/field.png");
        boolean success = ImageIO.write(img, "png", outputFile);
        if (!success) {
            throw new RuntimeException("Error al grabar el archivo");
        }
        
    }

}

