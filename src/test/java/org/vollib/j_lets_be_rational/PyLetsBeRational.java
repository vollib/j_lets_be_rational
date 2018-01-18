package org.vollib.j_lets_be_rational;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Maricris on 07/02/2017.
 */
public class PyLetsBeRational {

    private PythonInterpreter interpreter = new PythonInterpreter(null, new PySystemState());

    public PyLetsBeRational() {
        PySystemState sys = Py.getSystemState();
        try {
            sys.path.add(new PyString(getPyPath()));
            interpreter.exec("import py_lets_be_rational as p");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static String getPyPath() throws URISyntaxException {
        URL url = TestJython.class.getClassLoader().getResource("py_lets_be_rational");
        if (url == null) {
            throw new RuntimeException("'py_lets_be_rational' is not in test/resources folder.");
        }
        return new File(url.toURI()).getAbsolutePath();
    }

    public double black(double F, double K, double sigma, double T, double q /* q=±1 */) {
        interpreter.set("F", F);
        interpreter.set("K", K);
        interpreter.set("sigma", sigma);
        interpreter.set("T", T);
        interpreter.set("q", q);
        PyObject iv = interpreter.eval("p.black(F, K, sigma, T, q)");
        return iv.asDouble();
    }

    public double implied_volatility_from_a_transformed_rational_guess(double price, double F, double K, double T, double q /* q=±1 */) {
        interpreter.set("price", price);
        interpreter.set("F", F);
        interpreter.set("K", K);
        interpreter.set("T", T);
        interpreter.set("q", q);
        PyObject iv = interpreter.eval("p.implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q)");
        return iv.asDouble();
    }

    public double implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(double price, double F, double K, double T, double q /* q=±1 */, int N) {
        interpreter.set("price", price);
        interpreter.set("F", F);
        interpreter.set("K", K);
        interpreter.set("T", T);
        interpreter.set("q", q);
        interpreter.set("N", N);
        PyObject iv = interpreter.eval("p.implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(price, F, K, T, q, N)");
        return iv.asDouble();
    }

    public double normalised_black(double x, double s, double q /* q=±1 */) {
        interpreter.set("x", x);
        interpreter.set("s", s);
        interpreter.set("q", q);
        PyObject iv = interpreter.eval("p.normalised_black(x, s, q)");
        return iv.asDouble();
    }

    public double normalised_black_call(double x, double s) {
        interpreter.set("x", x);
        interpreter.set("s", s);
        PyObject iv = interpreter.eval("p.normalised_black_call(x, s)");
        return iv.asDouble();
    }

    public double normalised_implied_volatility_from_a_transformed_rational_guess(double beta, double x, double q /* q=±1 */) {
        interpreter.set("beta", beta);
        interpreter.set("x", x);
        interpreter.set("q", q);
        PyObject iv = interpreter.eval("p.normalised_implied_volatility_from_a_transformed_rational_guess(beta, x, q)");
        return iv.asDouble();
    }

    public double normalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(double beta, double x, double q /* q=±1 */, int N) {
        interpreter.set("beta", beta);
        interpreter.set("x", x);
        interpreter.set("q", q);
        interpreter.set("N", N);
        PyObject iv = interpreter.eval("p.normalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(beta, x, q, N)");
        return iv.asDouble();
    }

    public double normalised_vega(double x, double s) {
        interpreter.set("x", x);
        interpreter.set("s", s);
        PyObject iv = interpreter.eval("p.normalised_vega(x, s)");
        return iv.asDouble();
    }

    public double norm_cdf(double z) {
        interpreter.set("z", z);
        PyObject iv = interpreter.eval("p.norm_cdf(z)");
        return iv.asDouble();
    }

}
