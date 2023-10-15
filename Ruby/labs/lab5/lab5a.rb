def prm(a, b, function, epsilon = 0.0001)
  n = ((b-a) / epsilon).to_i
  h = (b-a) / n
  result = 0

  n.times do |i|
    x = a + i * h - h / 2
    result += function.call(x) * h
  end

  return result
end

def trp(a, b, function, epsilon = 0.0001)
  n = ((b-a) / epsilon).to_i
  h = (b-a) / n
  result = 0

  n.times do |i|
    x1 = a + i * h
    x2 = a + (1 + i) * h
    result += (function.call(x1) + function.call(x2)) * h / 2
  end

  return result
end

f1 = ->(x) {(2 ** x - 1) ** (1.0/2)}
a1 = 0.3
b1 = 1.0
puts "#{prm(a1, b1, f1)}"
puts "#{trp(a1, b1, f1)}\n\n"

f2 = ->(x) {(Math.asin(x ** (1.0/2))) / ((x * (1 - x)) ** (1.0/2))}
a2 = 0.2
b2 = 0.3
puts "#{prm(a2, b2, f2)}"
puts "#{trp(a2, b2, f2)}\n"
