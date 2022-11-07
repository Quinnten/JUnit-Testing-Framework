import java.util.*;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.Exception;
import java.lang.annotation.*;




public class Unit {


    public static Map<String, Throwable> testClass(String name) {
        HashMap<String, Throwable> testErrors = new HashMap<>();        

        try {
            Class<?> className = Class.forName(name);
            Constructor<?> cons = className.getConstructor();
            Object o = cons.newInstance();


            Method[] methods = className.getDeclaredMethods();

            ArrayList<Method> beforeClassMeths = getAnnotatedList(methods, "@BeforeClass()");
            ArrayList<Method> beforeMeths = getAnnotatedList(methods, "@Before()");
            ArrayList<Method> testMeths = getAnnotatedList(methods, "@Test()");
            ArrayList<Method> afterMeths = getAnnotatedList(methods, "@After()");
            ArrayList<Method> afterClassMeths = getAnnotatedList(methods, "@AfterClass()");

            //Invoke all the methods with annotation BeforeClass
            for (Method m: beforeClassMeths) {
                if (isStatic(m)) {
                    m.invoke(o);
                } else {
                    throw new RuntimeException();
                }
            }

            for (Method m: testMeths) {
               o = runMethod(beforeMeths, afterMeths, m, o, testErrors);
            }

            //Invoke all the methods with annotation AfterClass
            for (Method m: afterClassMeths) {
               if (isStatic(m)) {
                    m.invoke(o);
                } else {
                    throw new RuntimeException();
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);   
        }
        return testErrors;
    }



    // Returns a list of all methods with the annotation 'anno'
    private static ArrayList<Method> getAnnotatedList(Method[] methods, String anno) {
        ArrayList<Method> annoList = new ArrayList<>();
        for (Method m : methods) {
            Annotation[] annos = m.getDeclaredAnnotations();
            if (annos.length > 1) {
                throw new RuntimeException();
            } else if (annos.length == 1 && anno.equals(annos[0].toString())) {
                annoList.add(m);
            }
        }
        return sortMethods(annoList);
    }

    private static Object runMethod(ArrayList<Method> before, ArrayList<Method> after, Method test, Object o, HashMap<String, Throwable> testErrors) {
        Object result;

        try {
            //Run all before nethods
            for (Method m: before) {
                result = m.invoke(o);
            }
        } catch (Exception e){
            throw new RuntimeException("Error caught in Before methods");
        }

        try {
            result = test.invoke(o);
            testErrors.put(test.getName(), null);
        } catch (Exception e) {
            testErrors.put(test.getName(), e.getCause());
        }

        try {
            for (Method m: after) {
                result = m.invoke(o);
            }
        } catch(Exception e) {
            throw new RuntimeException("Error caught in After methods");
        }

        return o;
    }

    private static ArrayList<Method> sortMethods(ArrayList<Method> m) {
        int n = m.size();

        for (int i = 0; i < n; i++) {
            Method meth = m.get(i);
            String key = m.get(i).toString();
            int j = i - 1;

            while (j >= 0 && m.get(j).toString().compareTo(key) > 0) {
                m.set((j+1), m.get(j));
                j = j - 1;
            }
            m.set((j+1), meth);
        }
            return m;
    }

    private static boolean isStatic(Method m) {
        return m.toString().contains("static");
    }



//************************************* PART 2 ***********************************************/

    public static Map<String, Object[]> quickCheckClass(String name) {
        HashMap<String, Object[]> testErrors = new HashMap<>();  

        try {
            Class<?> className = Class.forName(name);
            Constructor<?> cons = className.getConstructor();
            Object o = cons.newInstance();

            Method[] methods = className.getDeclaredMethods();

            ArrayList<Method> beforePropMeths = getPropMeths(methods);


        } catch (Exception e) {
            throw new RuntimeException();
        }

        return testErrors;    
    }

    private static ArrayList<Method> getPropMeths(Method[] methods) {
        ArrayList<Method> annoList = new ArrayList<>();

        for (Method m : methods) {
            Annotation[] annos = m.getDeclaredAnnotations();
            

            for (int i = 0; i < annos.length; i++) {
                if (annos[i].toString().equals("@Property()")) {
                    annoList.add(m);
                }
            }
        }
        return sortMethods(annoList);
    }
} 


