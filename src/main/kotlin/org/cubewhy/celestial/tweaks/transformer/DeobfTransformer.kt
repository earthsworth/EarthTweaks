package org.cubewhy.celestial.tweaks.transformer

import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.tree.ClassNode

class DeobfTransformer(private val remapper: Remapper) : NodeTransformer() {
    override fun transform(cn: ClassNode): Boolean {
//        if (!cn.name.startsWith("com/moonsworth/lunar/client")) return false
        println(cn.name)
        // load mapping
        val classRemapper = ClassRemapper(cn, remapper)
        cn.accept(classRemapper)
        return true
    }
}