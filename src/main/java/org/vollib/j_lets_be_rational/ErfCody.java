package org.vollib.j_lets_be_rational;

import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static java.lang.Math.floor;

public class ErfCody {

    public static double d_int(final double x) {
        return ((x > 0) ? floor(x) : -floor(-x));
    }

    static final double A[] = new double[]{3.1611237438705656, 113.864154151050156, 377.485237685302021, 3209.37758913846947, .185777706184603153};
    static final double B[] = new double[]{23.6012909523441209, 244.024637934444173, 1282.61652607737228, 2844.23683343917062};
    static final double C[] = new double[]{.564188496988670089, 8.88314979438837594, 66.1191906371416295, 298.635138197400131, 881.95222124176909, 1712.04761263407058, 2051.07837782607147, 1230.33935479799725, 2.15311535474403846e-8};
    static final double D[] = new double[]{15.7449261107098347, 117.693950891312499, 537.181101862009858, 1621.38957456669019, 3290.79923573345963, 4362.61909014324716, 3439.36767414372164, 1230.33935480374942};
    static final double P[] = new double[]{.305326634961232344, .360344899949804439, .125781726111229246, .0160837851487422766, 6.58749161529837803e-4, .0163153871373020978};
    static final double Q[] = new double[]{2.56852019228982242, 1.87295284992346047, .527905102951428412, .0605183413124413191, .00233520497626869185};

    static final double ZERO = 0.;
    static final double HALF = .5;
    static final double ONE = 1.;
    static final double TWO = 2.;
    static final double FOUR = 4.;
    static final double SQRPI = 0.56418958354775628695;
    static final double THRESH = .46875;
    static final double SIXTEEN = 16.;


    /* ******************************************************************* */
   /* Explanation of machine-dependent Constants */
   /*   XMIN   = the smallest positive floating-point number. */
   /*   XINF   = the largest positive finite floating-point number. */
   /*   XNEG   = the largest negative argument acceptable to ERFCX; */
   /*            the negative of the solution to the equation */
   /*            2*exp(x*x) = XINF. */
   /*   XSMALL = argument below which erf(x) may be represented by */
   /*            2*x/sqrt(pi)  and above which  x*x  will not underflow. */
   /*            A conservative value is the largest machine number X */
   /*            such that   1.0 + X = 1.0   to machine precision. */
   /*   XBIG   = largest argument acceptable to ERFC;  solution to */
   /*            the equation:  W(x) * (1-0.5/x**2) = XMIN,  where */
   /*            W(x) = exp(-x*x)/[x*sqrt(pi)]. */
   /*   XHUGE  = argument above which  1.0 - 1/(2*x*x) = 1.0  to */
   /*            machine precision.  A conservative value is */
   /*            1/[2*sqrt(XSMALL)] */
   /*   XMAX   = largest acceptable argument to ERFCX; the minimum */
   /*            of XINF and 1/[sqrt(pi)*XMIN]. */
    // The numbers below were preselected for IEEE .
    static final double XINF = 1.79e308;
    static final double XNEG = -26.628;
    static final double XSMALL = 1.11e-16;
    static final double XBIG = 26.543;
    static final double XHUGE = 6.71e7;
    static final double XMAX = 2.53e307;

