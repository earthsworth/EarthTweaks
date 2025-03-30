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

fun createRelocationRemapper(mappings: MappingTree, toNamespace: String = "named"): Remapper {
    val namespace = mappings.getNamespaceId(toNamespace)
    return object : Remapper() {
        override fun map(name: String): String {
            val mappedClass = mappings.mapClassName(name.replace('.', '/'), namespace)
            if (mappedClass != name) {
                println("[Celestial] Map class: $name -> $mappedClass")
            }
            return mappedClass
        }

        override fun mapMethodName(owner: String, name: String, descriptor: String): String {
            val mappedClass = mappings.mapClassName(name.replace('.', '/'), namespace) ?: return name
            val mappedMethod = mappings.getClass(mappedClass, namespace)!!.getMethod(name, descriptor)
            val result = mappedMethod?.getName(namespace) ?: name
            if (result != name) {
                println("[Celestial] Map method: $mappedClass $name$descriptor -> $result$descriptor")
            } else {
                println("[Celestial] Map method: $mappedClass $result$descriptor (unmodified)")
            }
            return result
        }

        override fun mapFieldName(owner: String, name: String, descriptor: String): String {
            val mappedClass = mappings.mapClassName(name.replace('.', '/'), namespace) ?: return name
            val mappedField = mappings.getClass(mappedClass, namespace)!!.getField(name, descriptor)
            val result = mappedField?.getName(mappings.getNamespaceId(toNamespace)) ?: name
            if (result != name) {
                println("[Celestial] Map field: $mappedClass $name$descriptor -> $result$descriptor")
            } else {
                println("[Celestial] Map field: $mappedClass $result$descriptor (unmodified)")
            }
            return result
        }
    }
}
