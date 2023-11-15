import math
import time

import numpy as np
from numpy.polynomial import polynomial as p
import matplotlib.pyplot as plt


def f(x):
    return abs(x - 1)


# def finite_differences(data, h=1):
#     n = len(data)
#     differences = np.zeros(n - 1)
#
#     for i in range(n - 1):
#         differences[i] = data[1 + h] - data[i]
#
#     return differences


def divided_differences(y, x):
    size = len(x)
    diff_matrix = np.zeros((size, size))

    diff_matrix[:, 0] = y  # First column is y-values

    for j in range(1, size):
        for i in range(size - j):
            diff_matrix[i, j] = (diff_matrix[i + 1, j - 1] -
                                 diff_matrix[i, j - 1]) / (x[i + j] - x[i])

    return diff_matrix


def get_newton(y, x):
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

# --------------------------------------------------------
n = 10

x = np.linspace(a, b, n)
y = f(x)
# print("X: ", x)
# print("Y: ", y)

start = time.time()
newton = get_newton(y, x)
print("\nDivided time:", (time.time() - start)*1000, 'ms')

#print("\nDivided Differences Coefficients:\n", divided_differences(y, x))
# print("\n", newton)
# print(p.polyval(x, newton))

# prepare_plot(x, y)
# plt.plot(x, p.polyval(x, newton))
# plt.show()

x_values = np.linspace(0, 2, 100)
plt.scatter(x, y, label="Data Points", color="red")
plt.plot(x_values, p.polyval(x_values, newton), label="рівновіддалені", color="blue")
plt.xlabel("x")
plt.ylabel("y")
plt.title("рівновіддалені")
plt.legend()
plt.grid(True)
plt.show()

print("\n--------------------")

# --------------------------------------------------------


def chebyshev_s_nodes(k, n):
    return math.cos((2 * k + 1) * math.pi / (n * 2))


# def newton_interpolation_chebyshev(y, x):
#     poly = [0]
#
#     for i in range(0, len(x)):
#         # print("add", y[i])
#         poly_add = [y[i]]
#
#         for j in range(0, i):
#             # print("multiply", [-x[j], 1])
#             poly_add = p.polymul(poly_add, [-x[j], 1])
#
#         poly = p.polyadd(poly, poly_add)
#
#     return poly

start = time.time()
chebyshev_nodes = [(a + b) / 2 + (b - a) * chebyshev_s_nodes(k, n) / 2 for k in range(math.ceil(n))]
chebyshev_values = [f(x) for x in chebyshev_nodes]

newton_chebyshev = get_newton(chebyshev_values,
                                                  chebyshev_nodes)
print("\nChebyshev time:", (time.time() - start)*1000, 'ms')

# print("\nChebyshev points: ", chebyshev_nodes)
# print("Chebyshev values: ", chebyshev_values)
# print("\n", newton_chebyshev)
# print(p.polyval(chebyshev_nodes, newton_chebyshev))

# prepare_plot(chebyshev_nodes, chebyshev_values)
# plt.plot(chebyshev_nodes, p.polyval(chebyshev_nodes, newton_chebyshev))
# plt.show()

x_values = np.linspace(0, 2, 100)

plt.scatter(chebyshev_nodes, chebyshev_values, label="Data Points", color="red")
plt.plot(x_values, p.polyval(x_values, newton_chebyshev), label="Interpolation Polynomial", color="blue")
plt.xlabel("x")
plt.ylabel("y")
plt.title("Chebyshev Newton Interpolation")
plt.legend()
plt.grid(True)
plt.show()

x_values = np.linspace(0, 2, 100100)
y_values = [f(x) for x in x_values]

plt.scatter(x_values, y_values, label="Data Points", color="red")
plt.plot(x_values, y_values, label="Function", color="blue")
plt.xlabel("x")
plt.ylabel("y")
plt.title("Function")
plt.legend()
plt.grid(True)
plt.show()


x_comparing = 1
print(f"\nNewton: {p.polyval(x_comparing, newton)}")
print(f"Chebyshev: {p.polyval(x_comparing, newton_chebyshev)}")

