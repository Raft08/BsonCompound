package be.raft.compound;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import fr.atlasworld.common.compound.CompoundArray;
import fr.atlasworld.common.compound.CompoundElement;
import fr.atlasworld.common.compound.CompoundObject;
import org.bson.types.Binary;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BsonCompoundArray extends BsonCompoundElement implements CompoundArray {
    private final List<Object> elements;

    public BsonCompoundArray(@NotNull List<Object> elements) {
        Preconditions.checkNotNull(elements);

        this.elements = elements;
    }

    public BsonCompoundArray() {
        this(new ArrayList<>());
    }


    @Override
    public CompoundArray addObject(@NotNull Consumer<CompoundObject> builder) {
        Preconditions.checkNotNull(builder);

        BsonCompoundObject compoundObject = new BsonCompoundObject();
        builder.accept(compoundObject);

        this.elements.add(compoundObject.getDocument());

        return this;
    }

    @Override
    public CompoundArray addArray(@NotNull Consumer<CompoundArray> builder) {
        Preconditions.checkNotNull(builder);

        BsonCompoundArray compoundArray = new BsonCompoundArray();
        builder.accept(compoundArray);

        this.elements.add(compoundArray.elements);

        return this;
    }

    @Override
    public CompoundArray add(@NotNull CompoundElement value) {
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(value instanceof BsonCompoundElement, "Only BSON backed compounds are supported!");

        Object object = BsonCompoundElement.fromCompound((BsonCompoundElement) value);
        this.elements.add(object);

        return this;
    }

    @Override
    public CompoundArray add(boolean value) {
        this.elements.add(value);

        return this;
    }

    @Override
    public CompoundArray add(double value) {
        this.elements.add(value);

        return this;
    }

    @Override
    public CompoundArray add(long value) {
        this.elements.add(value);

        return this;
    }

    @Override
    public CompoundArray add(int value) {
        this.elements.add(value);

        return this;
    }

    @Override
    public CompoundArray add(byte value) {
        Binary binary = new Binary(new byte[]{value});
        this.elements.add(binary);

        return this;
    }

    @Override
    public CompoundArray add(byte[] value) {
        Binary binary = new Binary(value);
        this.elements.add(binary);

        return this;
    }

    @Override
    public CompoundArray add(char value) {
        this.elements.add(String.valueOf(value));

        return this;
    }

    @Override
    public CompoundArray add(@NotNull String value) {
        Preconditions.checkNotNull(value);

        this.elements.add(value);

        return this;
    }

    @Override
    public CompoundArray addAll(@NotNull CompoundArray array) {
        Preconditions.checkNotNull(array);
        Preconditions.checkArgument(array instanceof BsonCompoundArray, "Only BSON backed compounds are supported!");

        this.elements.addAll(((BsonCompoundArray) array).elements);

        return this;
    }

    @Override
    public CompoundElement setObject(int index, @NotNull Consumer<CompoundObject> builder) {
        Preconditions.checkNotNull(builder);

        BsonCompoundObject compoundObject = new BsonCompoundObject();
        builder.accept(compoundObject);

        this.elements.add(compoundObject.getDocument());
        return this;
    }

    @Override
    public CompoundElement setArray(int index, @NotNull Consumer<CompoundArray> builder) {
        Preconditions.checkNotNull(builder);

        BsonCompoundArray compoundArray = new BsonCompoundArray();
        builder.accept(compoundArray);

        this.elements.add(compoundArray.elements);
        return this;
    }

    @Override
    public CompoundElement set(int index, @NotNull CompoundElement element) {
        Preconditions.checkNotNull(element);
        Preconditions.checkArgument(element instanceof BsonCompoundElement, "Only BSON backed compounds are supported!");

        Object object = BsonCompoundElement.fromCompound((BsonCompoundElement) element);
        this.elements.set(index, object);

        return this;
    }

    @Override
    public CompoundElement set(int index, boolean value) {
        return null;
    }

    @Override
    public CompoundElement set(int index, double value) {
        this.elements.add(value);

        return this;
    }

    @Override
    public CompoundElement set(int index, long value) {
        this.elements.set(index, value);

        return this;
    }

    @Override
    public CompoundElement set(int index, int value) {
        this.elements.set(index, value);

        return this;
    }

    @Override
    public CompoundElement set(int index, byte value) {
        Binary binary = new Binary(new byte[]{value});
        this.elements.set(index, binary);

        return this;
    }

    @Override
    public CompoundElement set(int index, byte[] value) {
        Preconditions.checkNotNull(value);

        Binary binary = new Binary(value);
        this.elements.set(index, binary);

        return this;
    }

    @Override
    public CompoundElement set(int index, char value) {
        this.elements.set(index, value);

        return this;
    }

    @Override
    public CompoundElement set(int index, @NotNull String value) {
        Preconditions.checkNotNull(value);

        this.elements.set(index, value);

        return this;
    }

    @Override
    public boolean remove(@NotNull CompoundElement element) {
        Preconditions.checkNotNull(element);
        Preconditions.checkArgument(element instanceof BsonCompoundElement, "Only BSON backed compounds are supported!");

        Object object = BsonCompoundElement.fromCompound((BsonCompoundElement) element);
        return this.elements.remove(object);
    }

    @Override
    public CompoundElement remove(int index) {
        Object stored = this.elements.remove(index);
        return BsonCompoundElement.toCompound(stored);
    }

    @Override
    public boolean contains(@NotNull CompoundElement element) {
        Preconditions.checkNotNull(element);
        Preconditions.checkArgument(element instanceof BsonCompoundElement, "Only BSON backed compounds are supported!");

        Object object = BsonCompoundElement.fromCompound((BsonCompoundElement) element);
        return this.elements.contains(object);
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    @Override
    public CompoundElement get(int index) {
        return BsonCompoundElement.toCompound(this.elements.get(index));
    }

    @Override
    public List<CompoundElement> asList() {
        return this.elements.stream()
                .map(BsonCompoundElement::toCompound)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toJson() {
        JsonArray array = new JsonArray();
        this.elements.stream()
                .map(BsonCompoundElement::toCompound)
                .map(CompoundElement::toJson)
                .forEach(array::add);

        return array.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof BsonCompoundArray other))
            return false;

        return other.elements.equals(this.elements);
    }

    @Override
    public int hashCode() {
        return this.elements.hashCode();
    }

    @Override
    public BsonCompoundArray clone() {
        List<Object> list = new ArrayList<>();
        list.addAll(this.elements);

        return new BsonCompoundArray(list);
    }

    @NotNull
    @Override
    public Iterator<CompoundElement> iterator() {
        return this.elements.stream()
                .map(BsonCompoundElement::toCompound)
                .map(compound -> (CompoundElement) compound)
                .iterator();
    }

    public List<Object> getElements() {
        return elements;
    }
}
