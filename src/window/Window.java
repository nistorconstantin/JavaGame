package window;

import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback scrollCallback;
    private GLFWCursorPosCallback cursorPosCallback;

    // The window handle
    private long windowHandler;

    private static int WIDTH;
    private static int HEIGHT;

    // for time calculations
    private static float lastFrameTime;
    private static float deltaTime;

    public Window(){

        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        try {
            initGLFWsomething();
            prepareGLsomething();

        } finally {

        }

        // init the timer
        initTimer();
    }

    public void destroy(){
        // Release window and window callbacks
        glfwDestroyWindow(windowHandler);
        keyCallback.free();

        // Terminate GLFW and release the GLFWErrorCallback
        glfwTerminate();
        errorCallback.free();
    }

    private void initGLFWsomething() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (glfwInit() != true )
            throw new IllegalStateException("Unable to initialize GLFW");
        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        WIDTH = WindowSettings.window_width;
        HEIGHT = WindowSettings.window_height;
        // Create the window
        windowHandler = glfwCreateWindow(WIDTH, HEIGHT, WindowSettings.window_name, NULL, NULL);
        if ( windowHandler == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandler, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if(key <= TSTKeys.KEY_NUMBERS_SIZE) {
                    if (action == GLFW_PRESS) {
                        TSTKeys.allKeys[key] = true;
                    } else if (action == GLFW_RELEASE) {
                        TSTKeys.allKeys[key] = false;
                    } else {

                    }
                }
            }
        });
        //
        glfwSetMouseButtonCallback(windowHandler, mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                //button == GLFW_MOUSE_BUTTON_RIGHT &&
                if (action == GLFW_PRESS){
                    TSTKeys.mouseKeys[button] = true;
                }else if (action == GLFW_RELEASE){
                    TSTKeys.mouseKeys[button] = false;
                }
            }
        });

        //
        glfwSetScrollCallback(windowHandler, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                TSTKeys.mouse_DWheel = (float)yoffset;
            }
        });

        glfwSetCursorPosCallback(windowHandler,cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                TSTKeys.mouse_dx = (float)xpos - TSTKeys.old_mouse_x ;
                TSTKeys.mouse_dy = (float)ypos - TSTKeys.old_mouse_y;

                TSTKeys.old_mouse_x = (float)xpos;
                TSTKeys.old_mouse_y = (float)ypos;
            }
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                windowHandler,
                (vidmode.width() - WIDTH) / 2,
                (vidmode.height() - HEIGHT) / 2
        );
        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandler);
        // Enable v-sync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(windowHandler);
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(windowHandler);
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static float getDX(){
        return TSTKeys.mouse_dx;
    }

    public static float getDY(){
        return TSTKeys.mouse_dy;
    }

    public static float getDWheel(){
        return TSTKeys.mouse_DWheel;
    }

    public static boolean isKeyDown(int key){
        return TSTKeys.allKeys[key];
    }

    public static boolean isMButtonDown(int button){
        return TSTKeys.mouseKeys[button];
    }

    public void endDraw(){
        calculateDeltaTime();

        glfwSwapBuffers(windowHandler); // swap the color buffers
        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    // return delta time
    public static float getFrameTimeSeconds(){
        return deltaTime;
    }

    private static float getCurrentTime(){
        return (float)glfwGetTime();
    }

    private void calculateDeltaTime(){
        float currentFrameTime = (float)getCurrentTime();
        deltaTime = (currentFrameTime - lastFrameTime);
        lastFrameTime = currentFrameTime;
    }

    private void initTimer(){
        lastFrameTime = (float)getCurrentTime();
    }

    private void prepareGLsomething(){
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
    }

}
