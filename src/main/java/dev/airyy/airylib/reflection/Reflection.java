package dev.airyy.airylib.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Reflection {

    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
        return clazz.getAnnotation(annotationClass);
    }

    public static <T, K extends Annotation> K getAnnotation(Object object, Class<K> annotationClass) {
        return getAnnotation(object.getClass(), annotationClass);
    }

    public static <T> boolean hasAnnotation(Class<T> clazz, Class<? extends Annotation> annotationClass) {
        return clazz.isAnnotationPresent(annotationClass);
    }

    public static boolean hasAnnotation(Object object, Class<? extends Annotation> annotationClass) {
        return hasAnnotation(object.getClass(), annotationClass);
    }

    public static <T> boolean hasAnnotations(Class<T> clazz, Class<? extends Annotation>... annotationClasses) {
        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            if (!hasAnnotation(clazz, annotationClass)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasAnnotations(Object object, Class<? extends Annotation>... annotationClasses) {
        return hasAnnotations(object.getClass(), annotationClasses);
    }

    public static <T, K extends Annotation> List<Method> getAnnotatedMethods(Class<T> clazz, Class<K> annotationClass) {
        List<Method> methods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                methods.add(method);
            }
        }
        return methods;
    }

    public static <T> List<Method> getAnnotatedMethods(Object object, Class<? extends Annotation> annotationClass) {
        return getAnnotatedMethods(object.getClass(), annotationClass);
    }
}