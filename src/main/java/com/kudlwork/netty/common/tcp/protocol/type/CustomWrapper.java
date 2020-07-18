package com.kudlwork.netty.common.tcp.protocol.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CustomWrapper {
    @Getter
    @AllArgsConstructor
    public enum BusinessType implements EnumType {
        DEFAULT("001"),
        NONE("NONE");

        private String code;

        @Override
        public String readValue() {
            return String.valueOf(this.code);
        }
    }

    @Getter
    @AllArgsConstructor
    public enum MessageType implements EnumType {
        REQUEST("1000"),
        RESPONSE("1001"),
        NONE("NONE");

        private String code;

        @Override
        public String readValue() {
            return String.valueOf(this.code);
        }
    }

}
