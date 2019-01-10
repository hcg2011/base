package com.chungo.base.utils

import android.content.res.XmlResourceParser
import android.support.v4.util.ArrayMap

object XmlUtil {

    @Throws(Exception::class)
    fun parseNodes(xmlResourceParser: XmlResourceParser?): ArrayMap<String, ArrayMap<String, String>>? {
        if (xmlResourceParser == null)
            return null
        var map: ArrayMap<String, ArrayMap<String, String>>? = null
        var nodeMap: ArrayMap<String, String>? = null
        var root = xmlResourceParser.eventType
        while (root != XmlResourceParser.END_DOCUMENT) {
            when (root) {
                XmlResourceParser.START_DOCUMENT -> map = ArrayMap()
                XmlResourceParser.START_TAG -> if ("index" == xmlResourceParser.name) {
                    val nodeName = xmlResourceParser.getAttributeValue(0)
                    nodeMap = ArrayMap()
                    if (map != null) {
                        map[nodeName] = nodeMap
                    }
                } else if ("node" == xmlResourceParser.name) {
                    val node = xmlResourceParser.getAttributeValue(0)
                    val nodeName = xmlResourceParser.nextText()
                    if (nodeMap != null) {
                        nodeMap[node] = nodeName
                    }
                }
                XmlResourceParser.END_TAG -> if ("index" == xmlResourceParser.name) {
                    nodeMap = null
                }
            }
            root = xmlResourceParser.next()
        }
        return map
    }
}
