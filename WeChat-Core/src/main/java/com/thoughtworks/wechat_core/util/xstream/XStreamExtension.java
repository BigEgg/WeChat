package com.thoughtworks.wechat_core.util.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Writer;
import java.lang.reflect.Field;

public class XStreamExtension {
    public static XStream createXStreamWithCData() {
        return new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    boolean cdata = false;
                    Class<?> targetClass = null;

                    @Override
                    public void startNode(String name,
                                          @SuppressWarnings("rawtypes") Class clazz) {
                        super.startNode(name, clazz);
                        if (!name.equals("xml")) {
                            cdata = needCDATA(targetClass, name);
                        } else {
                            targetClass = clazz;
                        }
                    }

                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata) {
                            writer.write(wrapCData(text));
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        });
    }

    private static String wrapCData(final String text) {
        return String.format("<![CDATA[%s]]>", text);
    }

    private static boolean needCDATA(final Class<?> targetClass, final String fieldAlias) {
        if (existsCDATA(targetClass, fieldAlias)) return true;

        Class<?> superClass = targetClass.getSuperclass();
        while (!superClass.equals(Object.class)) {
            if (existsCDATA(superClass, fieldAlias)) return true;
            superClass = superClass.getClass().getSuperclass();
        }
        return false;
    }

    private static boolean existsCDATA(final Class<?> clazz, final String fieldAlias) {
        final Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(XStreamCData.class) != null) {
                final XStreamAlias xStreamAlias = field.getAnnotation(XStreamAlias.class);
                if (null != xStreamAlias) {
                    if (fieldAlias.equals(xStreamAlias.value())) {
                        return true;
                    }
                } else {
                    if (fieldAlias.equals(field.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
