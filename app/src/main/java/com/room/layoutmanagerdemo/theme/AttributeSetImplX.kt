package com.room.layoutmanagerdemo.theme

import android.util.AttributeSet
import org.xml.sax.Attributes


class AttributeSetImplX(val atts: AttributeSet) : AttributeSet {
    /**
     * Returns the number of attributes available in the set.
     *
     *
     * See also [XmlPullParser.getAttributeCount()][XmlPullParser.getAttributeCount],
     * which this method corresponds to when parsing a compiled XML file.
     *
     * @return A positive integer, or 0 if the set is empty.
     */
    override fun getAttributeCount(): Int {
        return atts.attributeCount
    }

    /**
     * Returns the name of the specified attribute.
     *
     *
     * See also [XmlPullParser.getAttributeName()][XmlPullParser.getAttributeName],
     * which this method corresponds to when parsing a compiled XML file.
     *
     * @param index Index of the desired attribute, 0...count-1.
     *
     * @return A String containing the name of the attribute, or null if the
     * attribute cannot be found.
     */
    override fun getAttributeName(index: Int): String {
        return atts.getAttributeName(index)
    }

    /**
     * Returns the value of the specified attribute as a string representation.
     *
     * @param index Index of the desired attribute, 0...count-1.
     *
     * @return A String containing the value of the attribute, or null if the
     * attribute cannot be found.
     */
    override fun getAttributeValue(index: Int): String {
        return atts.getAttributeValue(index)
    }

    /**
     * Returns the value of the specified attribute as a string representation.
     * The lookup is performed using the attribute name.
     *
     * @param namespace The namespace of the attribute to get the value from.
     * @param name The name of the attribute to get the value from.
     *
     * @return A String containing the value of the attribute, or null if the
     * attribute cannot be found.
     */
    override fun getAttributeValue(namespace: String?, name: String?): String {
        return atts.getAttributeValue(namespace, name)
    }

    /**
     * Returns a description of the current position of the attribute set.
     * For instance, if the attribute set is loaded from an XML document,
     * the position description could indicate the current line number.
     *
     * @return A string representation of the current position in the set,
     * may be null.
     */
    override fun getPositionDescription(): String {
        return atts.positionDescription
    }

    /**
     * Return the resource ID associated with the given attribute name.  This
     * will be the identifier for an attribute resource, which can be used by
     * styles.  Returns 0 if there is no resource associated with this
     * attribute.
     *
     *
     * Note that this is different than [.getAttributeResourceValue]
     * in that it returns a resource identifier for the attribute name; the
     * other method returns this attribute's value as a resource identifier.
     *
     * @param index Index of the desired attribute, 0...count-1.
     *
     * @return The resource identifier, 0 if none.
     */
    override fun getAttributeNameResource(index: Int): Int {
        return atts.getAttributeNameResource(index)
    }

    /**
     * Return the index of the value of 'attribute' in the list 'options'.
     *
     * @param namespace Namespace of attribute to retrieve.
     * @param attribute Name of attribute to retrieve.
     * @param options List of strings whose values we are checking against.
     * @param defaultValue Value returned if attribute doesn't exist or no
     * match is found.
     *
     * @return Index in to 'options' or defaultValue.
     */
    override fun getAttributeListValue(
        namespace: String?, attribute: String?, options: Array<out String>?, defaultValue: Int
    ): Int {
        return atts.getAttributeListValue(namespace, attribute, options, defaultValue)
    }

    /**
     * Return the index of the value of attribute at 'index' in the list
     * 'options'.
     *
     * @param index Index of the desired attribute, 0...count-1.
     * @param options List of strings whose values we are checking against.
     * @param defaultValue Value returned if attribute doesn't exist or no
     * match is found.
     *
     * @return Index in to 'options' or defaultValue.
     */
    override fun getAttributeListValue(index: Int, options: Array<out String>?, defaultValue: Int): Int {
        return atts.getAttributeListValue(index, options, defaultValue)
    }

    /**
     * Return the boolean value of 'attribute'.
     *
     * @param namespace Namespace of attribute to retrieve.
     * @param attribute The attribute to retrieve.
     * @param defaultValue What to return if the attribute isn't found.
     *
     * @return Resulting value.
     */
    override fun getAttributeBooleanValue(namespace: String?, attribute: String?, defaultValue: Boolean): Boolean {
        return atts.getAttributeBooleanValue(namespace, attribute, defaultValue)
    }

    /**
     * Return the boolean value of attribute at 'index'.
     *
     * @param index Index of the desired attribute, 0...count-1.
     * @param defaultValue What to return if the attribute isn't found.
     *
     * @return Resulting value.
     */
    override fun getAttributeBooleanValue(index: Int, defaultValue: Boolean): Boolean {
        return atts.getAttributeBooleanValue(index, defaultValue)
    }

