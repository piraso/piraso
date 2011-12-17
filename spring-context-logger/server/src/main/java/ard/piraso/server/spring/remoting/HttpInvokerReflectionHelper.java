/*
 * Copyright (c) 2011. Piraso Alvin R. de Leon. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Piraso licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ard.piraso.server.spring.remoting;

import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;

/**
 * Helper reflection for {@link SimpleHttpInvokerRequestExecutor} class.
 */
public class HttpInvokerReflectionHelper {

    private Class<SimpleHttpInvokerRequestExecutor> clazz = SimpleHttpInvokerRequestExecutor.class;

    private SimpleHttpInvokerRequestExecutor instance;

    public HttpInvokerReflectionHelper(SimpleHttpInvokerRequestExecutor instance) {
        this.instance = instance;
    }

    private Method findMethod(String name, Class<?>... paramTypes) {
        Method method = ReflectionUtils.findMethod(clazz, name, paramTypes);
        method.setAccessible(true);

        return method;
    }

    private Object invokeMethod(String name, Class<?> paramTypes, Object args) throws IOException {
        return invokeMethod(name, new Class[] {paramTypes}, new Object[] {args});
    }
    
    private Object invokeMethod(String name, Class<?>[] paramTypes, Object[] args) throws IOException {
            try {
            Method method = findMethod(name, paramTypes);

            return method.invoke(instance, args);
        } catch (InvocationTargetException e) {
            if(RuntimeException.class.isInstance(e.getTargetException())) {
                throw (RuntimeException) e.getTargetException();
            } else if(IOException.class.isInstance(e.getTargetException())) {
                throw (IOException) e.getTargetException();
            }

            throw new HttpInvokerReflectionException(e);
        } catch (IllegalAccessException e) {
            throw new HttpInvokerReflectionException(e);
        }
    }

    public ByteArrayOutputStream getByteArrayOutputStream(RemoteInvocation invocation) throws IOException {
        return (ByteArrayOutputStream) invokeMethod("getByteArrayOutputStream", RemoteInvocation.class, invocation);
    }

    public HttpURLConnection openConnection(HttpInvokerClientConfiguration config) throws IOException {
        return (HttpURLConnection) invokeMethod("openConnection", HttpInvokerClientConfiguration.class, config);
    }

    public void prepareConnection(HttpURLConnection con, int contentLength) throws IOException {
        invokeMethod("prepareConnection", new Class<?>[] {HttpURLConnection.class, Integer.TYPE}, new Object[] {con, contentLength});
    }

    public void writeRequestBody(
            HttpInvokerClientConfiguration config, HttpURLConnection con, ByteArrayOutputStream baos)
            throws IOException {
        invokeMethod("writeRequestBody",
                new Class<?>[] {HttpInvokerClientConfiguration.class, HttpURLConnection.class, ByteArrayOutputStream.class},
                new Object[] {config, con, baos});
    }

    public void validateResponse(HttpInvokerClientConfiguration config, HttpURLConnection con)
            throws IOException {
        invokeMethod("validateResponse",
                new Class<?>[] {HttpInvokerClientConfiguration.class, HttpURLConnection.class},
                new Object[] {config, con});
    }

    public InputStream readResponseBody(HttpInvokerClientConfiguration config, HttpURLConnection con)
            throws IOException {
        return (InputStream) invokeMethod("readResponseBody",
                new Class<?>[] {HttpInvokerClientConfiguration.class, HttpURLConnection.class},
                new Object[] {config, con});
    }

    public RemoteInvocationResult readRemoteInvocationResult(InputStream is, String codebaseUrl)
            throws IOException, ClassNotFoundException {
        return (RemoteInvocationResult) invokeMethod("readRemoteInvocationResult",
                new Class<?>[] {InputStream.class, String.class},
                new Object[] {is, codebaseUrl});
    }
}
