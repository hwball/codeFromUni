//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.01 at 11:12:43 PM NZST 
//


package shared.xml.boats;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Boats"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Boat" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="GPSposition"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="X" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *                                     &lt;attribute name="Y" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *                                     &lt;attribute name="Z" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                           &lt;attribute name="Type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="BoatName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="SourceID" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *                           &lt;attribute name="HullNum" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="ShortName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="ShapeID" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *                           &lt;attribute name="StoweName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "boats"
})
@XmlRootElement(name = "BoatConfig")
public class BoatConfig {

    @XmlElement(name = "Boats", required = true)
    protected BoatConfig.Boats boats;

    /**
     * Gets the value of the boats property.
     * 
     * @return
     *     possible object is
     *     {@link BoatConfig.Boats }
     *     
     */
    public BoatConfig.Boats getBoats() {
        return boats;
    }

    /**
     * Sets the value of the boats property.
     * 
     * @param value
     *     allowed object is
     *     {@link BoatConfig.Boats }
     *     
     */
    public void setBoats(BoatConfig.Boats value) {
        this.boats = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Boat" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="GPSposition"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="X" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
     *                           &lt;attribute name="Y" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
     *                           &lt;attribute name="Z" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *                 &lt;attribute name="Type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="BoatName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="SourceID" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
     *                 &lt;attribute name="HullNum" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="ShortName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="ShapeID" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
     *                 &lt;attribute name="StoweName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "boat"
    })
    public static class Boats {

        @XmlElement(name = "Boat", required = true)
        protected List<BoatConfig.Boats.Boat> boat;

        /**
         * Gets the value of the boat property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the boat property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getBoat().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BoatConfig.Boats.Boat }
         * 
         * @return List of Boat entries.
         */
        public List<BoatConfig.Boats.Boat> getBoat() {
            if (boat == null) {
                boat = new ArrayList<BoatConfig.Boats.Boat>();
            }
            return this.boat;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="GPSposition"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="X" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
         *                 &lt;attribute name="Y" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
         *                 &lt;attribute name="Z" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *       &lt;attribute name="Type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="BoatName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="SourceID" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
         *       &lt;attribute name="HullNum" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="ShortName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="ShapeID" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
         *       &lt;attribute name="StoweName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "gpSposition"
        })
        public static class Boat {

            @XmlElement(name = "GPSposition", required = true)
            protected BoatConfig.Boats.Boat.GPSposition gpSposition;
            @XmlAttribute(name = "Type", required = true)
            protected String type;
            @XmlAttribute(name = "BoatName", required = true)
            protected String boatName;
            @XmlAttribute(name = "SourceID", required = true)
            protected int sourceID;
            @XmlAttribute(name = "HullNum")
            protected String hullNum;
            @XmlAttribute(name = "ShortName")
            protected String shortName;
            @XmlAttribute(name = "ShapeID")
            protected Integer shapeID;
            @XmlAttribute(name = "StoweName")
            protected String stoweName;

            /**
             * Gets the value of the gpSposition property.
             * 
             * @return
             *     possible object is
             *     {@link BoatConfig.Boats.Boat.GPSposition }
             *     
             */
            public BoatConfig.Boats.Boat.GPSposition getGPSposition() {
                return gpSposition;
            }

            /**
             * Sets the value of the gpSposition property.
             * 
             * @param value
             *     allowed object is
             *     {@link BoatConfig.Boats.Boat.GPSposition }
             *     
             */
            public void setGPSposition(BoatConfig.Boats.Boat.GPSposition value) {
                this.gpSposition = value;
            }

            /**
             * Gets the value of the type property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getType() {
                return type;
            }

            /**
             * Sets the value of the type property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setType(String value) {
                this.type = value;
            }

            /**
             * Gets the value of the boatName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBoatName() {
                return boatName;
            }

            /**
             * Sets the value of the boatName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBoatName(String value) {
                this.boatName = value;
            }

            /**
             * Gets the value of the sourceID property.
             * @return source id.
             */
            public int getSourceID() {
                return sourceID;
            }

            /**
             * Sets the value of the sourceID property.
             * @param value new source id.
             */
            public void setSourceID(int value) {
                this.sourceID = value;
            }

            /**
             * Gets the value of the hullNum property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getHullNum() {
                return hullNum;
            }

            /**
             * Sets the value of the hullNum property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setHullNum(String value) {
                this.hullNum = value;
            }

            /**
             * Gets the value of the shortName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getShortName() {
                return shortName;
            }

            /**
             * Sets the value of the shortName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setShortName(String value) {
                this.shortName = value;
            }

            /**
             * Gets the value of the shapeID property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getShapeID() {
                return shapeID;
            }

            /**
             * Sets the value of the shapeID property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setShapeID(Integer value) {
                this.shapeID = value;
            }

            /**
             * Gets the value of the stoweName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStoweName() {
                return stoweName;
            }

            /**
             * Sets the value of the stoweName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStoweName(String value) {
                this.stoweName = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="X" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
             *       &lt;attribute name="Y" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
             *       &lt;attribute name="Z" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class GPSposition {

                @XmlAttribute(name = "X", required = true)
                protected Double x;
                @XmlAttribute(name = "Y", required = true)
                protected double y;
                @XmlAttribute(name = "Z", required = true)
                protected double z;

                /**
                 * Gets the value of the x property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getX() {
                    return x;
                }

                /**
                 * Sets the value of the x property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setX(Double value) {
                    this.x = value;
                }

                /**
                 * Gets the value of the y property.
                 * @return Y value.
                 */
                public double getY() {
                    return y;
                }

                /**
                 * Sets the value of the y property.
                 * @param value new y value.
                 */
                public void setY(double value) {
                    this.y = value;
                }

                /**
                 * Gets the value of the z property.
                 * @return z value.
                 */
                public double getZ() {
                    return z;
                }

                /**
                 * Sets the value of the z property.
                 * @param value new z value.
                 */
                public void setZ(double value) {
                    this.z = value;
                }

            }

        }

    }

}