package com.github.nginate.commons.testing;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;

import static com.github.nginate.commons.testing.NArrays.generateArray;
import static com.github.nginate.commons.testing.NArrays.setArrayField;
import static com.github.nginate.commons.testing.NPrimitives.setField;
import static com.github.nginate.commons.testing.Unique.*;
import static com.googlecode.gentyref.GenericTypeReflector.erase;
import static com.googlecode.gentyref.GenericTypeReflector.getExactFieldType;
import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public class Initializer<T> {

    public static <K, V> InitContext<? extends Map<K, V>> uniqueMap(Class<K> keyType, Class<V> valueType) {
        TypeToken<Map<K, V>> typeToken = new TypeToken<Map<K, V>>() {}
                .where(new TypeParameter<K>() {}, keyType)
                .where(new TypeParameter<V>() {}, valueType);
        return new InitContext<>(typeToken);
    }

    public static <T> InitContext<Set<T>> uniqueSet(Class<T> type) {
        TypeToken<Set<T>> typeToken = new TypeToken<Set<T>>() {}
                .where(new TypeParameter<T>() {}, type);
        return new InitContext<>(typeToken);
    }

    public static <T> InitContext<Queue<T>> uniqueQueue(Class<T> type) {
        TypeToken<Queue<T>> typeToken = new TypeToken<Queue<T>>() {}
                .where(new TypeParameter<T>() {}, type);
        return new InitContext<>(typeToken);
    }

    public static <T> InitContext<List<T>> uniqueList(Class<T> type) {
        TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {}
                .where(new TypeParameter<T>() {}, type);
        return new InitContext<>(typeToken);
    }

    public static <T> InitContext<T> uniqueObject(Class<T> type) {
        return new InitContext<>(TypeToken.of(type));
    }

    public static <T> InitContext<T> uniqueObject(TypeToken<T> token) {
        return new InitContext<>(token);
    }

    private static <T> T generate(InitContext<T> context) {
        return new Initializer<>(context).create();
    }

    private final InitContext<T> context;

    private Initializer(InitContext<T> context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    private T create() {
        TypeToken<T> type = context.getContextType();

        if (type.isPrimitive()) {
            return NPrimitives.createUnique((Class<T>) type.getRawType());
        }

        if (TypeToken.of(Short.class).isSupertypeOf(type)) {
            return (T) uniqueShort();
        }

        if (TypeToken.of(Integer.class).isSupertypeOf(type)) {
            return (T) uniqueInteger();
        }

        if (TypeToken.of(Long.class).isSupertypeOf(type)) {
            return (T) uniqueLong();
        }

        if (TypeToken.of(Double.class).isSupertypeOf(type)) {
            return (T) uniqueDouble();
        }

        if (TypeToken.of(String.class).isSupertypeOf(type)) {
            return (T) uniqueString();
        }

        if (TypeToken.of(Character.class).isSupertypeOf(type)) {
            return (T) uniqueCharacter();
        }

        if (TypeToken.of(Boolean.class).isSupertypeOf(type)) {
            return (T) uniqueBoolean();
        }

        if (TypeToken.of(Float.class).isSupertypeOf(type)) {
            return (T) uniqueFloat();
        }

        if (TypeToken.of(Byte.class).isSupertypeOf(type)) {
            return (T) uniqueByte();
        }

        if (context.getNestingDepth() < 0) {
            return null;
        }

        if (type.getRawType().isEnum()) {
            T[] constants = (T[]) type.getRawType().getEnumConstants();
            return constants[0];
        }

        if (TypeToken.of(Date.class).isSupertypeOf(type)) {
            return (T) uniqueDate();
        }

        if (TypeToken.of(Instant.class).isSupertypeOf(type)) {
            return (T) uniqueInstant();
        }

        if (TypeToken.of(BigDecimal.class).isSupertypeOf(type)) {
            return (T) uniqueBigDecimal();
        }

        if (TypeToken.of(BigInteger.class).isSupertypeOf(type)) {
            return (T) uniqueBigInteger();
        }

        if (TypeToken.of(UUID.class).isSupertypeOf(type)) {
            return (T) uniqueUUID();
        }

        if (type.isArray()) {
            return (T) generateArray(type, context.getCollectionSize(), token -> generate(context.nested(token)));
        }

        if (type.getType() instanceof ParameterizedType) {
            return generateParametrizedObjectValue(type);
        }

        if (type.getRawType().isInterface() || isAbstract(type.getRawType().getModifiers())) {
            context.contextType = context.mappingFor(type.getRawType());
            return generate(context);
        }

        if (type.getRawType().equals(Object.class)) {
            return (T) uniqueLong();
        }

        T instance = (T) instantiateClass(type.getRawType());
        fillObjectFields(instance);
        return instance;
    }

    private void fillObjectFields(T instance) {
        Set<String> excludedFields = context.excludedFieldsFor(context.getContextType());

        stream(FieldUtils.getAllFields(context.getContextType().getRawType()))
                .filter(field -> !excludedFields.contains(field.getName()))
                .filter(this::isFieldAcceptableForGeneration)
                .forEach(field -> {

                    Class<?> typeClass = field.getType();
                    if (typeClass.isPrimitive()) {
                        setField(instance, field);
                    } else if (typeClass.isArray()) {
                        Object array = generateArray(typeClass, context.getCollectionSize(),
                                token -> generate(context.nested(token)));
                        setArrayField(instance, field, array);
                    } else {
                        setObjectField(instance, field);
                    }
                });
    }

    private boolean isFieldAcceptableForGeneration(Field field) {
        return !isStatic(field.getModifiers()) && !isTransient(field.getModifiers());
    }

    private void setObjectField(Object instance, Field field) {
        try {
            field.setAccessible(true);
            Type type = getExactFieldType(field, instance.getClass());
            TypeToken<?> token = TypeToken.of(type);
            field.set(instance, generate(context.nested(token)));
        } catch (IllegalAccessException e) {
            throw new ObjectInitializationException(e);
        }
    }

    private <C> C generateParametrizedObjectValue(TypeToken<C> typeToken) {
        if (TypeToken.of(Collection.class).isSupertypeOf(typeToken)) {
            //noinspection unchecked
            return (C) uniqueCollection(typeToken);
        } else if (TypeToken.of(Map.class).isSupertypeOf(typeToken)) {
            //noinspection unchecked
            return (C) uniqueMap(typeToken);
        } else {
            throw new ObjectInitializationException("Unsupported parametrized field type : " + typeToken);
        }
    }

    private Collection uniqueCollection(TypeToken<?> typeToken) {
        Collection collection = (Collection) instantiateClass(typeToken.getRawType());
        Type[] genericTypes = ((ParameterizedType) typeToken.getType()).getActualTypeArguments();

        for (int i = 0; i < context.getCollectionSize(); i++) {
            Class<?> typeClass = erase(genericTypes[0]);
            //noinspection unchecked
            collection.add(generate(context.nested(TypeToken.of(typeClass))));
        }
        return collection;
    }

    private Map uniqueMap(TypeToken<?> typeToken) {
        Type[] genericTypes = ((ParameterizedType) typeToken.getType()).getActualTypeArguments();
        return IntStream.range(0, context.getCollectionSize())
                .boxed()
                .collect(
                        toMap(
                                integer -> generate(context.nested(TypeToken.of(erase(genericTypes[0])))),
                                integer -> generate(context.nested(TypeToken.of(erase(genericTypes[1]))))
                        )
                );
    }

    private <C> C instantiateClass(Class<C> clazz) {
        try {
            if (clazz.isInterface() || isAbstract(clazz.getModifiers())) {
                return (C) instantiateClass(context.mappingFor(clazz).getRawType());
            }
            if (hasDefaultConstructor(clazz)) {
                return clazz.newInstance();
            }

            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            Object[] arguments = generateConstructorParameters(constructor);

            return (C) constructor.newInstance(arguments);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new ObjectInitializationException(e);
        }
    }

    private Object[] generateConstructorParameters(Constructor<?> constructor) {
        return stream(constructor.getParameterTypes())
                .map(aClass -> generate(context.nested(TypeToken.of(aClass))))
                .toArray(Object[]::new);
    }


    private static boolean hasDefaultConstructor(Class<?> clazz) {
        return stream(clazz.getConstructors())
                .filter(constructor -> constructor.getParameterTypes().length == 0)
                .findAny()
                .isPresent();
    }

    @SuppressWarnings("WeakerAccess")
    public static class InitContext<T> {
        private static final Map<Class<?>, TypeToken<?>> DEFAULT_IMPLEMENTATION_MAPPINGS = new HashMap<>();

        private static final int DEFAULT_COLLECTION_SIZE = 1;
        private static final int DEFAULT_NESTING_DEPTH = 1;

        static {
            DEFAULT_IMPLEMENTATION_MAPPINGS.put(List.class, TypeToken.of(ArrayList.class));
            DEFAULT_IMPLEMENTATION_MAPPINGS.put(Set.class, TypeToken.of(HashSet.class));
            DEFAULT_IMPLEMENTATION_MAPPINGS.put(Map.class, TypeToken.of(HashMap.class));
            DEFAULT_IMPLEMENTATION_MAPPINGS.put(Queue.class, TypeToken.of(LinkedList.class));
            DEFAULT_IMPLEMENTATION_MAPPINGS.put(CharSequence.class, TypeToken.of(String.class));
            DEFAULT_IMPLEMENTATION_MAPPINGS.put(Serializable.class, TypeToken.of(String.class));
            DEFAULT_IMPLEMENTATION_MAPPINGS.put(Number.class, TypeToken.of(Long.class));
        }

        @Getter
        private TypeToken<T> contextType;
        @Getter
        private int collectionSize = DEFAULT_COLLECTION_SIZE;
        @Getter
        private int nestingDepth = DEFAULT_NESTING_DEPTH;
        @Getter
        private final Map<TypeToken<?>, Set<String>> excludedFields;
        @Getter
        private final Map<Class<?>, TypeToken<?>> mappings;

        InitContext(@NonNull TypeToken<T> contextType) {
            this.contextType = contextType;
            excludedFields = Maps.newHashMap();
            mappings = Maps.newHashMap(DEFAULT_IMPLEMENTATION_MAPPINGS);
        }

        public InitContext<T> withCollectionSize(int size) {
            collectionSize = size;
            return this;
        }

        public InitContext<T> withNestingDepth(int depth) {
            nestingDepth = depth;
            return this;
        }

        public InitContext<T> withExcludedFieldsFor(@Nonnull @NonNull Class<?> clazz,
                @Nonnull @NonNull String... fieldNames) {
            TypeToken<?> typeToken = TypeToken.of(clazz);
            excludedFields.putIfAbsent(typeToken, new HashSet<>());
            excludedFields.get(typeToken).addAll(Arrays.asList(fieldNames));
            return this;
        }

        public InitContext<T> withExcludedFields(@Nonnull @NonNull Map<TypeToken<?>, Set<String>> excludedFields) {
            this.excludedFields.putAll(excludedFields);
            return this;
        }

        public InitContext<T> withMapping(@Nonnull @NonNull Class<?> interfaceClass,
                @Nonnull @NonNull Class<?> implClass) {
            if (!interfaceClass.isInterface() && !isAbstract(interfaceClass.getModifiers())) {
                throw new ObjectInitializationException("Provided key is not interface : " + interfaceClass);
            }
            if (implClass.isInterface() || implClass.isPrimitive()) {
                throw new ObjectInitializationException("Cannot use as implementation for interface : " + implClass);
            }
            mappings.put(interfaceClass, TypeToken.of(implClass));
            return this;
        }

        public InitContext<T> withMappings(@Nonnull @NonNull Map<Class<?>, TypeToken<?>> mappings) {
            mappings.entrySet().stream().forEach(entry -> {
                if (!entry.getKey().isInterface() && !isAbstract(entry.getKey().getModifiers())) {
                    throw new ObjectInitializationException("Provided key is not interface/abstract:" + entry.getKey());
                }
                if (entry.getValue().getRawType().isInterface() || entry.getValue().isPrimitive()) {
                    throw new ObjectInitializationException("Cannot use as implementation for interface : " +
                            entry.getValue());
                }
            });
            this.mappings.putAll(mappings);
            return this;
        }

        <TOKEN> TypeToken<TOKEN> mappingFor(Class<?> interfaceClass) {
            //noinspection unchecked
            return (TypeToken<TOKEN>) Optional.ofNullable(mappings.get(interfaceClass))
                    .orElseThrow(() ->
                            new ObjectInitializationException("There is not mapping for : " + interfaceClass));
        }

        Set<String> excludedFieldsFor(TypeToken<?> token) {
            return excludedFields.getOrDefault(token, Collections.emptySet());
        }

        public T generate() {
            return new Initializer<>(this).create();
        }

        <N> InitContext<N> nested(TypeToken<N> nestedToken) {
            return new InitContext<>(nestedToken)
                    .withCollectionSize(collectionSize)
                    .withNestingDepth(nestingDepth-1)
                    .withMappings(mappings)
                    .withExcludedFields(excludedFields);
        }
    }
}
