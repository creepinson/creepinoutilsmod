package com.theoparis.creepinoutils.util

import java.lang.reflect.*
import java.util.*

/**
 * Generic static methods related Java Reflection.
 *
 * @author javaguides.net
 */
object ReflectionUtils {
    /**
     * Attempt to find a [Method] on the supplied class with the supplied name
     * and parameter types. Searches all superclasses up to `Object`.
     *
     *
     * Returns `null` if no [Method] can be found.
     *
     * @param clazz the class to introspect
     * @param name  the name of the method
     * @return the Method object, or `null` if none found
     */
    fun findMethod(clazz: Class<*>?, name: String): Method? {
        var searchType = clazz
        while (searchType != null) {
            val methods = if (searchType.isInterface) searchType.methods else searchType.declaredMethods
            for (method in methods) {
                if (name == method.name) {
                    return method
                }
            }
            searchType = searchType.superclass
        }
        return null
    }

    /**
     * Invoke the specified [Method] against the supplied target object with
     * no arguments. The target object can be `null` when invoking a static
     * [Method].
     *
     *
     * Thrown exceptions are handled via a call to
     * [.handleReflectionException].
     *
     * @param method the method to invoke
     * @param target the target object to invoke the method on
     * @return the invocation result, if any
     * @see .invokeMethod
     */
    fun invokeMethod(method: Method, target: Any?): Any {
        return invokeMethod(method, target, *arrayOfNulls<Any>(0))
    }

    /**
     * Get Method by passing class and method name.
     *
     * @param clazz
     * @param methodName
     */
    fun getMethod(clazz: Class<*>, methodName: String): Method? {
        val methods = clazz.methods
        for (method in methods) {
            if (method.name == methodName) {
                method.isAccessible = true
                return method
            }
        }
        return null
    }

    /**
     * Invoke the specified [Method] against the supplied target object with
     * the supplied arguments. The target object can be `null` when invoking a
     * static [Method].
     *
     *
     * Thrown exceptions are handled via a call to
     * [.handleReflectionException].
     *
     * @param method the method to invoke
     * @param target the target object to invoke the method on
     * @param args   the invocation arguments (may be `null`)
     * @return the invocation result, if any
     */
    fun invokeMethod(method: Method, target: Any?, vararg args: Any?): Any {
        try {
            return method.invoke(target, *args)
        } catch (ex: Exception) {
            handleReflectionException(ex)
        }
        throw IllegalStateException("Should never get here")
    }

    /**
     * Determine whether the given method is an "equals" method.
     *
     * @see Object.equals
     */
    fun isEqualsMethod(method: Method?): Boolean {
        if (method == null || method.name != "equals") {
            return false
        }
        val paramTypes = method.parameterTypes
        return paramTypes.size == 1 && paramTypes[0] == Any::class.java
    }

    /**
     * Determine whether the given method is a "hashCode" method.
     *
     * @see Object.hashCode
     */
    fun isHashCodeMethod(method: Method?): Boolean {
        return method != null && method.name == "hashCode" && method.parameterCount == 0
    }

    /**
     * Determine whether the given method is a "toString" method.
     *
     * @see Object.toString
     */
    fun isToStringMethod(method: Method?): Boolean {
        return method != null && method.name == "toString" && method.parameterCount == 0
    }

    /**
     * Determine whether the given method is originally declared by
     * [Object].
     */
    fun isObjectMethod(method: Method?): Boolean {
        return if (method == null) {
            false
        } else try {
            Any::class.java.getDeclaredMethod(method.name, *method.parameterTypes)
            true
        } catch (ex: Exception) {
            false
        }
    }

    /**
     * Make the given method accessible, explicitly setting it accessible if
     * necessary. The `setAccessible(true)` method is only called when
     * actually necessary, to avoid unnecessary conflicts with a JVM SecurityManager
     * (if active).
     *
     * @param method the method to make accessible
     * @see Method.setAccessible
     */
    // on JDK 9
    fun makeAccessible(method: Method) {
        if ((!Modifier.isPublic(method.modifiers) || !Modifier.isPublic(method.declaringClass.modifiers))
            && !method.isAccessible
        ) {
            method.isAccessible = true
        }
    }

    /**
     * Finds all setters in the given class and super classes.
     */
    fun getSetters(clazz: Class<*>): List<Method> {
        val methods = clazz.methods
        val list: MutableList<Method> = ArrayList()
        for (method in methods)
            if (isSetter(method))
                list.add(method)


        return list
    }

    fun isSetter(method: Method): Boolean {
        return (method.returnType == Void.TYPE && !Modifier.isStatic(method.modifiers)
                && method.parameterTypes.size == 1)
    }

    /**
     * Finds a public method of the given name, regardless of its parameter
     * definitions,
     */
    fun getPublicMethodNamed(c: Class<*>, methodName: String): Method? {
        for (m in c.methods) if (m.name == methodName) return m
        return null
    }

    /* ignore */
    val defaultClassLoader: ClassLoader
        get() {
            try {
                return Thread.currentThread().contextClassLoader
            } catch (ex: Throwable) {
                /* ignore */
            }
            return ClassLoader.getSystemClassLoader()
        }

