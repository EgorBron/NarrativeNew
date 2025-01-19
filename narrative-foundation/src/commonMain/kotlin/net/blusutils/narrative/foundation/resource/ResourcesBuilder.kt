package net.blusutils.narrative.foundation.resource

import net.blusutils.narrative.foundation.story.StoryScope

class ResourcesBuilder<T>(val resourceList: MutableMap<String, String>) : StoryScope {
    fun<N> defineResSet(resName:String, key:String=resName.substringAfterLast("/")): List<N> {
        val res = emptyList<N>()
        resourceList[key] = resName
        return res
    }
    fun<N> defineRes(resName: String, key: String=resName.substringAfterLast("/")): N? {
        resourceList[key] = resName
        return null
    }
}