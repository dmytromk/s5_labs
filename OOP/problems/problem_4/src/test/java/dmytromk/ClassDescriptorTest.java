package dmytromk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassDescriptorTest {

    @Test
    void getClassDescription() {
        // Create an instance of ClassDescriptor
        ClassDescriptor classDescriptor = new ClassDescriptor();

        try {
            // Get the class description for DummyClass
            String description = classDescriptor.getClassDescription("dmytromk.dummy.DummyClass");

            assertTrue(description.contains(
                    "Class name: dmytromk.dummy.DummyClass\n" +
                            "Class simple name: DummyClass\n" +
                            "Class canonical name: dmytromk.dummy.DummyClass\n" +
                            "Class superclass: class java.lang.Object\n" +
                            "Class modifiers: 1\n" +
                            "Class annotations: []\n" +
                            "Class interfaces: []\n" +
                            "\n"
            ));

            assertTrue(description.contains(
                    "Field: id\n" +
                            "Field type: int\n" +
                            "Field modifiers: 2\n"
            ));

            assertTrue(description.contains(
                    "Field: name\n" +
                            "Field type: java.lang.String\n" +
                            "Field modifiers: 2\n"
            ));

            assertTrue(description.contains(
                    "Constructor: dmytromk.dummy.DummyClass\n" +
                            "Constructor modifiers: 1\n" +
                            "Constructor parameter types: [int, class java.lang.String]\n"
            ));

            assertTrue(description.contains(
                    "Method: getId\n" +
                            "Method return type: int\n" +
                            "Method modifiers: 1\n" +
                            "Method parameter types: []\n"
            ));

            assertTrue(description.contains(
                    "Method: setId\n" +
                            "Method return type: void\n" +
                            "Method modifiers: 1\n" +
                            "Method parameter types: [int]\n"
            ));

            assertTrue(description.contains(
                    "Method: getName\n" +
                            "Method return type: java.lang.String\n" +
                            "Method modifiers: 1\n" +
                            "Method parameter types: []\n"
            ));

            assertTrue(description.contains(
                    "Method: setName\n" +
                            "Method return type: void\n" +
                            "Method modifiers: 1\n" +
                            "Method parameter types: [class java.lang.String]\n"
            ));

            assertTrue(description.contains(
                    "Method: printInfo\n" +
                            "Method return type: void\n" +
                            "Method modifiers: 1\n" +
                            "Method parameter types: []\n"
            ));

            assertTrue(description.contains(
                    "Method: performDummyOperation\n" +
                            "Method return type: void\n" +
                            "Method modifiers: 1\n" +
                            "Method parameter types: []\n"
            ));

            System.out.println(description);
        } catch (ClassNotFoundException e) {
            fail("Class not found: " + e.getMessage());
        }
    }

}