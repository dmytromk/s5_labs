//
// Created by Dmytro Mandziuk on 29.10.2023.
//
#pragma once

#include <iostream>

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

    // Function to create a square matrix
    double* create(unsigned int size);

    // Function to delete a square matrix
    void destroy(double* matrix);

    // Function to set a specific element in the matrix
    void set(double* matrix, unsigned int size, unsigned int row, unsigned int col, double value);

    // Function to get a specific element from the matrix
    double get(const double* matrix, unsigned int size, unsigned int row, unsigned int col);

    // Function to fill the matrix with elements
    void fill(double* matrix, unsigned int size, double element);

    // Function to transpose the square matrix in-place
    void transpose(double* matrix, unsigned int size);

    // Function to print the matrix
    void print(const double* matrix, unsigned int size);
}