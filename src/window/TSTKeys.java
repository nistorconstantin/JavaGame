package window;


public class TSTKeys {

    // try not to alloc to much space for keys
    static int KEY_NUMBERS_SIZE = 1024;

    // here ar keeped all the keys, if one key is pressed, that ascii code of that key
    // will turn true when pressed
    // and turn false when released
    static boolean[] allKeys = new boolean[KEY_NUMBERS_SIZE];

    // how many buttons your mouse have
    static int MOUSE_NUMBERS_SIZE = 4;

    static boolean[] mouseKeys = new boolean[MOUSE_NUMBERS_SIZE];


    static float old_mouse_x;
    static float old_mouse_y;
    static float mouse_dx;
    static float mouse_dy;

    static float old_mouse_DWheel;
    static float mouse_DWheel;
}
