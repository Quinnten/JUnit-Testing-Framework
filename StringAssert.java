public class StringAssert {

    private String s;

    public StringAssert(String s) {
        this.s = s;
    }

    public StringAssert isNotNull() {
        if (s == null) {
            throw new RuntimeException();
        } else {
            return this;
        }
    }

    public StringAssert isNull() {
        if (s != null) {
            throw new RuntimeException();
        } else {
            return this;
        }
    }

    public StringAssert isEqualTo(Object s2) {
        if (s.equals(s2)) {
            return this;
        } else {
            throw new RuntimeException();
        }
    }

    public StringAssert isNotEqualTo(Object s2) {
        if (!s.equals(s2)) {
            return this;
        } else {
            throw new RuntimeException();
        }
    }

    public StringAssert startsWith(String s2) {
      if (s.startsWith(s2)) {
            return this;
        } else {
            throw new RuntimeException();
        }
    }

    public StringAssert isEmpty(String s2) {
      if (s == "") {
            return this;
        } else {
            throw new RuntimeException();
        }
    }

    public StringAssert contains(String s2) {
      if (s.contains(s2)) {
            return this;
        } else {
            throw new RuntimeException();
        }
    }
}