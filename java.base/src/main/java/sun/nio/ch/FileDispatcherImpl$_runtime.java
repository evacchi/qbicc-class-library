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

package sun.nio.ch;

import static org.qbicc.runtime.CNative.*;
import static org.qbicc.runtime.posix.SysSocket.*;

import java.io.IOError;
import java.io.IOException;

import org.qbicc.rt.annotation.Tracking;
import org.qbicc.runtime.Build;
import org.qbicc.runtime.patcher.Add;
import org.qbicc.runtime.patcher.PatchClass;
import org.qbicc.runtime.patcher.RunTimeAspect;
import org.qbicc.runtime.posix.Unistd;

@PatchClass(FileDispatcherImpl.class)
@RunTimeAspect
@Tracking("src/java.base/unix/native/libnio/ch/FileDispatcherImpl.c")
@Tracking("src/java.base/unix/classes/sun/nio/ch/FileDispatcherImpl.java")
@Tracking("src/java.base/windows/native/libnio/ch/FileDispatcherImpl.c")
class FileDispatcherImpl$_runtime {
    @Add
    private static final c_int preCloseFd;

    static {
        if (Build.Target.isPosix()) {
            c_int[] sp = new c_int[2];
            if ((socketpair(AF_UNIX, SOCK_STREAM, zero(), sp).isNegative())) {
                throw new IOError(new IOException("socketpair failed"));
            }
            preCloseFd = sp[0];
            Unistd.close(sp[1]);
        } else {
            // not used on Windows
            preCloseFd = zero();
        }
    }
}
