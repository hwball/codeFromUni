package shared.model;

import shared.xml.Race.XMLCorner;

/**
 * Created by Gondr on 3/08/2017.
 */
public class Corner extends XMLCorner{

    private int id;

    public Corner(int id, int seqID, String rounding, int zoneSize){
        super();
        setCompoundMarkID(id);
        setSeqID(seqID);
        setRounding(rounding);
        setZoneSize(zoneSize);

        this.id = id;
    }

    public int getId() {
        return id;
    }
}
