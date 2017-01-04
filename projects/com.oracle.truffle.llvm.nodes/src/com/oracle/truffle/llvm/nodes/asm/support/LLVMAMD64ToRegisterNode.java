/*
 * Copyright (c) 2016, Oracle and/or its affiliates.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.oracle.truffle.llvm.nodes.asm.support;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.llvm.nodes.api.LLVMExpressionNode;

public abstract class LLVMAMD64ToRegisterNode extends LLVMExpressionNode {
    @NodeChildren({@NodeChild(value = "register", type = LLVMExpressionNode.class), @NodeChild(value = "from", type = LLVMExpressionNode.class)})
    public abstract static class LLVMI8ToR64 extends LLVMAMD64ToRegisterNode {
        private final int shift;
        private final long mask;

        public LLVMI8ToR64(int shift) {
            this.shift = shift;
            this.mask = ~((long) LLVMExpressionNode.I8_MASK << shift);
        }

        @Specialization
        public long executeI64(long register, byte from) {
            return (register & mask) | Byte.toUnsignedLong(from) << shift;
        }

        @Specialization
        public long executeI64(long register, short from) {
            return (register & mask) | (from & ~mask) << shift;
        }

        @Specialization
        public long executeI64(long register, int from) {
            return (register & mask) | (from & ~mask) << shift;
        }

        @Specialization
        public long executeI64(long register, long from) {
            return (register & mask) | (from & ~mask) << shift;
        }
    }

    @NodeChildren({@NodeChild(value = "register", type = LLVMExpressionNode.class), @NodeChild(value = "from", type = LLVMExpressionNode.class)})
    public abstract static class LLVMI16ToR64 extends LLVMAMD64ToRegisterNode {
        private final long mask;

        public LLVMI16ToR64() {
            this.mask = ~(long) LLVMExpressionNode.I16_MASK;
        }

        @Specialization
        public long executeI64(long register, byte from) {
            return (register & mask) | Byte.toUnsignedLong(from);
        }

        @Specialization
        public long executeI64(long register, short from) {
            return (register & mask) | Short.toUnsignedLong(from);
        }

        @Specialization
        public long executeI64(long register, int from) {
            return (register & mask) | (from & ~mask);
        }

        @Specialization
        public long executeI64(long register, long from) {
            return (register & mask) | (from & ~mask);
        }
    }

    @NodeChildren({@NodeChild(value = "register", type = LLVMExpressionNode.class), @NodeChild(value = "from", type = LLVMExpressionNode.class)})
    public abstract static class LLVMI32ToR64 extends LLVMAMD64ToRegisterNode {
        private final int mask;

        public LLVMI32ToR64() {
            this.mask = ~0xFFFFFFFF;
        }

        @Specialization
        public long executeI64(long register, byte from) {
            return (register & mask) | Byte.toUnsignedLong(from);
        }

        @Specialization
        public long executeI64(long register, short from) {
            return (register & mask) | Short.toUnsignedLong(from);
        }

        @Specialization
        public long executeI64(long register, int from) {
            return (register & mask) | Integer.toUnsignedLong(from);
        }

        @Specialization
        public long executeI64(long register, long from) {
            return (register & mask) | (from & ~mask);
        }
    }
}
