//
// MessagePack for Java
//
// Copyright (C) 2009-2011 FURUHASHI Sadayuki
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
package org.msgpack.unpacker;

import java.io.IOException;
import java.io.Closeable;
import java.nio.ByteBuffer;
import java.math.BigInteger;
import java.lang.Iterable;
import org.msgpack.type.Value;
import org.msgpack.MessagePack;
import org.msgpack.template.Template;
import org.msgpack.packer.Unconverter;


public abstract class AbstractUnpacker implements Unpacker {
    protected MessagePack msgpack;

    protected AbstractUnpacker(MessagePack msgpack) {
	this.msgpack = msgpack;
    }

    public ByteBuffer readByteBuffer() throws IOException {
        return ByteBuffer.wrap(readByteArray());
    }


    public void readArrayEnd() throws IOException {
        readArrayEnd(false);
    }


    public void readMapEnd() throws IOException {
        readMapEnd(false);
    }


    public String readString() throws IOException {
        // TODO encoding exception
        return new String(readByteArray(), "UTF-8");
    }

    public UnpackerIterator iterator() {
        return new UnpackerIterator(this);
    }

    protected abstract void readValue(Unconverter uc) throws IOException;

    public Value readValue() throws IOException {
        Unconverter uc = new Unconverter(msgpack);
        readValue(uc);
        return uc.getResult();
    }


    public <T> T read(Class<T> klass) throws IOException {
        Template<? super T> tmpl = msgpack.lookup(klass);
        return (T) tmpl.read(this, null);
    }

    public <T> T read(T to) throws IOException {
        Template<? super T> tmpl = msgpack.lookup((Class<T>) to.getClass());
        return (T) tmpl.read(this, to);
    }

    public <T> T readOptional(Class<T> klass) throws IOException {
        return readOptional(klass, null);
    }

    public <T> T readOptional(Class<T> klass, T defaultValue) throws IOException {
        if(trySkipNil()) {
            return defaultValue;
        }
        Template<? super T> tmpl = (Template<? super T>) msgpack.lookup(klass);
        return (T) tmpl.read(this, null);
    }

    public <T> T readOptional(T to, T defaultValue) throws IOException {
        if(trySkipNil()) {
            return defaultValue;
        }
        Template<? super T> tmpl = msgpack.lookup((Class<T>) to.getClass());
        return (T) tmpl.read(this, to);
    }

    public <T> T readOptional(T to) throws IOException {
        return readOptional(to, null);
    }


    public void close() throws IOException {
    }
}
