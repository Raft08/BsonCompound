package be.raft.compound;

import fr.atlasworld.common.compound.CompoundElement;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public abstract class BsonCompoundElement implements CompoundElement {

    public static BsonCompoundElement toCompound(Object object) {
        if (object == null)
            return BsonCompoundNull.NULL;

        if (object instanceof Collection<?> collection)
            return new BsonCompoundArray(new ArrayList<>(collection));

        if (object instanceof Document document)
            return new BsonCompoundObject(document);

        return new BsonCompoundPrimitive(object);
    }

    public static Object fromCompound(BsonCompoundElement element) {
        if (element instanceof BsonCompoundNull)
            return null;

        if (element instanceof BsonCompoundObject object)
            return object.getDocument();

        if (element instanceof BsonCompoundArray array)
            return array.getElements();

        throw new IllegalStateException("Unsupported BsonCompoundElement!");
    }

    @Override
    public boolean isArray() {
        return this instanceof BsonCompoundArray;
    }

    @Override
    public boolean isObject() {
        return this instanceof BsonCompoundObject;
    }

    @Override
    public boolean isPrimitive() {
        return this instanceof BsonCompoundPrimitive;
    }

    @Override
    public boolean isNull() {
        return this instanceof BsonCompoundNull;
    }

    @Override
    public BsonCompoundArray getAsArray() {
        if (this.isArray())
            return (BsonCompoundArray) this;

        throw new IllegalStateException("Not a CompoundArray: " + this);
    }

    @Override
    public BsonCompoundObject getAsObject() {
        if (this.isObject())
            return (BsonCompoundObject) this;

        throw new IllegalStateException("Not a CompoundObject: " + this);
    }

    @Override
    public BsonCompoundPrimitive getAsPrimitive() {
        if (this.isPrimitive())
            return (BsonCompoundPrimitive) this;

        throw new IllegalStateException("Not a CompoundPrimitive: " + this);
    }

    @Override
    public BsonCompoundNull getAsNull() {
        if (this.isNull())
            return (BsonCompoundNull) this;

        throw new IllegalStateException("Not a CompoundNull: " + this);
    }

    @Override
    public boolean getAsBoolean() {
        throw this.unsupportedOperation();
    }

    @Override
    public double getAsDouble() {
        throw this.unsupportedOperation();
    }

    @Override
    public long getAsLong() {
        throw this.unsupportedOperation();
    }

    @Override
    public int getAsInt() {
        throw this.unsupportedOperation();
    }

    @Override
    public byte getAsByte() {
        throw this.unsupportedOperation();
    }

    @Override
    public byte[] getAsByteArray() {
        throw this.unsupportedOperation();
    }

    @Override
    public Date getAsDate() {
        throw this.unsupportedOperation();
    }

    @Override
    public char getAsChar() {
        throw this.unsupportedOperation();
    }

    @Override
    public String getAsString() {
        throw this.unsupportedOperation();
    }

    private UnsupportedOperationException unsupportedOperation() {
        return new UnsupportedOperationException(this.getClass().getName());
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public String toString() {
        return this.toJson();
    }

    @Override
    public abstract BsonCompoundElement clone();
}
