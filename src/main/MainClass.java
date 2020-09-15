package main;


import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainClass {
    public static void main(String[] args) {

        Window win = new Window();
        Loader loader = new Loader();

        TexturedModel treeModel = new TexturedModel(OBJLoader.loadObjModel("sfera", loader),new ModelTexture(loader.loadTexture("white")));
        TexturedModel tubeModel = new TexturedModel(OBJLoader.loadObjModel("tube", loader),new ModelTexture(loader.loadTexture("white")));
/*
        TexturedModel grassModel = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),new ModelTexture(loader.loadTexture("grassTexture")));
        grassModel.getTexture().setHasTransparency(true);
        grassModel.getTexture().setUseFakeLighting(true);

        TexturedModel fernModel = new TexturedModel(OBJLoader.loadObjModel("fern", loader),new ModelTexture(loader.loadTexture("fern")));
        fernModel.getTexture().setHasTransparency(true);
        fernModel.getTexture().setUseFakeLighting(true);
*/
        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();

        /*
        for(int i=0;i<50;i++){
            entities.add(new Entity(treeModel,(new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600)),0,0,0,15));
            entities.add(new Entity(grassModel,(new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600)),0,0,0,1));
            entities.add(new Entity(fernModel,(new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600)),0,0,0,1));
        }
         */

        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++) {
                if(i < 5){
                    entities.add(new Entity(treeModel, (new Vector3f(i * 100, 0, j * 100)), 0, 0, 0,new Vector3f(15,15,15)));
                }else{
                    entities.add(new Entity(tubeModel, (new Vector3f(i * 100, 0, j * 100)), 0, 0, 0, new Vector3f(150,1,1)));
                }

            }
        }

        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));

        TexturedModel bunnyModel = new TexturedModel(OBJLoader.loadObjModel("person/person", loader),new ModelTexture(loader.loadTexture("person/playerTexture")));
        Player player = new Player(bunnyModel,new Vector3f(110,0,-90),0,0,0,new Vector3f(1,1,1));

        Camera camera = new Camera(player);
        MasterRenderer masterRenderer = new MasterRenderer();

        while (!win.shouldClose()) {
            camera.move();
            player.move();

            masterRenderer.processEntity(player);

            for(Entity entity:entities){
                masterRenderer.processEntity(entity);
            }

            masterRenderer.render(light, camera);

            win.endDraw();
        }
        masterRenderer.cleanUp();
        loader.clearUpBuffers();
        win.destroy();
    }
}
