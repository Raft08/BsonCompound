package be.raft.compound;

import fr.atlasworld.common.compound.CompoundNull;

public class BsonCompoundNull extends BsonCompoundElement implements CompoundNull {
    public static final BsonCompoundNull NULL = new BsonCompoundNull();

    private BsonCompoundNull() { // Single instance: No need for multiple instances of this class.
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        return obj instanceof BsonCompoundNull;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toJson() {
        return "null";
    }

    @Override
    public BsonCompoundNull clone() {
        return BsonCompoundNull.NULL;
    }
}
