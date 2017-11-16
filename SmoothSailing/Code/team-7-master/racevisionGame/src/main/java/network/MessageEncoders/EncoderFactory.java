package network.MessageEncoders;


import network.Exceptions.InvalidMessageTypeException;
import network.Messages.Enums.MessageType;

/**
 * Factory to create the appropriate encoder for a given message.
 */
public class EncoderFactory {


    /**
     * Private constructor. Currently doesn't need to be constructed.
     */
    private EncoderFactory(){

    }


    /**
     * Creates the correct type of encoder for a given message type.
     * @param type Type of message you want an encoder for.
     * @return The encoder.
     * @throws InvalidMessageTypeException If you pass in a {@link MessageType} that isn't recognised.
     */
    public static MessageEncoder create(MessageType type) throws InvalidMessageTypeException {


        switch (type) {

            case HEARTBEAT: return new HeartBeatEncoder();

            case RACESTATUS: return new RaceStatusEncoder();

            //case DISPLAYTEXTMESSAGE: return new DisplayTextMessageEncoder();//TODO

            case XMLMESSAGE: return new XMLMessageEncoder();

            case RACESTARTSTATUS: return new RaceStartStatusEncoder();

            case YACHTEVENTCODE: return new YachtEventCodeEncoder();

            //case YACHTACTIONCODE: return new YachtActionCodeEncoder();//TODO

            //case CHATTERTEXT: return new ChatterTextEncoder();//TODO

            case BOATLOCATION: return new BoatLocationEncoder();

            case MARKROUNDING: return new MarkRoundingEncoder();

            case COURSEWIND: return new CourseWindsEncoder();

            case AVGWIND: return new AverageWindEncoder();

            case REQUEST_TO_JOIN: return new RequestToJoinEncoder();

            case JOIN_ACCEPTANCE: return new JoinAcceptanceEncoder();

            case BOATACTION: return new BoatActionEncoder();

            case BOATSTATE: return new BoatStateEncoder();

            default: throw new InvalidMessageTypeException("Unrecognised message type: " + type);
        }



    }

}
