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
package java.lang.invoke;

import org.qbicc.rt.annotation.Tracking;
import org.qbicc.runtime.NoReflect;
import org.qbicc.runtime.patcher.Add;
import org.qbicc.runtime.patcher.Replace;
import org.qbicc.runtime.patcher.PatchClass;

@Tracking("src/java.base/share/classes/java/lang/invoke/MemberName.java")
@PatchClass(MemberName.class)
public class MemberName$_patch {
    // These fields are set via JNI by hotspot natives invoked from libjava/StackStreamFactory.c
    // Alias so we can emulate this behavior from our Java "natives"
    private Class<?> clazz;       // class in which the member is defined
    private String   name;        // may be null if not yet materialized

    @Add
    @NoReflect
    public final void setClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Add
    @NoReflect
    public final void setName(String name) {
        this.name = name;
    }
}
