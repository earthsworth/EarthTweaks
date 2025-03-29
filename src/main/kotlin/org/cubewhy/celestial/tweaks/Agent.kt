package org.cubewhy.celestial.tweaks

import org.cubewhy.celestial.tweaks.transformer.DeobfTransformer
import org.cubewhy.celestial.tweaks.utils.MappingUtils
import org.cubewhy.celestial.tweaks.utils.createRelocationRemapper
import java.lang.instrument.Instrumentation

@Suppress("UNUSED")
object Agent {
    @JvmStatic
    fun premain(args: String?, inst: Instrumentation) {
        println("[Celestial] Welcome to EarthTweaks!")
        System.setProperty("ichor.prebakeClasses", "false")
        // todo download mappings from mappings.lunarclient.top
        // load mappings
        val stream = Agent::class.java.getResourceAsStream("/mappings.tiny")
            ?: throw NullPointerException("Could not load mappings")
        // read mappings
        val mappings = MappingUtils.loadTinyV2(stream)
        val remapper = createRelocationRemapper(mappings)
//        println("[Celestial] Deobfuscating LunarClient...")
        inst.addTransformer(DeobfTransformer(remapper), true)
    }
}
