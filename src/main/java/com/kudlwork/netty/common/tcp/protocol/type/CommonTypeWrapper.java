package com.kudlwork.netty.common.tcp.protocol.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CommonTypeWrapper {

    @Getter
    @AllArgsConstructor
    public enum BooleanSpaceYNType implements EnumType {
        TRUE("Y"),
        FALSE("N"),
        SPACE(" "),
        NONE("");

        private String code;

        @Override
        public String readValue() {
            return this.code;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum BooleanYNType implements EnumType {
        TRUE("Y"),
        FALSE("N"),
        NONE("");

        private String code;

        @Override
        public String readValue() {
            return this.code;
        }
    }
}
