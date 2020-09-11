package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import window.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 18;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isInAir = false;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(){
        checkInputes();

        super.increaseRotation(0,currentTurnSpeed* Window.getFrameTimeSeconds() ,0);
        float distance = currentSpeed * Window.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx,0,dz);
        upwardsSpeed += GRAVITY * Window.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed * Window.getFrameTimeSeconds(), 0);

        if (super.getPosition().y < 0) {
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = 0;
        }
    }

    private void jump() {
        if (!isInAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputes(){
        if(Window.isKeyDown(GLFW_KEY_W)){
            currentSpeed = RUN_SPEED;
        }else if(Window.isKeyDown(GLFW_KEY_S)){
            currentSpeed = -RUN_SPEED;
        }else{
            currentSpeed = 0;
        }


        if(Window.isKeyDown(GLFW_KEY_D)){
            currentTurnSpeed = -TURN_SPEED;
        }else if(Window.isKeyDown(GLFW_KEY_A)){
            currentTurnSpeed = TURN_SPEED;
        }else{
            currentTurnSpeed = 0;
        }


        if (Window.isKeyDown(GLFW_KEY_SPACE)) {
            jump();
        }
    }
}
