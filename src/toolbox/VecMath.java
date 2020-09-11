package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class VecMath {

    public static Vector3f scale(float s, Vector3f vec){
        return new Vector3f(vec.x * s,vec.y * s,vec.z * s);
    }
}
