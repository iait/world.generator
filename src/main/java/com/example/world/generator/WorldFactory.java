package com.example.world.generator;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class WorldFactory {
    
    public static void main(String[] args) throws Exception {
        
        System.out.println("Creando world de campo");
        WorldFactory wf = new WorldFactory();
        FieldParameters p = new FieldParameters()
                .setWidth(20)
                .setDepth(30)
                .setRidge(0.24)
                .setCropHeight(0.3)
                .setFurrow(0.28)
                .setNum(38)
                .setHeadland(5);
        wf.createFieldWorld("simple_field", p);
        System.out.println("World creado!");
    }
    
    private void createFieldWorld(String name, FieldParameters p) throws Exception {
        
        Path resourcesPath = Paths.get(this.getClass().getResource("/").getPath());
        
        File emptyWorldFile = new File(resourcesPath.toAbsolutePath() + "/empty.world");
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(emptyWorldFile);
        
        Element sdf = document.getRootElement();
        Element world = sdf.getChild("world").setAttribute("name", name);
        world.getChild("state").setAttribute("world_name", name);
        
        // agrega el suelo
        File groundModelFile = new File(resourcesPath.toAbsolutePath() + "/ground.model");
        Element groundModel = 
                (Element) saxBuilder.build(groundModelFile).getRootElement().detach();
        Element groundVisual = groundModel.getChild("link").getChild("visual");
        groundVisual.addContent(Material.GROUND.createElement());
        groundVisual.getChild("geometry").getChild("plane")
                .addContent(new Element("size").setText(String.format(
                        "%d %d", p.getWidth(), p.getDepth())));
        Element groundCollision = groundModel.getChild("link").getChild("collision");
        groundCollision.getChild("geometry").getChild("plane")
                .addContent(new Element("size").setText(String.format(
                        "%d %d", p.getWidth(), p.getDepth())));
        addModel(world, groundModel, new Pose(0, 0, 0, 0, 0, 0));
        
        // modelo para línea de cultivo
        Element cropLineModel = createBox(
                p.getRidge(), 
                p.getDepth() - 2 * p.getHeadland(), 
                p.getCropHeight(), 
                Material.GREEN, "crop_line", false);
        
        // agrega las líneas de cultivos
        double start = - p.getWidth() / 2.0 + p.getSideland();
        if (-start * 2 > p.getWidth()) {
            throw new RuntimeException("Demasiadas filas de cultivos");
        }
        int count = 1;
        while (count <= p.getNum()) {
            Element crop = (Element) cropLineModel.clone();
            crop.setAttribute("name", "crop" + count);
            addModel(world, crop, new Pose(start, 0, p.getCropHeight() / 2.0, 0, 0, 0));
            count++;
            start += p.getRidgeFurrow();
        }
        
        File outputFile = new File(resourcesPath.toAbsolutePath() + "/field.world");
        
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat().setLineSeparator("\n"));
        xmlOutput.output(document, new FileWriter(outputFile));
        
    }
    
    private void addModel(Element world, Element model, Pose pose) {
        
        world.addContent(model);
        
        Element modelState = new Element("model")
                .setAttribute("name", model.getAttributeValue("name"))
                .addContent(pose.createElement());
        
        world.getChild("state").addContent(modelState);
        
    }
    
    private Element createBox(
            double x, double y, double z, Material material, String name, boolean collide) {
        
        Element geometry = new Element("geometry")
                .addContent(new Element("box")
                        .addContent(new Element("size")
                                .setText(String.format("%.2f %.2f %.2f", x, y, z))));
        Element surface = new Element("surface")
                .addContent(new Element("contact")
                        .addContent(new Element("collide_without_contact")
                                .setText(!collide + "")));
        
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
                                .addContent((Element) geometry.clone())
                                .addContent(surface)))
                .addContent(new Element("static").setText("1"));
    }

}