    fun isPublic(clazz: Class<*>): Boolean {
        return Modifier.isPublic(clazz.modifiers)
    }

    fun isPublic(member: Member): Boolean {
        return Modifier.isPublic(member.modifiers)
    }

    fun isPrivate(clazz: Class<*>): Boolean {
        return Modifier.isPrivate(clazz.modifiers)
    }

    fun isPrivate(member: Member): Boolean {
        return Modifier.isPrivate(member.modifiers)
    }

    fun isNotPrivate(member: Member): Boolean {
        return !isPrivate(member)
    }

    fun isAbstract(clazz: Class<*>): Boolean {
        return Modifier.isAbstract(clazz.modifiers)
    }

    fun isAbstract(member: Member): Boolean {
        return Modifier.isAbstract(member.modifiers)
    }

    fun isStatic(clazz: Class<*>): Boolean = Modifier.isStatic(clazz.modifiers)


    fun isStatic(member: Member): Boolean = Modifier.isStatic(member.modifiers)

    fun isNotStatic(member: Member): Boolean =
        !isStatic(member)

    /**
     * Determine if the supplied class is an *inner class* (i.e., a
     * non-static member class).
     *
     *
     *
     * Technically speaking (i.e., according to the Java Language Specification),
     * "an inner class may be a non-static member class, a local class, or an
     * anonymous class." However, this method does not return `true` for a
     * local or anonymous class.
     *
     * @param clazz the class to check; never `null`
     * @return `true` if the class is an *inner class*
     */
    fun isInnerClass(clazz: Class<*>): Boolean {
        return !isStatic(clazz) && clazz.isMemberClass
    }

    fun returnsVoid(method: Method): Boolean {
        return method.returnType == Void.TYPE
    }

    /**
     * Determine if the supplied object is an array.
     *
     * @param obj the object to test; potentially `null`
     * @return `true` if the object is an array
     */
    fun isArray(obj: Any?): Boolean {
        return obj != null && obj.javaClass.isArray
    }

    /**
     * Attempt to find a [field][Field] on the supplied [Class] with the
     * supplied `name` and/or [type][Class]. Searches all superclasses up
     * to [Object].
     *
     * @param clazz the class to introspect
     * @param name  the name of the field (may be `null` if type is specified)
     * @return the corresponding Field object, or `null` if not found
     */
    fun findField(clazz: Class<*>?, name: String?): Field? {
        var searchType = clazz
        while (Any::class.java != searchType && searchType != null) {
            val fields = searchType.declaredFields
            for (field in fields) {
                if (name == null || name == field.name) {
                    return field
                }
            }
            searchType = searchType.superclass
        }
        return null
    }

    /**
     * Set the field represented by the supplied [field object][Field] on the
     * specified [target object][Object] to the specified `value`. In
     * accordance with [Field.set] semantics, the new value is
     * automatically unwrapped if the underlying field has a primitive type.
     *
     *
     * Thrown exceptions are handled via a call to
     * [.handleReflectionException].
     *
     * @param field  the field to set
     * @param target the target object on which to set the field
     * @param value  the value to set (may be `null`)
     */
    fun setField(field: Field, target: Any?, value: Any?) {
        try {
            field[target] = value
        } catch (ex: IllegalAccessException) {
            throw IllegalStateException(
                "Unexpected reflection exception - " + ex.javaClass.name + ": " + ex.message
            )
        }
    }

    /**
     * Set the field represented by the supplied [field object][Field] on the
     * specified [target object][Object] to the specified `value`. In
     * accordance with [Field.set] semantics, the new value is
     * automatically unwrapped if the underlying field has a primitive type.
     *
     *
     * Thrown exceptions are handled via a call to
     * [.handleReflectionException].
     *
     * @param fieldName the name of the field to set
     * @param target    the target object on which to set the field
     * @param value     the value to set (may be `null`)
     */
    fun setField(fieldName: String?, target: Any, value: Any?) {
        try {
            val field = getField(target.javaClass, fieldName)
            if (field != null) {
                field.isAccessible = true
                field[target] = value
            }
        } catch (ex: IllegalAccessException) {
            throw IllegalStateException(
                "Unexpected reflection exception - " + ex.javaClass.name + ": " + ex.message
            )
        }
    }

    /**
     * Get the field represented by the supplied [field object][Field] on the
     * specified [target object][Object]. In accordance with
     * [Field.get] semantics, the returned value is automatically
     * wrapped if the underlying field has a primitive type.
     *
     *
     * Thrown exceptions are handled via a call to
     * [.handleReflectionException].
     *
     * @param field  the field to get
     * @param target the target object from which to get the field
     * @return the field's current value
     */
    fun getField(field: Field, target: Any?): Any {
        return try {
            field.isAccessible = true
            field[target]
        } catch (ex: IllegalAccessException) {
            throw IllegalStateException(
                "Unexpected reflection exception - " + ex.javaClass.name + ": " + ex.message
            )
        }
    }

