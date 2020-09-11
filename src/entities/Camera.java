package entities;

import org.lwjgl.util.vector.Vector3f;
import window.Window;

public class Camera {

    private float distanceFromPlayer = 70;
    private float angleAroundPlayer = 0;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 20;
    private float yaw = 0;
    private float roll;

    private Player player;

    public Camera(Player player){
        this.player = player;
    }

    public void move(){
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
        yaw %= 360;
    }

    public void invertPitch(){
        this.pitch = -pitch;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance){
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance + 4;
    }

    private float calculateHorizontalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch+4)));
    }

    private float calculateVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch+4)));
    }

    private void calculateZoom(){
        float zoomLevel = Window.getDWheel() * 0.03f;
        distanceFromPlayer -= zoomLevel;
        if(distanceFromPlayer<5){
            distanceFromPlayer = 5;
        }
    }

    private void calculatePitch(){
        if(Window.isMButtonDown(1)){
            float pitchChange = Window.getDY() * 0.2f;
            pitch -= pitchChange;
            if(pitch < 0){
                pitch = 0;
            }else if(pitch > 90){
                pitch = 90;
            }
        }
    }

    private void calculateAngleAroundPlayer(){
        if(Window.isMButtonDown(0)){
            float angleChange = Window.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }
}
