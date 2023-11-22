import math
import time

import numpy as np
from numpy.polynomial import polynomial as p
from numpy.polynomial import Polynomial as P
import matplotlib.pyplot as plt


def f(x):
    return abs(x - 1)


def divided_differences(y, x):
    size = len(x)
    diff_matrix = np.zeros((size, size))

    diff_matrix[:, 0] = y  # First column is y-values

    for j in range(1, size):
        for i in range(size - j):
            diff_matrix[i, j] = (diff_matrix[i + 1, j - 1] -
                                 diff_matrix[i, j - 1]) / (x[i + j] - x[i])

    return diff_matrix


def get_newton(x, y):
    div_diffs = divided_differences(y, x)

    # Forward formula
    poly = [0]

    for i in range(0, len(x)):
        # print("add", div_diffs[0, i])
        poly_add = [div_diffs[0, i]]

        for j in range(0, i):
            # print("multiply", [-x[j], 1])
            poly_add = p.polymul(poly_add, [-x[j], 1])

        poly = p.polyadd(poly, poly_add)

    return poly


def prepare_plot(scatter_x, scatter_y):
    # Draw axes at the center
    fig = plt.figure()
    ax = fig.add_subplot(1, 1, 1)
    ax.spines['left'].set_position('center')
    ax.spines['bottom'].set_position('zero')
    ax.spines['right'].set_color('none')
    ax.spines['top'].set_color('none')
    ax.xaxis.set_ticks_position('bottom')
    ax.yaxis.set_ticks_position('left')

    plt.scatter(scatter_x, scatter_y)


np.set_printoptions(suppress=True)

a = 0
b = 2

n = 100

x = np.linspace(a, b, n)
y = f(x)

x_values = np.linspace(0, 2, 100)
y_values = f(x_values)

print("\n--------------------")

plt.scatter(x, y, label="Data Points", color="red")
plt.plot(x_values, y_values, label="Function", color="blue")
plt.xlabel("x")
plt.ylabel("y")
plt.title("Function")
plt.legend()
plt.grid(True)
plt.show()

# ----

newton = get_newton(x, y)

plt.scatter(x, y, label="Data Points", color="red")
plt.plot(x_values, p.polyval(x_values, newton), label="рівновіддалені",
         color="blue")
plt.xlabel("x")
plt.ylabel("y")
plt.title("рівновіддалені")
plt.legend()
plt.grid(True)
plt.show()

# ---------------------------------------
#
# print("Piecewise spline:")
#
# piecewise = []
# plt.scatter(x, y, label="Data Points", color="red")
# for i in range(0, n - 1):
#     # x_lower = a + i / (n-1) * 2
#     # x_upper = a + (i+1) / (n-1) * 2
#     x_pair = np.array([x[i], x[i + 1]])
#     x_lower = x_pair[0]
#     x_upper = x_pair[1]
#     x_newton_interval = np.linspace(x_lower, x_upper, 3)
#     y_newton_interval = f(x_newton_interval)
#     local_newton = get_newton(x_newton_interval, y_newton_interval)
#     piecewise.append(local_newton)
#     x_interval = np.linspace(x_lower, x_upper, 50)
#     print("{0} for [{1}; {2}]".format(local_newton, x_lower, x_upper))
#     plt.plot(x_interval, p.polyval(x_interval, local_newton))
# plt.grid(True)
# plt.show()
#
# print("\n--------------------")
# # ---------------------------------------

def get_natural_cubic_spline(x, y):
    tridiag_a = np.zeros((x.size - 2, x.size - 2))
    b = np.empty(x.size - 2)

    for i in range(0, x.size - 2):
        # y[i] / h[i] - y[i+1] / h[i] - y[i+1] / h[i+1] + y[i+2] / h[i+1]
        # =
        # (y[i]- y[i+1]) / h[i] + (y[i+2] - y[i+1]) / h[i+1]
        b[i] = (y[i + 2] - y[i + 1]) / (x[i + 2] - x[i + 1]) - (
                y[i + 1] - y[i]) / (x[i + 1] - x[i])

        if i != 0:
            tridiag_a[i, i - 1] = (x[i + 1] - x[i]) / 6

        tridiag_a[i, i] = (x[i + 2] - x[i]) / 3

        if i < x.size - 3:
            tridiag_a[i, i + 1] = (x[i + 2] - x[i + 1]) / 6

    m = np.linalg.solve(tridiag_a, b)
    m = np.insert(m, 0, 0)
    m = np.insert(m, m.size, 0)

    polys = []

    for i in range(0, x.size - 1):
        poly = P([0])
        h = x[i + 1] - x[i]
        A = y[i] - m[i] * h * h / 6
        B = y[i + 1] - m[i + 1] * h * h / 6
        poly += P([x[i + 1], -1]) ** 3 * (m[i] / (6 * h))
        poly += P([-x[i], 1]) ** 3 * (m[i + 1] / (6 * h))
        poly += P([x[i + 1], -1]) * (A / h)
        poly += P([-x[i], 1]) * (B / h)

        polys.append(poly.coef)

    return polys


print("Natural cubic splines:")

spline_polynoms = get_natural_cubic_spline(x, y)

plt.scatter(x, y, label="Data Points", color="red")
for i in range(0, n - 1):
    # x_lower = a + i / (n-1) * 2
    # x_upper = a + (i+1) / (n-1) * 2
    x_lower = x[i]
    x_upper = x[i + 1]
    x_interval = np.linspace(x_lower, x_upper, 50)
    print("{0} for [{1}; {2}]".format(spline_polynoms[i], x_lower, x_upper))
    plt.plot(x_interval, p.polyval(x_interval, spline_polynoms[i]))
plt.grid(True)
plt.show()

print("\n--------------------")
# ---------------------------------------


x = np.linspace(1, 2, n)
y = f(x)
x_values = np.linspace(1, 2, 100)
y_values = f(x_values)
inverse_interpolation = get_newton(y, x)
plt.scatter(x, y, label="Data Points", color="red")
plt.plot(p.polyval(y_values, inverse_interpolation), y_values,
         label="Обернена", color="blue")
plt.xlabel("x")
plt.ylabel("y")
plt.title("Обернена")
plt.legend()
plt.grid(True)
plt.show()

a = 3
print(f"Inverse: a = {a}; x* = {p.polyval(a, inverse_interpolation)}")
