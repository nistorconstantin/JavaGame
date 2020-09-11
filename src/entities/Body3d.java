package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;


public class Body3d {
    private TexturedModel model;
    ///
    private Vector3f position;
    private Vector3f velocity;
    ///
    private float mass = 1.0f;
    private float radius = 1.0f;
    private float friction = 0.999f;

    //
    private float rotX, rotY, rotZ;

    public Body3d(TexturedModel model, Vector3f position, Vector3f velocity) {
        this.model = model;
        this.position = position;
        this.velocity = velocity;
    }

    public void update(){
        velocity.scale(friction);
        Vector3f.add(position,velocity,position);
    }

    public void otheParticle(Body3d particle){
        Vector3f dirrection = Vector3f.sub(particle.position,this.position,null);
        if(dirrection.lengthSquared() > (3 * radius)) {
            float qq = this.mass * particle.mass;

            float dirrectionMagnitude = dirrection.lengthSquared();

            float force = qq / dirrectionMagnitude;

            dirrection.scale(force);

            this.accelerate(dirrection);
        }else{
            //velocity.scale(0);
        }
    }

    private void accelerate(Vector3f acc){
        Vector3f.add(velocity,acc,velocity);
    }

    public TexturedModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return radius;
    }

    public void setScale(float scale) {
        this.radius = scale;
    }
}
