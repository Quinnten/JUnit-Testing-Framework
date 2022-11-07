public class ObjectAssert {

    private Object o;

    public ObjectAssert(Object o) {
        this.o = o;
    }

    public ObjectAssert isNotNull() {
        if (o == null) {
            throw new RuntimeException();
        } else {
            return this;
        }
    }

    public ObjectAssert isNull() {
        if (o != null) {
            throw new RuntimeException();
        } else {
            return this;
        }
    }

    public ObjectAssert isEqualTo(Object o2) {
        if (o.equals(o2)) {
            return this;
        } else {
            throw new RuntimeException();
        }
    }

    public ObjectAssert isNotEqualTo(Object o2) {
        if (!o.equals(o2)) {
            return this;
        } else {
            throw new RuntimeException();
        }
    }

    public ObjectAssert isInstanceOf(Class c) {
        if (c.isInstance(o)) {
            return this;
        } else {
            throw new RuntimeException();
        }
    }
}