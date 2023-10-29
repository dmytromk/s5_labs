//
// Created by Dmytro Mandziuk on 29.10.2023.
//
#pragma once

#include <iostream>
#include <random>

namespace SquareMatrix {
    // Namespace for square matrices stored as 1D arrays
    //
    // In this namespace, square matrices are represented as 1D arrays for memory
    // efficiency and simplicity. A square matrix of size N x N is stored as a
    // 1D array of size N*N. Elements are arranged in a row-major order, where each
    // row of the matrix corresponds to a contiguous block of elements in the array.
    //
    // Accessing elements of the matrix is achieved by converting 2D indices (row, col)
    // into a 1D index using the formula: row * N + col.
    //
    // Example:
    // A 3x3 matrix:
    // [ 1.0, 2.0, 3.0 ]
    // [ 4.0, 5.0, 6.0 ]
    // [ 7.0, 8.0, 9.0 ]
    //
    // Is stored as a 1D array with 9 elements:
    // [ 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 ]

    // Function to set a specific element in the matrix
    void set(double* matrix, unsigned int size, unsigned int row, unsigned int col, double value) {
        if (row < size && col < size) {
            matrix[row * size + col] = value;
        } else {
            std::cerr << "Invalid row or column index." << std::endl;
        }
    }

    // Function to get a specific element from the matrix
    double get(const double* matrix, unsigned int size, unsigned int row, unsigned int col) {
        if (row < size && col < size) {
            return matrix[row * size + col];
        } else {
            std::cerr << "Invalid row or column index." << std::endl;
            return 0;
        }
    }

    // Function to fill the matrix with elements
    void fill(double* matrix, unsigned int size, double element) {
        unsigned int square = size * size;
        for (int i = 0; i < square; i++) {
            matrix[i] = element;
        }
    }

    // Function to fill the matrix with random elements
    void generateRandom(double* matrix, unsigned int size, double min, double max) {
        std::random_device rd;
        std::mt19937 gen(rd());
        std::uniform_real_distribution<double> distribution(min, max);

        for (int i = 0; i < size * size; i++) {
            matrix[i] = int(distribution(gen));
        }
    }

    // Function to transpose the square matrix in-place
    void transpose(double* matrix, unsigned int size) {
        for (unsigned int i = 0; i < size; i++) {
            for (unsigned int j = i + 1; j < size; j++) {
                double temp = matrix[i * size + j];
                matrix[i * size + j] = matrix[j * size + i];
                matrix[j * size + i] = temp;
            }
        }
    }

    // Function to print the matrix
    void print(const double* matrix, unsigned int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                std::cout << get(matrix, size, i, j) << " ";
            }
            std::cout << std::endl;
        }
    }

    // Function to multiply two square matrices (naively)
    void multiply(double* matrix1, double* matrix2, double* result, unsigned int size) {
        for (unsigned int i = 0; i < size; i++) {
            for (unsigned int j = 0; j < size; j++) {
                result[i * size + j] = 0.0;
                for (unsigned int k = 0; k < size; k++) {
                    result[i * size + j] += matrix1[i * size + k] * matrix2[k * size + j];
                }
            }
        }
    }

    // Function to check if two matrices are equal
    bool areEqual(double* matrix1, double* matrix2, unsigned int size) {
        for (unsigned int i = 0; i < size; i++) {
            for (unsigned int j = 0; j < size; j++) {
                if (matrix1[i * size + j] != matrix2[i * size + j]) {
                    return false;
                }
            }
        }
        return true;
    }
}