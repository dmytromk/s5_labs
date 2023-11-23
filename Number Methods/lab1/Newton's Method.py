from _decimal import getcontext

import numpy as np
import matplotlib.pyplot as plt


# x^2 - 10*sin(x) = 0

def draw():
    x = np.linspace(-6, 6, 1000)
    y1 = f(x)
    y2 = df(x)
    y3 = d2f(x)
    fig, ax = plt.subplots()
    ax.plot(x, y1, label='f(x)=x**2 - 10*sin(x)')
    ax.plot(x, y2, label='f(x)=2x - 10*cos(x)')
    ax.plot(x, y3, label='f(x)=2 + 10*sin(x)')

    ax.axhline(0, color='black', linewidth=0.5)
    ax.axvline(0, color='black', linewidth=0.5)

    ax.set_title('Plotting Functions in Matplotlib', size=14)
    ax.set_xlim(-3.5, 3.5)
    ax.set_ylim(-15, 15)
    plt.legend()
    plt.show()


def f(x):
    return x ** 2 - 10 * np.sin(x)


def df(x):
    return 2 * x - 10 * np.cos(x)


def d2f(x):
    return 2 + 10 * np.sin(x)


def check_theorem_selection(a, b):
    return f(a) * f(b) < 0 and f(a) * d2f(a) > 0


def newton_calc(epsilon, x_n):
    step = 0
    while np.abs(f(x_n)) > epsilon:
        x_n = x_n - f(x_n) / df(x_n)
        print("Step %i: f(%f) = %f" % (step, x_n, f(x_n)))
        step += 1
    print("Result = %f" % x_n)


def newton(epsilon):
    a, b = 2, 3

    c = (a + b) / 2  # 2.5
    print("sign(f(c)): ", np.sign(f(c)))

    #b = c
    print("sign(f(a)): ", np.sign(f(a)))  # < 0
    print("sign(f(b)): ", np.sign(f(b)))  # > 0
    # f'(x) > 0 and f''(x) > 0 on (a,b) -> f(x) monotonically increasing

    x_0 = b  # 2.5
    # f(x_0) * f''(x_0) > 0

    m1 = abs(df(a))
    M2 = abs(d2f(a))
    q = (M2 * (b - a)) / (2 * m1)
    print("q = ", q)

    newton_calc(epsilon, b)


def modified_newton_calc(epsilon, x_n):
    step = 0
    const_df = df(x_n)
    while np.abs(f(x_n)) > epsilon:
        x_n = x_n - f(x_n) / const_df
        print("Step %i: f(%f) = %f" % (step, x_n, f(x_n)))
        step += 1
    print("Result = %f" % x_n)


epsilon = 1e-4

draw()
n_0 = np.rint(np.abs(np.log2(np.log(np.abs(3-2.5)/epsilon))/np.log(1/0.67959426))+1) + 1
print("n_0(e) = ", n_0)
print("\n----NEWTON----")
newton(epsilon)
print("\n----MODIFIED NEWTON----")
modified_newton_calc(epsilon, 3)
