import command.DontSave;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;

public class CustomClassLoader extends ObjectInputStream {
    public CustomClassLoader(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    @Override
    protected Class<?> resolveClass(final ObjectStreamClass objectStreamClass) {
        String classe = objectStreamClass.getName();
        try {

            Class<?> c = Class.forName(classe);
            Annotation a = c.getAnnotation(DontSave.class);
            if (a == null) {
                return c;
            }
            else return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
