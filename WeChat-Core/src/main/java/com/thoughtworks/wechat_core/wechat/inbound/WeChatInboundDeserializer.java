package com.thoughtworks.wechat_core.wechat.inbound;

import com.thoughtworks.wechat_core.wechat.inbound.event.WeChatSubscribeEvent;
import com.thoughtworks.wechat_core.wechat.inbound.message.WeChatInboundTextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class WeChatInboundDeserializer {
    private static final Map<Function<String, Boolean>, Class> canDeserialize;

    static {
        canDeserialize = new HashMap<>();

        canDeserialize.put(WeChatInboundDeserializer::canHandleSubscribeEvent, WeChatSubscribeEvent.class);
        canDeserialize.put(WeChatInboundDeserializer::canHandelTextMessage, WeChatInboundTextMessage.class);
    }

    public static Optional<WeChatInbound> tryDeserialize(String inputString) {
        for (Map.Entry<Function<String, Boolean>, Class> deserializerEntry : canDeserialize.entrySet()) {
            if (deserializerEntry.getKey().apply(inputString)) {
                XStream xstream = new XStream(new DomDriver());
                xstream.processAnnotations(deserializerEntry.getValue());
                return Optional.ofNullable((WeChatInbound) xstream.fromXML(inputString));
            }
        }
        return Optional.empty();
    }

    private static boolean canHandleSubscribeEvent(String inputString) {
        final String subscribeEventKeyPoint = "<Event><![CDATA[subscribe]]></Event>";
        return inputString.toLowerCase().contains(subscribeEventKeyPoint.toLowerCase());
    }

    private static boolean canHandelTextMessage(String inputString) {
        final String textMessageKeyPoint = "<MsgType><![CDATA[text]]></MsgType>";
        return inputString.toLowerCase().contains(textMessageKeyPoint.toLowerCase());
    }
}
