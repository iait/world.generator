package com.example.world.generator;

import org.jdom.Element;

public class Pose {
    
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;
    private double roll = 0.0;
    private double pitch = 0.0;
    private double yaw = 0.0;
    
    public Pose(double x, double y, double z, double roll, double pitch, double yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }
    
    public Element createElement() {
        Element pose = new Element("pose");
        pose.setAttribute("frame", "");
        pose.setText(String.format("%.2f %.2f %.2f %.2f %.2f %.2f", x, y, z, roll, pitch, yaw));
        return pose;
    }
}
