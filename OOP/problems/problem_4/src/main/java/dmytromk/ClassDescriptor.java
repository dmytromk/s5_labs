package dmytromk;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassDescriptor {
    private static final CustomClassLoader customClassLoader = new CustomClassLoader();

    public String getClassDescription(String className) throws ClassNotFoundException {
        Class<?> clazz = customClassLoader.findClass(className);

        StringBuilder stringBuilder = new StringBuilder()
                .append("Class name: ")
                .append(clazz.getName())
                .append("\nClass simple name: ")
                .append(clazz.getSimpleName())
                .append("\nClass canonical name: ")
                .append(clazz.getCanonicalName())
                .append("\nClass superclass: ")
                .append(clazz.getSuperclass())
                .append("\nClass modifiers: ")
                .append(clazz.getModifiers())
                .append("\nClass annotations: ")
                .append(Arrays.toString(clazz.getAnnotations()))
                .append("\nClass interfaces: ")
                .append(Arrays.toString(clazz.getInterfaces()))
                .append("\n\n");

        for (Constructor<?> constructor : clazz.getConstructors())
            appendConstructors(constructor, stringBuilder);

        for (Method method : clazz.getMethods())
            appendMethods(method, stringBuilder);

        for (Field field : clazz.getDeclaredFields())
            appendField(field, stringBuilder);

        return stringBuilder.toString();
    }

    private void appendConstructors(Constructor<?> constructor, StringBuilder stringBuilder) {
        stringBuilder
                .append("\nConstructor: ")
                .append(constructor.getName())
                .append("\nConstructor modifiers: ")
                .append(constructor.getModifiers())
                .append("\nConstructor parameter types: ")
                .append(Arrays.toString(constructor.getParameterTypes()))
                .append("\n");
    }

    private void appendMethods(Method method, StringBuilder stringBuilder) {
        stringBuilder
                .append("\nMethod: ")
                .append(method.getName())
                .append("\nMethod return type: ")
                .append(method.getReturnType().getCanonicalName())
                .append("\nMethod modifiers: ")
                .append(method.getModifiers())
                .append("\nMethod parameter types: ")
                .append(Arrays.toString(method.getParameterTypes()))
                .append("\n");
    }

    private void appendField(Field field, StringBuilder stringBuilder) {
        stringBuilder
                .append("\nField: ")
                .append(field.getName())
                .append("\nField type: ")
                .append(field.getType().getCanonicalName())
                .append("\nField modifiers: ")
                .append(field.getModifiers())
                .append("\n");
    }
}
