package me.creepinson.mod.api.util.transformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class CoreTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        boolean obfuscated = CoreLoader.isObfuscated;

        if (name.equals(obfuscated ? "chd" : "net.minecraft.server.integrated.IntegratedServer")) {
            Logger log = LogManager.getLogger("Default LAN Port");
            log.info("Found class \"IntegratedServer\"");

            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);

            for (MethodNode method : classNode.methods) {
                if (method.name.equals(obfuscated ? "a" : "shareToLAN") &&
                        method.desc.equals(obfuscated ? "(Lams;Z)Ljava/lang/String;" : "(Lnet/minecraft/world/GameType;Z)Ljava/lang/String;")) {

                    log.info("Found method \"shareToLAN\"");
                    for (AbstractInsnNode instruction : method.instructions.toArray()) {
                        if (instruction.getOpcode() == Opcodes.ALOAD) {
                            log.info("Found ALOAD");

                            InsnList toInsert = new InsnList();
                            toInsert.add(new IntInsnNode(Opcodes.SIPUSH, 25565));
                            toInsert.add(new VarInsnNode(Opcodes.ISTORE, 3));

                            method.instructions.insertBefore(instruction, toInsert);
                            break;
                        }
                    }
                }
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }

        return basicClass;
    }
}
