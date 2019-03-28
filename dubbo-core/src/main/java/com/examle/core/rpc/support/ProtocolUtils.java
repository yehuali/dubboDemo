package com.examle.core.rpc.support;

import com.examle.core.common.Constants;

public class ProtocolUtils {
    public static boolean isGeneric(String generic) {
        return generic != null
                && !"".equals(generic)
                && (Constants.GENERIC_SERIALIZATION_DEFAULT.equalsIgnoreCase(generic)  /* Normal generalization cal */
                || Constants.GENERIC_SERIALIZATION_NATIVE_JAVA.equalsIgnoreCase(generic) /* Streaming generalization call supporting jdk serialization */
                || Constants.GENERIC_SERIALIZATION_BEAN.equalsIgnoreCase(generic));
    }
}