    /**
     * Return the value of 'attribute' as a resource identifier.
     *
     *
     * Note that this is different than [.getAttributeNameResource]
     * in that it returns the value contained in this attribute as a
     * resource identifier (i.e., a value originally of the form
     * "@package:type/resource"); the other method returns a resource
     * identifier that identifies the name of the attribute.
     *
     * @param namespace Namespace of attribute to retrieve.
     * @param attribute The attribute to retrieve.
     * @param defaultValue What to return if the attribute isn't found.
     *
     * @return Resulting value.
     */
    override fun getAttributeResourceValue(namespace: String?, attribute: String?, defaultValue: Int): Int {
        return atts.getAttributeResourceValue(namespace, attribute, defaultValue)
    }

    /**
     * Return the value of attribute at 'index' as a resource identifier.
     *
     *
     * Note that this is different than [.getAttributeNameResource]
     * in that it returns the value contained in this attribute as a
     * resource identifier (i.e., a value originally of the form
     * "@package:type/resource"); the other method returns a resource
     * identifier that identifies the name of the attribute.
     *
     * @param index Index of the desired attribute, 0...count-1.
     * @param defaultValue What to return if the attribute isn't found.
     *
     * @return Resulting value.
     */
    override fun getAttributeResourceValue(index: Int, defaultValue: Int): Int {
        return atts.getAttributeResourceValue(index, defaultValue)
    }

    /**
     * Return the integer value of 'attribute'.
     *
     * @param namespace Namespace of attribute to retrieve.
     * @param attribute The attribute to retrieve.
     * @param defaultValue What to return if the attribute isn't found.
     *
     * @return Resulting value.
     */
    override fun getAttributeIntValue(namespace: String?, attribute: String?, defaultValue: Int): Int {
        return atts.getAttributeIntValue(namespace, attribute, defaultValue)
    }

    /**
     * Return the integer value of attribute at 'index'.
     *
     * @param index Index of the desired attribute, 0...count-1.
     * @param defaultValue What to return if the attribute isn't found.
     *
     * @return Resulting value.
     */
    override fun getAttributeIntValue(index: Int, defaultValue: Int): Int {
        return atts.getAttributeIntValue(index, defaultValue)
    }

    /**
     * Return the boolean value of 'attribute' that is formatted as an
     * unsigned value.  In particular, the formats 0xn...n and #n...n are
     * handled.
     *
     * @param namespace Namespace of attribute to retrieve.
     * @param attribute The attribute to retrieve.
     * @param defaultValue What to return if the attribute isn't found.
     *
     * @return Resulting value.
     */
    override fun getAttributeUnsignedIntValue(namespace: String?, attribute: String?, defaultValue: Int): Int {
        return atts.getAttributeUnsignedIntValue(namespace, attribute, defaultValue)
    }

    /**
     * Return the integer value of attribute at 'index' that is formatted as an
     * unsigned value.  In particular, the formats 0xn...n and #n...n are
     * handled.
     *
     * @param index Index of the desired attribute, 0...count-1.
     * @param defaultValue What to return if the attribute isn't found.
     *
     * @return Resulting value.
     */
    override fun getAttributeUnsignedIntValue(index: Int, defaultValue: Int): Int {
        return atts.getAttributeUnsignedIntValue(index, defaultValue)
    }

    /**
     * Return the float value of 'attribute'.
     *
     * @param namespace Namespace of attribute to retrieve.
     * @param attribute The attribute to retrieve.
     * @param defaultValue What to return if the attribute isn't found.
     *
     * @return Resulting value.
     */
    override fun getAttributeFloatValue(namespace: String?, attribute: String?, defaultValue: Float): Float {
        return atts.getAttributeFloatValue(namespace, attribute, defaultValue)
    }

    /**
     * Return the float value of attribute at 'index'.
     *
     * @param index Index of the desired attribute, 0...count-1.
     * @param defaultValue What to return if the attribute isn't found.
     *
     * @return Resulting value.
     */
    override fun getAttributeFloatValue(index: Int, defaultValue: Float): Float {
        return atts.getAttributeFloatValue(index, defaultValue)
    }

    /**
     * Return the value of the "id" attribute or null if there is not one.
     * Equivalent to getAttributeValue(null, "id").
     *
     * @return The id attribute's value or null.
     */
    override fun getIdAttribute(): String {
        return atts.idAttribute
    }

    /**
     * Return the value of the "class" attribute or null if there is not one.
     * Equivalent to getAttributeValue(null, "class").
     *
     * @return The class attribute's value or null.
     */
    override fun getClassAttribute(): String {
        return atts.classAttribute
    }

    /**
     * Return the integer value of the "id" attribute or defaultValue if there
     * is none.
     * Equivalent to getAttributeResourceValue(null, "id", defaultValue);
     *
     * @param defaultValue What to return if the "id" attribute isn't found.
     * @return int Resulting value.
     */
    override fun getIdAttributeResourceValue(defaultValue: Int): Int {
        return atts.getIdAttributeResourceValue(defaultValue)
    }

    /**
     *
     * Return the value of the "style" attribute or 0 if there is not one.
     * Equivalent to getAttributeResourceValue(null, "style").
     *
     * @return The style attribute's resource identifier or 0.
     */
    override fun getStyleAttribute(): Int {
        return styleAttribute
    }

}