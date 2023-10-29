//
// Created by Dmytro Mandziuk on 29.10.2023.
//

#include "matrix.h"

double *SquareMatrix::create(unsigned int size) {
    double* matrix = new double[size * size];
    return matrix;
}

void SquareMatrix::destroy(double *matrix) {
    delete[] matrix;
}

void SquareMatrix::transpose(double *matrix, unsigned int size) {
    for (unsigned int i = 0; i < size; i++) {
        for (unsigned int j = i + 1; j < size; j++) {
            double temp = matrix[i * size + j];
            matrix[i * size + j] = matrix[j * size + i];
            matrix[j * size + i] = temp;
        }
    }
}

void SquareMatrix::print(const double *matrix, unsigned int size) {
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            std::cout << get(matrix, size, i, j) << " ";
        }
        std::cout << std::endl;
    }
}

void SquareMatrix::fill(double *matrix, unsigned int size, double element) {
    unsigned int square = size * size;
    for (unsigned int i = 0; i < square; i++) {
        matrix[i] = element;
    }
}

void SquareMatrix::set(double *matrix, unsigned int size, unsigned int row, unsigned int col, double value) {
    if (row < size && col < size) {
        matrix[row * size + col] = value;
    } else {
        std::cerr << "Invalid row or column index." << std::endl;
    }
}

double SquareMatrix::get(const double *matrix, unsigned int size, unsigned int row, unsigned int col) {
    if (row < size && col < size) {
        return matrix[row * size + col];
    } else {
        std::cerr << "Invalid row or column index." << std::endl;
        return 0;
    }
}
