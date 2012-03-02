package des.game.config;

import des.game.base.GameComponent;

public class GameObjectDefinition {
    public float width;
    public float height;
    public int currentAction;
    
    public int physicsObjectIndex;
    public int spriteObjectIndex;
    
    public GameComponent[] components;
    public Dependency[] dependencies;
    public MethodCall[][] methodCalls;
    
    public GameComponent[] temp;
}
