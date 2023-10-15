def factorial(n)
  n > 1 ? n * factorial(n - 1) : 1
end

def f(x, i)
  ((-1) ** i) * ((2 * (i ** 2) + 1) / factorial(2 * i).to_f)
end

def series(f, x, n)
  x_range = 0.1..1
  n_range = 16..58
  epsilon = 0.001

  unless x_range.include? x
    raise "'x' is out of range"
  end

  res, i, cur = 0.0, n, epsilon
  if n_range.include? n
    (1..n).each do |i|
      cur = f.call(x, i)
      res += cur
    end
  else
    i = 0
    while cur.abs >= epsilon
      cur = f.call(x, i)
      res += cur
      i += 1
    end
  end

  return [res, i, cur]
end

sum, iter, error = series(method(:f), 0.1, 1)

puts "Sum: #{sum}"
puts "Iterations: #{iter}"
puts "Error: #{error}"