    /**
     * Get the field represented by the supplied [field object][Field] on the
     * specified [target object][Object]. In accordance with
     * [Field.get] semantics, the returned value is automatically
     * wrapped if the underlying field has a primitive type.
     *
     *
     * Thrown exceptions are handled via a call to
     * [.handleReflectionException].
     *
     * @param fieldName the name of the field to get
     * @param target    the target object from which to get the field
     * @return the field's current value
     */
    fun getField(fieldName: String?, target: Any): Any {
        return try {
            val field = findField(target.javaClass, fieldName)
            field!!.isAccessible = true
            field[target]
        } catch (ex: Exception) {
            throw IllegalStateException(
                "Unexpected reflection exception - " + ex.javaClass.name + ": " + ex.message
            )
        }
    }

    /**
     * Handle the given reflection exception. Should only be called if no checked
     * exception is expected to be thrown by the target method.
     *
     *
     * Throws the underlying RuntimeException or Error in case of an
     * InvocationTargetException with such a root cause. Throws an
     * IllegalStateException with an appropriate message or
     * UndeclaredThrowableException otherwise.
     *
     * @param ex the reflection exception to handle
     */
    fun handleReflectionException(ex: Exception) {
        check(ex !is NoSuchMethodException) { "Method not found: " + ex.message }
        check(ex !is IllegalAccessException) { "Could not access method: " + ex.message }
        if (ex is RuntimeException) {
            throw ex
        }
        throw UndeclaredThrowableException(ex)
    }

    /**
     * Get field from class.
     *
     * @param clazz
     * @param fieldName
     */
    fun getField(clazz: Class<*>, fieldName: String?): Field? {
        try {
            val f = clazz.getDeclaredField(fieldName)
            f.isAccessible = true
            return f
        } catch (ignored: NoSuchFieldException) {
        }
        return null
    }

    /**
     * Make the given field accessible, explicitly setting it accessible if
     * necessary. The `setAccessible(true)` method is only called when
     * actually necessary, to avoid unnecessary conflicts with a JVM SecurityManager
     * (if active).
     *
     * @param field the field to make accessible
     * @see Field.setAccessible
     */
    // on JDK 9
    fun makeAcmakeAccessible(field: Field) {
        if ((!Modifier.isPublic(field.modifiers) || !Modifier.isPublic(field.declaringClass.modifiers)
                    || Modifier.isFinal(field.modifiers)) && !field.isAccessible
        ) {
            field.isAccessible = true
        }
    }

    /**
     * Determine whether the given field is a "public static final" constant.
     *
     * @param field the field to check
     */
    fun isPublicStaticFinal(field: Field): Boolean {
        val modifiers = field.modifiers
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)
    }

    /**
     * This variant retrieves [Class.getDeclaredFields] from a local cache
     * in order to avoid the JVM's SecurityManager check and defensive array
     * copying.
     *
     * @param clazz the class to introspect
     * @return the cached array of fields
     * @throws IllegalStateException if introspection fails
     * @see Class.getDeclaredFields
     */
    private fun getDeclaredFields(clazz: Class<*>): Array<Field> {
        return clazz.declaredFields
    }

    fun getFieldByNameIncludingSuperclasses(fieldName: String?, clazz: Class<*>): Field? {
        var retValue: Field? = null
        try {
            retValue = clazz.getDeclaredField(fieldName)
        } catch (e: NoSuchFieldException) {
            val superclass = clazz.superclass
            if (superclass != null) {
                retValue = getFieldByNameIncludingSuperclasses(fieldName, superclass)
            }
        }
        return retValue
    }

    fun getFieldsIncludingSuperclasses(clazz: Class<*>): List<Field> {
        val fields: MutableList<Field> = ArrayList(Arrays.asList(*clazz.declaredFields))
        val superclass = clazz.superclass
        if (superclass != null) {
            fields.addAll(getFieldsIncludingSuperclasses(superclass))
        }
        return fields
    }

    /**
     * Make the given constructor accessible, explicitly setting it accessible if
     * necessary. The `setAccessible(true)` method is only called when
     * actually necessary, to avoid unnecessary conflicts with a JVM SecurityManager
     * (if active).
     *
     * @param ctor the constructor to make accessible
     * @see Constructor.setAccessible
     */
    // on JDK 9
    fun makeAccessible(ctor: Constructor<*>) {
        if ((!Modifier.isPublic(ctor.modifiers) || !Modifier.isPublic(ctor.declaringClass.modifiers))
            && !ctor.isAccessible
        ) {
            ctor.isAccessible = true
        }
    }

    /**
     * Obtain an accessible constructor for the given class and parameters.
     *
     * @param clazz          the clazz to check
     * @param parameterTypes the parameter types of the desired constructor
     * @return the constructor reference
     * @throws NoSuchMethodException if no such constructor exists
     * @since 5.0
     */
    @Throws(NoSuchMethodException::class)
    fun <T> accessibleConstructor(clazz: Class<T>, vararg parameterTypes: Class<*>?): Constructor<T> {
        val ctor = clazz.getDeclaredConstructor(*parameterTypes)
        makeAccessible(ctor)
        return ctor
    }
}