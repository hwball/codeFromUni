package visualiser.model;

/**
 * The properties of the boat currently being controlled by the player. Singleton.
 */
public class ThisBoat {
    private VisualiserBoat boat;
    private static ThisBoat instance = new ThisBoat();
    
    private ThisBoat(){}
    
    public static ThisBoat getInstance(){
        return instance;
    }

    public void setSailsOut(boolean sailsOut) {
        if (this.boat != null) {
            this.boat.setSailsOut(sailsOut);
        }
    }

    public boolean isSailsOut() {
        if (this.boat != null) {
            return this.boat.isSailsOut();
        } else {
            return true;//TODO junk value to allow the client to spectate.
        }
    }

    public int getSourceID() {
        if (this.boat != null) {
            return this.boat.getSourceID();
        } else {
            return 0;//TODO junk value to allow the client to spectate.
        }
    }

    public int getLegNumber(){
        if(this.boat != null){
            return this.boat.getCurrentLeg().getLegNumber();
        }else{
            return 0;
        }
    }

    public void setBoat(VisualiserBoat boat) {
        this.boat = boat;
    }
}
