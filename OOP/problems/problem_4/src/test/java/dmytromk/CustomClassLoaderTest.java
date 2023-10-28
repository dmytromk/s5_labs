package dmytromk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomClassLoaderTest {
    @Test
    void findClass() {
        CustomClassLoader customClassLoader = new CustomClassLoader();

        final String dummyClassName = "dmytromk.dummy.DummyClass";
        assertDoesNotThrow(() ->
                assertEquals("DummyClass", customClassLoader.findClass(dummyClassName).getSimpleName())
        );
        assertThrows(ClassNotFoundException.class, () ->
                customClassLoader.findClass("dmytromk.task4.NonExistentClass")
        );
    }
}