package be.raft.compound;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import fr.atlasworld.common.compound.CompoundArray;
import fr.atlasworld.common.compound.CompoundElement;
import fr.atlasworld.common.compound.CompoundObject;
import fr.atlasworld.common.compound.CompoundPrimitive;
import org.bson.Document;
import org.bson.types.Binary;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BsonCompoundObject extends BsonCompoundElement implements CompoundObject {
    private final Document document;

    public BsonCompoundObject(@NotNull Document document) {
        Preconditions.checkNotNull(document);

        this.document = document;
    }

    public BsonCompoundObject() {
        this(new Document());
    }

    @Override
    public CompoundElement remove(@NotNull String key) {
        Preconditions.checkNotNull(key);

        Object object = this.document.remove(key);
        return BsonCompoundElement.toCompound(object);
    }

    @Override
    public CompoundObject addObject(@NotNull String key, @NotNull Consumer<CompoundObject> builder) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(builder);

        BsonCompoundObject object = new BsonCompoundObject();
        builder.accept(object);

        this.document.put(key, object.getDocument());
        return this;
    }

    @Override
    public CompoundObject addArray(@NotNull String key, @NotNull Consumer<CompoundArray> builder) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(builder);

        BsonCompoundArray array = new BsonCompoundArray();
        builder.accept(array);

        this.document.put(key, array.getElements());
        return this;
    }

    @Override
    public CompoundObject add(@NotNull String key, @NotNull CompoundElement value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(value instanceof BsonCompoundElement, "Only BSON backed compounds are supported!");

        this.document.put(key, BsonCompoundElement.fromCompound((BsonCompoundElement) value));
        return this;
    }

    @Override
    public CompoundObject add(@NotNull String key, boolean value) {
        Preconditions.checkNotNull(key);

        this.document.put(key, value);
        return this;
    }

    @Override
    public CompoundObject add(@NotNull String key, double value) {
        Preconditions.checkNotNull(key);

        this.document.put(key, value);
        return this;
    }

    @Override
    public CompoundObject add(@NotNull String key, long value) {
        Preconditions.checkNotNull(key);

        this.document.put(key, value);
        return this;
    }

    @Override
    public CompoundObject add(@NotNull String key, int value) {
        Preconditions.checkNotNull(key);

        this.document.put(key, value);
        return this;
    }

    @Override
    public CompoundObject add(@NotNull String key, byte value) {
        Preconditions.checkNotNull(key);

        Binary binary = new Binary(new byte[]{value});

        this.document.put(key, binary);
        return this;
    }

    @Override
    public CompoundObject add(@NotNull String key, byte[] value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);

        Binary binary = new Binary(value);

        this.document.put(key, binary);
        return this;
    }

    @Override
    public CompoundObject add(@NotNull String key, char value) {
        Preconditions.checkNotNull(key);

        this.document.put(key, String.valueOf(value));
        return this;
    }

    @Override
    public CompoundObject add(@NotNull String key, @NotNull String value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);

        this.document.put(key, value);
        return this;
    }

    @Override
    public Set<Map.Entry<String, CompoundElement>> entrySet() {
        return this.document.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), (CompoundElement) BsonCompoundElement.toCompound(entry.getValue())))
                .collect(Collectors.toSet());
    }

    @Override
    public int size() {
        return this.document.size();
    }

     @Override
    public boolean isEmpty() {
        return this.document.isEmpty();
    }

    @Override
    public boolean has(@NotNull String key) {
        return this.document.containsKey(key);
    }

    @Override
    public CompoundElement get(@NotNull String key) {
        Preconditions.checkNotNull(key);

        return BsonCompoundElement.toCompound(this.document.get(key));
    }

    @Override
    public CompoundPrimitive getAsPrimitive(@NotNull String key) {
        return this.get(key).getAsPrimitive();
    }

    @Override
    public CompoundArray getAsArray(@NotNull String key) {
        return this.get(key).getAsArray();
    }

    @Override
    public CompoundObject getAsObject(@NotNull String key) {
        return this.get(key).getAsObject();
    }

    @Override
    public Map<String, CompoundElement> asMap() {
        Map<String, CompoundElement> map = new HashMap<>();
        this.document.forEach((key, value) -> {
            map.put(key, BsonCompoundElement.toCompound(value));
        });

        return map;
    }

    @Override
    public String toJson() {
        JsonObject object = new JsonObject();

        this.document.forEach((key, value) -> {
            object.addProperty(key, BsonCompoundElement.toCompound(value).toJson());
        });

        return object.toString();
    }

    @Override
    public BsonCompoundObject clone() {
        return new BsonCompoundObject(new Document(this.document));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof BsonCompoundObject other))
            return false;

        return other.document.equals(this.document);
    }

    @Override
    public int hashCode() {
        return this.document.hashCode();
    }

    public Document getDocument() {
        return document;
    }
}
