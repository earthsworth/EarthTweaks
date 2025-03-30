package org.cubewhy.celestial.tweaks.transformer

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain

abstract class NodeTransformer : ClassFileTransformer {

    override fun transform(
        module: Module,
        loader: ClassLoader,
        className: String,
        classBeingRedefined: Class<*>?,
        protectionDomain: ProtectionDomain?,
        classfileBuffer: ByteArray?
    ): ByteArray? {
        // read node
        val reader = ClassReader(classfileBuffer)
        val classNode = ClassNode()
        reader.accept(classNode, 0)
        // transform
        val newCn = this.transform(classNode)
        if (newCn != null) {
            // apply changes
            val writer = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
            newCn.accept(writer)
            return writer.toByteArray()
        }
        return null // nothing changed
    }

    abstract fun transform(cn: ClassNode): ClassNode?
}