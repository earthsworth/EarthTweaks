package org.cubewhy.celestial.tweaks.utils

import net.fabricmc.mappingio.format.tiny.Tiny2FileReader
import net.fabricmc.mappingio.tree.MappingTree
import net.fabricmc.mappingio.tree.MemoryMappingTree
import org.objectweb.asm.commons.Remapper
import java.io.InputStream

object MappingUtils {
    fun loadTinyV2(inputStream: InputStream): MemoryMappingTree {
        val mappingTree = MemoryMappingTree()
        inputStream.bufferedReader().use { reader ->
            Tiny2FileReader.read(reader, mappingTree)
        }
        return mappingTree
    }
}

fun createRelocationRemapper(mappings: MappingTree,toNamespace: String = "named"): Remapper {
    return object : Remapper() {
        override fun map(name: String): String {
//            println("[Celestial] DEBUG: map class: $name")
            val mappedClass = mappings.getClass(name.replace('.', '/'))
            return mappedClass?.getName(mappings.getNamespaceId(toNamespace))?.replace('/', '.') ?: name
        }

        override fun mapMethodName(owner: String, name: String, descriptor: String): String {
//            println("[Celestial] DEBUG: map method: $owner $name$descriptor")
            val mappedClass = mappings.getClass(owner.replace('.', '/')) ?: return name
            val mappedMethod = mappedClass.getMethod(name, descriptor)
            return mappedMethod?.getName(mappings.getNamespaceId(toNamespace)) ?: name
        }

        override fun mapFieldName(owner: String, name: String, descriptor: String): String {
//            println("[Celestial] DEBUG: map field: $owner $name$descriptor")
            val mappedClass = mappings.getClass(owner.replace('.', '/')) ?: return name
            val mappedField = mappedClass.getField(name, descriptor)
            return mappedField?.getName(mappings.getNamespaceId(toNamespace)) ?: name
        }
    }
}
