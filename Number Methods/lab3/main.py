import time
from math import sin, cos
import numpy as np
from scipy.optimize import newton
import matplotlib.pyplot as plt

def f(x, y, z=0):
    return x - 1/2 * sin((x-y)/2)
    # return x**2 / 4 + y**2 + z**2 / 9 - 1

def g(x, y, z=0):
    return y - 1/2 * cos((x+y)/2)
    # return x + y + z

def fDx(x, y):
    return 1 - 1/4 * cos((x-y)/2)
def fDy(x,y):
    return 1/4 * cos((x-y)/2)
def gDx(x, y):
    return 1/4 * sin((x+y)/2)
def gDy(x,y):
    return 1 + 1/4 * sin((x+y)/2)

# eps = 0.1 ** 4
eps = 0.2

approximation = np.array([0, 0])
new_approximation = np.array([0, 0])
step = 1

start = time.time()
while np.linalg.norm(new_approximation, np.inf) > eps or step == 1:
    approximation = approximation - new_approximation

    x, y = approximation[0], approximation[1]

    # Jacobi matrix
    A = np.array([[fDx(x, y), fDy(x, y)],
                  [gDx(x, y), gDy(x, y)]])

    F = np.array([f(x, y), g(x, y)])

    new_approximation = np.dot(np.linalg.inv(A), F)

    step += 1

end = time.time()
print('Execution time: ', (end-start)*1000, 'milliseconds')
print(f"Iterations: {step}\n")

print("Roots of a system of nonlinear equations with eigenvalues: ", new_approximation)
print(f"f(x) = {f(new_approximation[0], new_approximation[1])}")
print(f"g(x) = {g(new_approximation[0], new_approximation[1])}\n")

approximation = approximation - new_approximation
x, y = approximation[0], approximation[1]
print("Answer [x, y] is [", x, ",", y, "]")
print(f"f1 = {f(x, y)}")
print(f"f2 = {g(x, y)}")

print("\n\n-------------LINE-------------\n\n")


def f(x, y, z=0):
    return x**2 / 4 + y**2 + z**2 / 9 - 1

def g(x, y, z=0):
    return x + y + z

def fDx(x, y,z=0):
    return x/2
def fDy(x, y,z=0):
    return 2*y
def gDx(x, y,z=0):
    return 1
def gDy(x,y,z=0):
    return 1


def check_convergence(x, y, z):
    f_value = f(x, y, z)
    g_value = g(x, y, z)
    return abs(f_value) < eps and abs(g_value) < eps

z_values = np.linspace(-3, 3, 50)

x_values = []
y_values = []
z_values_found = []

start = time.time()
for zM in z_values:
    eps = 0.002
    approximation = np.array([-1, 1])

    x, y = approximation[0], approximation[1]

    if check_convergence(x, y, zM):
        x_values.append(x)
        y_values.append(y)
        z_values_found.append(zM)
        continue

    step = 0
    max_step = 100

    while step < max_step:
        approximation = approximation - new_approximation
        x, y = approximation[0], approximation[1]

        A = np.array([[fDx(x, y, zM), fDy(x, y, zM)],
                      [gDx(x, y, zM), gDy(x, y, zM)]])

        F = np.array([f(x, y, zM), g(x, y, zM)])

        new_approximation = np.linalg.solve(A, F)

        if np.linalg.norm(new_approximation) < eps:
            break

        step += 1

        # print(zM, step)

    if step != max_step:
        x_values.append((approximation - new_approximation)[0])
        y_values.append((approximation - new_approximation)[1])
        z_values_found.append(zM)

end = time.time()

for x, y, z in zip(x_values, y_values, z_values_found):
    # print(f"x: {x}, y: {y}, z: {z}")
    print("Answer (x, y, z) is [", x, ",", y, ",", z,"]")
    print(f"f1 = {f(x, y, z)}")
    print(f"f2 = {g(x, y, z)}")
    print("\n\n")

print('Execution time: ', (end-start)*1000, 'milliseconds')

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

ax.plot(x_values, y_values, z_values_found, label='Intersection Curve', color='b', marker='o', linestyle='')

ax.set_xlabel('X')
ax.set_ylabel('Y')
ax.set_zlabel('Z')

plt.legend()
plt.show()

