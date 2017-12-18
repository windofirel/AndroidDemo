package com.chinasie.common.models;

import com.chinasie.common.api.ApiResponse;
import com.google.gson.internal.$Gson$Types;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import okhttp3.Request;
import okhttp3.Response;

import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;
import static com.google.gson.internal.$Gson$Types.canonicalize;
import static com.google.gson.internal.$Gson$Types.typeToString;

/**
 * Created by Administrator on 2016/8/19.
 */
public abstract class BaseCallback<T> {
    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type[] types = parameterized.getActualTypeArguments();
        Type type = parameterized.getActualTypeArguments()[0];
        Type rType = parameterized.getRawType();
        Type oType = parameterized.getOwnerType();
        Type t = new ParameterizedTypeImpl(superclass, ApiResponse.class, type);
        return canonicalize(t);
        //return canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;

        public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
            // require an owner type if the raw type needs it
            if (rawType instanceof Class<?>) {
                Class<?> rawTypeAsClass = (Class<?>) rawType;
                boolean isStaticOrTopLevelClass = Modifier.isStatic(rawTypeAsClass.getModifiers())
                        || rawTypeAsClass.getEnclosingClass() == null;
                checkArgument(ownerType != null || isStaticOrTopLevelClass);
            }

            this.ownerType = ownerType == null ? null : canonicalize(ownerType);
            this.rawType = canonicalize(rawType);
            this.typeArguments = typeArguments.clone();
            for (int t = 0; t < this.typeArguments.length; t++) {
                checkNotNull(this.typeArguments[t]);
                checkNotPrimitive(this.typeArguments[t]);
                this.typeArguments[t] = canonicalize(this.typeArguments[t]);
            }
        }

        public Type[] getActualTypeArguments() {
            return typeArguments.clone();
        }

        public Type getRawType() {
            return rawType;
        }

        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof ParameterizedType
                    && $Gson$Types.equals(this, (ParameterizedType) other);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(typeArguments)
                    ^ rawType.hashCode()
                    ^ hashCodeOrZero(ownerType);
        }

        static int hashCodeOrZero(Object o) {
            return o != null ? o.hashCode() : 0;
        }

        static void checkNotPrimitive(Type type) {
            checkArgument(!(type instanceof Class<?>) || !((Class<?>) type).isPrimitive());
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(30 * (typeArguments.length + 1));
            stringBuilder.append(typeToString(rawType));

            if (typeArguments.length == 0) {
                return stringBuilder.toString();
            }

            stringBuilder.append("<").append(typeToString(typeArguments[0]));
            for (int i = 1; i < typeArguments.length; i++) {
                stringBuilder.append(", ").append(typeToString(typeArguments[i]));
            }
            return stringBuilder.append(">").toString();
        }

        private static final long serialVersionUID = 0;
    }

    public BaseCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    public abstract void onBeforeRequest(Request request);

    public abstract void onFailure(Request request, Exception e);

    /**
     * 请求成功时调用此方法
     *
     * @param response
     */
    public abstract void onResponse(Response response);

    /**
     * 状态码大于200，小于300 时调用此方法
     *
     * @param response
     * @param t
     * @throws
     */
    public abstract void onSuccess(Response response, T t);

    /**
     * 状态码400，404，403，500等时调用此方法
     *
     * @param response
     * @param code
     * @param e
     */
    public abstract void onError(Response response, int code, Exception e, String responseBody);
}
