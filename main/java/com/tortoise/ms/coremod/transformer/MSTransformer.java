package com.tortoise.ms.coremod.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import net.minecraft.launchwrapper.IClassTransformer;

/*
核心类
用于动态改写Java类型
 */
public class MSTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        /* 下面的输出 表示这个类开始被加载 */
        /*
         * 1.7.10使用Notch名混淆 sv是EntityLivingBase的混淆名,你可以在混淆表中查找
         */
        if (name.equals("net.minecraft.entity.EntityLivingBase") || name.equals("sv")) {
            ClassReader Reader = new ClassReader(basicClass);
            // ClassWriter.COMPUTE_MAXS:自动计算堆栈大小
            ClassWriter Writer = new ClassWriter(Reader, ClassWriter.COMPUTE_MAXS);
            /*
             * ClassReader和ClassWriter都继承于ClassVisitor 第一个参数是ASM版本,是int 第二个参数
             * ClassVis
             */
            ClassVisitor Visitor = new ClassVisitor(Opcodes.ASM4, Writer) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                        String[] exceptions) {
                    if ((name.equals("getHealth") || name.equals("aS")) && desc.equals("()F")) {
                        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
                        mv.visitCode();
                        Label Start = new Label();
                        mv.visitLabel(Start);
                        /*
                         * 表示修改的目标函数是静态调用,而不是被修改函数 把EntityLivi gBase.getHelath的代码修改为 Ev
                         * ntUtil.getH 表示EventUtil.getHealth(修改后的函数)的参数
                         */
                        mv.visitVarInsn(Opcodes.ALOAD/* 参数类型 */, 0/* 位置 */);
                        /*
                         * 刚刚炸端是因为这里函数签名指定是没有参数,返回值为float:java.lang.NoSuchMethodError
                         * 现指定第一个参数为EntityLivingBase 返回值为float
                         */
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/tortoise/ms/coremod/util/EventUtil",
                                "getHealth"/* 函数名 */, "(Lnet/minecraft/entity/EntityLivingBase;)F", false);
                        // 指定返回值是float
                        mv.visitInsn(Opcodes.FRETURN);
                        Label End = new Label();
                        mv.visitLabel(End);
                        /* L到;匹配一个参数类型,基类型无需L和; */
                        mv.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, Start, End, 0);
                        mv.visitEnd();
                        return null;/* 删除函数原函数,然后把EventUtil.getHealth替换进去 */
                    }
                    return cv.visitMethod(access, name, desc, signature, exceptions);
                }
            };
            Reader.accept(Visitor, Opcodes.ASM4);
            return Writer.toByteArray();
        }
        if (name.equals("net.minecraft.client.Minecraft") || name.equals("bao")) {
            ClassReader Reader = new ClassReader(basicClass);
            ClassWriter Writer = new ClassWriter(Reader, ClassWriter.COMPUTE_MAXS);
            ClassVisitor Visitor = new ClassVisitor(Opcodes.ASM4, Writer) {

                @Override
                public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                    if (name.equals("isGamePaused") || name.equals("T")) {
                        access = Opcodes.ACC_PUBLIC;
                    }
                    return super.visitField(access, name, desc, signature, value);
                }

                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                        String[] exceptions) {
                    if (name.equals("runGameLoop") || name.equals("ak")) {
                        return new MethodVisitor(Opcodes.ASM4,
                                cv.visitMethod(access, name, desc, signature, exceptions)) {

                            /* 在runGameLoop函数开头添加代码 */
                            public void visitCode() {
                                super.visitCode();
                                mv.visitVarInsn(Opcodes.ALOAD, 0);
                                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/tortoise/ms/coremod/util/EventUtil",
                                        "runGameLoop", "(Lnet/minecraft/client/Minecraft;)V", false);
                                // mv.visitInsn(Opcodes.RETURN); 我是傻逼 这一行会死循环。。。
                            };
                        };
                    }
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
            };
            Reader.accept(Visitor, Opcodes.ASM4);
            return Writer.toByteArray();
        }
        // 如果未更改,则返回原来的类
        return basicClass;
    }
}
