complex_numbers = []

# Generate and add complex numbers to the array
16.times do
  real_part = rand(-10.0..10.0).to_i  # Replace with your desired range for the real part
  imaginary_part = rand(-10.0..10.0).to_i  # Replace with your desired range for the imaginary part
  complex_number = Complex(real_part, imaginary_part)
  complex_numbers << complex_number
end

puts complex_numbers

sum_of_even_integer_parts = 0
sum_of_indices = 0

complex_numbers.each_with_index do |number, index|
  real_part = number.real.to_i  # Get the integer part of the real component
  if real_part.even?
    sum_of_even_integer_parts += real_part
  end
  sum_of_indices += index
end

# Print the sums
puts "Sum of even integer parts: #{sum_of_even_integer_parts}"
puts "Sum of indices: #{sum_of_indices}"