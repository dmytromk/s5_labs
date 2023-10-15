def add_matrices(matrix1, matrix2)
  if matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length
    puts "Matrices must have the same dimensions for addition."
    return nil
  end

  result = []

  for i in 0...matrix1.length
    row = []

    for j in 0...matrix1[i].length
      row << matrix1[i][j] + matrix2[i][j]
    end

    result << row
  end

  return result
end

matrix1 = [[1, 2, 3],
           [4, 5, 6]]

matrix2 = [[7, 8, 9],
           [10, 11, 12]]

result = add_matrices(matrix1, matrix2)

if result
  puts "Resultant matrix:"
  result.each { |row| puts row.join(" ") }
end
