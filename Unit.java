import java.util.*;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.Exception;
import java.lang.annotation.*;
import java.util.Arrays;


public class Unit {
    private static int count = 0;
    private static Object instance;

    public static Map<String, Throwable> testClass(String name) {
        HashMap<String, Throwable> testErrors = new HashMap<>();        

        try {
            Class<?> className = Class.forName(name);
            Constructor<?> cons = className.getConstructor();
            instance = cons.newInstance();


            Method[] methods = className.getDeclaredMethods();

            ArrayList<Method> beforeClassMeths = getAnnotatedList(methods, "@BeforeClass()");
            ArrayList<Method> beforeMeths = getAnnotatedList(methods, "@Before()");
            ArrayList<Method> testMeths = getAnnotatedList(methods, "@Test()");
            ArrayList<Method> afterMeths = getAnnotatedList(methods, "@After()");
            ArrayList<Method> afterClassMeths = getAnnotatedList(methods, "@AfterClass()");

            //Invoke all the methods with annotation BeforeClass
            for (Method m: beforeClassMeths) {
                if (isStatic(m)) {
                    m.invoke(instance);
                } else {
                    throw new RuntimeException();
                }
            }

            for (Method m: testMeths) {
               instance = runMethod(beforeMeths, afterMeths, m, testErrors);
            }

            //Invoke all the methods with annotation AfterClass
            for (Method m: afterClassMeths) {
               if (isStatic(m)) {
                    m.invoke(instance);
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

    private static Object runMethod(ArrayList<Method> before, ArrayList<Method> after, Method test, HashMap<String, Throwable> testErrors) {
        Object result;
        try {
            //Run all before nethods
            for (Method m: before) {
                result = m.invoke(instance);
            }
        } catch (Exception e){
            throw new RuntimeException("Error caught in Before methods");
        }

        try {
            result = test.invoke(instance);
            testErrors.put(test.getName(), null);
        } catch (Exception e) {
            testErrors.put(test.getName(), e.getCause());
        }

        try {
            for (Method m: after) {
                result = m.invoke(instance);
            }
        } catch(Exception e) {
            throw new RuntimeException("Error caught in After methods");
        }

        return instance;
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
//********************************************************************************************/
//********************************************************************************************/
//********************************************************************************************/


    public static Map<String, Object[]> quickCheckClass(String name) {
        HashMap<String, Object[]> testErrors = new HashMap<>();  

        try {
            Class<?> className = Class.forName(name);
            Constructor<?> cons = className.getConstructor();
            instance = null;
            instance = cons.newInstance();
      
            Method[] methods = className.getDeclaredMethods();

            ArrayList<Method> beforePropMeths = getPropMeths(methods);

            for (Method m : beforePropMeths) {
                runPropMeth(m, testErrors);
            }       

        } catch (Exception e) {
            //System.out.println(e.getCause());
            throw new RuntimeException();
        }
        System.out.println(testErrors);
        for (String o : testErrors.keySet()) {
            Object[] val = testErrors.get(o);
            if (val != null) {
                for (Object obj : val) {
                    System.out.println(obj);
                }
            } 
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

    private static void runPropMeth(Method m, HashMap<String, Object[]> testErrors) {
        Annotation[][] annos = m.getParameterAnnotations();
        ArrayList<Annotation> annotationList = new ArrayList<>();

        for (int i = 0; i < annos.length; i++) {
            annotationList.add(annos[i][0]);

            if (annos[i][0].annotationType().equals(ListLength.class)) {
                AnnotatedType type = m.getAnnotatedParameterTypes()[0];
                AnnotatedType ant = ((AnnotatedParameterizedType) type).getAnnotatedActualTypeArguments()[0];
                annotationList.add(ant.getAnnotations()[0]);
            }
        }

        try {
        count = 0;
        Object[] parameters = new Object[annotationList.size()];
        System.out.println("*****************************************************");

        System.out.println("Running new test " + m.getName()); 
        System.out.println("Num of Params: " + parameters.length);        
       


        testErrors.put(m.getName(), recurseInvoke(m, parameters, annotationList, 0));
        } catch (Exception e) {
            System.out.println("We have an error with " + m.getName());
            System.out.println(e.getCause());
        }
    }



    private static Object[] recurseInvoke(Method m, Object[] params, ArrayList<Annotation> anno, int index) {
        if (index == params.length && count < 100) {
            try {
                Object result;

                ArrayList<Object> newParams = new ArrayList<>();
                for (Object o : params) {
                    if (o != null) {
                        newParams.add(o);
                    }
                }

                if (index == 0) {
                    result = m.invoke(instance);
                } else {
                    result = m.invoke(instance, newParams.toArray());
                }
               // System.out.println("We've run this function exactly " + (count + 1) + " time(s)");
                boolean bool = (boolean) result;
                if (bool) {
                    count++;
                    return null;
                } else {
                    return params;
                }
            } catch (Exception e) {
                return params;
            }
        } else if (count >= 100) {
            return null;
        }

        Annotation a = anno.get(index);

        if (a.annotationType().equals(IntRange.class)) {
            IntRange aI = (IntRange) a;
            for (int i = aI.min(); i <= aI.max(); i++) {
                params[index] = i;
                if (recurseInvoke(m, params, anno, index + 1) != null) {
                    return params;
                }
            }
        }

        if (a.annotationType().equals(StringSet.class)) {
            StringSet aS = (StringSet) a;
            for (int i = 0; i < aS.strings().length; i++) {
                params[index] = aS.strings()[i];
                if (recurseInvoke(m, params, anno, index + 1) != null) {
                    return params;
                }
            }
        }

        if (a.annotationType().equals(ListLength.class)) {
            ListLength aL = (ListLength) a;

            //check what the type of the list is
            Annotation type = anno.get(index + 1);

            //What to do if it is a List of Intergers
            if (type.annotationType().equals(IntRange.class)) {
                IntRange typeI = (IntRange) type;

                ArrayList<Object> intRange = new ArrayList<>();
                for (int i = typeI.min(); i <= typeI.max(); i++) {
                    intRange.add(i);
                }

                for (int i = aL.min(); i <= aL.max(); i++) {
                    ArrayList<ArrayList<Object>> list = getListsOfSizeI(i, intRange);
                    for (ArrayList<Object> arr : list) {
                        params[index] = arr;
                        System.out.println("the array is " + arr);
                        if (recurseInvoke(m, params, anno, index + 2) != null) {
                            return params;
                        }
                    }
                }
            }

            if (type.annotationType().equals(StringSet.class)) {
                StringSet typeS = (StringSet) type;

                ArrayList<Object> stringRange = new ArrayList<>();
                for (int i = 0; i <= typeS.strings().length; i++) {
                    stringRange.add(typeS.strings()[i]);
                }

                for (int i = aL.min(); i <= aL.max(); i++) {
                    ArrayList<ArrayList<Object>> list = getListsOfSizeI(i, stringRange);
                    for (ArrayList<Object> arr : list) {
                        params[index] = arr;
                        System.out.println("the array is " + arr);
                        if (recurseInvoke(m, params, anno, index + 2) != null) {
                            return params;
                        }
                    }
                }
            }
        }


        // if (a.annotationType().equals(ForAll.class)) {
        //     return null;
        // }

        return null;
    }

    private static ArrayList<ArrayList<Object>> getListsOfSizeI(int index, ArrayList<Object> range) {

        ArrayList<ArrayList<Object>> oldList = new ArrayList<>();
        ArrayList<ArrayList<Object>> newList = new ArrayList<>();

        if (index == 0) {
            ArrayList<Object> newObj = new ArrayList<>();
            newList.add(newObj);
            return newList;
        }

        oldList = getListsOfSizeI(index-1, range);

        for (int i = 0; i < range.size(); i++) {
            for (ArrayList<Object> obj : oldList) { 
                ArrayList<Object> newObj = new ArrayList<>(); 
                for (Object o : obj) {
                    newObj.add(o);
                }
                newObj.add(range.get(i));
                newList.add(newObj);
                System.out.println("obj = " + obj);
            }
        }
        return newList;
    }
} 


