package renderEngine;

import entities.Body3d;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

public class Body3dRenderer {
    private StaticShader shader;

    public Body3dRenderer(StaticShader shader, Matrix4f projectionMatrix)
    {
        this.shader = shader;

        this.shader.start();
        this.shader.loadProjectionMatrix(projectionMatrix);
        this.shader.stop();
    }

    public void render(Map<TexturedModel, List<Body3d>> bodies3d)
    {
        for(TexturedModel model:bodies3d.keySet()){

            preparaTexturedModel(model);
            List<Body3d> batch = bodies3d.get(model);

            for(Body3d body3d: batch){
                prepareInstance(body3d);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private void preparaTexturedModel(TexturedModel model){
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture = model.getTexture();
        if(texture.isHasTransparency()){
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLightVariable(texture.isUseFakeLighting());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D,model.getTexture().getTextureID());

    }

    private void unbindTexturedModel(){
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Body3d body3d){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(body3d.getPosition(),
                body3d.getRotX(), body3d.getRotY(), body3d.getRotZ(), new Vector3f(1,1,1));
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
