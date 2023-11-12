import numpy as np
import matplotlib.pyplot as plt
import sympy as sympy


def f(x, y, z=0):
    return x**2 / 4 + y**2 + z**2 / 9 - 1

def g(x, y, z=0):
    return x + y + z

#

def fDx(x, y):
    return x/2
def fDy(x, y):
    return 2*y
def gDx(x, y):
    return 1
def gDy(x,y):
    return 1

v_1 = sympy.Matrix([
    [k*a / sympy.sqrt(k**2 + m**2)],
    [0],
    [-m*a / sympy.sqrt(k**2 + m**2)]
])
v_1

z_values = np.linspace(-2, 2, 100)

x_values = []
y_values = []
z_values_found = []

for zM in z_values:
    eps = 0.2

    approximation = np.array([1, 2])
    new_approximation = np.array([0, 0])
    step = 1

    while np.linalg.norm(new_approximation, np.inf) > eps or step == 1:
        approximation = approximation - new_approximation

        x, y = approximation[0], approximation[1]

        # Jacobi matrix
        A = np.array([[fDx(x, y), fDy(x, y)],
                      [gDx(x, y), gDy(x, y)]])

        F = np.array([f(x, y, zM), g(x, y, zM)])

        new_approximation = np.linalg.solve(A, F)

        # new_approximation = np.dot(np.linalg.inv(A), F)

        step += 1

        print(zM, step)

    x_values.append((approximation - new_approximation)[0])
    y_values.append((approximation - new_approximation)[1])
    z_values_found.append(zM)

for x, y, z in zip(x_values, y_values, z_values_found):
    print(f"x: {x}, y: {y}, z: {z}")
    print(f"f(x) = : {f(x, y, z)}")
    print(f"g(x) = : {g(x, y, z)}")
    print()
    print("Answer [x, y, z] is [", x, ",", y, ",", z,"]")
    print(f"f1 = {f(x, y, z)}")
    print(f"f2 = {g(x, y, z)}")
    print("\n\n")

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

ax.plot(x_values, y_values, z_values_found, label='Intersection Curve', color='b', marker='o', linestyle='')

ax.set_xlabel('X')
ax.set_ylabel('Y')
ax.set_zlabel('Z')

plt.legend()
plt.show()