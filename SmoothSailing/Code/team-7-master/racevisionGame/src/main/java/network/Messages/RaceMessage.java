package network.Messages;


import network.Messages.Enums.MessageType;

/**
 * Created by fwy13 on 19/04/17.
 */
public class RaceMessage extends AC35Data {

    private int lineNumber;
    private String messageText;

    public RaceMessage(int lineNumber, String messageText){
        super(MessageType.DISPLAYTEXTMESSAGE);
        this.lineNumber = lineNumber;
        this.messageText = messageText;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getMessageText() {
        return messageText;
    }
}
