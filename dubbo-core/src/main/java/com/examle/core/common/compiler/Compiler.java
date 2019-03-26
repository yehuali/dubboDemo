package com.examle.core.common.compiler;

import com.examle.core.common.extension.SPI;

@SPI("javassist")
public interface Compiler {

    Class<?> compile(String code, ClassLoader classLoader);
}
