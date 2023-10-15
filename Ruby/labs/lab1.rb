# frozen_string_literal: true
def Z(x, a, b, c, d, s)
  pi = Math::PI
  e = Math::E

  first_num = (Math.log(x**3, e))**2 + 0.375 * d
  first_den = (e ** (x ** 2)) * ((c + a) ** (1/2))
  second_num = Math.tan(pi/6) ** 2 - (x ** (3/s))
  second_den = (0.316*b*c - a**2).abs
  puts first_num/first_den + second_num/(second_den.abs)
end
Z(1,2,3,4,5,6)