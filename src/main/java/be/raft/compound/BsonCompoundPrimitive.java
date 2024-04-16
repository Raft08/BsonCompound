package be.raft.compound;

import com.google.common.base.Preconditions;
import fr.atlasworld.common.compound.CompoundPrimitive;
import org.bson.types.Binary;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;
import java.util.Date;

public class BsonCompoundPrimitive extends BsonCompoundElement implements CompoundPrimitive {
    private final @NotNull Object value;

    public BsonCompoundPrimitive(@NotNull Object value) {
        Preconditions.checkNotNull(value, "Use BsonCompoundNull for null values!");

        this.value = value;
    }

    @Override
    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }

    @Override
    public boolean getAsBoolean() {
        if (this.isBoolean())
            return (Boolean) this.value;

        throw new UnsupportedOperationException("Primitive is not a boolean: " + this);
    }

    @Override
    public boolean isDouble() {
        return this.value instanceof Double;
    }

    @Override
    public double getAsDouble() {
        if (this.isDouble())
            return (Double) this.value;

        throw new UnsupportedOperationException("Primitive is not double: " + this);
    }

    @Override
    public boolean isLong() {
        return this.value instanceof Long;
    }

    @Override
    public long getAsLong() {
        if (this.isLong())
            return (Long) this.value;

        throw new UnsupportedOperationException("Primitive is not long: " + this);
    }

    @Override
    public boolean isInt() {
        return this.value instanceof Integer;
    }

    @Override
    public int getAsInt() {
        if (this.isInt())
            return (Integer) this.value;

        throw new UnsupportedOperationException("Primitive is not integer: " + this);
    }

    @Override
    public boolean isByte() {
        if (!(this.value instanceof Binary binary))
            return false;

        return binary.length() < 2;
    }

    @Override
    public byte getAsByte() {
        if (!this.isByte())
            throw new UnsupportedOperationException("Primitive is not byte: " + this);

        Binary binary = (Binary) this.value;

        if (binary.length() < 1) // Empty Binary so we send back a 0-byte value.
            return (byte) 0;

        return binary.getData()[0];
    }

    @Override
    public boolean isByteArray() {
        return this.value instanceof Binary;
    }

    @Override
    public byte[] getAsByteArray() {
        if (this.isByteArray())
            return ((Binary) this.value).getData();

        throw new UnsupportedOperationException("Primitive is not byte array: " + this);
    }

    @Override
    public boolean isDate() {
        return this.value instanceof Date;
    }

    @Override
    public Date getAsDate() {
        if (this.isDate())
            return (Date) this.value;

        throw new UnsupportedOperationException("Primitive is not date: " + this);
    }

    @Override
    public boolean isChar() {
        if (!(this.value instanceof String string))
            return false;

        return string.length() == 1;
    }

    @Override
    public char getAsChar() {
        if (this.isChar())
            return ((String) this.value).charAt(0);

        throw new UnsupportedOperationException("Primitive is not char: " + this);
    }

    @Override
    public boolean isString() {
        return this.value instanceof String;
    }

    @Override
    public String getAsString() {
        if (this.isString())
            return (String) this.value;

        throw new UnsupportedOperationException("Primitive is not string: " + this);
    }

    @Override
    public String toJson() {
        if (this.value instanceof Binary binary) {
            byte[] data = binary.getData();
            return Base64.getEncoder().encodeToString(data);
        }

        return String.valueOf(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof BsonCompoundPrimitive bsonCompoundPrimitive))
            return false;

        return this.value.equals(bsonCompoundPrimitive.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public BsonCompoundPrimitive clone() {
        return new BsonCompoundPrimitive(this.value);
    }
}
