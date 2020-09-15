package main;

import entities.*;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SolarParticleTest {
    public static void main(String[] args) {
        Window win = new Window();
        Loader loader = new Loader();
        TexturedModel sferaModel = new TexturedModel(OBJLoader.loadObjModel("sfera", loader),new ModelTexture(loader.loadTexture("white")));

        List<Body3d> bodyes3d = new ArrayList<Body3d>();

        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++) {
                bodyes3d.add(new Body3d(sferaModel, (new Vector3f(i * 100, 0, j * 100)), new Vector3f(15,15,15)));
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
            // physics

            // draw
            masterRenderer.processEntity(player);

            for(Body3d body3d:bodyes3d){
                masterRenderer.processBody3d(body3d);
            }
           // masterRenderer.renderBody3d(light, camera);
            masterRenderer.render(light, camera);


            win.endDraw();
        }
        masterRenderer.cleanUp();
        loader.clearUpBuffers();
        win.destroy();
    }
}