    /*<       SUBROUTINE CALERF(ARG,RESULT,JINT) >*/
    public static double calerf(double x, final int jint) {

        double y, del, ysq, xden, xnum, result;

   /* ------------------------------------------------------------------ */
   /* This packet evaluates  erf(x),  erfc(x),  and  exp(x*x)*erfc(x) */
   /*   for a real argument  x.  It contains three FUNCTION type */
   /*   subprograms: ERF, ERFC, and ERFCX (or DERF, DERFC, and DERFCX), */
   /*   and one SUBROUTINE type subprogram, CALERF.  The calling */
   /*   statements for the primary entries are: */
   /*                   Y=ERF(X)     (or   Y=DERF(X)), */
   /*                   Y=ERFC(X)    (or   Y=DERFC(X)), */
   /*   and */
   /*                   Y=ERFCX(X)   (or   Y=DERFCX(X)). */
   /*   The routine  CALERF  is intended for internal packet use only, */
   /*   all computations within the packet being concentrated in this */
   /*   routine.  The function subprograms invoke  CALERF  with the */
   /*   statement */
   /*          CALL CALERF(ARG,RESULT,JINT) */
   /*   where the parameter usage is as follows */
   /*      Function                     Parameters for CALERF */
   /*       call              ARG                  Result          JINT */
   /*     ERF(ARG)      ANY REAL ARGUMENT         ERF(ARG)          0 */
   /*     ERFC(ARG)     ABS(ARG) .LT. XBIG        ERFC(ARG)         1 */
   /*     ERFCX(ARG)    XNEG .LT. ARG .LT. XMAX   ERFCX(ARG)        2 */
   /*   The main computation evaluates near-minimax approximations */
   /*   from "Rational Chebyshev approximations for the error function" */
   /*   by W. J. Cody, Math. Comp., 1969, PP. 631-638.  This */
   /*   transportable program uses rational functions that theoretically */
   /*   approximate  erf(x)  and  erfc(x)  to at least 18 significant */
   /*   decimal digits.  The accuracy achieved depends on the arithmetic */
   /*   system, the compiler, the intrinsic functions, and proper */
   /*   selection of the machine-dependent finalants. */
   /* ******************************************************************* */

   /*   Approximate values for some important machines are: */
   /*                          XMIN       XINF        XNEG     XSMALL */
   /*  CDC 7600      (S.P.)  3.13E-294   1.26E+322   -27.220  7.11E-15 */
   /*  CRAY-1        (S.P.)  4.58E-2467  5.45E+2465  -75.345  7.11E-15 */
   /*  IEEE (IBM/XT, */
   /*    SUN, etc.)  (S.P.)  1.18E-38    3.40E+38     -9.382  5.96E-8 */
   /*  IEEE (IBM/XT, */
   /*    SUN, etc.)  (D.P.)  2.23D-308   1.79D+308   -26.628  1.11D-16 */
   /*  IBM 195       (D.P.)  5.40D-79    7.23E+75    -13.190  1.39D-17 */
   /*  UNIVAC 1108   (D.P.)  2.78D-309   8.98D+307   -26.615  1.73D-18 */
   /*  VAX D-Format  (D.P.)  2.94D-39    1.70D+38     -9.345  1.39D-17 */
   /*  VAX G-Format  (D.P.)  5.56D-309   8.98D+307   -26.615  1.11D-16 */
   /*                          XBIG       XHUGE       XMAX */
   /*  CDC 7600      (S.P.)  25.922      8.39E+6     1.80X+293 */
   /*  CRAY-1        (S.P.)  75.326      8.39E+6     5.45E+2465 */
   /*  IEEE (IBM/XT, */
   /*    SUN, etc.)  (S.P.)   9.194      2.90E+3     4.79E+37 */
   /*  IEEE (IBM/XT, */
   /*    SUN, etc.)  (D.P.)  26.543      6.71D+7     2.53D+307 */
   /*  IBM 195       (D.P.)  13.306      1.90D+8     7.23E+75 */
   /*  UNIVAC 1108   (D.P.)  26.582      5.37D+8     8.98D+307 */
   /*  VAX D-Format  (D.P.)   9.269      1.90D+8     1.70D+38 */
   /*  VAX G-Format  (D.P.)  26.569      6.71D+7     8.98D+307 */
   /* ******************************************************************* */
   /* ******************************************************************* */
   /* Error returns */
   /*  The program returns  ERFC = 0      for  ARG .GE. XBIG; */
   /*                       ERFCX = XINF  for  ARG .LT. XNEG; */
   /*      and */
   /*                       ERFCX = 0     for  ARG .GE. XMAX. */
   /* Intrinsic functions required are: */
   /*     ABS, AINT, EXP */
   /*  Author: W. J. Cody */
   /*          Mathematics and Computer Science Division */
   /*          Argonne National Laboratory */
   /*          Argonne, IL 60439 */
   /*  Latest modification: March 19, 1990 */
   /* ------------------------------------------------------------------ */
   /*<       INTEGER I,JINT >*/
   /* S    REAL */
   /*<    >*/
   /*<       DIMENSION A(5),B(4),C(9),D(8),P(6),Q(5) >*/
   /* ------------------------------------------------------------------ */
   /*  Mathematical finalants */
   /* ------------------------------------------------------------------ */
   /* S    DATA FOUR,ONE,HALF,TWO,ZERO/4.0E0,1.0E0,0.5E0,2.0E0,0.0E0/, */
   /* S   1     SQRPI/5.6418958354775628695E-1/,THRESH/0.46875E0/, */
   /* S   2     SIXTEN/16.0E0/ */
   /*<    >*/
   /* ------------------------------------------------------------------ */
   /*  Machine-dependent finalants */
   /* ------------------------------------------------------------------ */
   /* S    DATA XINF,XNEG,XSMALL/3.40E+38,-9.382E0,5.96E-8/, */
   /* S   1     XBIG,XHUGE,XMAX/9.194E0,2.90E3,4.79E37/ */
   /*<    >*/
   /* ------------------------------------------------------------------ */
   /*  Coefficients for approximation to  erf  in first interval */
   /* ------------------------------------------------------------------ */
   /* S    DATA A/3.16112374387056560E00,1.13864154151050156E02, */
   /* S   1       3.77485237685302021E02,3.20937758913846947E03, */
   /* S   2       1.85777706184603153E-1/ */
   /* S    DATA B/2.36012909523441209E01,2.44024637934444173E02, */
   /* S   1       1.28261652607737228E03,2.84423683343917062E03/ */
   /*<    >*/
   /*<    >*/
   /* ------------------------------------------------------------------ */
   /*  Coefficients for approximation to  erfc  in second interval */
   /* ------------------------------------------------------------------ */
   /* S    DATA C/5.64188496988670089E-1,8.88314979438837594E0, */
   /* S   1       6.61191906371416295E01,2.98635138197400131E02, */
   /* S   2       8.81952221241769090E02,1.71204761263407058E03, */
   /* S   3       2.05107837782607147E03,1.23033935479799725E03, */
   /* S   4       2.15311535474403846E-8/ */
   /* S    DATA D/1.57449261107098347E01,1.17693950891312499E02, */
   /* S   1       5.37181101862009858E02,1.62138957456669019E03, */
   /* S   2       3.29079923573345963E03,4.36261909014324716E03, */
   /* S   3       3.43936767414372164E03,1.23033935480374942E03/ */
   /*<    >*/
   /*<    >*/
   /* ------------------------------------------------------------------ */
   /*  Coefficients for approximation to  erfc  in third interval */
   /* ------------------------------------------------------------------ */
   /* S    DATA P/3.05326634961232344E-1,3.60344899949804439E-1, */
   /* S   1       1.25781726111229246E-1,1.60837851487422766E-2, */
   /* S   2       6.58749161529837803E-4,1.63153871373020978E-2/ */
   /* S    DATA Q/2.56852019228982242E00,1.87295284992346047E00, */
   /* S   1       5.27905102951428412E-1,6.05183413124413191E-2, */
   /* S   2       2.33520497626869185E-3/ */
   /*<    >*/
   /*<    >*/
   /* ------------------------------------------------------------------ */
   /*<       X = ARG >*/
        // x = *arg;
   /*<       Y = ABS(X) >*/
        y = abs(x);
   /*<       IF (Y .LE. THRESH) THEN >*/
        if (y <= THRESH) {
      /* ------------------------------------------------------------------ */
      /*  Evaluate  erf  for  |X| <= 0.46875 */
      /* ------------------------------------------------------------------ */
      /*<             YSQ = ZERO >*/
            ysq = ZERO;
      /*<             IF (Y .GT. XSMALL) YSQ = Y * Y >*/
            if (y > XSMALL) {
                ysq = y * y;
            }
      /*<             XNUM = A(5)*YSQ >*/
            xnum = A[4] * ysq;
      /*<             XDEN = YSQ >*/
            xden = ysq;
      /*<             DO 20 I = 1, 3 >*/
            for (int i__ = 1; i__ <= 3; ++i__) {
         /*<                XNUM = (XNUM + A(I)) * YSQ >*/
                xnum = (xnum + A[i__ - 1]) * ysq;
         /*<                XDEN = (XDEN + B(I)) * YSQ >*/
                xden = (xden + B[i__ - 1]) * ysq;
         /*<    20       CONTINUE >*/
         /* L20: */
            }
      /*<             RESULT = X * (XNUM + A(4)) / (XDEN + B(4)) >*/
            result = x * (xnum + A[3]) / (xden + B[3]);
      /*<             IF (JINT .NE. 0) RESULT = ONE - RESULT >*/
            if (jint != 0) {
                result = ONE - result;
            }
      /*<             IF (JINT .EQ. 2) RESULT = EXP(YSQ) * RESULT >*/
            if (jint == 2) {
                result = exp(ysq) * result;
            }
      /*<             GO TO 800 >*/
            return result;
      /* ------------------------------------------------------------------ */
      /*  Evaluate  erfc  for 0.46875 <= |X| <= 4.0 */
      /* ------------------------------------------------------------------ */
      /*<          ELSE IF (Y .LE. FOUR) THEN >*/
        } else if (y <= FOUR) {
      /*<             XNUM = C(9)*Y >*/
            xnum = C[8] * y;
      /*<             XDEN = Y >*/
            xden = y;
      /*<             DO 120 I = 1, 7 >*/
            for (int i__ = 1; i__ <= 7; ++i__) {
         /*<                XNUM = (XNUM + C(I)) * Y >*/
                xnum = (xnum + C[i__ - 1]) * y;
         /*<                XDEN = (XDEN + D(I)) * Y >*/
                xden = (xden + D[i__ - 1]) * y;
         /*<   120       CONTINUE >*/
         /* L120: */
            }
      /*<             RESULT = (XNUM + C(8)) / (XDEN + D(8)) >*/
            result = (xnum + C[7]) / (xden + D[7]);
      /*<             IF (JINT .NE. 2) THEN >*/
            if (jint != 2) {
         /*<                YSQ = AINT(Y*SIXTEN)/SIXTEN >*/
                double d__1 = y * SIXTEEN;
                ysq = d_int(d__1) / SIXTEEN;
         /*<                DEL = (Y-YSQ)*(Y+YSQ) >*/
                del = (y - ysq) * (y + ysq);
         /*<                RESULT = EXP(-YSQ*YSQ) * EXP(-DEL) * RESULT >*/
                d__1 = exp(-ysq * ysq) * exp(-del);
                result = d__1 * result;
         /*<             END IF >*/
            }
      /* ------------------------------------------------------------------ */
      /*  Evaluate  erfc  for |X| > 4.0 */
      /* ------------------------------------------------------------------ */
      /*<          ELSE >*/
        } else {
      /*<             RESULT = ZERO >*/
            result = ZERO;
      /*<             IF (Y .GE. XBIG) THEN >*/
            if (y >= XBIG) {
         /*<                IF ((JINT .NE. 2) .OR. (Y .GE. XMAX)) GO TO 300 >*/
                if (jint != 2 || y >= XMAX) {
//                    goto L300;
                    return fixUpforNegativeArgumentErfEtc(x, jint, result);
                }
         /*<                IF (Y .GE. XHUGE) THEN >*/
                if (y >= XHUGE) {
            /*<                   RESULT = SQRPI / Y >*/
                    result = SQRPI / y;
            /*<                   GO TO 300 >*/
//                    goto L300;
                    return fixUpforNegativeArgumentErfEtc(x, jint, result);
            /*<                END IF >*/
                }
         /*<             END IF >*/
            }
      /*<             YSQ = ONE / (Y * Y) >*/
            ysq = ONE / (y * y);
      /*<             XNUM = P(6)*YSQ >*/
            xnum = P[5] * ysq;
      /*<             XDEN = YSQ >*/
            xden = ysq;
      /*<             DO 240 I = 1, 4 >*/
            for (int i__ = 1; i__ <= 4; ++i__) {
         /*<                XNUM = (XNUM + P(I)) * YSQ >*/
                xnum = (xnum + P[i__ - 1]) * ysq;
         /*<                XDEN = (XDEN + Q(I)) * YSQ >*/
                xden = (xden + Q[i__ - 1]) * ysq;
         /*<   240       CONTINUE >*/
         /* L240: */
            }
      /*<             RESULT = YSQ *(XNUM + P(5)) / (XDEN + Q(5)) >*/
            result = ysq * (xnum + P[4]) / (xden + Q[4]);
      /*<             RESULT = (SQRPI -  RESULT) / Y >*/
            result = (SQRPI - result) / y;
      /*<             IF (JINT .NE. 2) THEN >*/
            if (jint != 2) {
         /*<                YSQ = AINT(Y*SIXTEN)/SIXTEN >*/
                double d__1 = y * SIXTEEN;
                ysq = d_int(d__1) / SIXTEEN;
         /*<                DEL = (Y-YSQ)*(Y+YSQ) >*/
                del = (y - ysq) * (y + ysq);
         /*<                RESULT = EXP(-YSQ*YSQ) * EXP(-DEL) * RESULT >*/
                d__1 = exp(-ysq * ysq) * exp(-del);
                result = d__1 * result;
         /*<             END IF >*/
            }
      /*<       END IF >*/
        }
        return fixUpforNegativeArgumentErfEtc(x, jint, result);

   /* ---------- Last card of CALERF ---------- */
   /*<       END >*/
    } /* calerf_ */

