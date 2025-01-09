package net.blusutils.narrative.resource

/**
 * Resolves resource names to their placeholder values.
 * @/some/path/to/resource.txt -> <project resources root>/some/path/to/resource.txt
 * !/narrative/path/to/resource.txt -> <shared resources root>/path/to/resource.txt
 * #/some/path/to/resource.txt -> resolves from another resource
 * leading slash is interpreted as a path relative to the resources root
 */
class ResourceResolver {
    fun resolve(resName: String): String? {
        return null
    }
}