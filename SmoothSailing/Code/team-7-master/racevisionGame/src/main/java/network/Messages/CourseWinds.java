package network.Messages;


import network.Messages.Enums.MessageType;

import java.util.List;

/**
 * Represents the information in a CourseWind message (AC streaming spec: 4.11).
 */
public class CourseWinds extends AC35Data {

    /**
     * The current version number for this message type.
     */
    public static final byte currentMessageVersionNumber = 1;


    /**
     * The version number of this message.
     */
    private byte messageVersionNumber;

    /**
     * The ID of the wind source currently selected.
     */
    private byte selectedWindID;

    /**
     * A list of wind sources.
     */
    private List<CourseWind> courseWinds;


    /**
     * Constructs a CourseWinds with given parameters.
     * @param messageVersionNumber The version number of the message.
     * @param selectedWindID The selected wind ID.
     * @param courseWinds A list of wind sources.
     */
    public CourseWinds(byte messageVersionNumber, byte selectedWindID, List<CourseWind> courseWinds) {
        super(MessageType.COURSEWIND);
        this.messageVersionNumber = messageVersionNumber;
        this.selectedWindID = selectedWindID;
        this.courseWinds = courseWinds;
    }


    /**
     * Returns the version number of this message.
     * @return Version number of this message.
     */
    public byte getMessageVersionNumber() {
        return messageVersionNumber;
    }

    /**
     * Returns the ID of the selected wind source.
     * @return ID of the selected wind source.
     */
    public byte getSelectedWindID() {
        return selectedWindID;
    }

    /**
     * Returns the list of wind sources.
     * @return List of wind sources.
     */
    public List<CourseWind> getCourseWinds() {
        return courseWinds;
    }
}
