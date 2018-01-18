package org.vollib.j_lets_be_rational;

import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Maricris on 07/02/2017.
 */
public class TestJython {

    public static void main(String[] args) throws URISyntaxException {
        PythonInterpreter interpreter = new PythonInterpreter(null, new PySystemState());
        PySystemState sys = Py.getSystemState();

        String pyPath = getPyPath();
        sys.path.add(new PyString(pyPath));

        interpreter.exec("import py_lets_be_rational as p");

        PyFloat price = new PyFloat(1);
        PyFloat F = new PyFloat(100);
        PyFloat K = new PyFloat(100);
        PyFloat T = new PyFloat(0.2);
        PyFloat q = new PyFloat(1);

        interpreter.set("price", price);
        interpreter.set("F", F);
        interpreter.set("K", K);
        interpreter.set("T", T);
        interpreter.set("q", q);
        PyObject iv = interpreter.eval("p.implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q)");
        System.out.println(Double.valueOf(iv.toString()));
        System.out.println(sys.path);

    }

    private static String getPyPath() throws URISyntaxException {
        URL url = TestJython.class.getClassLoader().getResource("py_lets_be_rational");
        if (url == null) {
            throw new RuntimeException("'py_lets_be_rational' is not in test/resources folder.");
        }
        return new File(url.toURI()).getAbsolutePath();
    }

}
