package renderEngine;

import entities.Body3d;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;
import window.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
    private Matrix4f projectionMatrix;

    private StaticShader entityShader;
    private EntityRenderer entityRenderer;
    private Body3dRenderer body3dRenderer;

    private TerrainShader terrainShader;
    private TerrainRenderer terrainRenderer;

    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
    private Map<TexturedModel, List<Body3d>> bodies3d = new HashMap<TexturedModel,List<Body3d>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();

    public MasterRenderer(){
        enableCulling();

        init();
    }

    public static void enableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    private void init(){
        createProjectionMatrix();

        terrainShader = new TerrainShader();
        entityShader = new StaticShader();

        entityRenderer = new EntityRenderer(entityShader,projectionMatrix);

        terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);

        body3dRenderer = new Body3dRenderer(entityShader,projectionMatrix);
    }

    public void render(Light sun, Camera camera){
        prepare();

        terrainShader.start();
        terrainShader.loadLight(sun);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();

        entityShader.start();
        entityShader.loadLight(sun);
        entityShader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        entityShader.stop();
        entities.clear();

        entityShader.start();
        entityShader.loadLight(sun);
        entityShader.loadViewMatrix(camera);
        body3dRenderer.render(bodies3d);
        entityShader.stop();
        entities.clear();
    }

    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if(batch!=null){
            batch.add(entity);
        }else{
           List<Entity> newBatch = new ArrayList<Entity>();
           newBatch.add(entity);
           entities.put(entityModel,newBatch);
        }
    }

    public void processBody3d(Body3d body3d){
        TexturedModel body3dModel = body3d.getModel();
        List<Body3d> batch = bodies3d.get(body3dModel);
        if(batch!=null){
            batch.add(body3d);
        }else{
            List<Body3d> newBatch = new ArrayList<Body3d>();
            newBatch.add(body3d);
            bodies3d.put(body3dModel,newBatch);
        }
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void cleanUp(){
        entityShader.cleanUp();
        terrainShader.cleanUp();
    }

    private void prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0,0,0,1);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Window.getWIDTH() / (float) Window.getHEIGHT();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
}
