package Game.Actors.Player;

import Game.Utilities.Util;
import Game.Actors.Cars.Car;
import Game.Utilities.MovementDir;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;

/**
 * Used to handle the input it's a singleton class so we
 */
public class Controller implements KeyboardHandler {

    private static Controller instance;
    private Player playerOwner;
    private boolean canMove;
    private long canMoveTimeStamp;
    private boolean freezed;

    private Controller(Player playerOwner){
        this.playerOwner = playerOwner;
        this.canMove = true;
        this.canMoveTimeStamp = System.currentTimeMillis() / 1000L;
        this.freezed = true;
    }

    private Controller(){
        keyboardInit();
        this.canMove = true;
        this.canMoveTimeStamp = System.currentTimeMillis() / 1000L;
        this.freezed = true;
    }

    public static Controller getInstance(){
        if(instance == null)
            instance = new Controller();

        return instance;
    }

    public void setPlayerOwner(Player playerOwner, boolean freezed) {
        this.playerOwner = playerOwner;
        this.canMove = true;
        this.canMoveTimeStamp = System.currentTimeMillis() / 1000L;
        this.freezed = freezed;
    }

    public void resetPlayer(boolean freezed){
        this.canMove = true;
        this.canMoveTimeStamp = System.currentTimeMillis() / 1000L;
        this.freezed = freezed;

        int[] startPos = new int[]{10,10};
        int[] playerGridPos = Util.toGrid(playerOwner.getPlayerPic().getX(), playerOwner.getPlayerPic().getY());

        while(playerGridPos[0] != startPos[0]){

            int difference = startPos[0] - playerGridPos[0];

            MovementDir moveDir = difference > 0 ? MovementDir.RIGHT : MovementDir.LEFT;
            playerOwner.getPlayerPic().translate(1,1);

            playerGridPos = Util.toGrid(playerOwner.getPlayerPic().getX(), playerOwner.getPlayerPic().getY());
        }

        while(playerGridPos[1] != startPos[1]){
            playerOwner.getPlayerPic().translate(1,1);

            playerGridPos = Util.toGrid(playerOwner.getPlayerPic().getX(), playerOwner.getPlayerPic().getY());
        }

    }

    public void keyboardInit() {
        Keyboard keyboard = new Keyboard(this);

        KeyboardEvent rightKeyPressed = new KeyboardEvent();
        rightKeyPressed.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        rightKeyPressed.setKey(KeyboardEvent.KEY_RIGHT);

        KeyboardEvent leftKeyPressed = new KeyboardEvent();
        leftKeyPressed.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        leftKeyPressed.setKey(KeyboardEvent.KEY_LEFT);

        KeyboardEvent downKeyPressed = new KeyboardEvent();
        downKeyPressed.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        downKeyPressed.setKey(KeyboardEvent.KEY_DOWN);

        KeyboardEvent upKeyPressed = new KeyboardEvent();
        upKeyPressed.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        upKeyPressed.setKey(KeyboardEvent.KEY_UP);

        KeyboardEvent upKeyReleased = new KeyboardEvent();
        upKeyReleased.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);
        upKeyReleased.setKey(KeyboardEvent.KEY_UP);

        KeyboardEvent rightKeyReleased = new KeyboardEvent();
        rightKeyReleased.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);
        rightKeyReleased.setKey(KeyboardEvent.KEY_RIGHT);

        KeyboardEvent leftKeyReleased = new KeyboardEvent();
        leftKeyReleased.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);
        leftKeyReleased.setKey(KeyboardEvent.KEY_LEFT);

        KeyboardEvent downKeyReleased = new KeyboardEvent();
        downKeyReleased.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);
        downKeyReleased.setKey(KeyboardEvent.KEY_DOWN);
        
        //Press listeners
        keyboard.addEventListener(rightKeyPressed);
        keyboard.addEventListener(leftKeyPressed);
        keyboard.addEventListener(downKeyPressed);
        keyboard.addEventListener(upKeyPressed);

        //Key releases
        keyboard.addEventListener(upKeyReleased);
        keyboard.addEventListener(downKeyPressed);
        keyboard.addEventListener(leftKeyPressed);
        keyboard.addEventListener(rightKeyPressed);
        keyboard.addEventListener(downKeyReleased);
        keyboard.addEventListener(leftKeyReleased);
        keyboard.addEventListener(rightKeyReleased);
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean getCanMove() {
        return canMove;
    }

    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
    try {
        if (playerOwner == null)
            return;

        if (!playerOwner.isAlive())
            return;

        if (freezed) //Freeze time four our player
            freezed = ((System.currentTimeMillis() / 1000L) - canMoveTimeStamp) < 2.5;

        for (Car c : playerOwner.getCarArray()) {
            if (c.checkCollision(playerOwner)) {
                playerOwner.setAlive(false);
            }
        }

        if (canMove && !freezed) {

            switch (keyboardEvent.getKey()) {
                case KeyboardEvent.KEY_LEFT:
                    playerOwner.moveLeft();
                    break;
                case KeyboardEvent.KEY_RIGHT:
                    playerOwner.moveRight();
                    break;
                case KeyboardEvent.KEY_UP:
                    playerOwner.moveUp();
                    break;
                case KeyboardEvent.KEY_DOWN:
                    playerOwner.moveDown();
                    break;
            }

            for (Car c : playerOwner.getCarArray()) {
                if (c.checkCollision(playerOwner)) {
                    playerOwner.setAlive(false);
                }
            }
            canMove = false;
        }
    }catch (Exception e){
        System.out.println(e);
    }
    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {
        canMove = true;
    }
}