    private static double fixUpforNegativeArgumentErfEtc(double x, int jint, double result) {
        double ysq;
        double del;
        double y;
        /* ------------------------------------------------------------------ */
        /*  Fix up for negative argument, erf, etc. */
        /* ------------------------------------------------------------------ */
        /*<   300 IF (JINT .EQ. 0) THEN >*/
        // L300:
        if (jint == 0) {
      /*<             RESULT = (HALF - RESULT) + HALF >*/
            result = (HALF - result) + HALF;
      /*<             IF (X .LT. ZERO) RESULT = -RESULT >*/
            if (x < ZERO) {
                result = -(result);
            }
      /*<          ELSE IF (JINT .EQ. 1) THEN >*/
        } else if (jint == 1) {
      /*<             IF (X .LT. ZERO) RESULT = TWO - RESULT >*/
            if (x < ZERO) {
                result = TWO - result;
            }
      /*<          ELSE >*/
        } else {
      /*<             IF (X .LT. ZERO) THEN >*/
            if (x < ZERO) {
         /*<                IF (X .LT. XNEG) THEN >*/
                if (x < XNEG) {
            /*<                      RESULT = XINF >*/
                    result = XINF;
            /*<                   ELSE >*/
                } else {
            /*<                      YSQ = AINT(X*SIXTEN)/SIXTEN >*/
                    double d__1 = x * SIXTEEN;
                    ysq = d_int(d__1) / SIXTEEN;
            /*<                      DEL = (X-YSQ)*(X+YSQ) >*/
                    del = (x - ysq) * (x + ysq);
            /*<                      Y = EXP(YSQ*YSQ) * EXP(DEL) >*/
                    y = exp(ysq * ysq) * exp(del);
            /*<                      RESULT = (Y+Y) - RESULT >*/
                    result = y + y - result;
            /*<                END IF >*/
                }
         /*<             END IF >*/
            }
      /*<       END IF >*/
        }
        return result;
    }

