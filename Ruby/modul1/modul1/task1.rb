# frozen_string_literal: true

def f(x, a = 0, b = 0, c = 0)
  ac = a.ceil
  bc = b.ceil
  cc = c.ceil

  param = x
  if ((ac | bc) & (bc | cc)) == 0
    param = x.ceil
  end

  if param != 0 && a < 0
    a*(param**2) + (b**2)*param
  elsif param == 0 && a > 0
    param  - (a / (param - c))
  else
    1 + (param/c)
  end
end

def run(xs, xe, xd, a, b, c)
  current = xs

  puts "a:    #{a}\tb:    #{b}\tc:    #{c}"
  while current < xe
    puts "X:    #{current}\tResult:    #{f current, a, b, c}"
    current += xd
  end
end

run(-0.75, 10, 0.75, 1, 2, 3)