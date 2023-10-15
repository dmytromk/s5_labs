def calculate_polynomial(x)
  result = 1
  for n in 1..10
    result += (n + 1).to_f / (n + 2) * (x ** n) * (-1) ** (n)
  end
  return result
end

x = 2
result = calculate_polynomial(2)
puts "The result when x = #{x} is #{result}"

def calculate_three
  result = 0
  for x in 0..8
    result += 1/(3**x.to_f)
    end
  result
end

puts calculate_three