    /* S    REAL FUNCTION ERF(X) */
    /*<       DOUBLE PRECISION FUNCTION DERF(X) >*/
    public double erf_cody(double x) {
   /* -------------------------------------------------------------------- */
   /* This subprogram computes approximate values for erf(x). */
   /*   (see comments heading CALERF). */
   /*   Author/date: W. J. Cody, January 8, 1985 */
   /* -------------------------------------------------------------------- */
   /*<       INTEGER JINT >*/
   /* S    REAL             X, RESULT */
   /*<       DOUBLE PRECISION X, RESULT >*/
   /* ------------------------------------------------------------------ */
   /*<       JINT = 0 >*/
   /*<       CALL CALERF(X,RESULT,JINT) >*/
        return calerf(x, 0);
   /* S    ERF = RESULT */
   /*<       DERF = RESULT >*/
   /*<       RETURN >*/
   /* ---------- Last card of DERF ---------- */
   /*<       END >*/
    } /* derf_ */

    /* S    REAL FUNCTION ERFC(X) */
    /*<       DOUBLE PRECISION FUNCTION DERFC(X) >*/
    public static double erfc_cody(double x) {
   /* -------------------------------------------------------------------- */
   /* This subprogram computes approximate values for erfc(x). */
   /*   (see comments heading CALERF). */
   /*   Author/date: W. J. Cody, January 8, 1985 */
   /* -------------------------------------------------------------------- */
   /*<       INTEGER JINT >*/
   /* S    REAL             X, RESULT */
   /*<       DOUBLE PRECISION X, RESULT >*/
   /* ------------------------------------------------------------------ */
   /*<       JINT = 1 >*/
   /*<       CALL CALERF(X,RESULT,JINT) >*/
        return calerf(x, 1);
   /* S    ERFC = RESULT */
   /*<       DERFC = RESULT >*/
   /*<       RETURN >*/
   /* ---------- Last card of DERFC ---------- */
   /*<       END >*/
    } /* derfc_ */

    /* S    REAL FUNCTION ERFCX(X) */
    /*<       DOUBLE PRECISION FUNCTION DERFCX(X) >*/
    public static double erfcx_cody(double x) {
   /* ------------------------------------------------------------------ */
   /* This subprogram computes approximate values for exp(x*x) * erfc(x). */
   /*   (see comments heading CALERF). */
   /*   Author/date: W. J. Cody, March 30, 1987 */
   /* ------------------------------------------------------------------ */
   /*<       INTEGER JINT >*/
   /* S    REAL             X, RESULT */
   /*<       DOUBLE PRECISION X, RESULT >*/
   /* ------------------------------------------------------------------ */
   /*<       JINT = 2 >*/
   /*<       CALL CALERF(X,RESULT,JINT) >*/
        return calerf(x, 2);
   /* S    ERFCX = RESULT */
   /*<       DERFCX = RESULT >*/
   /*<       RETURN >*/
   /* ---------- Last card of DERFCX ---------- */
   /*<       END >*/
    } /* derfcx_ */


}
