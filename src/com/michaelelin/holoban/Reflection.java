package com.michaelelin.holoban;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import org.bukkit.Bukkit;

/**
 * Reflection helper methods.
 */
public class Reflection {
    /**
     * Return the version of the net.minecraft.server (NMS) package.
     *
     * @return the version of the net.minecraft.server (NMS) package.
     */
    public static String getNMSVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    /**
     * Return the CraftBukkit class with the specified partial package name and
     * class name after the NMS version.
     *
     * @param name the name of the class, optionally preceded by that part of
     *             the package name that follows the version number. This name
     *             should NOT begin with a period.
     * @return the class from CraftBukkit.
     * @throws ClassNotFoundException
     */
    public static Class<?> getCraftBukkitClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + getNMSVersion() + "." + name);
    }

    /**
     * Return the net.minecraft.server (NMS) class with the specified partial
     * package name and class name after the NMS version.
     *
     * @param name the name of the class, optionally preceded by that part of
     *             the package name that follows the version number. This name
     *             should NOT begin with a period.
     * @return the class from CraftBukkit.
     * @throws ClassNotFoundException
     */
    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + getNMSVersion() + "." + name);
    }

    /**
     * Return the MethodHandle of a static method.
     *
     * @param lookup         the MethodHandles.Lookup instance. Create this with
     *                       MethodHandles.lookup() and reuse it for multiple
     *                       look ups.
     * @param declaringClass the class declaring the method.
     * @param methodName     the method name.
     * @param returnType     the method return type.
     * @param parameterTypes the method parameter types.
     * @return the MethodHandle.
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    public static MethodHandle findStatic(Lookup lookup,
                                          Class<?> declaringClass,
                                          String methodName,
                                          Class<?> returnType,
                                          Class<?>... parameterTypes) throws NoSuchMethodException, IllegalAccessException {
        MethodType methodType = MethodType.methodType(returnType, parameterTypes);
        return lookup.findStatic(declaringClass, methodName, methodType);
    }

    /**
     * Return the MethodHandle of a regular virtual method.
     *
     * @param lookup         the MethodHandles.Lookup instance. Create this with
     *                       MethodHandles.lookup() and reuse it for multiple
     *                       look ups.
     * @param declaringClass the class declaring the method.
     * @param methodName     the method name.
     * @param returnType     the method return type.
     * @param parameterTypes the method parameter types.
     * @return the MethodHandle.
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    public static MethodHandle findVirtual(Lookup lookup,
                                           Class<?> containingClass,
                                           String methodName,
                                           Class<?> returnType,
                                           Class<?>... parameterTypes) throws NoSuchMethodException, IllegalAccessException {
        MethodType methodType = MethodType.methodType(returnType, parameterTypes);
        return lookup.findVirtual(containingClass, methodName, methodType);
    }
}
