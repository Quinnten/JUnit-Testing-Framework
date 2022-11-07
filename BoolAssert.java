public class BoolAssert {
  
  boolean b;

  public BoolAssert(boolean b) {
    this.b = b;
  }

  public BoolAssert isFalse() {
    if (!b) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public BoolAssert isTrue() {
    if (b) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public BoolAssert isEqualTo(boolean b2) {
    if (b == b2) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }
}