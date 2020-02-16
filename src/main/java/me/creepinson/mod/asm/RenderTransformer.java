//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.creepinson.mod.asm;

import java.util.Arrays;
import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class RenderTransformer implements IClassTransformer {
    public static final List<String> clazzes = Arrays.asList(new String[]{"net.minecraftforge.client.ForgeHooksClient", "net.minecraft.client.Minecraft", "net.minecraft.client.renderer.entity.RenderManager", "net.minecraft.client.renderer.texture.TextureManager", "net.minecraft.client.renderer.RenderItem"});

    public RenderTransformer() {
    }


    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) {
            return null;
        } else {
            int index = clazzes.indexOf(transformedName);
            return index != -1 ? this.transform(index, basicClass, !name.equals(transformedName)) : basicClass;
        }
    }

    public byte[] transform(int index, byte[] basicClass, boolean obfuscated) {
        Plugin.isObf = obfuscated;
        try {
            ClassNode classNode = new ClassNode();
            ClassReader reader = new ClassReader(basicClass);
            reader.accept(classNode, ClassReader.SKIP_DEBUG);
            switch (index) {
                case 0:
                    patchCameraTransform(classNode);
                    break;
                case 1:
                    patchMinecraft(classNode);
                    break;
                case 2:
                    patchRenderManager(classNode);
                    break;
                case 3:
                    patchTextureManager(classNode);
                    break;
                case 4:
                    patchRenderItem(classNode);
                    break;
            }

            ClassWriter classWriter = new ClassWriter(3);
            classNode.accept(classWriter);
            byte[] bytes = classWriter.toByteArray();

            return bytes;
        } catch (Throwable var7) {
            var7.printStackTrace();
            return basicClass;
        }
    }


    public static void patchRenderItem(ClassNode classNode) {
        //add IItemRendererHandler.applyGlTranslates(model) to RenderItem#renderItem
        System.out.println("patching RenderItem.class");
        // IItemRendererHandler.renderItemStack(stack, model);
        MethodNode renderItem = ASMHelper.getMethodNode(classNode, new MCPSidedString("renderItem", "func_180454_a").toString(), new MCPSidedString("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", "(LainLcfw;)V").toString());
        assert renderItem != null;
        AbstractInsnNode spotRenderItem = ASMHelper.getFirstInstruction(renderItem, Opcodes.INVOKESPECIAL);
        InsnList toInsert3 = new InsnList();
        toInsert3.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/creepinson/mod/api/util/client/IItemRendererHandler", "renderItemStack", new MCPSidedString("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", "(LainLcfw;)V").toString(), false));
        renderItem.instructions.insertBefore(spotRenderItem, toInsert3);

        MethodNode effects = ASMHelper.getMethodNode(classNode, new MCPSidedString("renderEffect", "func_191966_a").toString(), new MCPSidedString("(Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", "(Lcfw;)V").toString());
        assert effects != null;
        AbstractInsnNode spotEffect = ASMHelper.getFirstInstruction(effects, 3);
        InsnList toInsert2 = new InsnList();
        toInsert2.add(new FieldInsnNode(178, "me/creepinson/mod/api/util/client/IItemRendererHandler", "allowEnchants", "Z"));
        LabelNode l1 = new LabelNode();
        toInsert2.add(new JumpInsnNode(154, l1));
        LabelNode l2 = new LabelNode();
        toInsert2.add(l2);
        toInsert2.add(new InsnNode(177));
        toInsert2.add(l1);
        effects.instructions.insertBefore(spotEffect, toInsert2);

    }

    public static void patchCameraTransform(ClassNode classNode) {
        System.out.println("patching ForgeHooksClient#handleCameraTransforms");
        MethodNode camera = ASMHelper.getMethodNode(classNode, "handleCameraTransforms", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)Lnet/minecraft/client/renderer/block/model/IBakedModel;");
        InsnList toInsert = new InsnList();
        toInsert.add(new VarInsnNode(25, 1));
        toInsert.add(new VarInsnNode(21, 2));
        toInsert.add(new MethodInsnNode(184, "me/creepinson/mod/api/util/client/IItemRendererHandler", "handleCameraTransforms", "(Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V", false));
        toInsert.add(new FieldInsnNode(178, "me/creepinson/mod/api/util/client/IItemRendererHandler", "runTransforms", "Z"));
        LabelNode l1 = new LabelNode();
        toInsert.add(new JumpInsnNode(154, l1));
        LabelNode l2 = new LabelNode();
        toInsert.add(l2);
        toInsert.add(new VarInsnNode(25, 0));
        toInsert.add(new VarInsnNode(25, 1));
        toInsert.add(new MethodInsnNode(185, "net/minecraft/client/renderer/block/model/IBakedModel", "handlePerspective", "(Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)Lorg/apache/commons/lang3/tuple/Pair;", true));
        toInsert.add(new MethodInsnNode(182, "org/apache/commons/lang3/tuple/Pair", "getLeft", "()Ljava/lang/Object;", false));
        toInsert.add(new TypeInsnNode(192, "net/minecraft/client/renderer/block/model/IBakedModel"));
        toInsert.add(new InsnNode(176));
        toInsert.add(l1);
        AbstractInsnNode spot = camera.instructions.getFirst();
        if (spot instanceof LabelNode) {
            spot = spot.getNext();
            camera.instructions.insert(spot, toInsert);
        } else {
            camera.instructions.insertBefore(spot, toInsert);
        }

    }

    public static void patchMinecraft(ClassNode classNode) {
        System.out.println("patching Minecraft.class for custom RenderItem");
        MethodNode method = ASMHelper.getMethodNode(classNode, (new MCPSidedString("init", "init")).toString(), "()V"); // func_71384_a
        AbstractInsnNode start = null;
        AbstractInsnNode end = null;
        AbstractInsnNode[] nodes = method.instructions.toArray();
        AbstractInsnNode[] var5 = nodes;
        int var6 = nodes.length;

        for (int var7 = 0; var7 < var6; ++var7) {
            AbstractInsnNode ab = var5[var7];
            if (ab.getOpcode() == 187) {
                TypeInsnNode node = (TypeInsnNode) ab;
                if (node.desc.equals("net/minecraft/client/renderer/RenderItem")) {
                    start = node.getPrevious();
                }
            } else if (start != null && ab.getOpcode() == 181) {
                end = ab.getPrevious();
                break;
            }
        }

        InsnList toInsert = new InsnList();
        toInsert.add(new TypeInsnNode(187, "me/creepinson/mod/api/util/client/RenderItemObj"));
        toInsert.add(new InsnNode(89));
        method.instructions.insert(start, toInsert);
        method.instructions.insert(end, new MethodInsnNode(183, "me/creepinson/mod/api/util/client/RenderItemObj", "<init>", "(Lnet/minecraft/client/renderer/RenderItem;)V", false));
    }

    public static void patchRenderManager(ClassNode classNode) {
        MethodNode node = ASMHelper.getMethodNode(classNode, (new MCPSidedString("renderEntity", "func_188391_a")).toString(), "(Lnet/minecraft/entity/Entity;DDDFFZ)V");
        InsnList toInsert = new InsnList();
        toInsert.add(new VarInsnNode(25, 1));
        toInsert.add(new MethodInsnNode(184, "me/creepinson/mod/api/util/client/IItemRendererHandler", "updateLastPossiblePos", "(Lnet/minecraft/entity/Entity;)V", false));
        AbstractInsnNode point = ASMHelper.getFirstInstruction(node, 1);
        node.instructions.insertBefore(point, toInsert);
    }

    public static void patchTextureManager(ClassNode classNode) {
        System.out.println("Patching TextureManager.class to deny binding textures when IItemRendererHandler#canBind is false");
        MethodNode node = ASMHelper.getMethodNode(classNode, (new MCPSidedString("bindTexture", "func_110577_a")).toString(), "(Lnet/minecraft/util/ResourceLocation;)V");
        AbstractInsnNode start = ASMHelper.getFirstInstruction(node, 25);
        InsnList toInsert = new InsnList();
        toInsert.add(new FieldInsnNode(178, "me/creepinson/mod/api/util/client/IItemRendererHandler", "canBind", "Z"));
        LabelNode l1 = new LabelNode();
        toInsert.add(new JumpInsnNode(154, l1));
        LabelNode l2 = new LabelNode();
        toInsert.add(l2);
        toInsert.add(new InsnNode(177));
        toInsert.add(l1);
        node.instructions.insertBefore(start, toInsert);
    }
}
