import numpy as np

a = np.array([
    [0.32, -0.05, 0.11, -0.08],
    [0.11, 0.16, -0.28, -0.06],
    [-0.08, 0.15, 0, 0.12],
    [-0.21, 0.13, -0.27, 0]
])

b = np.array([-2.15, 0.83, -1.16, -0.44])

a_ = np.array([
    [-0.68, -0.05, 0.11, -0.08],
    [0.11, -0.84, -0.28, -0.06],
    [-0.08, 0.15, -1.0, 0.12],
    [-0.21, 0.13, -0.27, -1.0]
])


def solve_gauss(a: np.array, b: np.array):
    a = a.copy()
    b = b.copy()

    n = a.shape[0]
    x = np.zeros(n, dtype=np.float64)
    det = 1.0

    inverse = np.identity(n)

    for i in range(0, n):
        pivot_row_idx = np.argmax(a[i:, i]) + i
        pivot_el = a[pivot_row_idx, i]

        if pivot_row_idx != i:
            a[[pivot_row_idx, i], :] = a[[i, pivot_row_idx], :]
            b[[pivot_row_idx, i]] = b[[i, pivot_row_idx]]
            inverse[[pivot_row_idx, i], :] = inverse[[i, pivot_row_idx], :]
            det *= -1

        a[i, :] = a[i, :] / pivot_el
        b[i] = b[i] / pivot_el
        inverse[i, :] = inverse[i, :] / pivot_el
        det *= pivot_el

        for k in range(i + 1, n):
            multiplier = a[k, i]
            a[k, :] = a[k, :] - a[i, :] * multiplier
            b[k] = b[k] - b[i] * multiplier
            inverse[k, :] = inverse[k, :] - inverse[i, :] * multiplier

    for i in range(n - 1, -1, -1):
        x[i] = b[i] - np.sum(a[i, i + 1:] * x[i + 1:])

    #reverse for inverse matrix
    for i in range(n - 1, -1, -1):
        for j in range(i - 1, -1, -1):
            factor = a[j, i]
            a[j, i] -= factor
            inverse[j, :] -= factor * inverse[i, :]

    return x, det, inverse


def inverse_matrix(a):
    n = a.shape[0]
    identity = np.eye(n)
    inv_a = np.zeros_like(a)

    for i in range(n):
        b = identity[:, i]
        x, _ = solve_gauss(a, b)
        inv_a[:, i] = x

    return inv_a

def solve_jacobi(a: np.array, b: np.array, max_iterations=100, eps=0.001):
    a = a.copy()
    b = b.copy()
    n = a.shape[0]

    x = np.zeros(n)
    x_new = np.zeros(n)

    def check_convergance():
        for i in range(0, n):
            summ = sum(abs(a[i][j]) for j in range(0, a.shape[1]) if j != i)
            if summ > abs(a[i][i]):
                return False
        return True

    if not check_convergance():
        raise ValueError("The matrix does not satisfy the diagonal dominance condition. "
                         "Jacobi method may not converge.")

    for _ in range(max_iterations):
        for i in range(n):
            x_new[i] = (b[i] - np.dot(a[i, :i], x[:i]) - np.dot(a[i, i + 1:], x[i + 1:])) / a[i, i]

        if np.all(np.abs(x_new - x) < eps):
            return x_new

        x = x_new.copy()

    raise ValueError("Jacobi method did not converge within the specified number of iterations.")


print("Gauss Method with pivot element:")
solution, determinant, inverse_matrix = solve_gauss(a_, b)
for idx, elem in enumerate(solution):
    print(f'x_{idx + 1} = {elem}')

print(f"Determinant: {determinant}")
print(f"Inverse Matrix: ")
print(inverse_matrix)
print(f"Check inverse multiplication:\n {np.dot(a_, inverse_matrix)}")
print(f"Obumovlenist: {np.linalg.norm(a_)*np.linalg.norm(inverse_matrix)}")

print("\nJacobi Method:")
solution_jacobi = solve_jacobi(a_, b)
for idx, elem in enumerate(solution_jacobi):
    print(f'x_{idx + 1} = {elem}')