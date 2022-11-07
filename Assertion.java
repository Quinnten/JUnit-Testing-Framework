public class Assertion {
    /* You'll need to change the return type of the assertThat methods */
    static ObjectAssert assertThat(Object o) {
        ObjectAssert obj = new ObjectAssert(o);
        return obj;
    }

    static StringAssert assertThat(String s) {
	    StringAssert str = new StringAssert(s);
        return str;
    }

    static BoolAssert assertThat(boolean b) {
	    BoolAssert bool = new BoolAssert(b);
        return bool;
    }

    static IntAssert assertThat(int i) {
        IntAssert intA = new IntAssert(i);
        return intA;    
    }
}


