public class IntAssert {
  
  int i;

  public IntAssert(int i) {
    this.i = i;
  }

  public IntAssert isEqualTo(int i2) {
    if (i == i2) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public IntAssert isLessThan(int i2) {
    if (i < i2) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }

  public IntAssert isGreaterThan(int i2) {
    if (i > i2) {
      return this;
    } else {
      throw new RuntimeException();
    }
  }
}