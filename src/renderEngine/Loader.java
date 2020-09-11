package renderEngine;

import de.matthiasmann.twl.utils.PNGDecoder;
import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private List<Integer> vaos= new ArrayList<Integer>();
    private List<Integer> vbos= new ArrayList<Integer>();
    private List<Integer> textures= new ArrayList<Integer>();

    public RawModel loadToVAO(float[] vertices,float[] textureCoords,float[] normals, int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,vertices);
        storeDataInAttributeList(1,2,textureCoords);
        storeDataInAttributeList(2,3,normals);
        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }

    public void clearUpBuffers(){
        for(int vao:vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo:vbos){
            GL15.glDeleteBuffers(vbo);
        }
        for(int texture:textures)
        {
            GL11.glDeleteTextures(texture);
        }
    }

    public int loadTexture(String fileName){
        PNGDecoder decoder = null;

        //load png file
        try {
            FileInputStream in = new FileInputStream("res/" + fileName + ".png");
            decoder = new PNGDecoder(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create a byte buffer big enough to store RGBA values
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

        //decode
        try {
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //flip the buffer so its ready to read
        buffer.flip();

        //create a texture
        int id = GL11.glGenTextures();
        textures.add(id);

        //bind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

        //tell opengl how to unpack bytes
       // GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        //set the texture parameters, can be GL_LINEAR or GL_NEAREST
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        //upload texture
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0,GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        // Generate Mip Map
        //GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        return id;
    }

    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void bindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);

        //bind buffer
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,vboID);


        //store data
        IntBuffer buffer = storeDataInFloatBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttributeList(int attributeNumber,int coordinateSize, float[] data){
        //create buffer
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);

        //bind buffer
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);

        //store data
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber,coordinateSize, GL11.GL_FLOAT,false,0,0);

        //unbind buffer
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
    }

    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    private IntBuffer storeDataInFloatBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
