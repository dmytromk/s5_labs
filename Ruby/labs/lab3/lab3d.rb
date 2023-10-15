e = 0.00001

def factorial(n)
  n > 1 ? n * factorial(n - 1) : 1
end

def task_a(e)
  result = 0.0
  n = 2
  while
    to_add = (factorial(2*n-1)/(2.0*factorial(n+1))) ** (n*(n+1))
    result += to_add
    #puts "result(x) = #{result}"
    #puts "n = #{n}"
    #puts
    if result -to_add < e
      break
    end
    n += 1
  end
  return result
end

puts task_a(e)
#puts("END.")
puts

def task_b(e)
  result = 1.0
  n = 1
  while TRUE
    to_add = 1/(3+2.0*(n-1)) * ((-1) ** n)
    result += to_add
    #puts "result(x) = #{result}"
    #puts "n = #{n}"
    #puts

    if result - to_add < e || n > 100
      break
    end
    n += 1
  end
  return result
end

task_b = task_b(e)
puts
puts "Result = #{task_b}"
puts "Pi/4 = #{Math::PI/4}"
puts "END."
puts


def task_c(e)
  result = 0.0
  n = 1
  while TRUE
    to_add = (factorial(3*n-1)*factorial(2*n-1)) / (factorial(4*n) * (3.0 ** (2*n)) * factorial(2*n))
    result += to_add
    if result - to_add < e
      break
    end
    n += 1
  end
  return result
end

puts task_c(e)
puts("END.")
puts