/*
 * This code is based on OpenJDK source file(s) which contain the following copyright notice:
 *
 * ------
 * Copyright (c) 2001, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 * ------
 *
 * This file may contain additional modifications which are Copyright (c) Red Hat and other
 * contributors.
 */

package jdk.internal.misc;

import static org.qbicc.runtime.CNative.*;
import static org.qbicc.runtime.linux.Stdlib.*;
import static org.qbicc.runtime.stdc.Stdint.*;
import static org.qbicc.runtime.stdc.Stdlib.*;
import static org.qbicc.runtime.stdc.String.*;

import java.security.ProtectionDomain;

import jdk.internal.vm.annotation.IntrinsicCandidate;
import org.qbicc.rt.annotation.Tracking;
import org.qbicc.runtime.Build;
import org.qbicc.runtime.llvm.LLVM;
import org.qbicc.runtime.main.CompilerIntrinsics;
import org.qbicc.runtime.stdc.Stddef;
import org.qbicc.runtime.stdc.Stdint;

@Tracking("src/java.base/share/classes/jdk/internal/misc/Unsafe.java")
public final class Unsafe$_native {

    private static void registerNatives() {
        // no-op
    }

    int getLoadAverage0(double[] loadavg, int nelems) {
        if (Build.Target.isLinux()) {
            _Float64[] values = new _Float64[nelems];
            return getloadavg(values, word(nelems)).intValue();
        }
        return 0;
    }

    public void throwException(Throwable ee) throws Throwable {
        throw ee;
    }

    public Class<?> defineClass0(String name, byte[] b, int off, int len,
                                 ClassLoader loader,
                                 ProtectionDomain protectionDomain) {
        throw new UnsupportedOperationException("Cannot define classes at run time");
    }

    private Class<?> defineAnonymousClass0(Class<?> hostClass, byte[] data, Object[] cpPatches) {
        throw new UnsupportedOperationException("Cannot define classes at run time");
    }

    @SuppressWarnings("ConstantConditions")
    private Unsafe asUnsafe() {
        return (Unsafe) (Object) this;
    }

    public Object allocateInstance(Class<?> clazz) throws InstantiationException {
        return CompilerIntrinsics.emitNew(clazz);
    }

    private long allocateMemory0(long bytes) {
        return malloc(word(bytes).cast()).longValue();
    }

    private long reallocateMemory0(long address, long bytes) {
        return realloc(word(address).cast(), word(bytes).cast()).longValue();
    }

    private void freeMemory0(long address) {
        free(word(address).cast());
    }

    private void setMemory0(Object o, long offset, long bytes, byte value) {
        memset(refToPtr(o).cast(char_ptr.class).plus(offset).cast(), word(value).cast(), word(bytes).cast());
    }

    // todo: remove when qbicc 0.17.0 is released
    @extern
    @include("<string.h>")
    public static native void_ptr memmove(void_ptr dest, const_void_ptr src, Stddef.size_t n);

    private void copyMemory0(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes) {
        ptr<c_char> srcPtr = refToPtr(srcBase).cast(char_ptr.class).plus(srcOffset);
        ptr<c_char> destPtr = refToPtr(destBase).cast(char_ptr.class).plus(destOffset);
        memmove(destPtr.cast(), srcPtr.cast(), word(bytes).cast());
    }

    private void copySwapMemory0(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes, long elemSize) {
        if (elemSize > 16 || Long.bitCount(elemSize) != 1) {
            throw new IllegalArgumentException();
        }
        switch ((int) elemSize) {
            case 1 -> copyMemory0(srcBase, srcOffset, destBase, destOffset, bytes);
            case 2 -> {
                int16_t_ptr srcPtr = refToPtr(srcBase).cast(char_ptr.class).plus(srcOffset).cast(int16_t_ptr.class);
                int16_t_ptr destPtr = refToPtr(destBase).cast(char_ptr.class).plus(destOffset).cast(int16_t_ptr.class);
                long cnt = bytes >> 1;
                for (long i = 0; i < cnt; i ++) {
                    destPtr.plus(i).storePlain(word(LLVM.byteSwap(srcPtr.plus(i).loadPlain().shortValue())));
                }
            }
            case 4 -> {
                int32_t_ptr srcPtr = refToPtr(srcBase).cast(char_ptr.class).plus(srcOffset).cast(int32_t_ptr.class);
                int32_t_ptr destPtr = refToPtr(destBase).cast(char_ptr.class).plus(destOffset).cast(int32_t_ptr.class);
                long cnt = bytes >> 2;
                for (long i = 0; i < cnt; i ++) {
                    destPtr.plus(i).storePlain(word(LLVM.byteSwap(srcPtr.plus(i).loadPlain().intValue())));
                }
            }
            case 8 -> {
                int64_t_ptr srcPtr = refToPtr(srcBase).cast(char_ptr.class).plus(srcOffset).cast(int64_t_ptr.class);
                int64_t_ptr destPtr = refToPtr(destBase).cast(char_ptr.class).plus(destOffset).cast(int64_t_ptr.class);
                long cnt = bytes >> 3;
                for (long i = 0; i < cnt; i ++) {
                    destPtr.plus(i).storePlain(word(LLVM.byteSwap(srcPtr.plus(i).loadPlain().longValue())));
                }
            }
            case 16 -> {
                int64_t_ptr srcPtr = refToPtr(srcBase).cast(char_ptr.class).plus(srcOffset).cast(int64_t_ptr.class);
                int64_t_ptr destPtr = refToPtr(destBase).cast(char_ptr.class).plus(destOffset).cast(int64_t_ptr.class);
                long cnt = bytes >> 3;
                for (long i = 0; i < cnt; i += 2) {
                    long a = srcPtr.plus(i).loadPlain().longValue();
                    long b = srcPtr.plus(i + 1).loadPlain().longValue();
                    destPtr.plus(i).storePlain(word(LLVM.byteSwap(b)));
                    destPtr.plus(i + 1).storePlain(word(LLVM.byteSwap(a)));
                }
            }
        }
    }

    //TODO:

    //unpark
    //park
}
