package com.example.world.generator;

import java.awt.Color;

import org.jdom.Element;

public class Material {
    
    public static final Material GROUND = new Material(102, 74, 53, 102, 74, 53);
    public static final Material GREEN = new Material(33, 153, 84);
    public static final Material BLACK = new Material(25, 25, 25);
    public static final Material BLUE = new Material(10, 51, 102);
    public static final Material RED = new Material(158, 21, 21);
    
    private Color ambientColor;
    private Color diffuseColor;
    
    public Material(int r, int g, int b) {
        this.ambientColor = new Color(r, g, b);
        this.diffuseColor = ambientColor.brighter();
    }
    
    public Material(int ar, int ag, int ab, int dr, int dg, int db) {
        this.ambientColor = new Color(ar, ag, ab);
        this.diffuseColor = new Color(dr, dg, db);
    }
    
    public Element createElement() {
        Element material = new Element("material");
        
        Element ambient = new Element("ambient");
        float[] ambientComps = ambientColor.getComponents(new float[4]);
        ambient.setText(String.format("%.2f %.2f %.2f %.2f", 
                ambientComps[0], ambientComps[1], ambientComps[2], ambientComps[3]));
        material.addContent(ambient);
        
        Element diffuse = new Element("diffuse");
        float[] diffuseComps = diffuseColor.getComponents(new float[4]);
        diffuse.setText(String.format("%.2f %.2f %.2f %.2f", 
                diffuseComps[0], diffuseComps[1], diffuseComps[2], diffuseComps[3]));
        material.addContent(diffuse);
        
        Element specular = new Element("specular");
        specular.setText("0.01 0.01 0.01 1");
        material.addContent(specular);
        
        Element emissive = new Element("emissive");
        emissive.setText("0 0 0 1");
        material.addContent(emissive);
        
        return material;
    }

}
