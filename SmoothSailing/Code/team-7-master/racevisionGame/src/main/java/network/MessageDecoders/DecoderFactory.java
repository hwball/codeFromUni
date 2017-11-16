package network.MessageDecoders;


import network.Exceptions.InvalidMessageTypeException;
import network.Messages.Enums.MessageType;

/**
 * Factory to create the appropriate decoder for a given message.
 */
public class DecoderFactory {


    /**
     * Private constructor. Currently doesn't need to be constructed.
     */
    private DecoderFactory(){

    }


    /**
     * Creates the correct type of decoder for a given message type.
     * @param type Type of message you want a decoder for.
     * @return The decoder.
     * @throws InvalidMessageTypeException If you pass in a {@link MessageType} that isn't recognised.
     */
    public static MessageDecoder create(MessageType type) throws InvalidMessageTypeException {


        switch (type) {

            case HEARTBEAT: return new HeartBeatDecoder();

            case RACESTATUS: return new RaceStatusDecoder();

            //case DISPLAYTEXTMESSAGE: return new DisplayTextMessageDecoder();//TODO

            case XMLMESSAGE: return new XMLMessageDecoder();

            case RACESTARTSTATUS: return new RaceStartStatusDecoder();

            case YACHTEVENTCODE: return new YachtEventCodeDecoder();

            //case YACHTACTIONCODE: return new YachtActionCodeDecoder();//TODO

            //case CHATTERTEXT: return new ChatterTextDecoder();//TODO

            case BOATLOCATION: return new BoatLocationDecoder();

            case MARKROUNDING: return new MarkRoundingDecoder();

            case COURSEWIND: return new CourseWindsDecoder();

            case AVGWIND: return new AverageWindDecoder();

            case REQUEST_TO_JOIN: return new RequestToJoinDecoder();

            case JOIN_ACCEPTANCE: return new JoinAcceptanceDecoder();

            case BOATACTION: return new BoatActionDecoder();

            case BOATSTATE: return new BoatStateDecoder();

            default: throw new InvalidMessageTypeException("Unrecognised message type: " + type);
        }



    }

}
