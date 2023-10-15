def y(x, c, n)
  sum1 = (x**2 + x * ((x**2 - c**2) ** (1.0/2)))
  sum2 = (x**2 + 1) / (1.0/x + 1/n)
  sum3 = (3.0*x + c ** x) / (x+1)
  return sum1 + sum2 + sum3
end

def z(x, c, n)
  sum1 = (1/Math.sin(4*x) - Math.tan(7.0*Math::PI/2 + 4*x))
  sum2 = Math.tan(5*Math::PI + x)
  return 2*sum1 + sum2
end

def f(x, c, n)
  if x > 2 && x < n
    return y(x, c, n)
  elsif x > n && x < 2*n
    return z(x, c, n)
  else
    return y(x, c, n) + z(x, c, n)
  end
end

def task_1(c, n)
  x = 1.0
  step = (n - 1)/(n + c)
  puts "Task 1"
  puts "Step = #{step}"
  while x <= n
    puts "y(#{x}) = #{y(x, c, n)}"
    x += step
  end
  puts
end

def task_2(c, n)
  x = 1.0
  step = (Math::PI - Math::PI/n)/((3.0/2) * n + c)
  puts "Task 2"
  puts "Step = #{step}"
  while x <= Math::PI
    puts "z(#{x}) = #{z(x, c, n)}"
    x += step
  end
  puts
end

def task_3(c, n)
  x = 2.0
  step = (c - 2)/(2 * n)
  puts "Task 3"
  puts "Step = #{step}"
  while x <= c
    puts "f(#{x}) = #{f(x, c, n)}"
    x += step
  end
  puts
end

print "Enter N: "
n = gets.chomp.to_f

print "Enter c: "
c = gets.chomp.to_f

x = 2.0

task_1(c, n)
task_2(c, n)
task_3(c, n)