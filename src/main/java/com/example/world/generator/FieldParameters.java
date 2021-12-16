package com.example.world.generator;

// para una resolución de 50px por metro las medidas deberían ser múltiplos de 0.02 metros
public class FieldParameters {
    
    private int width = 10;
    private int depth = 30;
    private double ridge = 0.32;
    private double cropHeight = 0.3;
    private double furrow = 0.2;
    private int num = 32;
    private double headland = 5;
    private double border = 0.04;
    
    public FieldParameters() {
    }

    public int getWidth() {
        return width;
    }

    public FieldParameters setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getDepth() {
        return depth;
    }

    public FieldParameters setDepth(int depth) {
        this.depth = depth;
        return this;
    }

    public double getRidge() {
        return ridge;
    }

    public FieldParameters setRidge(double ridge) {
        this.ridge = ridge;
        return this;
    }
    
    public double getCropHeight() {
        return cropHeight;
    }
    
    public FieldParameters setCropHeight(double cropHeight) {
        this.cropHeight = cropHeight;
        return this;
    }

    public double getFurrow() {
        return furrow;
    }

    public FieldParameters setFurrow(double furrow) {
        this.furrow = furrow;
        return this;
    }
    
    public int getNum() {
        return num;
    }
    
    public FieldParameters setNum(int num) {
        this.num = num;
        return this;
    }
    
    public double getHeadland() {
        return headland;
    }
    
    public FieldParameters setHeadland(double headland) {
        this.headland = headland;
        return this;
    }

    public double getBorder() {
        return border;
    }

    public FieldParameters setBorder(double border) {
        this.border = border;
        return this;
    }
    
    public double getRidgeFurrow() {
        return ridge + furrow;
    }
    
    public double getSideland() {
        double crops = (num - 1.0) * getRidgeFurrow(); 
        return (width - crops) / 2.0;
    }
}
