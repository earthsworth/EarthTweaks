package org.cubewhy.celestial.tweaks.transformer

import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.tree.ClassNode

class DeobfTransformer(private val remapper: Remapper) : NodeTransformer() {
    override fun transform(cn: ClassNode): ClassNode {
        // load mapping
        val newNode = ClassNode()
        val classRemapper = ClassRemapper(newNode, remapper)

        cn.accept(classRemapper)
        return newNode
    }
}